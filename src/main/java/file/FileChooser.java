package file;

import javax.swing.*;
import java.io.*;

public class FileChooser {
    private static JFileChooser jfc = new JFileChooser();

    public static File choose() {
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnVal = jfc.showOpenDialog(null);
        if(returnVal == 0) {
            return jfc.getSelectedFile();
        } else {
            return null;
        }
    }

    public static String chooseDirectory() {
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = jfc.showOpenDialog(null);
        if(returnVal == 0) {
            return jfc.getSelectedFile().getAbsolutePath();
        } else {
            return null;
        }
    }
}
