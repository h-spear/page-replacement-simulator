package simulator;

import settings.Settings;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class FIFOSimulator extends ReplacementSimulator {
    private Deque<Integer> queue = new ArrayDeque<>();
    private Map<Long, Integer> map = new HashMap<>();

    protected FIFOSimulator() {

    }

    public FIFOSimulator(int bufferSize) {
        super.name = Settings.FIFO_SIMULATOR_NAME;
        initialization(bufferSize);
    }

    public void put(Long data) {
        totalHit++;

        if (search(data)) {
            cacheHit++;

            hitBufferIdx = map.get(data);
            missBufferIdx = -1;
            return;
        }

        cacheMiss++;
        int idx;
        if (queue.size() < bufferSize) {
            idx = queue.size();
        } else {
            idx = queue.removeFirst();
            map.remove(buffer[idx]);
        }
        queue.addLast(idx);
        buffer[idx] = data;
        map.put(data, idx);

        hitBufferIdx = -1;
        missBufferIdx = idx;
    }

    public boolean search(Long data) {
        return map.containsKey(data);
    }
}
