package org.functions.Bukkit.API.FunctionsSQL;

import java.io.BufferedReader;
import java.io.File;
import java.util.List;

public class SQLFile extends SQLMain {
    File file;
    List<String> texts;
    private SQLFile(File file) {
        String format = ".SQLFile";
        if (!file.getName().endsWith(format)) {
            file = new File(file.getPath() + format);
        }
        this.file = file;
    }
    public SQLFile(File file,List<String> texts) {
        this(file);
        this.texts = texts;
    }
    public File getFile() {
        return file;
    }
}
