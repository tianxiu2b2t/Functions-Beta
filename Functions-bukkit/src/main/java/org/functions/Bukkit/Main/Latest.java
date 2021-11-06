package org.functions.Bukkit.Main;

import org.functions.Bukkit.API.FPI;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.GZIPOutputStream;

public class Latest {
    File path = null;
    File latest = null;
    public Latest(File path) {
        this.path = path;
        if (!path.exists()) {
            path.mkdirs();
        }
        latest = new File(path,"Latest.log");
        if (!latest.exists()) {
            //pack(latest.getName());
            try {
                latest.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private String ReadLatest() {
        BufferedReader reader = null;
        StringBuilder sbf = new StringBuilder();
        try {
            reader = new BufferedReader(new FileReader(latest));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                sbf.append(tempStr);
            }
            reader.close();
            return sbf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return sbf.toString();
    }
    public void print(Object text) {
        text = Functions.instance.getAPI().getDateTime() + " INFO: " + text;
        FileWriter fw= null;
        try {
            fw = new FileWriter(latest,true);
            if (ReadLatest().equalsIgnoreCase("")) {
                fw.write(text.toString());
            } else {
                fw.write("\r\n" + text.toString());
            }
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void pack(String inFileName) {
        String outFileName = new SimpleDateFormat().format(new Date()) + ".log.gz";
        FileInputStream in = null;
        try {
            in = new FileInputStream(inFileName);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        GZIPOutputStream out = null;
        try {
            out = new GZIPOutputStream(new FileOutputStream(outFileName));
        }catch (IOException e) {
            e.printStackTrace();
        }
        byte[] buf = new byte[10240];
        int len = 0;
        try {
            while (true) {
                assert in != null;
                if (!((in.available()>10240)&& (in.read(buf)) > 0)) break;
                assert out != null;
                out.write(buf);
            }
            len = in.available();
            in.read(buf, 0, len);
            assert out != null;
            out.write(buf, 0, len);
            in.close();
            out.flush();
            out.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
