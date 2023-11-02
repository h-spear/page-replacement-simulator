package simulator;

import settings.Settings;

import java.util.HashMap;
import java.util.Map;

import static etc.IOFont.*;
import static etc.IO.*;

public class ClockSimulator extends ReplacementSimulator {
    private Map<Long, Integer> map = new HashMap<>();
    private int pointer;
    private boolean[] referenceBit;

    protected ClockSimulator() {

    }

    public ClockSimulator(int bufferSize) {
        super.name = Settings.CLOCK_SIMULATOR_NAME;
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
    public void printTitle() {
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
        System.out.print("|");
        System.out.println();
    }

    @Override
    public void showBuffer(Integer t, Long currData) {
        System.out.print("|");
        printf(FONT_GREEN, centerAlign(t.toString(), 6));
        System.out.print("|");

        printf(FONT_PURPLE, centerAlign(currData.toString(), 8));
        System.out.print("|");

        for (int i = 0; i < bufferSize; i++) {
            long data = buffer[i];
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
                    printf(color, centerAlignAndEffect(Long.toString(data), 8, LINE));
                } else {
                    printf(color, centerAlign(Long.toString(data), 8));
                }
            }
            System.out.print("|");
        }
        printf(FONT_CYAN, centerAlign(Integer.toString(pointer), 6));
        System.out.print("|");
        System.out.println();
    }

    @Override
    public void writeTitleToXSSF() {
        xssfHelper.createRow(1);
        xssfHelper.writeCell(1, name + " simulator report", xssfHelper.styleOfTitle);

        xssfHelper.createRow(9);
        xssfHelper.writeCell(1, "time", xssfHelper.styleOfFontGreen);
        xssfHelper.writeCell(2, "stream", xssfHelper.styleOfFontViolet);
        xssfHelper.writeCell(3, "cursor", xssfHelper.styleOfFontIndigo);
        for (int i = 1; i <= bufferSize; i++) {
            xssfHelper.writeCell(3 + i, "page " + i, xssfHelper.styleOfDefault);
        }
    }

    @Override
    public void writeBufferLineToXSSF(int t, long data) {
        xssfHelper.createRow(t + 9);
        xssfHelper.writeCell(1, t, xssfHelper.styleOfFontGreen);
        xssfHelper.writeCell(2, data, xssfHelper.styleOfFontViolet);
        xssfHelper.writeCell(3, pointer, xssfHelper.styleOfFontIndigo);
        xssfHelper.writeBufferLine(4, buffer, referenceBit, hitBufferIdx, missBufferIdx);
    }
}
