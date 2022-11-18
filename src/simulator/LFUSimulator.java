package simulator;

import java.util.*;

import settings.Settings;
import simulator.structure.HeapElement;
import static simulator.structure.HeapElement.*;

public class LFUSimulator extends ReplacementSimulator {
    private Map<Long, Integer> map = new HashMap<>();
    private HeapElement[] heap;
    private int heapSize;

    protected LFUSimulator() {

    }

    public LFUSimulator(int bufferSize) {
        super.name = Settings.LFU_SIMULATOR_NAME;
        heap = new HeapElement[bufferSize];
        heapSize = 0;
        initialization(bufferSize);
    }

    public void put(Long data) {
        totalHit++;

        if (search(data)) {
            cacheHit++;
            increment(map.get(data));
        } else {
            cacheMiss++;
            insert(data);
        }
    }

    public boolean search(Long data) {
        return map.containsKey(data);
    }

    private void increment(int i) {
        heap[i].refCount++;
        hitBufferIdx = heap[i].idx;
        missBufferIdx = -1;
        heapify(i);
    }

    private void insert(Long data) {
        // x : data, y : frequency
        int idx = heapSize;
        if (heapSize == bufferSize) {
            idx = heap[0].idx;
            map.remove(buffer[idx]);
            heap[0] = heap[--heapSize];
            heapify(0);
        }
        heap[heapSize] = new HeapElement(idx);
        buffer[idx] = data;
        hitBufferIdx = -1;
        missBufferIdx = idx;
        map.put(data, heapSize++);

        int i = heapSize - 1;
        while (i > 0 && heap[parent(i)].refCount > heap[i].refCount) {
            map.put(buffer[heap[i].idx], parent(i));
            map.put(buffer[heap[parent(i)].idx], i);

            HeapElement temp = heap[i];
            heap[i] = heap[parent(i)];
            heap[parent(i)] = temp;

            i = parent(i);
        }
    }

    private void heapify(int i) {
        int l = left(i), r = right(i), minimum;

        if (l < heapSize) {
            minimum = heap[i].refCount < heap[l].refCount ? i : l;
        } else {
            minimum = i;
        }
        if (r < heapSize)
            minimum = heap[minimum].refCount < heap[r].refCount ? minimum : r;

        if (minimum != i) {
            map.put(buffer[heap[minimum].idx], i);
            map.put(buffer[heap[i].idx], minimum);

            HeapElement temp = heap[minimum];
            heap[minimum] = heap[i];
            heap[i] = temp;

            heapify(minimum);
        }
    }
}
