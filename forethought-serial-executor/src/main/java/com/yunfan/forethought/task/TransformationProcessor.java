package com.yunfan.forethought.task;

import com.yunfan.forethought.api.impls.action.*;
import com.yunfan.forethought.api.impls.transformation.Transformation;
import com.yunfan.forethought.api.impls.transformation.pair.*;
import com.yunfan.forethought.api.monad.CommonMonad;
import com.yunfan.forethought.api.monad.PairMonad;
import com.yunfan.forethought.enums.ActionType;
import com.yunfan.forethought.enums.TransformationalType;
import com.yunfan.forethought.iterators.RepeatableIterator;
import com.yunfan.forethought.type.Tuple;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 处理中间Transformation操作的处理器
 *
 * @param <R> Action操作返回值类型
 */
class TransformationProcessor<R> {

    /**
     * 保存转换操作的列表
     */
    private final List<Tuple<Transformation, TransformationalType>> transformations = new LinkedList<>();

    /**
     * Action操作对象
     */
    private final Action<R, ?> action;

    /**
     * Action操作对应的类型
     */
    private final ActionType actionType;

    private boolean isSorted = false;

    private Comparator order = null;

    /**
     * 构造方法，需要传入最终的Action操作
     *
     * @param action Action操作对象
     */
    TransformationProcessor(Action<R, ?> action) {
        this.actionType = action.type();
        this.action = action;
    }

    /**
     * 像处理器中添加Transformation操作
     *
     * @param transformation 需要添加的Transformation操作
     */
    void add(Transformation transformation) {
        Tuple<Transformation, TransformationalType> tuple = new Tuple<>(transformation, transformation.type());
        if (tuple.value() == TransformationalType.SORT) { //sort操作
            if (tuple.key() instanceof PairMonad) {
                SortImpl sort = (SortImpl) tuple.key();
                order = sort.getSortRule();
                isSorted = true;
            } else if (tuple.key() instanceof CommonMonad) {
                com.yunfan.forethought.api.impls.transformation.common.SortImpl sort =
                        (com.yunfan.forethought.api.impls.transformation.common.SortImpl) tuple.key();
                if (sort.getSortRule().isPresent()) { //存在自定义Comparator
                    order = (Comparator) sort.getSortRule().get();
                }
                isSorted = true;
            } else {
                throw new UnsupportedOperationException("执行引擎发现除了PairMonad和CommonMonad以外的Monad，执行失败！");
            }
        }
        transformations.add(tuple);
    }

    /**
     * 进行计算得到最终结果
     *
     * @param dataSource 数据源迭代器
     * @return 最终结果
     */
    @SuppressWarnings("unchecked")
    //因为在DAG端实际类型约束就丢失了，所以类型安全必须用程序逻辑来保证
    R process(RepeatableIterator<?> dataSource) {
        try {
            Iterator<?>[] iterator = {new Iterator<Object>() {

                Queue<Object> pairFlatMapIterators = new LinkedList<>();

                Queue<Object> commonFlatMapIterators = new LinkedList<>();

                private int flatMapPos = -1;
                private int unionPos = -1;

                @Override
                public boolean hasNext() {
                    return dataSource.hasNext() || !pairFlatMapIterators.isEmpty() || !commonFlatMapIterators.isEmpty();
                }

                @Override
                public Object next() {
                    Object data = null;
                    if (dataSource.hasNext()) {
                        data = dataSource.next();
                    } else if (pairFlatMapIterators.isEmpty() && commonFlatMapIterators.isEmpty()) {
                        throw new NoSuchElementException("迭代器已经没有元素了，不能执行next()！");
                    }
                    for (int index = 0; index < transformations.size(); index++) {
                        Tuple<Transformation, TransformationalType> tuple = transformations.get(index);
                        if (tuple.value() == TransformationalType.FILTER) { //filter操作
                            if (data == null) { //这时数据来自flatMap和Union
                                if (index < flatMapPos || index < unionPos) {
                                    data = !pairFlatMapIterators.isEmpty() ?
                                            pairFlatMapIterators.remove() : commonFlatMapIterators.remove();
                                    continue;
                                }
                            }
                            if (tuple.key() instanceof PairMonad) {
                                FilterImpl filter = (FilterImpl) tuple.key();
                                Predicate predicate = filter.getTransformationalFunction();
                                if (!predicate.test(data)) {
                                    return null; //如果不符合过滤条件，直接过滤掉
                                }
                            } else if (tuple.key() instanceof CommonMonad) {
                                com.yunfan.forethought.api.impls.transformation.common.FilterImpl filter =
                                        (com.yunfan.forethought.api.impls.transformation.common.FilterImpl) tuple.key();
                                Predicate predicate = filter.getTransformationalFunction();
                                if (!predicate.test(data)) {
                                    return null; //如果不符合过滤条件，直接过滤掉
                                }
                            } else {
                                throw new UnsupportedOperationException("执行引擎发现除了PairMonad和CommonMonad以外的Monad，执行失败！");
                            }
                        } else if (tuple.value() == TransformationalType.FLATMAP) { //flatMap操作
                            flatMapPos = index;
                            if (tuple.key() instanceof PairMonad) {
                                FlatMapImpl flatMap = (FlatMapImpl) tuple.key();
                                Function function = flatMap.getTransformationalFunction();
                                PairMonad pair = (PairMonad) function.apply(data);
                                pair.toIterator().forEachRemaining(pairFlatMapIterators::add);
                            } else if (tuple.key() instanceof CommonMonad) {
                                com.yunfan.forethought.api.impls.transformation.common.FlatMapImpl flatMap =
                                        (com.yunfan.forethought.api.impls.transformation.common.FlatMapImpl) tuple.key();
                                CommonMonad common = (CommonMonad) flatMap.getTransformationalFunction().apply(data);
                                common.toIterator().forEachRemaining(commonFlatMapIterators::add);
                            } else {
                                throw new UnsupportedOperationException("执行引擎发现除了PairMonad和CommonMonad以外的Monad，执行失败！");
                            }
                        } else if (tuple.value() == TransformationalType.MAP) { //map操作
                            if (data == null) { //这时数据来自flatMap和Union
                                if (index < Math.min(flatMapPos, unionPos)) {
                                    data = !pairFlatMapIterators.isEmpty() ?
                                            pairFlatMapIterators.remove() : commonFlatMapIterators.remove();
                                }
                            }
                            if (tuple.key() instanceof PairMonad) {
                                MapImpl map = (MapImpl) tuple.key();
                                Function function = map.getTransformationalFunction();
                                data = function.apply(data);
                            } else if (tuple.key() instanceof CommonMonad) {
                                com.yunfan.forethought.api.impls.transformation.common.MapImpl map =
                                        (com.yunfan.forethought.api.impls.transformation.common.MapImpl) tuple.key();
                                Function function = map.getTransformationalFunction();
                                data = function.apply(data);
                            } else {
                                throw new UnsupportedOperationException("执行引擎发现除了PairMonad和CommonMonad以外的Monad，执行失败！");
                            }
                        } else if (tuple.value() == TransformationalType.SORT) { //sort操作
                            if (tuple.key() instanceof CommonMonad) {
                                com.yunfan.forethought.api.impls.transformation.common.SortImpl sort =
                                        (com.yunfan.forethought.api.impls.transformation.common.SortImpl) tuple.key();
                                if (sort.getSortRule().isPresent()) { //存在自定义Comparator
                                    order = (Comparator) sort.getSortRule().get();
                                } else {
                                    if (!(data instanceof Comparable)) {
                                        throw new IllegalArgumentException("需要排序的对象不是Comparable对象！");
                                    }
                                }
                                isSorted = true;
                            } else {
                                throw new UnsupportedOperationException("执行引擎发现除了PairMonad和CommonMonad以外的Monad，执行失败！");
                            }
                        } else if (tuple.value() == TransformationalType.UNION) { //union操作
                            unionPos = index;
                            if (tuple.key() instanceof PairMonad) {
                                UnionImpl union = (UnionImpl) tuple.key();
                                PairMonad pair = union.other();
                                pair.toIterator().forEachRemaining(pairFlatMapIterators::add);
                            } else if (tuple.key() instanceof CommonMonad) {
                                com.yunfan.forethought.api.impls.transformation.common.UnionImpl union =
                                        (com.yunfan.forethought.api.impls.transformation.common.UnionImpl) tuple.key();
                                CommonMonad common = union.other();
                                common.toIterator().forEachRemaining(commonFlatMapIterators::add);
                            } else {
                                throw new UnsupportedOperationException("执行引擎发现除了PairMonad和CommonMonad以外的Monad，执行失败！");
                            }
                        }
                    }
                    return data;
                }
            }};

            Iterator<Object> resultIterator = new Iterator<Object>() {

                private Object now = null;
                private boolean nowDefined = false;

                @Override
                public boolean hasNext() {
                    if (nowDefined) {
                        return true;
                    }
                    do {
                        if (!iterator[0].hasNext()) {
                            return false;
                        }
                        now = iterator[0].next();
                    } while (now == null);
                    nowDefined = true;
                    return true;
                }

                @Override
                public Object next() {
                    if (hasNext()) {
                        nowDefined = false;
                        return now;
                    } else {
                        throw new NoSuchElementException("迭代器不能再迭代了！");
                    }
                }
            };
            if (isSorted) {
                resultIterator = sortIterator(resultIterator, order);
            }
            //Action操作逻辑
            if (actionType == ActionType.COLLECT) { //Collect操作就返回迭代器
                return (R) resultIterator;
            } else if (actionType == ActionType.PREDICATE) { //Predicate操作返回Boolean值
                PredicateImpl predicate = (PredicateImpl) action;
                boolean isAny = predicate.isAny();
                Object next = resultIterator.next();
                while (resultIterator.hasNext()) {
                    if (isAny) { //是any的情况下，返回一个true就返回
                        boolean test = predicate.actionFunc().test(next);
                        if (test) {
                            return (R) Boolean.TRUE;
                        }
                    } else { //不是any的情况，必须全部为true
                        boolean test = predicate.actionFunc().test(next);
                        if (!test) { //如果有一个不成立就返回False
                            return (R) Boolean.FALSE;
                        }
                    }
                }
                if (isAny) { //any的情况下循环内没返回说明不存在任何一个为true
                    return (R) Boolean.FALSE;
                } else { //all的情况下循环内没返回说明全部都成立
                    return (R) Boolean.TRUE;
                }
            } else if (actionType == ActionType.REDUCE) { //Reduce操作就互相累加
                ReduceImpl reduce = (ReduceImpl) action;
                Object now = null;
                Object next = resultIterator.next();
                Object next2 = resultIterator.next();
                while (resultIterator.hasNext()) {
                    if (now == null) {
                        now = next;
                    }
                    if (next2 != null) { //如果数据源只有一个元素，这里就需要判断一下
                        now = reduce.actionFunc().apply(now, next2); //累加
                    } else {
                        return (R) now;
                    }
                    next2 = resultIterator.next();
                }
                return (R) now;
            } else if (actionType == ActionType.DROP) {
                DropImpl drop = (DropImpl) action;
                List result = new LinkedList();
                int dropNum = drop.dropNumber();
                Object next = resultIterator.next();
                if (drop.isStartWithLeft()) { //drop
                    while (resultIterator.hasNext()) {
                        if (dropNum <= 0) { //如果drop完成了，则添加数据
                            result.add(next);
                            next = resultIterator.next();
                        } else { //忽略数据
                            next = resultIterator.next();
                        }
                        dropNum--; //每次减小数量
                    }
                    if (isSorted) {
                        Collections.sort(result);
                    }
                    return (R) result;
                } else { //dropRight
                    int length = 0;
                    List list = new LinkedList();
                    while (resultIterator.hasNext()) {
                        list.add(next);
                        next = resultIterator.next();
                        length++;
                    }
                    return (R) list.subList(0, Math.min(length, length - dropNum));
                }
            } else if (actionType == ActionType.TAKE) {
                TakeImpl take = (TakeImpl) action;
                Predicate predicate = take.actionFunc();
                List result = new LinkedList();
                while (resultIterator.hasNext()) { //拿到转换后的数据对象
                    Object nextData = resultIterator.next();
                    if (predicate.test(nextData)) { //如果函数测试成立，则直接返回结果
                        return (R) result;
                    } else { //否则就添加结果集
                        result.add(nextData);
                    }
                }
                return (R) result; //函数一直不成立，数据都遍历完了就返回
            } else {
                throw new UnsupportedOperationException("执行引擎未知的Action操作！");
            }
        } catch (ClassCastException ex) { //中间类型不安全引发异常
            throw new IllegalStateException("执行引擎逻辑出错，Action操作类型错误，类型转换不能完成！", ex);
        }
    }

    /**
     * 对迭代器进行排序
     *
     * @param iterator   需要排序的迭代器
     * @param comparator Comparator对象，如果迭代器元素是Comparable的，则可以传null
     * @param <T>        迭代器元素类型
     * @return 排好序的迭代器
     */
    @SuppressWarnings("unchecked")
    private <T> Iterator<T> sortIterator(Iterator<T> iterator, Comparator<T> comparator) {
        List<T> list = new LinkedList<>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        if (comparator != null) {
            list.sort(comparator);
        } else {
            list.sort((Comparator) Comparator.naturalOrder());
        }
        return list.iterator();
    }
}
