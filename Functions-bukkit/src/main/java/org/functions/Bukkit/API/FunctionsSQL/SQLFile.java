package org.functions.Bukkit.API.FunctionsSQL;

import java.io.BufferedReader;
import java.io.File;
import java.util.List;

public class SQLFile extends SQLMain {
    File file;
    List<String> texts;
    private final String format = ".SQLFile";
    private SQLFile(File file) {
        if (!file.getName().endsWith(format)) {
            file = new File(file.getName() + format);
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
