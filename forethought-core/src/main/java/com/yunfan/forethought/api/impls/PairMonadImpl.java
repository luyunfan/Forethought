package com.yunfan.forethought.api.impls;

import com.yunfan.forethought.api.dependency.Dependency;
import com.yunfan.forethought.api.impls.action.Action;
import com.yunfan.forethought.api.impls.action.CollectImpl;
import com.yunfan.forethought.api.impls.action.PredicateImpl;
import com.yunfan.forethought.api.impls.action.ReduceImpl;
import com.yunfan.forethought.api.impls.transformation.pair.*;
import com.yunfan.forethought.api.monad.CommonMonad;
import com.yunfan.forethought.api.monad.Monad;
import com.yunfan.forethought.api.monad.PairMonad;
import com.yunfan.forethought.api.task.JobSubmitter;
import com.yunfan.forethought.dag.Graph;
import com.yunfan.forethought.iterators.RepeatableIterator;
import com.yunfan.forethought.type.Tuple;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class PairMonadImpl<K, V> implements PairMonad<K, V> {

    /**
     * 代表本Monad的依赖对象
     */
    private final Dependency<Tuple<K, V>> thisDept = new Dependency<Tuple<K, V>>() {
        @Override
        public PairMonad<K, V> get() {
            return PairMonadImpl.this;
        }
    };

    /**
     * 上层依赖对象
     */
    private Dependency<?> fatherDependency;

    /**
     * 数据源
     */
    private RepeatableIterator<Tuple<K, V>> dataSource;

    /**
     * 代表首个元素的缓存
     */
    private Tuple<K, V> firstElement = null;

    /**
     * 注入上层依赖的构造函数
     *
     * @param father 上层依赖对象
     */
    protected PairMonadImpl(Dependency<?> father) {
        fatherDependency = father;
        dataSource = null;
    }

    /**
     * 注入数据源的构造函数
     *
     * @param source 数据源
     */
    public PairMonadImpl(RepeatableIterator<Tuple<K, V>> source) {
        dataSource = source;
        fatherDependency = null;
    }

    /**
     * 将PairMonad转换为普通的Monad结构
     *
     * @param mapFunc 进行转换映射的函数，输入参数为Tuple，返回值为单一类型的值
     * @return 返回包含映射函数返回值的Monad对象
     */
    @Override
    public <T> CommonMonad<T> mapToNormal(@NotNull Function<? super Tuple<K, V>, ? extends T> mapFunc) {
        return new com.yunfan.forethought.api.impls.transformation.common.MapImpl<>(thisDept, mapFunc);
    }

    /**
     * 对同一Key的元素进行reduce归并操作，得到归并后的PairMonad
     *
     * @param reduceFunc 进行归并的函数，输入参数为两两归并的两个参数，返回同类型归并后的结果
     * @return 归并后的PairMonad
     */
    @Override
    public PairMonad<K, V> reduceByKey(@NotNull BiFunction<V, V, V> reduceFunc) {
        return null;
    }

    /**
     * 根据Key进行排序
     *
     * @return 根据Key排好序的集合
     */
    @Override
    public PairMonad<K, V> sortByKey() {
        return null;
    }

    /**
     * 计算同一Key的元素数量，得到(Key,Number)键值对集合
     *
     * @return 一个包含(键, 数量)的Map对象
     */
    @Override
    public PairMonad<K, Long> countByKey() {
        return this.map(tuple -> new Tuple<>(tuple.key(), 1L))
                .reduceByKey((x, y) -> x + y);
    }

    /**
     * 取出集合第一个元素
     *
     * @return 第一个元素组成的Tuple
     */
    @Override
    public Tuple<K, V> first() {
        return getFirst();
    }

    /**
     * 获取第一个元素，如果是第一次调用则触发计算出第一个元素，否则就返回缓存值
     *
     * @return 第一个元素
     */
    private Tuple<K, V> getFirst() {
        if (firstElement == null) {
            firstElement = toIterator().next();
        }
        return firstElement;
    }

    /**
     * 取出集合一些元素组成Map返回
     *
     * @param takeNum 取出的元素数量
     * @return 取出的元素组成的Map对象
     */
    @Override
    public PairMonad<K, V> take(int takeNum) {
        return null;
    }

    /**
     * 取出集合元素，直到func成立时
     *
     * @param func 取出元素直到成立的验证函数
     * @return 取出的元素组成的Monad对象
     */
    @Override
    public PairMonad<K, V> takeWhile(@NotNull Predicate<Tuple<K, V>> func) {
        return null;
    }

    /**
     * 对其它Key相同的PairMonad执行内连接操作，得到连接后的PairMonad对象
     *
     * @param otherPairMonad 相同Key的其它PairMonad对象
     * @return 连接后的(Key, Tuple) PairMonad对象
     */
    @Override
    public <NK> PairMonad<K, Tuple<V, NK>> join(@NotNull PairMonad<K, NK> otherPairMonad) {
        return null;
    }

    /**
     * 对同一Key的元素执行group分组操作，得到分组后的PairMonad
     *
     * @return 进行分组后的(Key, Collection)类型的PairMonad对象
     */
    @Override
    public PairMonad<K, Collection<V>> groupByKey() {
        return null;
    }

    /**
     * 对其它同类型数据集合相连接，连接后将合并所有数据到一个新数据集合中
     *
     * @param other 其它同类型数据集合
     * @return 合并后的数据集合
     */
    @Override
    public PairMonad<? extends K, ? extends V> union(@NotNull PairMonad<? extends K, ? extends V> other) {
        return new UnionImpl<>(thisDept, other);
    }

    /**
     * 将集合中所有元素添加到数组中，返回数组
     *
     * @return 包含集合中所有数据的数组
     */
    @SuppressWarnings("unchecked") //Java API的类型返回Object，没有类型安全问题
    @Override
    public Tuple<K, V>[] toArray() {
        long length = count();
        if (length != (int) length)
            throw new UnsupportedOperationException("toArray()调用必须保证Monad元素数量在32位整数范围之内！数组不能容纳更多的元素");
        Tuple<K, V>[] result = (Tuple[]) Array.newInstance(Tuple.class, (int) count());
        Iterator<Tuple<K, V>> iterator = toIterator();
        for (int index = 0; index < length; index++) {
            result[index] = iterator.next();
        }
        return result;
    }

    /**
     * 过滤本数据元素，返回被过滤后的元素集合
     *
     * @param predicate 对每个元素执行的过滤函数，如果函数返回true，则保留该元素，否则放弃该元素
     * @return 被过滤后的元素集合
     */
    @Override
    public PairMonad<K, V> filter(@NotNull Predicate<? super Tuple<K, V>> predicate) {
        return new FilterImpl<>(thisDept, predicate);
    }

    /**
     * 检测所有元素是否都符合输入验证函数的要求
     *
     * @param predicate 验证要求函数，输入当前元素，返回Boolean值
     * @return 是否所有元素都能通过检测
     */
    @Override
    public boolean all(@NotNull Predicate<? super Tuple<K, V>> predicate) {
        PredicateImpl<Tuple<K, V>> action = new PredicateImpl<>(predicate, false);
        return JobSubmitter.INSTANCE.submitTask(createDAG(this), action);
    }

    /**
     * 检测任一元素是否符合输入验证函数的要求
     *
     * @param predicate 验证要求函数，输入当前元素，返回Boolean值
     * @return 只要有元素能通过检测就返回true，否则返回false
     */
    @Override
    public boolean any(@NotNull Predicate<? super Tuple<K, V>> predicate) {
        PredicateImpl<Tuple<K, V>> action = new PredicateImpl<>(predicate, true);
        return JobSubmitter.INSTANCE.submitTask(createDAG(this), action);
    }

    /**
     * 对本集合每一个元素进行映射操作，将元素映射成不同的类型
     *
     * @param mapFunc 对每个元素执行的映射变换函数，函数输入参数为当前迭代元素，返回值为变换后的元素
     * @param <K1>    变换后生成的元素Key类型
     * @param <V1>    变换后生成的元素Value类型
     * @return 被变换后的元素集合
     */
    @Override
    public <K1, V1> PairMonad<K1, V1> map(@NotNull Function<? super Tuple<K, V>, ? extends Tuple<K1, V1>> mapFunc) {
        return new MapImpl<>(thisDept, mapFunc);
    }

    /**
     * 对本集合中每一个元素执行映射操作，映射后为同样的集合，本方法将会把这些类似的集合压扁，将元素取出，整合为同一个集合返回
     *
     * @param mapFunc 对每个元素执行的映射变换函数，函数输入参数为当前迭代元素，返回值为变换后的包含元素的Monad对象
     * @return 被变换后的元素集合
     */
    @Override
    public <K1, V1> PairMonad<K1, V1> flatMap(@NotNull Function<? super Tuple<K, V>, ? extends PairMonad<? extends K1, ? extends V1>> mapFunc) {
        return new FlatMapImpl<>(thisDept, mapFunc);
    }

    /**
     * 对本集合进行去重操作
     *
     * @return 去重后的元素集合
     */
    @Override
    public PairMonad<K, V> distinct() {
        return map(tuple -> new Tuple<>(tuple, null)).reduceByKey((a, b) -> a).map(Tuple::key);
    }

    /**
     * 对本集合进行排序操作，自定义元素排序方法
     *
     * @param comparator 自定义comparator
     * @return 排好序的集合
     */
    @Override
    public PairMonad<K, V> sortWith(@NotNull Comparator<Tuple<K, V>> comparator) {
        return new SortImpl<>(thisDept, comparator);
    }

    /**
     * 从集合起始端取出一个元素组成集合
     *
     * @return 取出一个元素后的元素集合
     */
    @Override
    public PairMonad<K, V> drop() {
        return new DropImpl<>(thisDept, 1, true);
    }

    /**
     * 从集合起始端取出一些元素组成集合
     *
     * @param dropNum 需要取出的元素数量
     * @return 取出一些元素后的元素集合
     */
    @Override
    public PairMonad<K, V> drop(int dropNum) {
        return new DropImpl<>(thisDept, dropNum, true);
    }

    /**
     * 从集合末端取出一个元素组成集合
     *
     * @return 取出一个元素后的元素集合
     */
    @Override
    public PairMonad<K, V> dropRight() {
        return new DropImpl<>(thisDept, 1, false);
    }

    /**
     * 从集合末端取出一些元素组成集合
     *
     * @param dropNum 需要取出的元素数量
     * @return 取出一些元素后的元素集合
     */
    @Override
    public PairMonad<K, V> dropRight(int dropNum) {
        return new DropImpl<>(thisDept, dropNum, false);
    }

    /**
     * 将集合中的所有数据相互进行归并操作，计算出归并的最终值
     *
     * @param reduceFunc 对每两个元素进行互相归并的函数
     * @return 归并后计算得出的结果
     */
    @Override
    public Tuple<K, V> reduce(@NotNull BiFunction<Tuple<K, V>, Tuple<K, V>, Tuple<K, V>> reduceFunc) {
        ReduceImpl<Tuple<K, V>> action = new ReduceImpl<>(reduceFunc);
        return JobSubmitter.INSTANCE.submitTask(createDAG(this), action);
    }

    /**
     * 将集合中所有元素转化为迭代器
     *
     * @return 一个 Iterator对象
     */
    @Override
    public Iterator<Tuple<K, V>> toIterator() {
        CollectImpl<Tuple<K, V>> action = new CollectImpl<>();
        return JobSubmitter.INSTANCE.submitTask(createDAG(this), action);
    }

    /**
     * 代表该Monad是否是数据源Monad
     *
     * @return 是否是包含数据的第一个Monad
     */
    @Override
    public boolean isDataSource() {
        return dataSource != null;
    }

    /**
     * @return 返回本Monad的上层依赖
     */
    @Override
    public Optional<Dependency<?>> getFatherDependency() {
        return fatherDependency == null ? Optional.empty() : Optional.of(fatherDependency);
    }

    /**
     * 获取中间转换的函数
     *
     * @return 中间转换操作函数对象
     */
    protected Object getTransformationalFunction() {
        throw new UnsupportedOperationException("本对象不是Transformation对象！不能调用getTransformationalFunction()方法！");
    }

    /**
     * 生成DAG
     *
     * @param finalMonad 最终的Monad对象
     * @return 根据Monad依赖生成的DAG
     */
    protected Graph<Monad<Tuple<K, V>>> createDAG(Monad<Tuple<K, V>> finalMonad) {
        Graph<Monad<Tuple<K, V>>> result = new Graph<>();
        //TODO 待实现
        return result;
    }
}
