package etc;

import javax.swing.*;
import java.io.File;

public class FileChooser {
    private static JFileChooser jfc = new JFileChooser();

    public static File choose() {
        int returnVal = jfc.showOpenDialog(null);
        if(returnVal == 0) {
            return jfc.getSelectedFile();
        } else {
            return null;
        }
    }
}
