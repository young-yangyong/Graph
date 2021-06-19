package graph.graphImpl;

import graph.GraphKind;

import java.util.stream.Collectors;


public class test {
    public static void main(String[] args) {
        GraphByAdjacentMatrix<String, String, String> graph = new GraphByAdjacentMatrix<>(GraphKind.DG);
        for (int i = 1; i < 10; i++) {
            graph.addVertex("V" + i);
        }
        graph.addEdgeByIndex(0, 1);
        graph.addEdgeByIndex(0, 2);
        graph.addEdgeByIndex(1, 3);
        graph.addEdgeByIndex(1, 4);
        graph.addEdgeByIndex(1, 8);
        graph.addEdgeByIndex(2, 7);
        graph.addEdgeByIndex(5, 2);
        graph.addEdgeByIndex(5, 7);
        graph.addEdgeByIndex(6, 3);
        graph.addEdgeByIndex(6, 8);
        System.out.println(graph);
        graph.BFSTraverse().forEach(path -> {
            System.out.println(path.stream().map(graph::getVertex).collect(Collectors.toList()));
        });

    }
}
