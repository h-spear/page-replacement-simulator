package simulator;

import settings.Settings;

import java.util.*;

public class RandomSimulator extends ReplacementSimulator {
    private Map<Long, Integer> map = new HashMap<>();
    private Random random = new Random();

    protected RandomSimulator() {

    }

    public RandomSimulator(int bufferSize) {
        super.name = Settings.RANDOM_SIMULATOR_NAME;
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
        if (map.size() < bufferSize) {
            idx = map.size();
        } else {
            idx = random.nextInt(bufferSize);
            map.remove(buffer[idx]);
        }
        buffer[idx] = data;
        map.put(data, idx);

        hitBufferIdx = -1;
        missBufferIdx = idx;
    }

    public boolean search(Long data) {
        return map.containsKey(data);
    }
}
