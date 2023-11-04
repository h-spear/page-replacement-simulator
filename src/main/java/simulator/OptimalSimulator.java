package simulator;

import file.FileManager;
import etc.IOFont;
import settings.Settings;
import file.XSSFHelper;

import java.io.*;
import java.util.*;

public class OptimalSimulator extends ReplacementSimulator {

    private Map<Long, Integer> map = new HashMap<>();
    private long[] stream;
    private int streamSize;
    private int streamPointer;

    protected OptimalSimulator() {

    }

    public OptimalSimulator(int bufferSize) {
        super.name = Settings.OPT_SIMULATOR_NAME;
        initialization(bufferSize);
        streamPointer = -1;
    }

    @Override
    public void simulate(File file) throws IOException {
        stream = FileManager.fileToStream(file);
        if (stream == null) {
            System.out.println(IOFont.FONT_RED + "optimal simulator cannot be operated when the stream size is greater than " + Settings.MAX_OPTIMAL_STREAM_SIZE + IOFont.RESET);
            System.out.println();
            return;
        }
        streamSize = stream.length;
        for (int i = 0; i < streamSize; i++) {
            put(stream[i]);
        }
        report();
        System.out.println();
    }

    @Override
    public void simulate(File file, File outputFile) throws IOException {
        xssfHelper = new XSSFHelper();
        writeTitleToXSSF();
        stream = FileManager.fileToStream(file);
        if (stream == null) {
            System.out.println(IOFont.FONT_RED + "optimal simulator cannot be operated when the stream size is greater than " + Settings.MAX_OPTIMAL_STREAM_SIZE + IOFont.RESET);
            System.out.println();
            return;
        }
        streamSize = stream.length;
        Integer t = 0;
        for (int i = 0; i < streamSize; i++) {
            ++t;
            put(stream[i]);
            writeBufferLineToXSSF(t, stream[i]);
        }
        report();
        writeReportToXSSF();
        String filePath = xssfHelper.save(outputFile);
        System.out.println("saved! " + filePath);
        System.out.println();
    }

    @Override
    public void simulate(long[] stream, boolean animation) {
        this.stream = stream;
        this.streamSize = stream.length;
        super.simulate(stream, animation);
    }

    @Override
    public void simulate(long[] stream, boolean animation, File outputFile) {
        this.stream = stream;
        this.streamSize = stream.length;
        super.simulate(stream, animation, outputFile);
    }

    public void put(Long data) {
        totalHit++;
        streamPointer++;

        if (search(data)) {
            cacheHit++;
            hitBufferIdx = map.get(data);
            missBufferIdx = -1;
            return;
        }

        cacheMiss++;
        int idx;
        if (streamPointer < bufferSize) {
            buffer[streamPointer] = stream[streamPointer];
            idx = streamPointer;
        } else {
            int i = predict(streamPointer + 1);
            map.remove(buffer[i]);
            buffer[i] = stream[streamPointer];
            idx = i;
        }
        map.put(buffer[idx], idx);
        hitBufferIdx = -1;
        missBufferIdx = idx;
    }

    public boolean search(Long data) {
        return map.containsKey(data);
    }

    private int predict(int index)
    {
        // Store the index of pages which are going
        // to be used recently in future
        int res = -1, farthest = index;
        for (int i = 0; i < bufferSize; i++) {
            int j;
            for (j = index; j < streamSize; j++) {
                if (buffer[i] == stream[j]) {
                    if (j > farthest) {
                        farthest = j;
                        res = i;
                    }
                    break;
                }
            }

            // If a page is never referenced in future, return it.
            if (j == streamSize)
                return i;
        }

        return (res == -1) ? 0 : res;
    }
}
