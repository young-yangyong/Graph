package graph;

import java.util.List;
import java.util.Set;

/**
 * 数据结构-图
 *
 * @param <E> 顶点类型
 * @param <T> 弧/边权值类型
 * @param <V> 弧/边信息类型
 */
public interface Graph<E, T, V> {

    int getVertexNum();

    int getEdgeNum();

    GraphKind getKind();

    int findVertex(E vertex);

    int findLastVertex(E vertex);

    Set<Integer> findVertexIndexes(E vertex);

    E getVertex(int index);

    E setVertex(int index, E vertex);

    boolean addVertex(E vertex);

    boolean addVertexes(List<E> vertexes);

    void addVertex(int index, E vertex);

    E deleteVertex(int index);

    int deleteFirstVertex(E vertex);

    List<E> deleteVertexes(int[] vertexIndexes);

    Edge<T, V> addEdgeByIndex(int index1, int index2);

    Edge<T, V> addEdgeByIndex(int index1, int index2, T weight, V info);

    Set<Edge<T, V>> addEdgesByIndexes(Set<int[]> indexes);

    Edge<T, V> deleteEdge(int index1, int index2);

    Edge<T, V> updateEdge(int index1, int index2, T weight, V info);

    Edge<T, V> updateEdgeWeight(int index1, int index2, T weight);

    Edge<T, V> updateEdgeInfo(int index1, int index2, V info);

    boolean hasEdge(int index1, int index2);

    Edge<T, V> getEdge(int index1, int index2);

    Edge<T, V> getFirstAdjacentEdge(int index);

    Set<Edge<T, V>> getAdjacentEdges(int index);

    Set<Edge<T, V>> getInEdges(int index);

    Edge<T, V> getFirstInEdge(int index);

    Set<Edge<T, V>> getOutEdges(int index);

    Edge<T, V> getFirstOutEdge(int index);

    int getInDegree(int index);

    int getOutDegree(int index);

    int getDegree(int index);

    E getFirstInAdjacentVertex(int index);

    int getFirstInAdjacentVertexIndex(int index);

    List<E> getInAdjacentVertexes(int index);

    Set<Integer> getInAdjacentVertexIndexes(int index);

    E getFirstOutAdjacentVertex(int index);

    int getFirstOutAdjacentVertexIndex(int index);

    List<E> getOutAdjacentVertexes(int index);

    Set<Integer> getOutAdjacentVertexIndexes(int index);

    E getFirstAdjacentVertex(int index);

    int getFirstAdjacentVertexIndex(int index);

    List<E> getAdjacentVertexes(int index);

    Set<Integer> getAdjacentVertexIndexes(int index);

    List<List<Integer>> DFSTraverse();

    List<List<Integer>> BFSTraverse();

    boolean isDirectedGraph();

    boolean isCompletedGraph();

    boolean isConnectedGraph();

    boolean isEmptyGraph();

    boolean isNetwork();

    Object[] getVertexes();

    Set<Edge<T, V>> getEdges();

    List<Integer> getFirstPath(int index1, int index2);

    List<List<Integer>> getPaths(int index1, int index2);

    boolean hasPath(int index1, int index2);

    List<Integer> getFirstCycle(int index);

    List<List<Integer>> getCycles(int index);

    boolean hasCycle(int index);

    boolean hasCycle();

    List<List<Integer>> getCycles();

    Edge<T, V> getLoop(int index);

    boolean hasLoop(int index);

    boolean hasLoop();

    boolean isConnected(int index1, int index2);

}
