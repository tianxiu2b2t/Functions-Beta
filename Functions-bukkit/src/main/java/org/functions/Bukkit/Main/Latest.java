package org.functions.Bukkit.Main;


import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
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
            pack();
            latest.deleteOnExit();
            latest.delete();
        }
    }

    /**
     * 
     * @return Read Latest in text.
     */
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

    public String getDate() {
        Date date = new Date();
        SimpleDateFormat Date = new SimpleDateFormat("yyyy-MM-dd");
        return Date.format(date);
    }
    public String getDateTime() {
        return getDate() + " " + getTime();
    }
    public String getTime() {
        Date date = new Date();
        SimpleDateFormat Date = new SimpleDateFormat("HH:mm:ss");
        return Date.format(date);
    }

    /**
     * 
     * @param text Text to in latest log.
     */
    public void print(Object text) {
        print(text,Level.INFO);
    }
    public void print(Object text, Level level) {
        text = getDateTime() + " " + level.name().toUpperCase() + ": " + text;
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

    public enum Level {
        INFO,
        DEBUG,
        WARNING,
        ERROR,
        ERR422;
        public String toString(Level level) {
            return level.name().toUpperCase();
        }
    }
    private static void compress(File sourceFile, ZipOutputStream zos, String name, boolean KeepDirStructure) throws Exception{
        byte[] buf = new byte[1024];
        if(sourceFile.isFile()){
            // ???zip????????????????????????zip?????????????????????name???zip????????????????????????
            zos.putNextEntry(new ZipEntry(name));
            // copy?????????zip????????????
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
                // ????????????????????????????????????,?????????????????????????????????
                if(KeepDirStructure){
                    // ?????????????????????
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    // ?????????????????????????????????copy
                    zos.closeEntry();
                }
            }else {
                for (File file : listFiles) {
                    // ?????????????????????????????????????????????
                    if (KeepDirStructure) {
                        // ?????????file.getName()???????????????????????????????????????????????????,
                        // ????????????????????????????????????????????????????????????,???????????????????????????????????????????????????
                        compress(file, zos, name + "/" + file.getName(),KeepDirStructure);
                    } else {
                        compress(file, zos, file.getName(),KeepDirStructure);
                    }
                }
            }
        }
    }
    private static void toZip(File sourceFile, OutputStream out, boolean KeepDirStructure)
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
    private void pack() {
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
