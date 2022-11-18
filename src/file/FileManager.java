package file;

import settings.Settings;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileManager {

    public static long[] fileToStream(File file) throws IOException {
        InputStream in = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        List<Long> streamList = new ArrayList<>();

        String line;
        int count = 0;
        while ((line = br.readLine()) != null) {
            if (count > Settings.MAX_OPTIMAL_STREAM_SIZE) {
                return null;
            }
            streamList.add(Long.parseLong(line));
            count++;
        }
        return streamList.stream()
                .mapToLong(Number::longValue).toArray();
    }

    public static String getFileName(String name) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmSS") ;
        return "/" + name.toUpperCase() + "_" + dateFormat.format(new Date()) + ".xlsx";
    }
}
