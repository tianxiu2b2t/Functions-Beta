package org.functions.Bukkit.API.FunctionsSQL;

import java.io.*;
import java.util.*;

public class SQLMain {
    public static void main(String[] args) {
        List<String> s = new ArrayList<>();
        s.add("awa:aaaa废物");
        new SQLReader(new File("O:\\awa")).save();
    }
    private static final String format = ".SQLFile";
    public static List<SQLRead> getSubSQLFiles(File dir) {
        List<SQLRead> list = new ArrayList<>();
        if (isDirAnyFiles(dir)) {
            getFiles(dir).forEach(e->{
                list.add(new SQLRead(e));
            });
        }
        return list;
    }
    public static List<File> getFiles(File dir) {
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
    public static List<String> getFilesNotFormat(File dir) {
        List<String> list = new ArrayList<>();
        if (!isDirAnyFiles(dir)) {
            return list;
        }
        Arrays.asList(Objects.requireNonNull(dir.listFiles())).forEach(e->{
            list.add(e.getName().split("\\.")[e.getName().split("\\.").length - 1]);
        });
        return list;
    }
    public static boolean isDirAnyFiles(File dir) {
        if (dir.exists()) {
            if (dir.listFiles()!=null) {
                return true;
            }
        }
        dir.mkdirs();
        return false;
    }
}
