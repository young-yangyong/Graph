package graph;

import com.sun.istack.internal.NotNull;

public  class Edge<T, V> {
    @NotNull
    private final int headIndex;
    @NotNull
    private final int tailIndex;

    /**
     * 对无权图，用true或false表示相邻否；
     * 对带权图，则为权值。
     */
    private Object weight;
    /**
     * info表示该弧/边携带的信息
     */
    private V info;

    public Edge(int headIndex, int tailIndex, Object weight, V info) {
        this.headIndex = headIndex;
        this.tailIndex = tailIndex;
        this.weight = weight;
        this.info = info;
    }

    public int getHeadIndex() {
        return headIndex;
    }

    public int getTailIndex() {
        return tailIndex;
    }

    @SuppressWarnings("unchecked")
    public T getWeight() {
        return (T) weight;
    }

    public void setWeight(Object weight) {
        this.weight = weight;
    }

    public V getInfo() {
        return info;
    }

    public void setInfo(V info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "headIndex=" + headIndex +
                ", tailIndex=" + tailIndex +
                ", weight=" + weight +
                ", info=" + info +
                '}';
    }
}
