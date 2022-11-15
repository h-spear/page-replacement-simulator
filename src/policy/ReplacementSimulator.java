package policy;

import static etc.IO.*;
import static etc.Color.*;

import java.io.*;
import java.util.List;

abstract class ReplacementSimulator implements Simulator {
    protected String name = "Simulator";
    protected Long[] buffer;
    protected int bufferSize;
    protected int totalHit;
    protected int cacheHit;
    protected int cacheMiss;
    protected int hitBufferIdx;
    protected int missBufferIdx;

    abstract void put(Long data);
    abstract boolean search(Long data);

    @Override
    public void simulate(File file) throws IOException {
        InputStream in = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = br.readLine()) != null) {
            Long data = Long.parseLong(line);
            put(data);
        }
        report();
        System.out.println();
    }

    @Override
    public void simulate(Long[] stream) {
        System.out.println();
        System.out.printf("%s animation. (", name);
        printf(FONT_BLUE, "blue : hit, ");
        printf(FONT_RED, "red : page fault");
        System.out.println(")");
        System.out.printf("|");
        printf(FONT_GREEN, " time ");
        System.out.printf("|");
        printf(FONT_PURPLE, "  data  ");
        System.out.printf("|");
        for (int i = 1; i <= bufferSize; i++) {
            System.out.printf(centerAlign("page"+i, 8));
            System.out.printf("|");
        }
        System.out.println();
        Integer t = 0;
        for (Long data: stream) {
            ++t;
            put(data);
            System.out.printf("|");
            printf(FONT_GREEN, centerAlign(t.toString(), 6));
            System.out.printf("|");

            printf(FONT_PURPLE, centerAlign(data.toString(), 8));
            System.out.printf("|");
            showBuffer();
        }
        System.out.println();
        report();
        System.out.println("===============================================================");
        System.out.println();
    }

    protected void initialization(int _bufferSize) {
        totalHit = 0;
        cacheHit = 0;
        cacheMiss = 0;
        bufferSize = _bufferSize;
        buffer = new Long[bufferSize];
        for (int i = 0; i < bufferSize; ++i)
            buffer[i] = -1L;    // 데이터가 없으면 -1
    }

    private void showBuffer() {
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
                printf(color, centerAlign(data.toString(), 8));
            }
            System.out.printf("|");
        }
        System.out.println();
    }

    private void report() {
        double hitRatio =  ((double)cacheHit / (double)totalHit) * 100;
        println(FONT_BLACK + BACKGROUND_YELLOW, " " + name + " simulator report ");
        System.out.println("  Buffer Size : " + bufferSize);
        println(FONT_BLUE, "  Cache Hit   : " + cacheHit);
        println(FONT_RED, "  Cache Miss  : " + cacheMiss);
        println(FONT_GREEN, "  Total Hit   : " + totalHit);
        System.out.printf(FONT_PURPLE + "  Hit Ratio   : %8.6f%%\n" + RESET, hitRatio);
    }
}
