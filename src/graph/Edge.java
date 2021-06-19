package graph;

import com.sun.istack.internal.NotNull;

/**
 * 弧（Arc）/边（Edge）
 * 边：表示两个顶点之间的关系
 * 弧：有方向的边
 *
 * @param <T> 权值类型
 * @param <V> 信息类型
 */
public class Edge<T, V> {
    /**
     * 弧尾（Tail）/初始点
     * （在有向图中用于区分边的方向并用于记录顶点所对应的边在邻接矩阵中的坐标；在无向图中则没有方向可言）
     */
    @NotNull
    private final int tailIndex;

    /**
     * 弧头（Head）/终端点
     * （在有向图中用于区分边的方向并用于记录顶点所对应的边在邻接矩阵中的坐标；在无向图中则没有方向可言）
     */
    @NotNull
    private final int headIndex;

    /**
     * 对无权图，用true或false表示相邻否；
     * 对带权图，则为权值。
     */
    private Object weight;

    /**
     * info表示该弧/边携带的信息
     */
    private V info;

    public Edge(@NotNull int tailIndex, @NotNull int headIndex, Object weight, V info) {
        this.tailIndex = tailIndex;
        this.headIndex = headIndex;
        this.weight = weight;
        this.info = info;
    }

    public int getTailIndex() {
        return tailIndex;
    }

    public int getHeadIndex() {
        return headIndex;
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
                "tailIndex=" + tailIndex +
                ", headIndex=" + headIndex +
                ", weight=" + weight +
                ", info=" + info +
                '}';
    }
}
