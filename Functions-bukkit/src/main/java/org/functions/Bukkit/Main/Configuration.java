package org.functions.Bukkit.Main;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.functions.Bukkit.API.FPI;
import org.functions.Bukkit.Main.functions.Animation;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class Configuration {
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
            // ע��д�뻻�з�
            line = URLEncoder.encode(line, "utf8");
            sb.append(line);

        }
        br.close();
        isr.close();

        File targetFile = new File(file.getPath());
        OutputStreamWriter osw = new OutputStreamWriter(
                new FileOutputStream(targetFile), StandardCharsets.UTF_8);
        BufferedWriter bw = new BufferedWriter(osw);
        // ���ַ�������ʽһ����д��
        bw.write(URLDecoder.decode(sb.toString(), "utf8"));
        bw.close();
        osw.close();

        System.out.println("Deal:" + file.getPath());
    }

    public File wry = new File(getDataFolder(), "ip.dat");

    public String changeCharset(String str, String newCharset) throws UnsupportedEncodingException {
        if (str != null) {
            // ��Ĭ���ַ���������ַ�����
            byte[] bs = str.getBytes();
            // ���µ��ַ����������ַ���
            return new String(bs, newCharset);
        }
        return null;
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
                URL url = new URL(Functions.instance.getConfig().getString("AddressCheck.IPUrl", "https://gitee.com/tianxiu2b2t/Functions-Beta/raw/master/ip.txt"));
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
            // �������ѹ��������������
            Enumeration<? extends ZipEntry> files = zip.entries();
            ZipEntry entry ;
            while ((entry = zis.getNextEntry())!=null) {
                String url = files.nextElement().getName().replace("\"", "/");
                // ֻ�� .class �ļ�
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
            // �������ѹ��������������
            Enumeration<? extends ZipEntry> files = zip.entries();
            ZipEntry entry ;
            while ((entry = zis.getNextEntry())!=null) {
                String url = files.nextElement().getName().replace("\"", "/");
                // ֻ�� .class �ļ�
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