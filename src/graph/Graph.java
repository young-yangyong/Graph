package graph;

import java.util.List;
import java.util.Map;
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

    List<Integer> findVertexIndexes(E vertex);

    E getVertex(int index);

    E setVertex(int index, E vertex);

    boolean addVertex(E vertex);

    boolean addVertexes(List<E> vertexes);

    void addVertex(int index, E vertex);

    E deleteVertex(int index);

    E deleteFirstVertex(E vertex);

    List<E> deleteVertexes(E vertex);

    Edge<T, V> addEdgeByIndex(int index1, int index2);
    Edge<T, V> addEdgeByIndex(int index1, int index2,T weight,V info);

    List<Edge<T, V>> addEdgesByIndex(Set<int[]> indexes);

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

    List<E> getInAdjacentVertexes(int index);

    E getFirstOutAdjacentVertex(int index);

    List<E> getOutAdjacentVertexes(int index);

    E getFirstAdjacentVertex(int index);

    List<E> getAdjacentVertexes(int index);

    List<E> DFSTraverse(int index1, int index2);

    List<E> BFSTraverse(int index1, int index2);


    boolean isDirectedGraph();

//    boolean isSparseGraph();

    boolean isCompletedGraph();

    boolean isConnectedGraph();

    boolean isNetwork();

    List<E> getVertexes();

    List<Edge<T, V>> getEdges();

    List<E> getFirstPath(int index1, int index2);

    List<List<E>> getPaths(int index1, int index2);

    boolean hasPath(int index1, int index2);

    List<E> getCycle(int index);

    boolean isCycle(int index);

    boolean isConnected(int index1, int index2);

}
