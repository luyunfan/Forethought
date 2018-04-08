package com.yunfan.forethought.task;

import com.yunfan.forethought.api.impls.action.*;
import com.yunfan.forethought.api.impls.transformation.Transformation;
import com.yunfan.forethought.api.monad.CommonMonad;
import com.yunfan.forethought.api.monad.PairMonad;
import com.yunfan.forethought.enums.ActionType;
import com.yunfan.forethought.enums.TransformationalType;
import com.yunfan.forethought.iterators.RepeatableIterator;
import com.yunfan.forethought.type.Tuple;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
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

    /**
     * 构造方法，需要传入最终的Action操作
     *
     * @param action Action操作对象
     */
    TransformationProcessor(Action<R, ?> action) {
        if (action instanceof CollectImpl<?>) {
            actionType = ActionType.COLLECT;
        } else if (action instanceof PredicateImpl) {
            actionType = ActionType.PREDICATE;
        } else if (action instanceof ReduceImpl) {
            actionType = ActionType.REDUCE;
        } else {
            throw new UnsupportedOperationException("执行引擎未知的Action操作！");
        }
        this.action = action;
    }

    /**
     * 像处理器中添加Transformation操作
     *
     * @param transformation 需要添加的Transformation操作
     */
    void add(Transformation transformation) {
        if (transformation instanceof com.yunfan.forethought.api.impls.transformation.pair.FilterImpl || //filter
                transformation instanceof com.yunfan.forethought.api.impls.transformation.common.FilterImpl) {
            transformations.add(new Tuple<>(transformation, TransformationalType.FILTER));
        } else if (transformation instanceof com.yunfan.forethought.api.impls.transformation.pair.FlatMapImpl || //flatMap
                transformation instanceof com.yunfan.forethought.api.impls.transformation.common.FlatMapImpl) {
            transformations.add(new Tuple<>(transformation, TransformationalType.FLATMAP));
        } else if (transformation instanceof com.yunfan.forethought.api.impls.transformation.pair.MapImpl || //map
                transformation instanceof com.yunfan.forethought.api.impls.transformation.common.MapImpl) {
            transformations.add(new Tuple<>(transformation, TransformationalType.MAP));
        } else if (transformation instanceof com.yunfan.forethought.api.impls.transformation.pair.SortImpl || //sort
                transformation instanceof com.yunfan.forethought.api.impls.transformation.common.SortImpl) {
            transformations.add(new Tuple<>(transformation, TransformationalType.SORT));
        } else if (transformation instanceof com.yunfan.forethought.api.impls.transformation.pair.UnionImpl || //union
                transformation instanceof com.yunfan.forethought.api.impls.transformation.common.UnionImpl) {
            transformations.add(new Tuple<>(transformation, TransformationalType.UNION));
        } else if (transformation instanceof com.yunfan.forethought.api.impls.transformation.pair.ShuffleMonad) {
            transformations.add(new Tuple<>(transformation, TransformationalType.SHUFFLE));
        } else {
            throw new UnsupportedOperationException("执行引擎不能识别中间的Transformation操作！");
        }
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
//        int[] temp = {0, 0, 0};//Drop、take操作的记录数  todo 不能这样实现，必须把代码融合在迭代器中
//        Function<Object, Object> processAllTransformation = data -> {//为某一个数据元素执行全部的Transformation操作的函数
//            Object result = null;
//            for (Tuple<Transformation, TransformationalType> tuple : transformations) {
//                if (tuple.value() == TransformationalType.DROP) {
//                    if (tuple.key() instanceof PairMonad) {
//                        com.yunfan.forethought.api.impls.transformation.pair.DropImpl drop = (DropImpl) tuple.key();
//                    } else if (tuple.key() instanceof CommonMonad) {
//                        com.yunfan.forethought.api.impls.action.DropImpl drop =
//                                (com.yunfan.forethought.api.impls.action.DropImpl) tuple.key();
//                    } else {
//                        throw new UnsupportedOperationException("执行引擎发现除了PairMonad和CommonMonad以外的Monad，执行失败！");
//                    }
//                }
//            }
//            return result;
//        };
        try {
            Iterator<?> iterator = new Iterator<Object>() {

                @Override
                public boolean hasNext() {
                    return dataSource.hasNext();
                }

                @Override
                public Object next() { //todo 1.把tran操作融合在next内 2.下面其他操作全部调用这个result，迭代器生成放到if外面
//                    for (Tuple<Transformation, TransformationalType> tuple : transformations) {
//                        if (tuple.value() == TransformationalType.DROP) {
//                            if (tuple.key() instanceof PairMonad) {
//                                com.yunfan.forethought.api.impls.transformation.pair.DropImpl drop = (DropImpl) tuple.key();
//
//                            } else if (tuple.key() instanceof CommonMonad) {
//                                com.yunfan.forethought.api.impls.action.DropImpl drop =
//                                        (com.yunfan.forethought.api.impls.action.DropImpl) tuple.key();
//                            } else {
//                                throw new UnsupportedOperationException("执行引擎发现除了PairMonad和CommonMonad以外的Monad，执行失败！");
//                            }
//                        }
//                    }
                    return null;
                }
            };
            if (actionType == ActionType.COLLECT) { //Collect操作就返回迭代器
                return (R) iterator;
            } else if (actionType == ActionType.PREDICATE) { //Predicate操作返回Boolean值
                PredicateImpl predicate = (PredicateImpl) action;
                boolean isAny = predicate.isAny();
                while (iterator.hasNext()) {
                    if (isAny) { //是any的情况下，返回一个true就返回
                        Boolean test = predicate.actionFunc().test(iterator.next());
                        if (test) {
                            return (R) Boolean.TRUE;
                        }
                    } else { //不是any的情况，必须全部为true
                        Boolean test = predicate.actionFunc().test(iterator.next());
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
                while (iterator.hasNext()) {
                    if (now == null) {
                        now = iterator.next();
                    }
                    if (iterator.hasNext()) { //如果数据源只有一个元素，这里就需要判断一下
                        now = reduce.actionFunc().apply(now, iterator.next()); //累加
                    } else {
                        return (R) now;
                    }
                }
                return (R) now;
            } else if (actionType == ActionType.DROP) {
                DropImpl drop = (DropImpl) action;
                List result = new LinkedList();
                int dropNum = drop.dropNumber();
                if (drop.isStartWithLeft()) { //drop
                    while (iterator.hasNext()) {
                        if (dropNum <= 0) { //如果drop完成了，则添加数据
                            result.add(iterator.next());
                        }
                        dropNum--; //每次减小数量
                    }
                    return (R) result;
                } else { //dropRight
                    int length = 0;
                    List list = new LinkedList();
                    while (iterator.hasNext()) {
                        list.add(iterator.next());
                        length++;
                    }
                    return (R) list.subList(0, Math.min(length, length - dropNum));
                }
            } else if (actionType == ActionType.TAKE) {
                TakeImpl take = (TakeImpl) action;
                Predicate predicate = take.actionFunc();
                List result = new LinkedList();
                while (iterator.hasNext()) { //拿到转换后的数据对象
                    Object nextData = iterator.next();
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
}
