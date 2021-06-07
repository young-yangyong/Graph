package graph;

import com.sun.istack.internal.NotNull;

public  class Vertex<E> {

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
