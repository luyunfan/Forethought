package com.yunfan.forethought.dag;

class Vertex<T> {

    /**
     * 顶点的构造函数
     *
     * @param value 初始化顶点数据值
     */
    public Vertex(T value) {
        this.data = value;
    }

    /**
     * 数据
     */
    private final T data;

    /**
     * 邻接点链表头指针
     */
    private Node<T> firstEdge;

    /**
     * 访问标志，遍历时使用
     */
    private boolean visited;

    /**
     * 获取邻接点链表头指针
     *
     * @return 邻接点链表头指针
     */
    public Node<T> getFirstEdge() {
        return firstEdge;
    }

    /**
     * 修改邻接点链表头指针
     *
     * @param firstEdge 新的邻接点链表头指针
     */
    public void setFirstEdge(Node<T> firstEdge) {
        this.firstEdge = firstEdge;
    }

    /**
     * 访问标志，遍历时使用
     *
     * @return 是否被访问过
     */
    public boolean isVisited() {
        return visited;
    }

    /**
     * 修改访问标志
     *
     * @param visited 新的访问标志
     */
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    /**
     * 获取数据值
     *
     * @return 顶点中保存的数据
     */
    public T getData() {
        return data;
    }
}
