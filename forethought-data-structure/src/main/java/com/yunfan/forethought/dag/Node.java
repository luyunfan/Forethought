package com.yunfan.forethought.dag;

class Node<T> {

    /**
     * 邻接点域
     */
    private final Vertex<T> adjVertex;

    /**
     * 下一个邻接点指针域
     */
    private Node<T> next;

    /**
     * 构造方法
     *
     * @param value 邻接点域
     */
    public Node(Vertex<T> value) {
        this.adjVertex = value;
    }

    /**
     * 获取邻接点域
     *
     * @return 邻接点域
     */
    public Vertex<T> getAdjVertex() {
        return adjVertex;
    }

    /**
     * 获取下一个邻接点指针域
     *
     * @return 下一个邻接点指针域
     */
    public Node<T> getNext() {
        return next;
    }

    /**
     * 修改下一个邻接点指针域
     *
     * @param next 新的下一个邻接点指针域
     */
    public void setNext(Node<T> next) {
        this.next = next;
    }
}
