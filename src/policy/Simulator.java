package policy;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface Simulator {
    void simulate(File file) throws IOException;
    void simulate(Long[] stream);
}
