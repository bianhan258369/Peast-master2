package util;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class XMLFileFilter extends FileFilter {
    @Override
    public boolean accept(File f) {
        return f.isDirectory() || f.getName().endsWith("xml");
    }

    @Override
    public String getDescription() {
        return "(*.xml)";
    }
}
