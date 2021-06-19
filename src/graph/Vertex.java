package graph;

/**
 * 该类未启用（为具有平行边的多重图做准备）
 *
 * @param <E> 顶点携带信息的类型
 */
public class Vertex<E> {

    private E value;

    public Vertex(E value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "graph.Vertex{" +
                "value=" + value +
                '}';
    }
}
