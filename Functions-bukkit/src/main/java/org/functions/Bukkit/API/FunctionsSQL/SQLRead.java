package org.functions.Bukkit.API.FunctionsSQL;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SQLRead {
    List<String> texts = new ArrayList<>();
    File file;
    public SQLRead(File file) {
        String format = ".SQLFile";
        if (!file.getName().endsWith(format)) {
            file = new File(file.getPath() + format);
        }
        this.file = file;
        readString();
    }
    public void readString() {
        List<String> ls = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String input;
            while ((input = reader.readLine()) != null) {
                ls.add(toString(input));
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.texts = ls;
        if (file.exists()) {
            if (file.length() != 0) {
                if (texts.size() == 0) {
                    readString();
                }
            }
        }
    }
    public SQLReader getReader() {
        return new SQLReader(file,texts);
    }
    private String toString(String binary) {
        String[] tempStr = binary.split(" ");
        char[] tempChar = new char[tempStr.length];
        for(int i = 0; i < tempStr.length; i++) {
            char[] tempchar = tempStr[i].toCharArray();
            int[] result = new int[tempchar.length];
            for(int g = 0; g < tempchar.length; g++) {
                result[g] = tempchar[g] - 48;
            }
            int sum=0;
            for(int g = 0; g < result.length; g++){
                sum += result[result.length - 1 - g] << g;
            }
            tempChar[i] = (char)sum;
        }
        return String.valueOf(tempChar);
    }
}
