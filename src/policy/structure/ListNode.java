package policy.structure;

public class ListNode {
    public int idx;
    public Long data;
    public ListNode prev;
    public ListNode next;

    public ListNode(int idx, Long data) {
        this.idx = idx;
        this.data = data;
        this.prev = null;
        this.next = null;
    }
}
