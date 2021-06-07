import com.sun.istack.internal.NotNull;
import graph.GraphKind;

import java.util.ArrayList;
import java.util.List;


/**
 * 数据结构-图（邻接矩阵表示法）
 * 索引均从0开始计算
 *
 */
public class MGraph<E, T, V> {
    /**
     * 顶点向量
     */
    private List<E> vexs;

    /**
     * 邻接矩阵
     */
    private List<List<AdjMatrix<T,V>>> arcs;
    /**
     * 图的当前顶点数和弧数
     */
    private int vexNum, arcNum;
    /**
     * 图的种类标志
     */
    private GraphKind kind;

    /**
     * 生成kind类型的无弧/边图
     *
     * @param vexs 顶点集合
     * @param kind 图类型
     */
    public MGraph(@NotNull List<E> vexs, @NotNull GraphKind kind) {
        this.vexs = vexs;
        this.vexNum = vexs.size();
        this.kind = kind;
    }

    /**
     * 生成kind类型的有弧/边图
     *
     * @param vexs     顶点集合
     * @param arcCells 弧/边集合
     * @param kind     图类型
     */
    public MGraph(@NotNull List<E> vexs, @NotNull List<ArcCell<T, V>> arcCells, @NotNull GraphKind kind) {
        this(vexs, kind);
        this.arcs = arcCellsToAdjMatrix(arcCells, vexs.size());
        this.arcNum = arcCells.size();
    }

    /**
     * 判断顶点下标是否越界
     *
     * @param index 顶点下标
     */
    private void rangeCheck(int index) {
        if (index < 0 || index > vexNum - 1)
            throw new IndexOutOfBoundsException("顶点下标必须<" + (vexNum - 1) + "并且>=0！");
    }


    /**
     * 将弧/边集合转换成邻接矩阵
     *
     * @param arcCells 弧/边集合
     * @param length   邻接矩阵的阶数
     * @return 邻接矩阵
     */
    private List<List<AdjMatrix<T,V>>> arcCellsToAdjMatrix(List<ArcCell<T, V>> arcCells, int length) {
        List<List<AdjMatrix<T,V>>> arcs = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            ArrayList<AdjMatrix<T,V>> adjMatrices = new ArrayList<>();
            for (int j = 0; j < length; j++) {
                adjMatrices.add(null);
            }
            arcs.add(adjMatrices);
        }
        switch (kind) {
            case DG:
                arcCells.forEach(e -> {
                    rangeCheck(e.i);
                    rangeCheck(e.j);
                    arcs.get(e.i).set(e.j,  new AdjMatrix(true, e.v));
                });
                break;
            case DN:
                arcCells.forEach(e -> {
                    rangeCheck(e.i);
                    rangeCheck(e.j);
                    arcs.get(e.i).set(e.j, new AdjMatrix<>(e.weight, e.v));
                });
                break;
            case UDG:
                arcCells.forEach(e -> {
                    rangeCheck(e.i);
                    rangeCheck(e.j);
                    arcs.get(e.j).set(e.i, new AdjMatrix(true, e.v));
                    arcs.get(e.i).set(e.j, new AdjMatrix(true, e.v));
                });
                break;
            case UDN:
                arcCells.forEach(e -> {
                    rangeCheck(e.i);
                    rangeCheck(e.j);
                    arcs.get(e.i).set(e.j, new AdjMatrix<>(e.weight, e.v));
                    arcs.get(e.j).set(e.i, new AdjMatrix<>(e.weight, e.v));
                });
                break;
            default:
                throw new NullPointerException("未指定图类型！");

        }
        return arcs;
    }

    @Override
    public String toString() {
        return "test.test.MGraph{\n" +
                "vexs=" + vexs +
                ", \narcs=" + arcsToString(arcs) +
                ", \nvexNum=" + vexNum +
                ", \narcnum=" + arcNum +
                ", \nkind=" + kind +
                "\n}";
    }

    /**
     * 实现邻接矩阵的打印方法以辅助MGraph类实现toString方法
     *
     * @param arcs 邻接矩阵
     * @return 邻接矩阵的toString()
     */
    private String arcsToString(List<List<AdjMatrix<T,V>>> arcs) {
        final StringBuilder stringBuilder = new StringBuilder().append("[\n");
        for (List<AdjMatrix<T,V>> arc : arcs) {
            stringBuilder.append(arc + "\n");
        }
        return stringBuilder.append("]").toString();
    }

    /**
     * 判断顶点o在图中是否存在
     *
     * @param vex 顶点变量
     * @return true表示存在
     */
    public boolean contains(E vex) {
        return vexs.contains(vex);
    }

    /**
     * 返回图中指定顶点第一次出现的索引，
     * 如果此图不包含该顶点，则返回 -1。
     *
     * @param vex 顶点变量
     * @return -1 or index
     */
    public int indexOf(E vex) {
        return vexs.indexOf(vex);
    }

    /**
     * 根据index索引获取顶点
     *
     * @param index 索引值
     * @return index索引下顶点
     */
    public E getVex(int index) {
        return vexs.get(index);
    }

    /**
     * 修改index索引下的顶点
     *
     * @param index 索引值
     * @param vex   新的顶点
     * @return 旧的顶点
     */
    public E setVex(int index, E vex) {
        return vexs.set(index, vex);
    }

    /**
     * 向图中添加顶点，默认在List末尾
     *
     * @param vex 待添加的顶点
     * @return true表示添加成功
     */
    public boolean addVex(E vex) {

        vexs.add(vex);
        vexNum += 1;
        ArrayList<AdjMatrix<T,V>> adjMatrices = new ArrayList<>();
        //循环旧的vexnum次
        arcs.forEach(e -> {
            e.add(null);
            //复用循环，降低时间复杂度
            adjMatrices.add(null);
        });
        adjMatrices.add(null);
        arcs.add(adjMatrices);
        return true;
    }

    /**
     * 向图中index索引处添加顶点；
     * 该方法时间复杂度巨大（ArrayList本身的add()方法就是O(n/2)的复杂度），不建议使用，
     * 使用 addVex(E vex)已经足够了
     *
     * @param index 索引值
     * @param vex   待添加的顶点
     */
    public void add(int index, E vex) {
        vexs.add(index, vex);
        vexNum += 1;
        ArrayList<AdjMatrix<T,V>> adjMatrices = new ArrayList<>();
        //循环旧的vexnum次
        arcs.forEach(e -> {
            e.add(index, null);
            //复用循环，降低时间复杂度
            adjMatrices.add(null);
        });
        adjMatrices.add(null);
        arcs.add(index, adjMatrices);
    }

    /**
     * 返回图的顶点数量
     *
     * @return 顶点数量
     */
    public int getVexNum() {
        return vexNum;
    }

    /**
     * 返回图的弧/边数量
     *
     * @return 弧/边数量
     */
    public int getArcNum() {
        return arcNum;
    }

    /**
     * 返回图的类型
     *
     * @return 图的类型
     */
    public GraphKind getKind() {
        return kind;
    }

    /**
     * 通过顶点索引值判断是否存在边
     *
     * @param index1 顶点1的索引值
     * @param index2 顶点2的索引值
     * @return true表示两个顶点存在边
     */
    public boolean hasEdgeByIndex(int index1, int index2) {
        rangeCheck(index1);
        rangeCheck(index2);
        return arcs.get(index1).get(index2) != null;
    }

    /**
     * 通过顶点判断是否存在边
     * 注意：使用该方法要确保图中顶点是不重复的，
     * 否则得到的结果可能存在偏差，
     * 建议使用hasEdgeByIndex(int index1, int index2)
     *
     * @param vex1 顶点1
     * @param vex2 顶点1
     * @return true表示两个顶点存在边
     */
    public boolean hasEdgeByVex(E vex1, E vex2) {
        int index1 = indexOf(vex1);
        int index2 = indexOf(vex2);
        if (index1 == -1)
            throw new RuntimeException("顶点" + vex1 + "不存在于该图！");
        if (index2 == -1)
            throw new RuntimeException("顶点" + vex2 + "不存在于该图！");
        return hasEdgeByIndex(index1, index2);
    }

    public List<ArcCell<T, V>> getAdjEdgesByIndex(int index) {
        final ArrayList<ArcCell<T,V>> arcCells = new ArrayList<>();

        List<AdjMatrix<T,V>> adjMatrices = arcs.get(index);
        for (int i = 0; i < adjMatrices.size(); i++) {
            AdjMatrix<T, V> tvAdjMatrix = adjMatrices.get(i);
            if (tvAdjMatrix!=null)
            arcCells.add(new ArcCell<>(index, i, tvAdjMatrix.adj, tvAdjMatrix.info));
        }
        return arcCells;
    }

    public List<ArcCell<T, V>> getAdjEdgesByVex(E vex) {
        int index = indexOf(vex);
        if (index==-1)
            throw new RuntimeException("顶点" + vex + "不存在于该图！");
        return getAdjEdgesByIndex(index);
    }



    /**
     * 弧（Arc）有方向，边（graph.Edge）没有方向；
     * 两者都是表示顶点（graph.Vertex）之间的关系。
     *
     * @param <T> 对无权图，使用boolean类型，用true或false表示相邻否；对带权图，则为权值类型。
     * @param <V> 该弧/边携带的信息类型
     */
    class AdjMatrix<T , V> {
        /**
         * 对无权图，用true或false表示相邻否；
         * 对带权图，则为权值。
         */
        private T adj;
        /**
         * info表示该弧/边携带的信息
         */
        private V info;

        public AdjMatrix(T adj, V info) {
            this.adj = adj;
            this.info = info;
        }

        @Override
        public String toString() {
            return "test.test.ArcCell{" +
                    "adj=" + adj +
                    ", info=" + info +
                    '}';
        }

    }
}
