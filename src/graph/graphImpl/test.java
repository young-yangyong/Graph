package graph.graphImpl;

import graph.Edge;
import graph.GraphKind;

import javax.xml.ws.soap.Addressing;
import java.util.ArrayList;
import java.util.List;

class aa extends GraphByAdjacentMatrix<Integer, String, String> {

    public aa(List<Integer> vertexes, List<Edge<String, String>> edges, GraphKind graphKind) {
        super(vertexes, edges, graphKind);
    }

    public aa(GraphKind graphKind) {
        super(graphKind);
    }

    public aa(List<Integer> vertexes, GraphKind graphKind) {
        super(vertexes, graphKind);
    }
}
public class test {
    public static void main(String[] args) {
        ArrayList<Edge<String,String>> edges = new ArrayList<>();
        edges.add(new Edge<>(1, 1, "qw", "nihao"));
        edges.add(new Edge<>(1, 2, "wq", "nihao"));
        edges.add(new Edge<>(0, 2, "true", "nihao"));
//        System.out.println(new aa(new ArrayList<Integer>() {
//            {
//                add(1);
//                add(2);
//                add(3);
//            }
//        }, edges, GraphKind.DN));
        System.out.println(new aa(new ArrayList<Integer>() {
            {
                add(1);
                add(2);
                add(3);
            }
        }, GraphKind.DN));
    }
}
