package org.functions.Bukkit.Main;

import org.functions.Bukkit.API.FPI;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
            try {
                latest.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            pack(latest);
            latest.deleteOnExit();
            latest.delete();
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
    private static void compress(File sourceFile, ZipOutputStream zos, String name,
                                 boolean KeepDirStructure) throws Exception{
        byte[] buf = new byte[1024];
        if(sourceFile.isFile()){
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(new ZipEntry(name));
            // copy文件到zip输出流中
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1){
                zos.write(buf, 0, len);
                zos.flush();
            }
            // Complete the entry
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if(listFiles == null || listFiles.length == 0){
                // 需要保留原来的文件结构时,需要对空文件夹进行处理
                if(KeepDirStructure){
                    // 空文件夹的处理
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    // 没有文件，不需要文件的copy
                    zos.closeEntry();
                }
            }else {
                for (File file : listFiles) {
                    // 判断是否需要保留原来的文件结构
                    if (KeepDirStructure) {
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        compress(file, zos, name + "/" + file.getName(),KeepDirStructure);
                    } else {
                        compress(file, zos, file.getName(),KeepDirStructure);
                    }
                }
            }
        }
    }
    public static void toZip(File sourceFile, OutputStream out, boolean KeepDirStructure)
            throws RuntimeException{
        ZipOutputStream zos = null ;
        try {
            zos = new ZipOutputStream(out);
            compress(sourceFile,zos,sourceFile.getName(),KeepDirStructure);
            sourceFile.deleteOnExit();
        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtils",e);
        }finally{
            if(zos != null){
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void pack(File file) {
        FileOutputStream fos1 = null;
        try {
            fos1 = new FileOutputStream(new File(latest + " " + new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date()) + ".zip"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        toZip(latest, fos1,true);

//        File t = new File(file.getParent(),(LocalTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.LONG)) + "Latest.log.gz").replace(":",""));
//        try {
//            System.out.println(t);
//            System.out.println(file);
//            t.createNewFile();
//            FileOutputStream output = new FileOutputStream(t);
//            InputStream is = new FileInputStream(file);
//            GZIPInputStream gis = new GZIPInputStream();
//            GZIPOutputStream gout = new GZIPOutputStream(output);
//            byte[] buf = new byte[1024];
//            int len;
//            while ((len = gis.read(buf)) > 0) {
//                gout.write(buf, 0, len);
//                gout.flush();
//            }
//            output.close();
//            is.close();
//            gis.close();
//            gout.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
