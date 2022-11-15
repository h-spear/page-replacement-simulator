package policy.structure;

public class ListNode {
    public int idx;
    public ListNode prev;
    public ListNode next;

    public ListNode(int idx) {
        this.idx = idx;
        this.prev = null;
        this.next = null;
    }
}
