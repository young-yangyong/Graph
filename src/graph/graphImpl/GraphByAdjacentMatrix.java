package graph.graphImpl;


import com.sun.istack.internal.NotNull;
import graph.Edge;
import graph.Graph;
import graph.GraphKind;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 无向
 */
public abstract class GraphByAdjacentMatrix<E, T, V> implements Graph<E, T, V> {
    /**
     * 顶点向量
     */
    private List<E> vertexes;

    /**
     * 邻接矩阵
     */
    private final List<List<Edge<T, V>>> AdjacentMatrix;
    /**
     * 图的当前顶点数和弧数
     */
    private int VertexNum, EdgeNum;
    /**
     * 图的种类标志
     */
    private final GraphKind graphKind;

    @Override
    public String toString() {
        return "GraphByAdjacentMatrix{\n" +
                "vertexes=" + vertexes +
                ",\nAdjacentMatrix=" + adjacentMatrixToString(AdjacentMatrix) +
                ",\nVertexNum=" + VertexNum +
                ",\nEdgeNum=" + EdgeNum +
                ",\ngraphKind=" + graphKind +
                '}';
    }

    /**
     * 实现邻接矩阵的打印方法以辅助Graph类实现toString方法
     *
     * @param AdjacentMatrix 邻接矩阵
     * @return 邻接矩阵的toString()
     */
    private String adjacentMatrixToString(List<List<Edge<T, V>>> AdjacentMatrix) {
        final StringBuilder stringBuilder = new StringBuilder().append("[\n");
        for (List<Edge<T, V>> edges : AdjacentMatrix) {
            stringBuilder.append(edges).append("\n");
        }
        return stringBuilder.append("]").toString();
    }

    public GraphByAdjacentMatrix(@NotNull List<E> vertexes, @NotNull List<Edge<T, V>> edges, @NotNull GraphKind graphKind) {
        this.vertexes = vertexes;
        this.VertexNum = vertexes.size();
        this.AdjacentMatrix = createAdjacentMatrix(edges, edges.size(), graphKind);
        this.graphKind = graphKind;
    }

    public GraphByAdjacentMatrix(@NotNull GraphKind graphKind) {
        this.AdjacentMatrix = createAdjacentMatrix(null, 0, graphKind);
        this.graphKind = graphKind;
    }

    public GraphByAdjacentMatrix(@NotNull List<E> vertexes, @NotNull GraphKind graphKind) {
        this(graphKind);
        this.vertexes = vertexes;
        this.VertexNum = vertexes.size();
    }

    private void growEdgeNum(@NotNull List<List<Edge<T, V>>> adjacentMatrix, int headIndex, int tailIndex) {
        rangeCheck(headIndex);
        rangeCheck(tailIndex);
        if (adjacentMatrix.get(headIndex).get(tailIndex) == null)
            ++EdgeNum;
    }

    private List<List<Edge<T, V>>> createAdjacentMatrix(List<Edge<T, V>> edges, int order, GraphKind graphKind) {
        List<List<Edge<T, V>>> adjacentMatrix = new ArrayList<>();
        for (int i = 0; i < order; i++) {
            List<Edge<T, V>> vector = new ArrayList<>();
            for (int j = 0; j < order; j++) {
                vector.add(null);
            }
            adjacentMatrix.add(vector);
        }
        if (edges != null)
            switch (graphKind) {
                case DG:
                    edges.forEach(edge -> {
                        int headIndex = edge.getHeadIndex();
                        int tailIndex = edge.getTailIndex();
                        edge.setWeight(true);
                        growEdgeNum(adjacentMatrix, headIndex, tailIndex);
                        adjacentMatrix.get(headIndex).set(tailIndex, edge);
                    });
                    break;
                case DN:
                    edges.forEach(edge -> {
                        int headIndex = edge.getHeadIndex();
                        int tailIndex = edge.getTailIndex();
                        growEdgeNum(adjacentMatrix, headIndex, tailIndex);
                        adjacentMatrix.get(headIndex).set(tailIndex, edge);
                    });
                    break;
                case UDG:
                    edges.forEach(edge -> {
                        int headIndex = edge.getHeadIndex();
                        int tailIndex = edge.getTailIndex();
                        edge.setWeight(true);
                        growEdgeNum(adjacentMatrix, headIndex, tailIndex);
                        adjacentMatrix.get(headIndex).set(tailIndex, edge);
                        adjacentMatrix.get(tailIndex).set(headIndex, edge);
                    });
                    break;
                case UDN:
                    edges.forEach(edge -> {
                        int headIndex = edge.getHeadIndex();
                        int tailIndex = edge.getTailIndex();
                        growEdgeNum(adjacentMatrix, headIndex, tailIndex);
                        adjacentMatrix.get(headIndex).set(tailIndex, edge);
                        adjacentMatrix.get(tailIndex).set(headIndex, edge);
                    });
                    break;
                default:
                    throw new RuntimeException("未知的图类型！");
            }
        return adjacentMatrix;
    }


    /**
     * 判断顶点索引值是否越界
     *
     * @param index 顶点索引值
     */
    private void rangeCheck(int index) {
        if (index < 0 || index > VertexNum - 1)
            throw new IndexOutOfBoundsException("顶点下标必须<" + (VertexNum - 1) + "并且>=0！");
    }

    @Override
    public int getVertexNum() {
        return VertexNum;
    }

    @Override
    public int getEdgeNum() {
        return EdgeNum;
    }

    @Override
    public GraphKind getKind() {
        return graphKind;
    }

    @Override
    public int findVertex(E vertex) {
        return vertexes.indexOf(vertex);
    }

    @Override
    public int findLastVertex(E vertex) {
        return vertexes.lastIndexOf(vertex);
    }

    @Override
    public List<Integer> findVertexIndexes(E vertex) {

        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < vertexes.size(); i++) {
            if (vertexes.get(i).equals(vertex))
                indexes.add(i);
        }
        return indexes;
    }

    @Override
    public E getVertex(int index) {
        return vertexes.get(index);
    }

    @Override
    public E setVertex(int index, E vertex) {
        return vertexes.set(index, vertex);
    }

    @Override
    public boolean addVertex(E vertex) {
        vertexes.add(vertex);
        List<Edge<T, V>> vector = new ArrayList<>();
        //循环旧的VertexNum次
        AdjacentMatrix.forEach(edges -> {
            edges.add(null);
            //复用循环，降低时间复杂度
            vector.add(null);
        });
        vector.add(null);
        AdjacentMatrix.add(vector);
        ++VertexNum;
        return true;
    }

    @Override
    public boolean addVertexes(List<E> vertexes) {
        if (vertexes != null) {
            vertexes.forEach(this::addVertex);
        }
        return true;
    }

    @Override
    public void addVertex(int index, E vertex) {
        vertexes.add(vertex);
        List<Edge<T, V>> vector = new ArrayList<>();
        //循环旧的VertexNum次
        AdjacentMatrix.forEach(edges -> {
            edges.add(index, null);
            //复用循环，降低时间复杂度
            vector.add(null);
        });
        vector.add(null);
        AdjacentMatrix.add(index, vector);
        ++VertexNum;
    }

    @Override
    public E deleteVertex(int index) {
        return null;
    }

    @Override
    public E deleteFirstVertex(E Vertex) {
        return null;
    }

    @Override
    public List<E> deleteVertexes(E Vertex) {
        return null;
    }

    @Override
    public Edge<T, V> addEdgeByIndex(int index1, int index2) {
        return addEdgeByIndex(index1, index2, null, null);
    }

    @Override
    public Edge<T, V> addEdgeByIndex(int index1, int index2, T weight, V info) {

        growEdgeNum(AdjacentMatrix, index1, index2);
        Edge<T, V> edge;
        if (isNetwork())
            edge = new Edge<>(index1, index2, weight, info);
        else
            edge = new Edge<>(index1, index2, true, info);
        if (!isDirectedGraph()) {

            AdjacentMatrix.get(index2).set(index1, edge);
        }
        return AdjacentMatrix.get(index1).set(index2, edge);
    }

    @Override
    public List<Edge<T, V>> addEdgesByIndex(Set<int[]> indexes) {
        ArrayList<Edge<T, V>> edges = new ArrayList<>();
        if (isNetwork() && isDirectedGraph()) {
            indexes.forEach(e -> {
                if (e.length != 2)
                    throw new RuntimeException("生成弧/边有且仅有两个顶点的索引值");
                else {
                    growEdgeNum(AdjacentMatrix, e[0], e[1]);
                    edges.add(AdjacentMatrix.get(e[0]).set(e[1], new Edge<>(e[0], e[1], null, null)));
                }
            });
        } else if (!isNetwork() && isDirectedGraph()) {
            indexes.forEach(e -> {
                if (e.length != 2)
                    throw new RuntimeException("生成弧/边有且仅有两个顶点的索引值");
                else {
                    growEdgeNum(AdjacentMatrix, e[0], e[1]);
                    edges.add(AdjacentMatrix.get(e[0]).set(e[1], new Edge<>(e[0], e[1], true, null)));
                }
            });
        } else if (isNetwork() && !isDirectedGraph()) {
            indexes.forEach(e -> {
                if (e.length != 2)
                    throw new RuntimeException("生成弧/边有且仅有两个顶点的索引值");
                else {
                    growEdgeNum(AdjacentMatrix, e[0], e[1]);
                    Edge<T, V> edge = new Edge<>(e[0], e[1], null, null);
                    edges.add(AdjacentMatrix.get(e[0]).set(e[1], edge));
                    AdjacentMatrix.get(e[1]).set(e[0], edge);
                }
            });
        } else {
            indexes.forEach(e -> {
                if (e.length != 2)
                    throw new RuntimeException("生成弧/边有且仅有两个顶点的索引值");
                else {
                    growEdgeNum(AdjacentMatrix, e[0], e[1]);
                    Edge<T, V> edge = new Edge<>(e[0], e[1], true, null);
                    edges.add(AdjacentMatrix.get(e[0]).set(e[1], edge));
                    AdjacentMatrix.get(e[1]).set(e[0], edge);
                }
            });
        }
        return edges;
    }

   //下面都有错误
    @Override
    public Edge<T, V> deleteEdge(int index1, int index2) {
        rangeCheck(index1);
        rangeCheck(index2);
        if (!isDirectedGraph()){
            AdjacentMatrix.get(index2).set(index1,null);
        }
        --VertexNum;
        return AdjacentMatrix.get(index1).set(index2,null);
    }

    @Override
    public Edge<T, V> updateEdge(int index1, int index2, T weight, V info) {
        Edge<T, V> edge=getEdge(index1,index2);
        if (edge!=null) {
            if (isNetwork())
                edge.setWeight(weight);
            edge.setInfo(info);
            return edge;
        }else
            throw new RuntimeException("<"+index1+","+index2+">"+"不是弧/边！");
    }

    @Override
    public Edge<T, V> updateEdgeWeight(int index1, int index2, T weight) {
        Edge<T, V> edge=getEdge(index1,index2);
        if (edge!=null) {
            if (isNetwork())
                edge.setWeight(weight);
            return edge;
        }else
            throw new RuntimeException("<"+index1+","+index2+">"+"不是弧/边！");
    }

    @Override
    public Edge<T, V> updateEdgeInfo(int index1, int index2, V info) {
        Edge<T, V> edge=getEdge(index1,index2);
        if (edge!=null) {
            edge.setInfo(info);
            return edge;
        }else
            throw new RuntimeException("<"+index1+","+index2+">"+"不是弧/边！");
    }

    @Override
    public boolean hasEdge(int index1, int index2) {
        rangeCheck(index1);
        rangeCheck(index2);
        return AdjacentMatrix.get(index1).get(index2)!=null;
    }

    @Override
    public Edge<T, V> getEdge(int index1, int index2) {
        rangeCheck(index1);
        rangeCheck(index2);
        return AdjacentMatrix.get(index1).get(index2);
    }

    @Override
    public Edge<T, V> getFirstAdjacentEdge(int index) {
        return getFirstOutEdge(index);
    }

    @Override
    public Set<Edge<T, V>> getAdjacentEdges(int index) {
        Set<Edge<T, V>> edges= getInEdges(index);
        if (isDirectedGraph()){
            AdjacentMatrix.get(index).forEach(edge->{
                if (edge!=null){
                    edges.add(edge);
                }
            });
        }
        return edges;
    }

    @Override
    public Set<Edge<T, V>> getInEdges(int index) {
        rangeCheck(index);
        Set<Edge<T, V>> edges= new HashSet<>();
        AdjacentMatrix.forEach(vector->{
            Edge<T, V> edge = vector.get(index);
            if (edge!=null)
                edges.add(edge);
        });
        return edges;
    }

    @Override
    public Edge<T, V> getFirstInEdge(int index) {
        rangeCheck(index);
        for (List<Edge<T, V>> vector : AdjacentMatrix) {
            Edge<T, V> edge = vector.get(index);
            if (edge!=null)
                return edge;
        }
        return null;
    }

    @Override
    public Set<Edge<T, V>> getOutEdges(int index) {
        rangeCheck(index);
        Set<Edge<T, V>> edges= new HashSet<>();
        AdjacentMatrix.get(index).forEach(edge->{
            if (edge!=null){
                edges.add(edge);
            }
        });
        return edges;
    }

    @Override
    public Edge<T, V> getFirstOutEdge(int index) {
        rangeCheck(index);
        for (Edge<T, V> edge : AdjacentMatrix.get(index)) {
            if (edge!=null)
                return edge;
        }
        return null;
    }

    @Override
    public int getInDegree(int index) {
        return 0;
    }

    @Override
    public int getOutDegree(int index) {
        return 0;
    }

    @Override
    public int getDegree(int index) {
        return 0;
    }

    @Override
    public E getFirstInAdjacentVertex(int index) {
        return null;
    }

    @Override
    public List<E> getInAdjacentVertexes(int index) {
        return null;
    }

    @Override
    public E getFirstOutAdjacentVertex(int index) {
        return null;
    }

    @Override
    public List<E> getOutAdjacentVertexes(int index) {
        return null;
    }

    @Override
    public E getFirstAdjacentVertex(int index) {
        return null;
    }

    @Override
    public List<E> getAdjacentVertexes(int index) {
        return null;
    }

    @Override
    public List<E> DFSTraverse(int index1, int index2) {
        return null;
    }

    @Override
    public List<E> BFSTraverse(int index1, int index2) {
        return null;
    }

    @Override
    public boolean isDirectedGraph() {
        return graphKind == GraphKind.DG || graphKind == GraphKind.UDG;
    }


    @Override
    public boolean isCompletedGraph() {
        return false;
    }

    @Override
    public boolean isConnectedGraph() {
        return false;
    }

    @Override
    public boolean isNetwork() {
        return graphKind == GraphKind.DN || graphKind == GraphKind.UDN;
    }

    @Override
    public List<E> getVertexes() {
        return null;
    }

    @Override
    public List<Edge<T, V>> getEdges() {
        return null;
    }

    @Override
    public List<E> getFirstPath(int index1, int index2) {
        return null;
    }

    @Override
    public List<List<E>> getPaths(int index1, int index2) {
        return null;
    }

    @Override
    public boolean hasPath(int index1, int index2) {
        return false;
    }

    @Override
    public List<E> getCycle(int index) {
        return null;
    }

    @Override
    public boolean isCycle(int index) {
        return false;
    }

    @Override
    public boolean isConnected(int index1, int index2) {
        return false;
    }
}
