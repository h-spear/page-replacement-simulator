package simulator.structure;

public class HeapElement {
    public int idx;
    public int refCount;

    public static int parent(int i) {
        return (i - 1) >> 1;
    }

    public static int left(int i) {
        return (i << 1) + 1;
    }

    public static int right(int i) {
        return (i << 1) + 2;
    }

    public HeapElement(int idx) {
        this.idx = idx;
        this.refCount = 1;
    }
}
