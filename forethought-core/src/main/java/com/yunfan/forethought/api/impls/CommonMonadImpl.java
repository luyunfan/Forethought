package com.yunfan.forethought.api.impls;

import com.yunfan.forethought.api.dependency.Dependency;
import com.yunfan.forethought.api.impls.action.Action;
import com.yunfan.forethought.api.impls.transformation.common.*;
import com.yunfan.forethought.api.monad.CommonMonad;
import com.yunfan.forethought.api.monad.Monad;
import com.yunfan.forethought.api.monad.PairMonad;
import com.yunfan.forethought.dag.Graph;
import com.yunfan.forethought.iterators.RepeatableIterator;
import com.yunfan.forethought.type.Tuple;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 基于Monad实现
 *
 * @param <T> 内部元素类型
 */
public class CommonMonadImpl<T> implements CommonMonad<T> {

    /**
     * 表示上层依赖对象，如果上层没有依赖（本Monad是数据源），则为null
     */
    private final Dependency<?> fatherDependency;

    /**
     * 表示数据来源的迭代器，如果本Monad不是数据源，则为null
     */
    private final RepeatableIterator<T> dataSource;

    /**
     * 代表首个元素的缓存
     */
    private T firstElement = null;

    /**
     * 代表本Monad的依赖对象
     */
    private final Dependency<T> thisDept = new Dependency<T>() {
        @Override
        public Monad<T> get() {
            return CommonMonadImpl.this;
        }
    };


    /**
     * 注入上层依赖的构造函数
     *
     * @param father 上层依赖对象
     */
    protected CommonMonadImpl(Dependency<?> father) {
        fatherDependency = father;
        dataSource = null;
    }

    /**
     * 注入数据源的构造函数
     *
     * @param source 数据源
     */
    public CommonMonadImpl(RepeatableIterator<T> source) {
        dataSource = source;
        fatherDependency = null;
    }


    /**
     * 将Monad映射为键值对Monad结构
     *
     * @param mapFunc 对每个元素执行的映射变换函数，必须映射为Tuple类型
     * @return 被变换后的PairMonad集合
     */
    @Override
    public <K, V> PairMonad<K, V> mapToPair(@NotNull Function<? super T, ? extends Tuple<K, V>> mapFunc) {
        return new com.yunfan.forethought.api.impls.transformation.pair.MapImpl<>(thisDept, mapFunc);
    }

    /**
     * 取出集合第一个元素
     *
     * @return 第一个元素
     */
    @Override
    public T first() {
//        if (firstElement != null) //返回缓存数据
//            return firstElement;
//        else if (fatherDependency == null && dataSource != null) //如果本Monad就是数据源，则返回迭代器的首个元素
//            return dataSource.firstElement();
//        else if (fatherDependency != null && dataSource == null) //如果不是数据源则递归寻找上一个依赖 TODO 依赖层数太多可能爆栈，后期需要优化
//            return fatherDependency.get().first();
//        else //肯定逻辑出错了
//            throw new IllegalArgumentException("调用Monad.first()出现内部错误！Monad既不是数据源也没有上层依赖！");
        return null;
    }

    /**
     * 过滤本数据元素，返回被过滤后的元素集合
     *
     * @param predicate 对每个元素执行的过滤函数，如果函数返回true，则保留该元素，否则放弃该元素
     * @return 被过滤后的元素集合
     */
    @Override
    public CommonMonad<T> filter(@NotNull Predicate<? super T> predicate) {
        return new FilterImpl<>(thisDept, predicate);
    }

    /**
     * 检测所有元素是否都符合输入验证函数的要求
     *
     * @param predicate 验证要求函数，输入当前元素，返回Boolean值
     * @return 是否所有元素都能通过检测
     */
    @Override
    public boolean all(@NotNull Predicate<? super T> predicate) {
        return false;
    }

    /**
     * 检测任一元素是否符合输入验证函数的要求
     *
     * @param predicate 验证要求函数，输入当前元素，返回Boolean值
     * @return 只要有元素能通过检测就返回true，否则返回false
     */
    @Override
    public boolean any(@NotNull Predicate<? super T> predicate) {
        return false;
    }

    /**
     * 对本集合每一个元素进行映射操作，将元素映射成不同的类型
     *
     * @param mapFunc 对每个元素执行的映射变换函数，函数输入参数为当前迭代元素，返回值为变换后的元素
     * @return 被变换后的元素集合
     */
    @Override
    public <R> CommonMonad<R> map(@NotNull Function<? super T, ? extends R> mapFunc) {
        return new MapImpl<>(thisDept, mapFunc);
    }

    /**
     * 对本集合中每一个元素执行映射操作，映射后为同样的集合，本方法将会把这些类似的集合压扁，将元素取出，整合为同一个集合返回
     *
     * @param mapFunc 对每个元素执行的映射变换函数，函数输入参数为当前迭代元素，返回值为变换后的包含元素的Monad对象
     * @return 被变换后的元素集合
     */
    @Override
    public <R> CommonMonad<R> flatMap(@NotNull Function<? super T, ? extends Monad<? extends R>> mapFunc) {
        return new FlatMapImpl<>(thisDept, mapFunc);
    }

    /**
     * 对本集合进行去重操作
     *
     * @return 去重后的元素集合
     */
    @Override
    public CommonMonad<T> distinct() {
        return mapToPair(item -> new Tuple<>(item, null)).reduceByKey((a, b) -> a).mapToNormal(Tuple::key);
    }

    /**
     * 对本集合进行排序操作，自定义元素排序方法
     *
     * @param comparator 自定义comparator
     * @return 排好序的集合
     */
    @Override
    public CommonMonad<T> sortWith(@NotNull Comparator<T> comparator) {
        return new SortImpl<>(thisDept, Optional.of(comparator));
    }

    /**
     * 从集合起始端扔掉一个元素组成集合
     *
     * @return 扔掉一个元素后的元素集合
     */
    @Override
    public CommonMonad<T> drop() {
        return new DropImpl<>(thisDept, 1, true);
    }

    /**
     * 从集合起始端取出一些元素组成集合
     *
     * @param dropNum 需要取出的元素数量
     * @return 取出一些元素后的元素集合
     */
    @Override
    public CommonMonad<T> drop(int dropNum) {
        return new DropImpl<>(thisDept, dropNum, true);
    }

    /**
     * 从集合末端取出一个元素组成集合
     *
     * @return 取出一个元素后的元素集合
     */
    @Override
    public CommonMonad<T> dropRight() {
        return new DropImpl<>(thisDept, 1, false);
    }

    /**
     * 从集合末端取出一些元素组成集合
     *
     * @param dropNum 需要取出的元素数量
     * @return 取出一些元素后的元素集合
     */
    @Override
    public CommonMonad<T> dropRight(int dropNum) {
        return new DropImpl<>(thisDept, dropNum, false);
    }

    /**
     * 将集合中的所有数据相互进行归并操作，计算出归并的最终值
     *
     * @param reduceFunc 对每两个元素进行互相归并的函数
     * @return 归并后计算得出的结果
     */
    @Override
    public T reduce(@NotNull BiFunction<T, T, T> reduceFunc) {
        return null;
    }


    /**
     * 将集合中所有元素转化为迭代器
     *
     * @return 一个 Iterator对象
     */
    @Override
    public Iterator<T> toIterator() {
        return null;
    }

    /**
     * 遍历数据集合中的所有元素
     *
     * @param forFunc 遍历时执行的操作
     */
    @Override
    public void foreach(@NotNull Consumer<? super T> forFunc) {
        this.toIterator().forEachRemaining(forFunc);
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
     * 取出集合一些元素组成List返回
     *
     * @param takeNum 取出的元素数量
     * @return 取出的元素组成的List对象
     */
    @Override
    public CommonMonad<T> take(int takeNum) {
        return null;
    }

    /**
     * 取出集合元素，直到func成立时
     *
     * @param func 取出元素直到成立的验证函数
     * @return 取出的元素组成的Monad对象
     */
    @Override
    public CommonMonad<T> takeWhile(@NotNull Predicate<T> func) {
        return null;
    }

    /**
     * 对其它同类型数据集合相连接，连接后将合并所有数据到一个新数据集合中
     *
     * @param other 其它同类型数据集合
     * @return 合并后的数据集合
     */
    @Override
    public CommonMonad<? extends T> union(@NotNull CommonMonad<? extends T> other) {
        return new UnionImpl<>(thisDept, other);
    }

    /**
     * 对本集合进行排序操作
     *
     * @return 排序后的集合
     */
    @Override
    public CommonMonad<T> sort() {
        return new SortImpl<>(thisDept, Optional.empty());
    }

    /**
     * 将集合中所有元素添加到数组中，返回数组
     *
     * @param arrayType 代表数组中元素类型
     * @return 包含集合中所有数据的数组
     */
    @Override
    @SuppressWarnings("unchecked") //Java API的类型返回Object，没有类型安全问题
    public T[] toArray(@NotNull Class<T> arrayType) {
        long length = count();
        if (length != (int) length)
            throw new UnsupportedOperationException("toArray()调用必须保证Monad元素数量在32位整数范围之内！数组不能容纳更多的元素");
        T[] result = (T[]) Array.newInstance(arrayType, (int) count());
        Iterator<T> iterator = toIterator();
        for (int index = 0; index < length; index++) {
            result[index] = iterator.next();
        }
        return result;
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
    protected Graph<Monad<T>> createDAG(Monad<T> finalMonad) {
        if (!(finalMonad instanceof Action)) {
            throw new IllegalStateException("创建有向无环图时，最终计算操作Monad：" + finalMonad + "不是Action操作！");
        }
        Graph<Monad<T>> result = new Graph<>();
        //TODO 待实现
        return result;
    }

}












