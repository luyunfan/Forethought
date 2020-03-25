package com.yunfan.forethought.task;

import com.yunfan.forethought.api.impls.action.*;
import com.yunfan.forethought.api.impls.transformation.Transformation;
import com.yunfan.forethought.api.impls.transformation.common.*;
import com.yunfan.forethought.api.impls.transformation.pair.AggregateShuffleImpl;
import com.yunfan.forethought.api.impls.transformation.pair.CombineShuffleImpl;
import com.yunfan.forethought.api.impls.transformation.pair.JoinImpl;
import com.yunfan.forethought.api.impls.transformation.pair.SortedShuffleImpl;
import com.yunfan.forethought.api.monad.CommonMonad;
import com.yunfan.forethought.api.monad.PairMonad;
import com.yunfan.forethought.enums.ActionType;
import com.yunfan.forethought.iterators.RepeatableIterator;
import com.yunfan.forethought.type.Tuple;

import java.util.*;
import java.util.function.BiFunction;
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
    private final List<Transformation> transformations = new LinkedList<>();

    /**
     * Action操作对象
     */
    private final Action<R, ?> action;

    /**
     * Action操作对应的类型
     */
    private final ActionType actionType;

    @SuppressWarnings("rawtypes")
    private List memoryCache = new LinkedList();

    @SuppressWarnings("rawtypes")
    private List memoryCacheTemp = new LinkedList();

    //@SuppressWarnings("rawtypes")
    private Map<Object, Object> shuffleMap = new HashMap<>();

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
        transformations.add(transformation);
    }

    /**
     * 进行计算得到最终结果
     *
     * @param dataSource 数据源迭代器
     * @return 最终结果
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    //因为在DAG端实际类型约束就丢失了，所以类型安全必须用程序逻辑来保证
    R process(RepeatableIterator<?> dataSource) {
        loadData(dataSource);
        for (Transformation task : transformations) {
            if (task instanceof FilterImpl) {
                for (Object item : memoryCache) {
                    if (((FilterImpl) task)
                            .getTransformationalFunction().test(item)) {
                        memoryCacheTemp.add(item);
                    }
                }
                memoryCache = memoryCacheTemp;
                memoryCacheTemp = new LinkedList();
            } else if (task instanceof com.yunfan.forethought.
                    api.impls.transformation.pair.FilterImpl) {
                for (Object item : memoryCache) {
                    Predicate func = ((com.yunfan.forethought.
                            api.impls.transformation.pair.FilterImpl) task)
                            .getTransformationalFunction();
                    if (func.test(item)) {
                        memoryCacheTemp.add(item);
                    }
                }
                memoryCache = memoryCacheTemp;
                memoryCacheTemp = new LinkedList();
            } else if (task instanceof MapImpl) {
                for (Object item : memoryCache) {
                    memoryCacheTemp.add(
                            ((MapImpl) task)
                                    .getTransformationalFunction().apply(item)
                    );
                }
                memoryCache = memoryCacheTemp;
                memoryCacheTemp = new LinkedList<>();
            } else if (task instanceof
                    com.yunfan.forethought.api
                            .impls.transformation.pair.MapImpl) {
                for (Object item : memoryCache) {
                    memoryCacheTemp.add(
                            ((com.yunfan.forethought.api
                                    .impls.transformation.pair.MapImpl) task)
                                    .getTransformationalFunction().apply(item)
                    );
                }
                memoryCache = memoryCacheTemp;
                memoryCacheTemp = new LinkedList();
            } else if (task instanceof SortImpl) {
                Optional<Comparator> comparator = ((SortImpl) task).getSortRule();
                if (comparator.isPresent()) {
                    memoryCache.sort((Comparator<? super Object>) comparator.get());
                } else {
                    Collections.sort(memoryCache);
                }
            } else if (task instanceof com.yunfan.forethought
                    .api.impls.transformation.pair.SortImpl) {
                Comparator comparator = ((com.yunfan.forethought.api.impls.transformation.pair.SortImpl) task).getSortRule();
                memoryCache.sort((Comparator<? super Object>) comparator);
            } else if (task instanceof UnionImpl) {
                ((UnionImpl) task).other()
                        .foreach(memoryCache::add);
            } else if (task instanceof com.yunfan.forethought.api.impls.transformation.pair.UnionImpl) {
                ((com.yunfan.forethought.api.impls.
                        transformation.pair.UnionImpl) task)
                        .other()
                        .foreach(memoryCache::add);
            } else if (task instanceof FlatMapImpl) {
                for (Object item : memoryCache) {
                    ((CommonMonad) ((FlatMapImpl) task)
                            .getTransformationalFunction()
                            .apply(item)).foreach(memoryCacheTemp::add);
                }
                memoryCache = memoryCacheTemp;
                memoryCacheTemp = new LinkedList();
            } else if (task instanceof com.yunfan.forethought
                    .api.impls.transformation.pair.FlatMapImpl) {
                for (Object item : memoryCache) {
                    ((PairMonad) ((com.yunfan.forethought.api.
                            impls.transformation.pair.FlatMapImpl) task)
                            .getTransformationalFunction()
                            .apply(item)).foreach(memoryCacheTemp::add);
                }
                memoryCache = memoryCacheTemp;
                memoryCacheTemp = new LinkedList();
            } else if (task instanceof AggregateShuffleImpl) {

                BiFunction func = ((AggregateShuffleImpl) task)
                        .getTransformationalFunction();
                doShuffle();
                memoryCache.clear();
                for (Map.Entry<Object, Object> shuffleTemp : shuffleMap.entrySet()) {
                    List<Object> values = (List<Object>) shuffleTemp.getValue();
                    Object first = values.get(0);
                    for (Object value : values) {
                        first = func.apply(first, value);
                    }
                    memoryCache.add(new Tuple<>(shuffleTemp.getKey(), first));
                }
            } else if (task instanceof CombineShuffleImpl) {
                doShuffle();
                memoryCache.clear();
                for (Map.Entry<Object, Object> shuffleTemp : shuffleMap.entrySet()) {
                    memoryCache.add(new Tuple<>(shuffleTemp.getKey(), shuffleTemp.getValue()));
                }
            } else if (task instanceof JoinImpl) {
                doShuffle();
                List<Tuple> otherPairs = ((JoinImpl) task).joinPair()
                        .toList();
                Map<Object, Tuple> resultMap = new HashMap<>();
                for (Tuple otherPair : otherPairs) {
                    Object key = otherPair.key();
                    if (shuffleMap.containsKey(key)) {
                        Object originalValue = shuffleMap.get(key);
                        resultMap.put(key, new Tuple<>(originalValue, otherPair.value()));
                    }
                }
                shuffleMap = (Map) resultMap;
            } else if (task instanceof SortedShuffleImpl) {
                doShuffle();
                Object[] keyArray = shuffleMap.keySet().toArray();
                Arrays.sort(keyArray);
                memoryCache.clear();
                for (Object key : keyArray) {
                    Object value = shuffleMap.get(key);
                    memoryCache.add(new Tuple<>(key, value));
                }
            } else {
                throw new UnsupportedOperationException("执行引擎探测到未知的Transformation类型：" + task.getClass() + "，无法完成计算！");
            }
        }

        //最后执行action操作
        switch (actionType) {
            case COLLECT:
                return (R) memoryCache.iterator();
            case DROP:
                List dropResultList = new ArrayList();
                DropImpl dropimpl = (DropImpl) action;
                int size = memoryCache.size();
                long dropNumber = dropimpl.dropNumber() > size ?
                        size : dropimpl.dropNumber();
                if (dropimpl.isStartWithLeft()) {
                    for (Object item : memoryCache) {
                        if (dropNumber > 0) {
                            dropNumber--;
                            continue;
                        }
                        dropResultList.add(item);
                    }
                } else {
                    dropNumber -= size;
                    for (Object item : memoryCache) {
                        if (dropNumber == 0) {
                            break;
                        }
                        dropResultList.add(item);
                        dropNumber--;
                    }
                }
                return (R) dropResultList;
            case PREDICATE:
                PredicateImpl predicate = (PredicateImpl) action;
                boolean isAny = predicate.isAny();
                for (Object item : memoryCache) {
                    if (isAny) {
                        if (predicate.actionFunc().test(item)) {
                            return (R) Boolean.TRUE;
                        }
                    } else {
                        if (!predicate.actionFunc().test(item)) {
                            return (R) Boolean.FALSE;
                        }
                    }
                }
                if (isAny) {
                    return (R) Boolean.FALSE;  //最后都没return，肯定是False
                } else {
                    return (R) Boolean.TRUE;
                }
            case REDUCE:
                Object result = ((LinkedList) memoryCache).getFirst();
                for (Object item : memoryCache) {
                    if (result == item) {
                        continue;
                    }
                    result = ((ReduceImpl) action).actionFunc().apply(item, result);
                }
                return (R) result;
            case TAKE:
                List resultTakeList = new ArrayList();
                for (Object item : memoryCache) {
                    if (((TakeImpl) action).actionFunc().test(item)) {
                        resultTakeList.add(item);
                    }
                }
                return (R) resultTakeList;
            default:
                throw new UnsupportedOperationException("执行引擎探测到未知Action类型：" + actionType + "，无法完成计算！");
        }
    }

    /**
     * 从数据源读取数据
     *
     * @param dataSource 数据源迭代器
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private void loadData(RepeatableIterator dataSource) {
        while (dataSource.hasNext()) {
            this.memoryCache
                    .add(dataSource.next());
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void doShuffle() {
        shuffleMap.clear();
        for (Object tuple : memoryCache) {
            Object key = ((Tuple) tuple).key();
            Object value = ((Tuple) tuple).value();
            if (shuffleMap.containsKey(key)) {
                List<Object> valueList = (List<Object>) shuffleMap.get(key);
                valueList.add(value);
            } else {
                List<Object> valueList = new ArrayList<>();
                valueList.add(value);
                shuffleMap.put(key, valueList);
            }
        }
    }
}
