package simulator;

import settings.Settings;
import file.XSSFHelper;

import static etc.IO.*;
import static etc.IOFont.*;

import java.io.*;
import java.util.Objects;

abstract class ReplacementSimulator implements Simulator {
    protected String name = "Simulator";
    protected long[] buffer;
    protected int bufferSize;
    protected int totalHit;
    protected int cacheHit;
    protected int cacheMiss;
    protected int hitBufferIdx;
    protected int missBufferIdx;
    protected XSSFHelper xssfHelper;

    abstract void put(Long data);
    abstract boolean search(Long data);

    @Override
    public String getName() {
        return name;
    }

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
    }

    @Override
    public void simulate(File file, File outputFile) throws IOException  {
        InputStream in = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        xssfHelper = new XSSFHelper();
        writeTitleToXSSF();
        String line;
        int t = 0;
        while ((line = br.readLine()) != null) {
            ++t;
            Long data = Long.parseLong(line);
            put(data);
            writeBufferLineToXSSF(t, data);
        }
        report();
        writeReportToXSSF();
        String filePath = xssfHelper.save(outputFile);
        System.out.println("saved! " + filePath);
    }

    @Override
    public void simulate(long[] stream, boolean animation) {
        System.out.println();
        if (animation)
            printTitle();

        Integer t = 0;
        for (Long data: stream) {
            ++t;
            put(data);
            if (animation)
                showBuffer(t, data);
        }
        report();
        if (animation) {
            System.out.println("===============================================================");
        }
    }

    @Override
    public void simulate(long[] stream, boolean animation, File outputFile) {
        xssfHelper = new XSSFHelper();
        writeTitleToXSSF();
        System.out.println();
        if (animation)
            printTitle();

        Integer t = 0;
        for (Long data: stream) {
            ++t;
            put(data);

            if (animation)
                showBuffer(t, data);
            writeBufferLineToXSSF(t, data);
        }
        report();
        writeReportToXSSF();
        String filePath = xssfHelper.save(outputFile);
        System.out.println("saved! " + filePath);
        if (animation) {
            System.out.println("===============================================================");
            System.out.println();
        }
    }

    protected void initialization(int _bufferSize) {
        totalHit = 0;
        cacheHit = 0;
        cacheMiss = 0;
        bufferSize = _bufferSize;
        buffer = new long[bufferSize];
        for (int i = 0; i < bufferSize; ++i)
            buffer[i] = -1L;    // 데이터가 없으면 -1
    }

    protected void printTitle() {
        System.out.printf("%s animation. (", name);
        printf(FONT_BLUE, "blue : hit, ");
        printf(FONT_RED, "red : page fault");
        System.out.println(")");
        System.out.print("|");
        printf(FONT_GREEN, " time ");
        System.out.print("|");
        printf(FONT_PURPLE, " stream ");
        System.out.print("|");
        for (int i = 1; i <= bufferSize; i++) {
            System.out.printf(centerAlign("page"+i, 8));
            System.out.print("|");
        }
        System.out.println();
    }

    protected void showBuffer(Integer t, Long currData) {
        System.out.print("|");
        printf(FONT_GREEN, centerAlign(t.toString(), 6));
        System.out.print("|");

        printf(FONT_PURPLE, centerAlign(currData.toString(), 8));
        System.out.print("|");

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
            System.out.print("|");
        }
        System.out.println();
    }

    protected void report() {
        double hitRatio =  ((double)cacheHit / (double)totalHit) * 100;
        System.out.println();
        println(FONT_BLACK + BACKGROUND_YELLOW, " " + name + " simulator report ");
        System.out.println("  Buffer Size : " + bufferSize);
        println(FONT_BLUE, "  Cache Hit   : " + cacheHit);
        println(FONT_RED, "  Cache Miss  : " + cacheMiss);
        println(FONT_PURPLE, "  Total Hit   : " + totalHit);
        System.out.printf(FONT_GREEN + "  Hit Ratio   : %8.6f%%\n" + RESET, hitRatio);
    }

    // excel write
    protected void writeTitleToXSSF() {
        xssfHelper.createRow(1);
        xssfHelper.writeCell(1, name + " simulator report", xssfHelper.styleOfTitle);

        xssfHelper.createRow(9);
        xssfHelper.writeCell(1, "time", xssfHelper.styleOfFontGreen);
        xssfHelper.writeCell(2, "stream", xssfHelper.styleOfFontViolet);
        for (int i = 1; i <= bufferSize; i++) {
            xssfHelper.writeCell(2 + i, "page " + i, xssfHelper.styleOfDefault);
        }
    }

    protected void writeBufferLineToXSSF(int t, long data) {
        xssfHelper.createRow(t + 9);
        xssfHelper.writeCell(1, t, xssfHelper.styleOfFontGreen);
        xssfHelper.writeCell(2, data, xssfHelper.styleOfFontViolet);
        xssfHelper.writeBufferLine(3, buffer, hitBufferIdx, missBufferIdx);
    }

    protected void writeReportToXSSF() {
        xssfHelper.createRow(3);
        xssfHelper.writeCell(1, "buffer size", xssfHelper.styleOfDefault);
        xssfHelper.writeCell(2, bufferSize, xssfHelper.styleOfDefault);

        xssfHelper.createRow(4);
        xssfHelper.writeCell(1, "cache hit", xssfHelper.styleOfFontBlue);
        xssfHelper.writeCell(2, cacheHit, xssfHelper.styleOfFontBlue);

        xssfHelper.createRow(5);
        xssfHelper.writeCell(1, "cache miss", xssfHelper.styleOfFontRed);
        xssfHelper.writeCell(2, cacheMiss, xssfHelper.styleOfFontRed);

        xssfHelper.createRow(6);
        xssfHelper.writeCell(1, "total hit", xssfHelper.styleOfFontViolet);
        xssfHelper.writeCell(2, totalHit, xssfHelper.styleOfFontViolet);

        xssfHelper.createRow(7);
        xssfHelper.writeCell(1, "hit ratio", xssfHelper.styleOfFontGreen);
        xssfHelper.writeCell(2, ((double)cacheHit / (double)totalHit) * 100, xssfHelper.styleOfFontGreen);
        xssfHelper.writeCell(4, "hit", xssfHelper.styleOfHit);
        xssfHelper.writeCell(5, "miss", xssfHelper.styleOfMiss);
        if (Objects.equals(name, Settings.CLOCK_SIMULATOR_NAME))
            xssfHelper.writeCell(6, "reference", xssfHelper.styleOfRef);
    }
}
