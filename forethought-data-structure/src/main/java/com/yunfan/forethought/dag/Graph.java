package com.yunfan.forethought.dag;

import java.util.*;
import java.util.function.Consumer;

/**
 * 图数据结构实现
 *
 * @param <T> 存储数据类型
 */
public class Graph<T> {

    /**
     * 图的顶点集合
     */
    private final List<Vertex<T>> items;

    /**
     * 图中边的数量
     */
    private int edgeNum = 0;

    /**
     * 图中顶点的数量
     */
    private int vertexNum = 0;

    /**
     * 图的构造方法
     *
     * @param capacity 初始化容量
     */
    public Graph(int capacity) {
        items = new ArrayList<>(capacity);
    }

    /**
     * 图的默认构造方法（初始容量为10）
     */
    public Graph() {
        this(10);
    }

    /**
     * 添加一个顶点（不能重复添加相同顶点）
     *
     * @param item 新顶点值
     */
    public void addVertex(T item) {
        if (this.contains(item)) //不允许插入重复顶点值
            throw new IllegalArgumentException("插入了重复顶点！");
        items.add(new Vertex<>(item));
        vertexNum++;
    }

    /**
     * 在两个顶点之间添加无向边
     *
     * @param from 第一个顶点元素
     * @param to   第二个顶点元素
     */
    public void addEdge(T from, T to) {
        Vertex<T> fromVertex = this.find(from).orElseThrow(() -> new IllegalArgumentException("头顶点并不存在！"));
        Vertex<T> toVertex = this.find(to).orElseThrow(() -> new IllegalArgumentException("尾顶点并不存在！"));
        //无向边两个顶点都要记录信息
        this.addDirectedEdge(fromVertex, toVertex);
        this.addDirectedEdge(toVertex, fromVertex);
        edgeNum++;
    }

    /**
     * 在两个顶点之间添加有向边
     *
     * @param from 第一个顶点元素
     * @param to   第二个顶点元素
     */
    public void addDirectedEdge(T from, T to) {
        Vertex<T> fromVertex = this.find(from).orElseThrow(() -> new IllegalArgumentException("头顶点并不存在！"));
        Vertex<T> toVertex = this.find(to).orElseThrow(() -> new IllegalArgumentException("尾顶点并不存在！"));
        this.addDirectedEdge(fromVertex, toVertex);
        edgeNum++;
    }

    /**
     * 在两个顶点之间添加有向边
     *
     * @param fromVertex 第一个顶点
     * @param toVertex   第二个顶点
     */
    private void addDirectedEdge(Vertex<T> fromVertex, Vertex<T> toVertex) {
        if (fromVertex.getFirstEdge() == null) {
            fromVertex.setFirstEdge(new Node<>(toVertex));
        } else {
            Node<T> temp;
            Node<T> node = fromVertex.getFirstEdge();
            do {
                if (node.getAdjVertex().getData().equals(toVertex.getData())) {
                    throw new IllegalArgumentException("添加了重复的边！");
                }
                temp = node;
                node = node.getNext();
            } while (node != null);
            temp.setNext(new Node<>(toVertex));
        }
    }

    /**
     * 查找图中是否包含某项元素
     *
     * @param item 查找的元素对象
     * @return 是否包含
     */
    public boolean contains(T item) {
        for (Vertex<T> value : items) {
            if (value.getData().equals(item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 查找指定元素并返回
     *
     * @param item 需要查找的元素值
     * @return 返回查找的Optional结果
     */
    private Optional<Vertex<T>> find(T item) {
        for (Vertex<T> value : items) {
            if (value.getData().equals(item)) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }

    /**
     * 初始化访问标志
     */
    private void initVisited() {
        for (Vertex<T> value : items) {
            value.setVisited(false);
        }
    }

    /**
     * 执行深度优先遍历
     *
     * @param consumer 遍历访问元素执行的函数
     */
    public void forEachByDFS(Consumer<T> consumer) {
        if (items.isEmpty()) { //空集合不作任何操作
            return;
        }
        initVisited();
        dfs(items.get(0), consumer); //获取首顶点开始遍历
    }

    /**
     * 深度优先遍历
     *
     * @param value    开始遍历的顶点值
     * @param consumer 遍历元素访问函数
     */
    private void dfs(Vertex<T> value, Consumer<T> consumer) {
        value.setVisited(true); //设置访问标志
        consumer.accept(value.getData()); //执行遍历访问函数
        Node<T> node = value.getFirstEdge();
        while (node != null) { //访问此顶点的所有邻接点
            if (!node.getAdjVertex().isVisited()) { //如果邻接点未被访问过，则递归访问它的边
                dfs(node.getAdjVertex(), consumer);
            }
            node = node.getNext(); //访问下一个邻接点
        }
    }

    /**
     * 执行广度优先遍历
     *
     * @param consumer 遍历访问元素执行的函数
     */
    public void forEachByBFS(Consumer<T> consumer) {
        if (items.isEmpty()) { //空集合不作任何操作
            return;
        }
        initVisited();
        Vertex<T> value = items.get(0); //获取首顶点开始遍历
        Queue<Vertex<T>> queue = new LinkedList<>(); //创建一个队列
        consumer.accept(value.getData()); //访问第一个元素
        value.setVisited(true);
        queue.add(value); //入队
        while (!queue.isEmpty()) { //如果队列不为空
            Vertex<T> w = queue.remove();
            Node<T> node = w.getFirstEdge();
            while (node != null) { //访问此顶点的所有邻接点
                if (!node.getAdjVertex().isVisited()) { //如果邻接点没有被访问，则访问它的边
                    consumer.accept(node.getAdjVertex().getData()); //访问
                    node.getAdjVertex().setVisited(true); //设置访问标志
                    queue.add(node.getAdjVertex()); //入队
                }
                node = node.getNext(); //访问下一个邻接点
            }
        }

    }

    /**
     * 重写图的toString方法
     *
     * @return 顶点对应邻接表字符串
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Vertex<T> value : items) {
            result.append(value.getData()).append(":");
            if (value.getFirstEdge() != null) {
                Node<T> temp = value.getFirstEdge();
                while (temp != null) {
                    result.append(temp.getAdjVertex().getData());
                    temp = temp.getNext();
                }
            }
            result.append(System.getProperty("line.separator")); //换行
        }
        return result.toString();
    }

    /**
     * @return 图中边的数量
     */
    public int getEdgeNum() {
        return edgeNum;
    }

    /**
     * @return 图中顶点的数量
     */
    public int getVertexNum() {
        return vertexNum;
    }
}
