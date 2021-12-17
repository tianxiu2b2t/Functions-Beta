package org.functions.Bukkit.Main;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.FileUtil;
import org.functions.Bukkit.API.FPI;
import org.functions.Bukkit.Main.functions.Animation;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class Configuration {
    long size = 0;
    public String DirSize() {
        DecimalFormat decimalFormat = new DecimalFormat( ".00" ); //构造方法的字符格式这里如果小数不足2位,会以0补足.
        return decimalFormat.format(getFileLength(getDataFolder()).doubleValue() / 1024 / 1024) + " MB";
    }
    public Long getFileLength(File dir) {

            //1,定义一个求和变量

            long len = 0;

            //2,获取该文件夹下所有的文件和文件夹listFiles();

            File[] subFiles = dir.listFiles();            //Demo1_Student.class Demo1_Student.java

            //3,遍历数组

            assert subFiles != null;
            for (File subFile : subFiles) {

                //4,判断是文件就计算大小并累加

                if(subFile.isFile()) {

                    len = len + subFile.length();

                    //5,判断是文件夹,递归调用

                }else {

                    len = len + getFileLength(subFile);

                }

            }

            return len;

        }
    public InputStream getResource(String file) {
        return Functions.instance.getResource(file);
    }

    public File getDataFolder() {
        return Functions.instance.getDataFolder();
    }

    public FileConfiguration getConfig() {
        return Functions.instance.getConfig();
    }

    public void install() {
        onLanguage();
        onSettings();
        onRecoveryCode();
        onCommands();
        onGroups();
        readGroups();
        onAnimation();
        readAnimation();
        onServerTitle();
    }

    public void reload() {
        install();
        Functions.instance.reloadConfig();
    }

    private final File language_file = new File(getDataFolder(), "Language-" + getConfig().getString("Language", "zn_cn") + ".yml");
    private final FileConfiguration language = new YamlConfiguration();

    private void onLanguage() {
        onLoadFile(language_file, language, "Language.yml", false);
    }

    public String ReadRecoveryFile() {
        BufferedReader reader = null;
        StringBuilder sbf = new StringBuilder();
        try {
            reader = new BufferedReader(new FileReader(recovery));
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

    public void changeEnCoding(File file) throws IOException {
        InputStreamReader isr = new InputStreamReader(new FileInputStream(
                file), "GBK");
        BufferedReader br = new BufferedReader(isr);
        StringBuffer sb = new StringBuffer();
        String line = null;
        while ((line = br.readLine()) != null) {
            // 注意写入换行符
            line = URLEncoder.encode(line, "utf8");
            sb.append(line);

        }
        br.close();
        isr.close();

        File targetFile = new File(file.getPath());
        OutputStreamWriter osw = new OutputStreamWriter(
                new FileOutputStream(targetFile), StandardCharsets.UTF_8);
        BufferedWriter bw = new BufferedWriter(osw);
        // 以字符串的形式一次性写入
        bw.write(URLDecoder.decode(sb.toString(), "utf8"));
        bw.close();
        osw.close();

        System.out.println("Deal:" + file.getPath());
    }

    public File wry = new File(getDataFolder(), "ip.dat");

    public String changeCharset(String str, String newCharset) throws UnsupportedEncodingException {
        if (str != null) {
            // 用默认字符编码解码字符串。
            byte[] bs = str.getBytes();
            // 用新的字符编码生成字符串
            return new String(bs, newCharset);
        }
        return null;
    }
    public FileConfiguration title = new YamlConfiguration();
    private File ftitle = new File(getDataFolder(),"Title.yml");
    public void onServerTitle() {
        onLoadFile(ftitle, title, "Title.yml", false);
    }

    public void onQQAddress() {
        /*BufferedReader reader = null;

        BufferedWriter writer = null;
        try {
            StringBuffer sb = new StringBuffer();

            reader = new BufferedReader(new InputStreamReader(getResource("ip.jar"), "GBK"));

            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(wry), "GBK"));

            String str;
            while ((str = reader.readLine()) != null) {
                sb.append(str);

                writer.write(sb.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (writer != null) {
            try {
                writer.close();

            } catch (IOException e) {
                e.printStackTrace();

            }
        }
    }*/


        File file = wry;
        if (!file.exists()) {
            try {
                file.createNewFile();
                URL url = new URL(Functions.instance.getConfig().getString("AddressCheck.IPUrl", "http://lt.limc.cc:38309/ip.txt"));
                URLConnection urlc = url.openConnection();
                urlc.setReadTimeout(5000);
                InputStream in = urlc.getInputStream();
                OutputStream out = new FileOutputStream(file);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                    out.flush();
                }
                out.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public File recovery = new File(getDataFolder(), "RecoveryPassword.html");

    public void onRecoveryCode() {
        File file = recovery;
        if (!file.exists()) {
            try {
                file.createNewFile();
                InputStream in = getResource("RecoveryPassword.html");
                OutputStream out = new FileOutputStream(file);
                byte[] buf = new byte[10240];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onLoadZipFile(File file, FileConfiguration config, InputStream in, boolean replace) {
        if (!file.exists()) {
            try {
                file.createNewFile();
                OutputStream out = new FileOutputStream(file);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        YamlConfiguration.loadConfiguration(file);
        onLoadConfiguration(file, config, file.getName());
    }

    public void onLoadFile(File file, FileConfiguration config, String name, boolean replace) {
        if (!file.exists()) {
            try {
                file.createNewFile();
                InputStream in = getResource(name);
                OutputStream out = new FileOutputStream(file);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        YamlConfiguration.loadConfiguration(file);
        onLoadConfiguration(file, config, name);
    }

    public void onLoadConfiguration(File file, FileConfiguration config, String name) {
        File Error_Arching = new File(getDataFolder(), name.split("\\.")[0] + "-Error-Arching.yml");
        try {
            config.load(file);
        } catch (FileNotFoundException e) {
            if (Error_Arching.exists()) {
                Error_Arching.deleteOnExit();
                Error_Arching.delete();
            }
            file.renameTo(Error_Arching);
            file.deleteOnExit();
            file.delete();
            onLoadFile(file, config, name, false);
            e.printStackTrace();
        } catch (IOException e) {
            if (Error_Arching.exists()) {
                Error_Arching.deleteOnExit();
                Error_Arching.delete();
            }
            file.renameTo(Error_Arching);
            file.deleteOnExit();
            file.delete();
            onLoadFile(file, config, name, false);
            e.printStackTrace();

        } catch (InvalidConfigurationException e) {
            if (Error_Arching.exists()) {
                Error_Arching.deleteOnExit();
                Error_Arching.delete();
            }
            file.renameTo(Error_Arching);
            file.deleteOnExit();
            file.delete();
            onLoadFile(file, config, name, false);
            e.printStackTrace();

        }
    }
    public void onLoadZipConfiguration(File file, FileConfiguration config, InputStream in) {
        File Error_Arching = new File(getDataFolder(), file.getName().split("\\.")[0] + "-Error-Arching.yml");
        try {
            config.load(file);
        } catch (FileNotFoundException e) {
            if (Error_Arching.exists()) {
                Error_Arching.deleteOnExit();
                Error_Arching.delete();
            }
            file.renameTo(Error_Arching);
            file.deleteOnExit();
            file.delete();
            onLoadZipFile(file, config, in, false);
            e.printStackTrace();
        } catch (IOException e) {
            if (Error_Arching.exists()) {
                Error_Arching.deleteOnExit();
                Error_Arching.delete();
            }
            file.renameTo(Error_Arching);
            file.deleteOnExit();
            file.delete();
            onLoadZipFile(file, config, in, false);
            e.printStackTrace();

        } catch (InvalidConfigurationException e) {
            if (Error_Arching.exists()) {
                Error_Arching.deleteOnExit();
                Error_Arching.delete();
            }
            file.renameTo(Error_Arching);
            file.deleteOnExit();
            file.delete();
            onLoadZipFile(file, config, in, false);
            e.printStackTrace();

        }
    }
    public FileConfiguration getLanguage() {
        return language;
    }

    public void saveLanguage() {
        try {
            getLanguage().save(language_file);
            getLanguage().saveToString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File settings_file = new File(getDataFolder(), "Settings.yml");
    private FileConfiguration settings = new YamlConfiguration();

    private void onSettings() {
        onLoadFile(settings_file, settings, settings_file.getName(), false);
    }

    public FileConfiguration getSettings() {
        return settings;
    }

    private File commands_file = new File(getDataFolder(), "Commands.yml");
    private FileConfiguration commands = new YamlConfiguration();

    private void onCommands() {
        onLoadFile(commands_file, commands, commands_file.getName(), false);
    }

    public FileConfiguration getCommands() {
        return commands;
    }

    public LinkedHashMap<String, FileConfiguration> groups = new LinkedHashMap<>();

    public void onGroups() {
        File file;
        String path = getDataFolder()+"";
        path = path.replace("\\","/");
        File pathf = new File( path+ "/Groups");
        pathf.mkdirs();
        File[] filess = (pathf).listFiles();
        assert filess != null;
        if (filess.length != 0) {
            return;
        }
        try {
            int i = 0;
            String jar = URLDecoder.decode(Functions.instance.getClass().getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
            ZipFile zip = new ZipFile(jar);
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(jar));
            ZipInputStream zis = new ZipInputStream(bis);
            // 遍历插件压缩包內所有内容
            Enumeration<? extends ZipEntry> files = zip.entries();
            ZipEntry entry ;
            while ((entry = zis.getNextEntry())!=null) {
                String url = files.nextElement().getName().replace("\"", "/");
                // 只认 .class 文件
                if (url.toLowerCase().endsWith(".yml")) {
                    if (url.startsWith("Groups/")) {
                        file = new File(path + "/" + url.split("/")[0],url.split("/")[1]);
                        onLoadZipFile(file, groupc, zip.getInputStream(entry), false);
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    FileConfiguration groupc = new YamlConfiguration();
    public List<String> group_Name = new ArrayList<>();
    public void readGroups() {
        groups.clear();
        group_Name.clear();
        String path = getDataFolder()+"";
        path = path.replace("\\","/");
        File[] files = (new File( path+ "/Groups")).listFiles();
        for (File file : files) {
            try {
                FileConfiguration group = new YamlConfiguration();
                group.load(file);
                groups.put(group.getString("Group"),group);
                group_Name.add(group.getString("Group"));
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }
    }
    public void onAnimation() {
        File file;
        String path = getDataFolder()+"";
        path = path.replace("\\","/");
        File pathf = new File(path + "/Animations");
        pathf.mkdirs();
        File[] filess = (pathf).listFiles();
        assert filess != null;
        if (filess.length != 0) {
            return;
        }
        try {
            int i = 0;
            String jar = URLDecoder.decode(Functions.instance.getClass().getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
            ZipFile zip = new ZipFile(jar);
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(jar));
            ZipInputStream zis = new ZipInputStream(bis);
            // 遍历插件压缩包內所有内容
            Enumeration<? extends ZipEntry> files = zip.entries();
            ZipEntry entry ;
            while ((entry = zis.getNextEntry())!=null) {
                String url = files.nextElement().getName().replace("\"", "/");
                // 只认 .class 文件
                if (url.toLowerCase().endsWith(".yml")) {
                    if (url.startsWith("Animations/")) {
                        file = new File(path + "/" + url.split("/")[0],url.split("/")[1]);
                        onLoadZipFile(file, groupc, zip.getInputStream(entry), false);
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public List<String> animations_Name = new ArrayList<>();
    public LinkedHashMap<String, Animation> animations = new LinkedHashMap<>();
    public void readAnimation() {
        animations.clear();
        animations_Name.clear();
        String path = getDataFolder()+"";
        path = path.replace("\\","/");
        File[] files = (new File( path+ "/Animations")).listFiles();
        assert files != null;
        for (File file : files) {
            try {
                FileConfiguration animation = new YamlConfiguration();
                animation.load(file);
                animations.put(file.getName().replace(".yml",""),new Animation(animation));
                animations_Name.add(file.getName().replace(".yml",""));
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }
    }
}