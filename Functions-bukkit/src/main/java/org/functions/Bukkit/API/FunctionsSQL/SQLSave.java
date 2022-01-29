package org.functions.Bukkit.API.FunctionsSQL;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class SQLSave extends SQLRead{
    public SQLSave(File file, List<String> texts) {
        super(file);
        try {
            if (file.exists()) file.deleteOnExit();
            if (!file.exists()) file.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            texts.forEach(e->{
                try {
                    writer.write(toBinary(e));
                    writer.newLine();
                    writer.flush();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String toBinary(String str){
        //把字符串转成字符数组
        char[] strChar=str.toCharArray();
        StringBuilder result= new StringBuilder();
        for (char c : strChar) {
            //toBinaryString(int i)返回变量的二进制表示的字符串
            //toHexString(int i) 八进制
            //toOctalString(int i) 十六进制
            result.append(Integer.toBinaryString(c)).append(" ");
        }
        return result.toString();
    }
}
