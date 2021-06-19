package graph.graphImpl;


import com.sun.istack.internal.NotNull;
import graph.Edge;
import graph.Graph;
import graph.GraphKind;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 千行代码，带你入门图论世界
 * 图-实现类
 * 特性：
 * 1、具有自环边，不具备平行边；
 * 2、有权值；
 * 3、有方向；
 * 4、所有算法均考虑了有向图的方向性，但不涉及与权值有关的任何算法（权值需要比较，邻接矩阵可能并不适合一些拓扑算法的实现）
 *
 * 算法人为时空复杂度操碎了心，如果有人愿意细品我的写的算法，可能会明白我的苦心，呜呜呜~~~
 *
 */
public class GraphByAdjacentMatrix<E, T, V> implements Graph<E, T, V> {
    /**
     * 邻接矩阵
     */
    private final List<List<Edge<T, V>>> adjacencyMatrix;
    /**
     * 图的种类标志
     */
    private final GraphKind graphKind;
    /**
     * 顶点向量
     */
    private final List<E> vertexes;
    /**
     * 图的当前顶点数和弧数
     */
    private int vertexNum, edgeNum;
    /**
     * 辅助遍历的空间
     */
    private boolean[] visited;
    private Queue<Integer> queue;
    private int traversableNum;
    private List<List<Integer>> cycles;
    private List<List<Integer>> paths;

    public GraphByAdjacentMatrix(@NotNull List<E> vertexes, @NotNull List<Edge<T, V>> edges, @NotNull GraphKind graphKind) {
        this.vertexes = vertexes;
        this.vertexNum = vertexes.size();
        this.adjacencyMatrix = createAdjacentMatrix(edges, vertexes.size(), graphKind);
        this.graphKind = graphKind;
    }

    public GraphByAdjacentMatrix(@NotNull GraphKind graphKind) {
        vertexes = new ArrayList<>();
        this.adjacencyMatrix = createAdjacentMatrix(null, 0, graphKind);
        this.graphKind = graphKind;
    }

    public GraphByAdjacentMatrix(@NotNull List<E> vertexes, @NotNull GraphKind graphKind) {
        this.vertexes = vertexes;
        this.vertexNum = vertexes.size();
        this.adjacencyMatrix = createAdjacentMatrix(null, 0, graphKind);
        this.graphKind = graphKind;
    }

    @Override
    public String toString() {
        return "GraphByAdjacentMatrix{\n" +
                "vertexes=" + vertexes +
                ",\nAdjacentMatrix=" + adjacentMatrixToString(adjacencyMatrix) +
                ",\nVertexNum=" + vertexNum +
                ",\nEdgeNum=" + edgeNum +
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
        StringBuilder stringBuilder = new StringBuilder().append("[\n");
        for (List<Edge<T, V>> edges : AdjacentMatrix)
            stringBuilder.append(edges).append("\n");
        return stringBuilder.append("]").toString();
    }

    /**
     * 稳定的自增弧/边的数量
     *
     * @param tailIndex 弧/边的起始点
     * @param headIndex 弧/边的终端点
     */
    private void growEdgeNum(int tailIndex, int headIndex) {
        rangeCheck(tailIndex);
        rangeCheck(headIndex);
        if (adjacencyMatrix.get(tailIndex).get(headIndex) == null)
            ++edgeNum;
    }

    /**
     * 根据传入指定的图类型graphKind、顶点数量order和弧/边集合来生成邻接矩阵
     *
     * @param edges     弧/边集
     * @param order     顶点数量
     * @param graphKind 生成的图类型
     * @return 邻接矩阵
     */
    private List<List<Edge<T, V>>> createAdjacentMatrix(List<Edge<T, V>> edges, int order, GraphKind graphKind) {
        /*
          根据传入的顶点数量order来初始化邻接矩阵
         */
        List<List<Edge<T, V>>> adjacencyMatrix = new ArrayList<>();
        for (int i = 0; i < order; i++) {
            List<Edge<T, V>> vector = new ArrayList<>();
            for (int j = 0; j < order; j++)
                vector.add(null);
            adjacencyMatrix.add(vector);
        }
        /*
         生成邻接矩阵
         */
        if (edges != null)
            switch (graphKind) {
                case DG:
                    edges.forEach(edge -> {
                        int tailIndex = edge.getTailIndex();
                        int headIndex = edge.getHeadIndex();
                        edge.setWeight(true);
                        growEdgeNum(tailIndex, headIndex);
                        adjacencyMatrix.get(tailIndex).set(headIndex, edge);
                    });
                    break;
                case DN:
                    edges.forEach(edge -> {
                        int tailIndex = edge.getTailIndex();
                        int headIndex = edge.getHeadIndex();
                        growEdgeNum(tailIndex, headIndex);
                        adjacencyMatrix.get(tailIndex).set(headIndex, edge);
                    });
                    break;
                case UDG:
                    edges.forEach(edge -> {
                        int tailIndex = edge.getTailIndex();
                        int headIndex = edge.getHeadIndex();
                        edge.setWeight(true);
                        growEdgeNum(tailIndex, headIndex);
                        adjacencyMatrix.get(tailIndex).set(headIndex, edge);
                        adjacencyMatrix.get(headIndex).set(tailIndex, edge);
                    });
                    break;
                case UDN:
                    edges.forEach(edge -> {
                        int tailIndex = edge.getTailIndex();
                        int headIndex = edge.getHeadIndex();
                        growEdgeNum(tailIndex, headIndex);
                        adjacencyMatrix.get(tailIndex).set(headIndex, edge);
                        adjacencyMatrix.get(headIndex).set(tailIndex, edge);
                    });
                    break;
                default:
                    throw new RuntimeException("未知的图类型！");
            }
        return adjacencyMatrix;
    }

    /**
     * 判断顶点索引值是否越界;
     * 越界抛出运行时异常
     *
     * @param index 顶点索引值
     */
    private void rangeCheck(int index) {
        if (index < 0 || index > vertexNum - 1)
            throw new IndexOutOfBoundsException("顶点下标必须<" + (vertexNum - 1) + "并且>=0！");
    }

    @Override
    public int getVertexNum() {
        return vertexNum;
    }

    @Override
    public int getEdgeNum() {
        return edgeNum;
    }

    @Override
    public GraphKind getKind() {
        return graphKind;
    }

    /**
     * 获取第一个顶点是vertex的索引值
     *
     * @param vertex 顶点
     * @return 索引值
     */
    @Override
    public int findVertex(E vertex) {
        return vertexes.indexOf(vertex);
    }

    /**
     * 获取最后一个顶点是vertex的索引值
     *
     * @param vertex 顶点
     * @return 索引值
     */
    @Override
    public int findLastVertex(E vertex) {
        return vertexes.lastIndexOf(vertex);
    }

    @Override
    public Set<Integer> findVertexIndexes(E vertex) {
        Set<Integer> indexes = new HashSet<>();
        for (int i = 0; i < vertexes.size(); i++)
            if (vertexes.get(i).equals(vertex))
                indexes.add(i);
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
        adjacencyMatrix.forEach(edges -> {
            edges.add(null);
            //复用循环，降低时间复杂度
            vector.add(null);
        });
        vector.add(null);
        adjacencyMatrix.add(vector);
        ++vertexNum;
        return true;
    }

    @Override
    public boolean addVertexes(List<E> vertexes) {
        if (vertexes != null) {
            vertexes.forEach(this::addVertex);
            return true;
        }
        return false;
    }

    /**
     * 该方法时间复杂度超高，极其费时，不建议使用；
     * 事实上没有必要指定位置插入顶点，这对图数据结构可能是毫无意义的
     *
     * @param index  指定插入位置的索引值
     * @param vertex 待插入顶点
     */
    @Override
    public void addVertex(int index, E vertex) {
        vertexes.add(vertex);
        List<Edge<T, V>> vector = new ArrayList<>();
        //循环旧的VertexNum次
        adjacencyMatrix.forEach(edges -> {
            edges.add(index, null);
            //复用循环，降低时间复杂度
            vector.add(null);
        });
        vector.add(null);
        adjacencyMatrix.add(index, vector);
        ++vertexNum;
    }

    /**
     * 删除指定索引值对应位置的顶点
     *
     * @param index 删除index索引值对应的顶点
     * @return 被删除的顶点
     */
    @Override
    public E deleteVertex(int index) {
        rangeCheck(index);
        E vertex = vertexes.remove(index);
        --vertexNum;
        int ReducedEdgeNum = 0;
        for (int i = 0; i < vertexNum; i++) {
            if (adjacencyMatrix.get(i).remove(index) != null)
                ++ReducedEdgeNum;
            if (adjacencyMatrix.get(index).get(i) != null)
                ++ReducedEdgeNum;
        }
        adjacencyMatrix.remove(index);
        edgeNum -= ReducedEdgeNum;
        return vertex;
    }

    @Override
    public int deleteFirstVertex(E vertex) {
        int index = findVertex(vertex);
        if (index != -1)
            deleteVertex(index);
        return index;
    }

    @Override
    public List<E> deleteVertexes(int[] vertexIndexes) {
        List<E> vertexes = new ArrayList<>();
        if (vertexIndexes == null)
            return vertexes;
        for (int vertexIndex : vertexIndexes)
            vertexes.add(deleteVertex(vertexIndex));
        return vertexes;
    }

    /**
     * 通过索引值添加一条边
     *
     * @return 旧的边
     */
    @Override
    public Edge<T, V> addEdgeByIndex(int index1, int index2) {
        return addEdgeByIndex(index1, index2, null, null);
    }

    /**
     * 通过索引值添加一条边
     *
     * @return 旧的边
     */
    @Override
    public Edge<T, V> addEdgeByIndex(int index1, int index2, T weight, V info) {
        growEdgeNum(index1, index2);
        Edge<T, V> edge;
        if (isNetwork())
            edge = new Edge<>(index1, index2, weight, info);
        else
            edge = new Edge<>(index1, index2, true, info);
        if (!isDirectedGraph())
            adjacencyMatrix.get(index2).set(index1, edge);
        return adjacencyMatrix.get(index1).set(index2, edge);
    }

    @Override
    public Set<Edge<T, V>> addEdgesByIndexes(Set<int[]> indexes) {
        Set<Edge<T, V>> edges = new HashSet<>();
        if (isNetwork() && isDirectedGraph())
            indexes.forEach(e -> {
                if (e == null || e.length != 2)
                    throw new RuntimeException("生成弧/边有且仅有两个顶点的索引值");
                else {
                    growEdgeNum(e[0], e[1]);
                    edges.add(adjacencyMatrix.get(e[0]).set(e[1], new Edge<>(e[0], e[1], null, null)));
                }
            });
        else if (!isNetwork() && isDirectedGraph())
            indexes.forEach(e -> {
                if (e == null || e.length != 2)
                    throw new RuntimeException("生成弧/边有且仅有两个顶点的索引值");
                else {
                    growEdgeNum(e[0], e[1]);
                    edges.add(adjacencyMatrix.get(e[0]).set(e[1], new Edge<>(e[0], e[1], true, null)));
                }
            });
        else if (isNetwork() && !isDirectedGraph())
            indexes.forEach(e -> {
                if (e == null || e.length != 2)
                    throw new RuntimeException("生成弧/边有且仅有两个顶点的索引值");
                else {
                    growEdgeNum(e[0], e[1]);
                    Edge<T, V> edge = new Edge<>(e[0], e[1], null, null);
                    edges.add(adjacencyMatrix.get(e[0]).set(e[1], edge));
                    adjacencyMatrix.get(e[1]).set(e[0], edge);
                }
            });
        else
            indexes.forEach(e -> {
                if (e == null || e.length != 2)
                    throw new RuntimeException("生成弧/边有且仅有两个顶点的索引值");
                else {
                    growEdgeNum(e[0], e[1]);
                    Edge<T, V> edge = new Edge<>(e[0], e[1], true, null);
                    edges.add(adjacencyMatrix.get(e[0]).set(e[1], edge));
                    adjacencyMatrix.get(e[1]).set(e[0], edge);
                }
            });
        return edges;
    }

    @Override
    public Edge<T, V> deleteEdge(int index1, int index2) {
        if (hasEdge(index1, index2)) {
            if (!isDirectedGraph())
                adjacencyMatrix.get(index2).set(index1, null);
            --vertexNum;
            return adjacencyMatrix.get(index1).set(index2, null);
        }
        return null;
    }

    @Override
    public Edge<T, V> updateEdge(int index1, int index2, T weight, V info) {
        Edge<T, V> edge = getEdge(index1, index2);
        if (edge != null) {
            if (isNetwork())
                edge.setWeight(weight);
            edge.setInfo(info);
            return edge;
        } else
            throw new RuntimeException("<" + index1 + "," + index2 + ">" + "不是弧/边！");
    }

    @Override
    public Edge<T, V> updateEdgeWeight(int index1, int index2, T weight) {
        Edge<T, V> edge = getEdge(index1, index2);
        if (edge != null) {
            if (isNetwork())
                edge.setWeight(weight);
            return edge;
        } else
            throw new RuntimeException("<" + index1 + "," + index2 + ">" + "不是弧/边！");
    }

    @Override
    public Edge<T, V> updateEdgeInfo(int index1, int index2, V info) {
        Edge<T, V> edge = getEdge(index1, index2);
        if (edge != null) {
            edge.setInfo(info);
            return edge;
        } else
            throw new RuntimeException("<" + index1 + "," + index2 + ">" + "不是弧/边！");
    }

    @Override
    public boolean hasEdge(int index1, int index2) {
        return getEdge(index1, index2) != null;
    }

    @Override
    public Edge<T, V> getEdge(int index1, int index2) {
        rangeCheck(index1);
        rangeCheck(index2);
        return adjacencyMatrix.get(index1).get(index2);
    }

    @Override
    public Set<Edge<T, V>> getInEdges(int index) {
        rangeCheck(index);
        Set<Edge<T, V>> edges = new HashSet<>();
        adjacencyMatrix.forEach(vector -> {
            Edge<T, V> edge = vector.get(index);
            if (edge != null)
                edges.add(edge);
        });
        return edges;
    }

    @Override
    public Edge<T, V> getFirstInEdge(int index) {
        rangeCheck(index);
        for (List<Edge<T, V>> vector : adjacencyMatrix) {
            Edge<T, V> edge = vector.get(index);
            if (edge != null)
                return edge;
        }
        return null;
    }

    @Override
    public Set<Edge<T, V>> getOutEdges(int index) {
        rangeCheck(index);
        Set<Edge<T, V>> edges = new HashSet<>();
        adjacencyMatrix.get(index).forEach(edge -> {
            if (edge != null)
                edges.add(edge);
        });
        return edges;
    }

    @Override
    public Edge<T, V> getFirstOutEdge(int index) {
        rangeCheck(index);
        for (Edge<T, V> edge : adjacencyMatrix.get(index))
            if (edge != null)
                return edge;
        return null;
    }

    @Override
    public Edge<T, V> getFirstAdjacentEdge(int index) {
        Edge<T, V> firstOutEdge = getFirstOutEdge(index);
        if (firstOutEdge != null)
            return firstOutEdge;
        return getFirstInEdge(index);
    }

    @Override
    public Set<Edge<T, V>> getAdjacentEdges(int index) {
        Set<Edge<T, V>> edges = getInEdges(index);
        /*
          优化时间复杂度
         */
        if (isDirectedGraph())
            adjacencyMatrix.get(index).forEach(edge -> {
                if (edge != null)
                    edges.add(edge);
            });
        return edges;
    }

    @Override
    public int getInDegree(int index) {
        rangeCheck(index);
        return (int) adjacencyMatrix.stream().filter(vector -> vector.get(index) != null).count();
    }

    @Override
    public int getOutDegree(int index) {
        rangeCheck(index);
        return (int) adjacencyMatrix.get(index).stream().filter(Objects::nonNull).count();
    }

    @Override
    public int getDegree(int index) {
        int degree = getOutDegree(index);
        if (isDirectedGraph()) {
            degree += (int) adjacencyMatrix.stream().filter(vector -> vector.get(index) != null).count();
            if (hasEdge(index, index))
                return degree - 1;
            return degree;
        }
        return degree;
    }

    @Override
    public int getFirstInAdjacentVertexIndex(int index) {
        Edge<T, V> firstInEdge = getFirstInEdge(index);
        if (firstInEdge == null)
            return -1;
        return firstInEdge.getTailIndex();
    }

    /**
     * 不能用返回值来作为衡量顶点存在的依据，因为顶点可以存入null
     */
    @Override
    public E getFirstInAdjacentVertex(int index) {
        int firstInAdjacentVertexIndex = getFirstInAdjacentVertexIndex(index);
        if (firstInAdjacentVertexIndex == -1)
            return null;
        return vertexes.get(firstInAdjacentVertexIndex);
    }

    @Override
    public List<E> getInAdjacentVertexes(int index) {
        if (isDirectedGraph()) {
            rangeCheck(index);
            List<E> vertexes = new ArrayList<>();
            for (int i = 0; i < this.vertexes.size(); i++)
                if (index != i && adjacencyMatrix.get(i).get(index) != null)
                    vertexes.add(this.vertexes.get(i));
            return vertexes;
        }
        return getAdjacentVertexes(index);
    }

    @Override
    public Set<Integer> getInAdjacentVertexIndexes(int index) {
        if (isDirectedGraph()) {
            rangeCheck(index);
            Set<Integer> vertexIndexes = new HashSet<>();
            for (int i = 0; i < this.vertexes.size(); i++)
                if (index != i && adjacencyMatrix.get(i).get(index) != null)
                    vertexIndexes.add(i);
            return vertexIndexes;
        }
        return getAdjacentVertexIndexes(index);
    }

    @Override
    public int getFirstOutAdjacentVertexIndex(int index) {
        Edge<T, V> firstOutEdge = getFirstOutEdge(index);
        if (firstOutEdge == null)
            return -1;
        return firstOutEdge.getHeadIndex();
    }

    @Override
    public E getFirstOutAdjacentVertex(int index) {
        int firstOutAdjacentVertexIndex = getFirstOutAdjacentVertexIndex(index);
        if (firstOutAdjacentVertexIndex == -1)
            return null;
        return vertexes.get(firstOutAdjacentVertexIndex);
    }

    @Override
    public List<E> getOutAdjacentVertexes(int index) {
        if (isDirectedGraph()) {
            rangeCheck(index);
            List<E> vertexes = new ArrayList<>();
            for (int i = 0; i < this.vertexes.size(); i++)
                if (index != i && adjacencyMatrix.get(index).get(i) != null)
                    vertexes.add(this.vertexes.get(i));
            return vertexes;
        }
        return getAdjacentVertexes(index);
    }

    @Override
    public Set<Integer> getOutAdjacentVertexIndexes(int index) {
        if (isDirectedGraph()) {
            rangeCheck(index);
            Set<Integer> vertexIndexes = new HashSet<>();
            for (int i = 0; i < this.vertexes.size(); i++)
                if (index != i && adjacencyMatrix.get(index).get(i) != null)
                    vertexIndexes.add(i);
            return vertexIndexes;
        }
        return getAdjacentVertexIndexes(index);
    }

    @Override
    public E getFirstAdjacentVertex(int index) {
        return getFirstOutAdjacentVertex(index);
    }

    @Override
    public int getFirstAdjacentVertexIndex(int index) {
        return getFirstOutAdjacentVertexIndex(index);
    }

    @Override
    public List<E> getAdjacentVertexes(int index) {
        rangeCheck(index);
        List<E> vertexes = new ArrayList<>();
        for (int i = 0; i < this.vertexes.size(); i++) {
            if (index == i)
                continue;
            if (adjacencyMatrix.get(index).get(i) != null)
                vertexes.add(this.vertexes.get(i));
            if (isDirectedGraph() && adjacencyMatrix.get(i).get(index) != null)
                vertexes.add(this.vertexes.get(i));
        }
        return vertexes;
    }

    @Override
    public Set<Integer> getAdjacentVertexIndexes(int index) {
        rangeCheck(index);
        Set<Integer> vertexIndexes = new HashSet<>();
        for (int i = 0; i < this.vertexes.size(); i++) {
            if (index == i)
                continue;
            if (adjacencyMatrix.get(index).get(i) != null)
                vertexIndexes.add(i);
            if (isDirectedGraph() && adjacencyMatrix.get(i).get(index) != null)
                vertexIndexes.add(i);
        }
        return vertexIndexes;
    }

    /**
     * 在有向图中具有方向性，对没有入边的顶点，遍历不在同一个连通分量中
     */
    @Override
    public List<List<Integer>> DFSTraverse() {
        List<List<Integer>> oneTraversal = new ArrayList<>();
        if (vertexNum == 0)
            return oneTraversal;
        visited = new boolean[vertexNum];
        for (int i = 0; i < vertexNum; i++)
            if (!visited[i])
                oneTraversal.add(DFS(new ArrayList<>(), i));

        return oneTraversal;
    }

    private List<Integer> DFS(List<Integer> path, int v) {
        visited[v] = true;
        path.add(v);
        getOutAdjacentVertexIndexes(v).forEach(index -> {
            if (!visited[index])
                DFS(path, index);
        });
        return path;
    }

    /**
     * 在有向图中具有方向性，对没有入边的顶点，遍历不在同一个连通分量中
     */
    @Override
    public List<List<Integer>> BFSTraverse() {
        List<List<Integer>> oneTraversal = new ArrayList<>();
        if (vertexNum == 0)
            return oneTraversal;
        visited = new boolean[vertexNum];
        queue = new LinkedList<>();
        for (int i = 0; i < vertexNum; i++)
            if (!visited[i])
                oneTraversal.add(BFS(new ArrayList<>(), i));

        return oneTraversal;
    }

    private List<Integer> BFS(List<Integer> path, int v) {
        visited[v] = true;
        path.add(v);
        queue.offer(v);
        while (!queue.isEmpty()) {
            getOutAdjacentVertexIndexes(queue.poll()).forEach(index -> {
                if (!visited[index]) {
                    visited[index] = true;
                    path.add(index);
                    queue.offer(index);
                }
            });
        }
        return path;
    }

    @Override
    public boolean isDirectedGraph() {
        return graphKind == GraphKind.DG || graphKind == GraphKind.DN;
    }

    @Override
    public boolean isCompletedGraph() {
        if ((isDirectedGraph() && edgeNum == vertexNum * (vertexNum - 1)) || (edgeNum == vertexNum * (vertexNum - 1) / 2)) {
            for (int i = 0; i < vertexNum; i++)
                if (adjacencyMatrix.get(i).get(i) != null)
                    return false;
            return true;
        }
        return false;
    }

    /**
     * 无向图中所有顶点之间均可达；
     * 有向图中所有顶点之间都有路径
     * （该图只有一个联通分量）
     *
     * @return true表示该图是连通图
     */
    @Override
    public boolean isConnectedGraph() {
        if (edgeNum == 0) {
            return false;
        }
        visited = new boolean[vertexNum];
        traversableNum = 0;
        return isConnectedGraphByDFS(0) == vertexNum;
    }

    @Override
    public boolean isEmptyGraph() {
        return vertexNum == 0;
    }

    private int isConnectedGraphByDFS(int v) {
        visited[v] = true;
        ++traversableNum;
        getOutAdjacentVertexIndexes(v).forEach(index -> {
            if (!visited[index])
                isConnectedGraphByDFS(index);
        });
        return traversableNum;
    }

    @Override
    public boolean isNetwork() {
        return graphKind == GraphKind.DN || graphKind == GraphKind.UDN;
    }

    @Override
    public Object[] getVertexes() {
        return vertexes.toArray();
    }

    @Override
    public Set<Edge<T, V>> getEdges() {
        Set<Edge<T, V>> edges = new HashSet<>();
        for (int i = 0; i < vertexNum; i++)
            for (int j = i; j < vertexNum; j++) {
                Edge<T, V> edge = adjacencyMatrix.get(i).get(j);
                if (edge != null)
                    edges.add(edge);
            }
        if (isDirectedGraph()) {
            for (int i = 0; i < vertexNum; i++)
                for (int j = 0; j < i; j++) {
                    Edge<T, V> edge = adjacencyMatrix.get(i).get(j);
                    if (edge != null)
                        edges.add(edge);
                }

        }
        return edges;
    }

    /**
     * 单源路径问题，在有向图中具有有方向性，
     *
     * @param index1 顶点1索引值
     * @param index2 顶点2索引值
     * @return 路径经过的顶点索引值的集合
     */
    @Override
    public List<Integer> getFirstPath(int index1, int index2) {
        rangeCheck(index1);
        rangeCheck(index2);
        LinkedList<Integer> path = new LinkedList<>();
        if (edgeNum == 0)
            return path;
        if (index1 == index2) {
            if (adjacencyMatrix.get(index1).get(index2) != null) {
                path.add(index1);
                path.add(index2);
            }
            return path;
        }
        visited = new boolean[vertexNum];
        getFirstPathByDFS(path, index1, index2);
        return path;
    }

    private boolean getFirstPathByDFS(LinkedList<Integer> path, int v, int index2) {
        visited[v] = true;
        path.offerLast(v);
        if (v == index2)
            return true;
        for (Integer index : getOutAdjacentVertexIndexes(v)) {
            if (!visited[index]) {
                if (getFirstPathByDFS(path, index, index2))
                    return true;
            }
        }
        path.pollLast();
        return false;
    }

    @Override
    public List<List<Integer>> getPaths(int index1, int index2) {
        rangeCheck(index1);
        rangeCheck(index2);
        paths = new ArrayList<>();
        if (edgeNum == 0)
            return paths;
        if (index1 == index2)
            if (adjacencyMatrix.get(index1).get(index2) != null)
                paths.add(Stream.of(index1, index2).collect(Collectors.toList()));
        visited = new boolean[vertexNum];
        getPathsByDFS(new LinkedList<>(), index2, index1, index1);
        return paths;
    }


    @SuppressWarnings("unchecked")
    private void getPathsByDFS(LinkedList<Integer> path, int index2, int v, int pre) {
        visited[v] = true;
        path.offerLast(v);
        if (v == index2 && pre != index2)
            paths.add((List<Integer>) path.clone());
        for (Integer index : getOutAdjacentVertexIndexes(v)) {
            if (index == pre)
                continue;
            if (!visited[index])
                getPathsByDFS(path, index2, index, v);
        }
        path.pollLast();
        visited[v] = false;
    }


    private boolean hasPathByDFS(int v, int index2) {
        visited[v] = true;
        if (v == index2)
            return true;
        for (Integer index : getOutAdjacentVertexIndexes(v))
            if (!visited[index]) {
                if (hasPathByDFS(index, index2))
                    return true;
            }

        return false;
    }

    /**
     * 在有向图中该方法具有方向性
     *
     * @param index1 索引值1
     * @param index2 索引值2
     * @return true表示两点之间存在路径
     */
    @Override
    public boolean hasPath(int index1, int index2) {
        rangeCheck(index1);
        rangeCheck(index2);
        if (index1 == index2)
            if (adjacencyMatrix.get(index1).get(index2) != null)
                return true;
        visited = new boolean[vertexNum];
        return hasPathByDFS(index1, index2);
    }

    /**
     * 这是我写的最后一个方法，不想优化了，写吐了，就这样吧。。。
     */
    @Override
    public List<Integer> getFirstCycle(int index) {
        List<List<Integer>> cycles = getCycles(index);
        if (cycles.size() > 0) {
            return cycles.get(0);
        }
        return new ArrayList<>();
    }

    /**
     * 在有向图中具有方向性
     *
     * @param index 顶点索引值
     * @return true表示该顶点存在回路/环
     */
    @Override
    public boolean hasCycle(int index) {
        if (edgeNum == 0)
            return false;
        if (hasLoop(index))
            return true;
        visited = new boolean[vertexNum];
        return hasCycleByDFS(index, index, index);
    }

    private boolean hasCycleByDFS(int v, int pre) {
        visited[v] = true;
        for (Integer index : getOutAdjacentVertexIndexes(v)) {
            if (index == pre)
                continue;
            if (visited[index] || adjacencyMatrix.get(index).get(index) != null)
                return true;
            if (hasCycleByDFS(index, v))
                return true;
        }
        return false;
    }

    private boolean hasCycleByDFS(int root, int v, int pre) {
        visited[v] = true;
        for (Integer index : getOutAdjacentVertexIndexes(v)) {
            if (index == pre)
                continue;
            if (visited[index] && index == root)
                return true;
            if (hasCycleByDFS(root, index, v))
                return true;
        }
        return false;
    }

    /**
     * 在有向图中具有方向性
     *
     * @return true表示该图存在环
     */
    @Override
    public boolean hasCycle() {
        if (edgeNum == 0)
            return false;
        visited = new boolean[vertexNum];
        return hasCycleByDFS(0, 0);
    }

    @Override
    public List<List<Integer>> getCycles(int index) {
        rangeCheck(index);
        cycles = new ArrayList<>();
        if (edgeNum == 0)
            return cycles;
        visited = new boolean[vertexNum];
        getCyclesByDFS(new LinkedList<>(), index, index, index);
        Edge<T, V> edge = adjacencyMatrix.get(index).get(index);
        if (edge != null)
            cycles.add(Stream.of(index).collect(Collectors.toList()));
        return cycles;
    }

    @Override
    public List<List<Integer>> getCycles() {
        cycles = new ArrayList<>();
        if (edgeNum == 0)
            return cycles;
        visited = new boolean[vertexNum];
        for (int i = 0; i < vertexNum; i++) {
            getCyclesByDFS(new LinkedList<>(), i, i, i);
            Edge<T, V> edge = adjacencyMatrix.get(i).get(i);
            if (edge != null)
                cycles.add(Stream.of(i).collect(Collectors.toList()));
        }
        return cycles;
    }


    @SuppressWarnings("unchecked")
    private void getCyclesByDFS(LinkedList<Integer> cycle, int root, int v, int pre) {
        visited[v] = true;
        cycle.offerLast(v);
        for (Integer index : getOutAdjacentVertexIndexes(v)) {
            if (index == pre)
                continue;
            if (visited[index]) {
                if (index == root)
                    cycles.add((List<Integer>) cycle.clone());
                continue;
            }
            getCyclesByDFS(cycle, root, index, v);
        }
        cycle.pollLast();
        visited[v] = false;
    }

    @Override
    public Edge<T, V> getLoop(int index) {
        rangeCheck(index);
        return adjacencyMatrix.get(index).get(index);
    }

    @Override
    public boolean hasLoop(int index) {
        return getLoop(index) != null;
    }

    @Override
    public boolean hasLoop() {
        for (int i = 0; i < vertexNum; i++)
            if (adjacencyMatrix.get(i).get(i) != null)
                return true;
        return false;
    }

    private void isConnectedByDFS(int[] visited, int v, int flag) {
        visited[v] = flag;
        getOutAdjacentVertexIndexes(v).forEach(index -> {
            if (visited[index] == 0)
                isConnectedByDFS(visited, index, flag);
        });
    }


    /**
     * 判断索引值对应的顶点是否连通
     * index1==index2：检查索引值是否有自环边
     * 无环图中无方向性
     * 有环图中具有方向性
     *
     * @param index1 索引值1
     * @param index2 索引值2
     * @return true表示两点连通
     */
    @Override
    public boolean isConnected(int index1, int index2) {
        rangeCheck(index1);
        rangeCheck(index2);
        if (index1 == index2)
            return adjacencyMatrix.get(index1).get(index1) != null;
        int[] visited = new int[vertexNum];
        for (int i = index1, flag = 1; i < vertexNum; i++)
            if (visited[i] == 0) {
                isConnectedByDFS(visited, i, flag);
                flag++;
            }
        return visited[index1] == visited[index2];
    }
}
