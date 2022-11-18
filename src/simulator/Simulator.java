package simulator;

import java.io.File;
import java.io.IOException;

public interface Simulator {
    String getName();
    void simulate(File file) throws IOException;
    void simulate(File file, File outputFile) throws IOException;
    void simulate(long[] stream, boolean animation);
    void simulate(long[] stream, boolean animation, File outputFile) throws IOException;
}
