package policy.structure;

public class HeapElement {
    public Long data;
    public int refCount;
    public int bufferIdx;

    public static int parent(int i) {
        return (i - 1) / 2;
    }

    public static int left(int i) {
        return 2 * i + 1;
    }

    public static int right(int i) {
        return 2 * i + 2;
    }

    public HeapElement(Long data, int refCount, int bufferIdx) {
        this.data = data;
        this.refCount = refCount;
        this.bufferIdx = bufferIdx;
    }
}
