package com.yunfan.forethought.api.monad;

import com.yunfan.forethought.api.dependency.Dependency;
import com.yunfan.forethought.dag.Graph;
import com.yunfan.forethought.enums.MonadType;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * 代表封装数据的惰性求值数据集合接口
 *
 * @param <T> 封装的数据类型
 */
public interface Monad<T> {

    /**
     * 检测所有元素是否都符合输入验证函数的要求
     *
     * @param predicate 验证要求函数，输入当前元素，返回Boolean值
     * @return 是否所有元素都能通过检测
     */
    boolean all(@NotNull Predicate<? super T> predicate);

    /**
     * 检测任一元素是否符合输入验证函数的要求
     *
     * @param predicate 验证要求函数，输入当前元素，返回Boolean值
     * @return 只要有元素能通过检测就返回true，否则返回false
     */
    boolean any(@NotNull Predicate<? super T> predicate);

    /**
     * 取出集合第一个元素
     *
     * @return 第一个元素
     */
    T first();

    /**
     * 将集合中的所有数据相互进行归并操作，计算出归并的最终值
     *
     * @param reduceFunc 对每两个元素进行互相归并的函数
     * @return 归并后计算得出的结果
     */
    T reduce(@NotNull BiFunction<T, T, T> reduceFunc);

    /**
     * 将集合中所有元素添加到List中，返回List
     *
     * @return 包含集合中所有数据的List
     */
    default List<T> toList() {
        List<T> result = new ArrayList<>();
        this.toIterator().forEachRemaining(result::add);
        return result;
    }

    /**
     * 将集合中所有元素添加到Set中，返回Set
     *
     * @return 包含集合中所有数据的Set
     */
    default Set<T> toSet() {
        Set<T> result = new HashSet<>();
        this.toIterator().forEachRemaining(result::add);
        return result;
    }

    /**
     * 将集合中所有元素转化为迭代器
     *
     * @return 一个 Iterator对象
     */
    Iterator<T> toIterator();

    /**
     * 返回本数据集合中元素数量的方法
     *
     * @return 数据集合中元素数量
     */
    default long count() {
        long[] result = {0};
        this.toIterator().forEachRemaining(item -> result[0]++);
        return result[0];
    }

    /**
     * 遍历数据集合中的所有元素
     *
     * @param forFunc 遍历时执行的操作
     */
    default void foreach(@NotNull Consumer<? super T> forFunc) {
        this.toIterator().forEachRemaining(forFunc);
    }

    /**
     * 代表集合是否为空
     *
     * @return 集合是否为空
     */
    default boolean isEmpty() {
        return this.toIterator().hasNext();
    }

    /**
     * 代表该Monad是否是数据源Monad
     *
     * @return 是否是包含数据的第一个Monad
     */
    boolean isDataSource();

    /**
     * @return 当前Monad的类型
     */
    MonadType monadType();

    /**
     * @return 返回本Monad的上层依赖
     */
    Optional<Dependency<?>> getFatherDependency();

    /**
     * 从当前Monad对象中生成生成DAG
     *
     * @return 根据Monad依赖生成的DAG
     */
    default Graph<Monad> createDAG() {
        Graph<Monad> result = new Graph<>();
        Monad temp = this;
        while (temp != null) {
            try {
                result.addVertex(temp);
            } catch (IllegalArgumentException ignored) { //插入重复顶点是插入了father的顶点，忽略掉
            }
            if (temp.getFatherDependency().isPresent()) { //还有上层依赖
                Monad father = ((Dependency<?>) temp.getFatherDependency().get()).get();
                try {
                    result.addVertex(father);
                } catch (IllegalArgumentException ignored) { //插入重复顶点是插入了father的顶点，忽略掉
                }
                result.addDirectedEdge(father, temp);
                temp = father;
            } else {
                temp = null;
            }
        }
        return result;
    }
}
