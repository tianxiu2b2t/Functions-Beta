package org.functions.Bukkit.API.FunctionsSQL;

import java.io.*;
import java.util.*;

public class SQLMain {
    public static void main(String[] args) {
        List<String> s = new ArrayList<>();
        s.add("awa:aaaa废物");
        new SQLReader(new File("O:\\awa")).save();
    }
    private final String format = ".SQLFile";
    public List<SQLRead> getSubSQLFiles(File dir) {
        List<SQLRead> list = new ArrayList<>();
        if (isDirAnyFiles(dir)) {
            getFiles(dir).forEach(e->{
                list.add(new SQLRead(e));
            });
        }
        return list;
    }
    public List<File> getFiles(File dir) {
        List<File> list = new ArrayList<>();
        if (!isDirAnyFiles(dir)) {
            return list;
        }
        Arrays.asList(Objects.requireNonNull(dir.listFiles())).forEach(e->{
            if (e.getName().endsWith(format)) {
                list.add(e);
            }
        });
        return list;
    }
    public boolean isDirAnyFiles(File dir) {
        if (dir.exists()) {
            if (dir.listFiles()!=null) {
                return true;
            }
        }
        dir.mkdirs();
        return false;
    }
}
