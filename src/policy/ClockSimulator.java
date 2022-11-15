package policy;

import java.util.HashMap;
import java.util.Map;

import static etc.Font.*;
import static etc.IO.*;

public class ClockSimulator extends ReplacementSimulator {
    private Map<Long, Integer> map = new HashMap<>();
    private int pointer;
    private boolean[] referenceBit;

    protected ClockSimulator() {

    }

    public ClockSimulator(int bufferSize) {
        super.name = "clock";
        initialization(bufferSize);
        referenceBit = new boolean[bufferSize];
        pointer = -1;
    }

    public void put(Long data) {
        totalHit++;

        int i;
        if (search(data)) {
            cacheHit++;
            i = map.get(data);
            validate(i);

            hitBufferIdx = i;
            missBufferIdx = -1;
            return;
        }

        cacheMiss++;
        if (map.size() < bufferSize) {
            i = map.size();
            buffer[i] = data;
            map.put(data, i);
            hitBufferIdx = -1;
            missBufferIdx = i;
        } else {
            while (true) {
                advancePointer();
                if (!isValid(pointer))
                    break;
                invalidate(pointer);
            }

            map.remove(buffer[pointer]);
            buffer[pointer] = data;
            map.put(data, pointer);
            hitBufferIdx = -1;
            missBufferIdx = pointer;
        }
    }

    public boolean search(Long data) {
        return map.containsKey(data);
    }

    private void invalidate(int i) {
        referenceBit[i] = false;
    }

    private void validate(int i) {
        referenceBit[i] = true;
    }

    private boolean isValid(int i) {
        return referenceBit[i];
    }

    // circular queue
    private void advancePointer() {
        pointer = (pointer + 1) % bufferSize;
    }

    @Override
    protected void printTitle() {
        System.out.println();
        System.out.printf("%s animation. (", name);
        printf(FONT_BLUE, "blue : hit, ");
        printf(FONT_RED, "red : page fault, ");
        printf(LINE, "underline : reference bit");
        System.out.println(")");
        System.out.printf("|");
        printf(FONT_GREEN, " time ");
        System.out.printf("|");
        printf(FONT_PURPLE, " stream ");
        System.out.printf("|");
        for (int i = 1; i <= bufferSize; i++) {
            System.out.printf(centerAlign("page"+i, 8));
            System.out.printf("|");
        }
        printf(FONT_CYAN, "cursor");
        System.out.printf("|");
        System.out.println();
    }

    public void showBuffer() {
        for (int i = 0; i < bufferSize; i++) {
            Long data = buffer[i];
            if (data == -1L) {
                System.out.printf(centerAlign("X", 8));
            } else {
                String color;
                if (i == hitBufferIdx) {
                    color = FONT_BLUE;
                } else if (i == missBufferIdx) {
                    color = FONT_RED;
                } else {
                    color = RESET;
                }

                if (isValid(i)) {
                    printf(color, centerAlignAndEffect(data.toString(), 8, LINE));
                } else {
                    printf(color, centerAlign(data.toString(), 8));
                }
            }
            System.out.printf("|");
        }
        printf(FONT_CYAN, centerAlign(Integer.toString(pointer), 6));
        System.out.printf("|");
        System.out.println();
    }
}
