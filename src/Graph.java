

import java.util.List;
import java.util.Map;

/**
 * 数据结构-图
 *
 * @param <E> 顶点类型
 * @param <T> 弧/边权值类型
 * @param <V> 弧/边信息类型
 */
public interface Graph<E, T, V> {

    int getVexNum();

    int getEdgeNum();

    int findVertex(E Vertex);

    List<Integer> findVertexes(E Vertex);

    E getVertex(int index);

    E setVertex(int index, E Vertex);

    boolean addVertex(E Vertex);

    boolean addVertexes(List<E> Vertexes);

    boolean addVertex(int index, E Vertex);

    E deleteVertex(int index);

    E deleteFirstVertex(E Vertex);

    List<E> deleteVertexes(E Vertex);

    Edge<T, V> addEdgeByIndex(int index1, int index2);

    List<Edge<T, V>> addEdgesByIndex(Map<Integer, Integer> indexes);

    Edge<T, V> deleteEdge(int index1, int index2);

    Edge<T, V> updateEdge(int index1, int index2, T weight, V info);

    Edge<T, V> updateEdgeWeight(int index1, int index2, T weight);

    Edge<T, V> updateEdgeInfo(int index1, int index2, V info);

    boolean hasEdge(int index1, int index2);

    Edge<T, V> getEdge(int index1, int index2);

    Edge<T, V> getFirstAdjacentEdge(int index);

    List<Edge<T, V>> getAdjacentEdges(int index);

    List<Edge<T, V>> getInEdges(int index);

    Edge<T, V> getFirstInEdge(int index);

    List<Edge<T, V>> getOutEdges(int index);

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

    List<E> DFSTraverse(E[] Vertexes);

    List<E> BFSTraverse(E[] Vertexes);

    boolean isDigraph();

    boolean isUnDigraph();

    boolean isCompletedGraph();

    boolean isConnectedGraph();

    boolean isNetwork();

    List<E> getVertexes();

    List<Edge<T, V>> getEdges();


}
