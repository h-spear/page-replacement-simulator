package simulator;

import java.io.File;
import java.io.IOException;

public interface Simulator {
    void simulate(File file) throws IOException;
    void simulate(long[] stream);
}
