package policy;

import policy.structure.ListNode;

import java.util.HashMap;
import java.util.Map;

public class LRUSimulator extends ReplacementSimulator {
    private Map<Long, ListNode> map = new HashMap<>();
    private ListNode head;
    private ListNode tail;

    protected LRUSimulator() {

    }

    public LRUSimulator(int bufferSize) {
        super.name = "LRU";
        initialization(bufferSize);

        // double linked list
        head = new ListNode(-1);
        tail = new ListNode(-1);
        head.next = tail;
        tail.prev = head;
    }

    public void put(Long data) {
        totalHit++;

        if (search(data)) {
            cacheHit++;
            ListNode node = map.get(data);
            remove(node);
            addFirst(node);

            hitBufferIdx = node.idx;
            missBufferIdx = -1;
            return;
        }

        cacheMiss++;
        int idx;
        if (map.size() < bufferSize) {
            idx = map.size();
        } else {
            idx = tail.prev.idx;
            remove(tail.prev);
            map.remove(buffer[idx]);
        }
        buffer[idx] = data;
        ListNode node = new ListNode(idx);
        addFirst(node);
        map.put(data, node);

        hitBufferIdx = -1;
        missBufferIdx = idx;
    }

    public boolean search(Long data) {
        return map.containsKey(data);
    }

    private ListNode remove(ListNode node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
        return node;
    }

    private void addFirst(ListNode node) {
        head.next.prev = node;
        node.next = head.next;
        node.prev = head;
        head.next = node;
    }
}
