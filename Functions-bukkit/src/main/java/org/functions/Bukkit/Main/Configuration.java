package org.functions.Bukkit.Main;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.functions.Bukkit.API.FPI;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

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


    public String changeCharset(String str, String newCharset)
            throws UnsupportedEncodingException {
        if (str != null) {
            // 用默认字符编码解码字符串。
            byte[] bs = str.getBytes();
            // 用新的字符编码生成字符串
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
                URL url = new URL(Functions.instance.getConfig().getString("AddressCheck.IPUrl","http://lt.limc.cc:38309/ip.txt"));
                URLConnection urlc = url.openConnection();
                urlc.setReadTimeout(5000);
                InputStream in = urlc.getInputStream();
                OutputStream out = new FileOutputStream(file);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf,0,len);
                    out.flush();
                }
                out.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public File recovery = new File(getDataFolder(),"RecoveryPassword.html");
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
    public void onLoadFile(File file,FileConfiguration config,String name,boolean replace) {
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
        onLoadConfiguration(file,config,name);
    }
    public void onLoadConfiguration(File file,FileConfiguration config,String name) {
        File Error_Arching = new File(getDataFolder(),name.split("\\.")[0] + "-Error-Arching.yml");
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
            onLoadFile(file,config,name,false);
            e.printStackTrace();
        } catch (IOException e) {
            if (Error_Arching.exists()) {
                Error_Arching.deleteOnExit();
                Error_Arching.delete();
            }
            file.renameTo(Error_Arching);
            file.deleteOnExit();
            file.delete();
            onLoadFile(file,config,name,false);
            e.printStackTrace();

        } catch (InvalidConfigurationException e) {
            if (Error_Arching.exists()) {
                Error_Arching.deleteOnExit();
                Error_Arching.delete();
            }
            file.renameTo(Error_Arching);
            file.deleteOnExit();
            file.delete();
            onLoadFile(file,config,name,false);
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
    private File settings_file = new File(getDataFolder(),"Settings.yml");
    private FileConfiguration settings = new YamlConfiguration();
    private void onSettings() {
        onLoadFile(settings_file,settings,settings_file.getName(),false);
    }
    public FileConfiguration getSettings() {
        return settings;
    }
    private File commands_file = new File(getDataFolder(),"Commands.yml");
    private FileConfiguration commands = new YamlConfiguration();
    private void onCommands() {
        onLoadFile(commands_file,commands,commands_file.getName(),false);
    }
    public FileConfiguration getCommands() {
        return commands;
    }
}
