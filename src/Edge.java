import com.sun.istack.internal.NotNull;

public abstract class Edge<T, V> {

    /**
     * 对无权图，用true或false表示相邻否；
     * 对带权图，则为权值。
     */
    private T weight;
    /**
     * info表示该弧/边携带的信息
     */
    private V info;

    public Edge(T weight, V info) {
        this.weight = weight;
        this.info = info;
    }

    @Override
    public String toString() {
        return "test.test.ArcCell{" +
                "adj=" + weight +
                ", info=" + info +
                '}';
    }


}
