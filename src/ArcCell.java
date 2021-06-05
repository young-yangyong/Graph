import com.sun.istack.internal.NotNull;

public class ArcCell<T, V> {
    @NotNull
    public int i;
    @NotNull
    public int j;
    public T weight;
    public V v;

    public ArcCell(int i, int j, T weight, V v) {
        this(i, j);
        this.weight = weight;
        this.v = v;
    }

    public ArcCell(int i, int j) {
        rangeCheck(i);
        rangeCheck(j);
        this.i = i;
        this.j = j;
    }

    private void rangeCheck(int index) {
        if (index < 0)
            throw new IndexOutOfBoundsException(index + "必须>=0!");
    }


    @Override
    public String toString() {
        return "Matrix{" +
                "i=" + i +
                ", j=" + j +
                ", weight=" + weight +
                ", v=" + v +
                '}';
    }
}

