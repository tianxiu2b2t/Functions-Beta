package org.functions.Bukkit.Main.functions;

import org.bukkit.entity.Player;
import org.functions.Bukkit.Main.Functions;

import javax.net.ssl.SSLException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Updater {
    public Updater() {
        
    }
    long check = 0;
    String plugin_version = Functions.instance.getDescription().getVersion();
    public void test(Player player) {
        /*
        String t = "";
        String[] args;
        //String t = "%plugin.yml|%WebVersion|%InfoMessage|%NowVersion";
        if ((t = getAllInfo()) != null) {
            args = t.split("\\|");
            String latest_version = args[0];
            int valueWebVersion = Integer.parseInt(args[1]);
            message.clear();
            String[] args_msg = args[2].split("\n");
            message.addAll(Arrays.asList(args_msg));
            int valueNowVersion = Integer.parseInt(args[3]);
            Functions.instance.print(latest_version + "|" + valueNowVersion  + "|" + valueWebVersion  + "|" + message.toString());
        }*/
        LinkedHashMap<String, Object> link = new LinkedHashMap<>();
        if (link.size() != 0) {
            if (check <= 0) link = getAllInfo();
            if (Functions.instance.getConfig().getBoolean("Updater.Enable", true)) {
                if (player != null) {
                    //LinkedHashMap<String, Object> link = getAllInfo();
                    int now = Integer.parseInt(link.get("local_version") + "");
                    int pre = Integer.parseInt(link.get("latest_version") + "");
                    if (pre > now) {
                        downloader(pre);
                        String plugin = link.get("latest_plugin") + "";
                        List<String> messages = new ArrayList<>();
                        link.forEach((name, e) -> {
                            if (name.startsWith("InfoMessage_")) {
                                messages.add(Functions.instance.getAPI().replace(e, player, new String[]{"%plugin%", "%latest_version%", "%url%", "%version%", "local_version"}, new String[]{plugin, pre + "", "https://gitee.com/tianxiu2b2t/Functions-Beta/releases/" + pre, plugin_version, now + ""}));
                            }
                        });
                        messages.forEach(player::sendMessage);
                        return;
                    }
                }
                if (check <= 0) {
                    //LinkedHashMap<String, Object> link = getAllInfo();
                    int now = Integer.parseInt(link.get("local_version") + "");
                    int pre = Integer.parseInt(link.get("latest_version") + "");
                    if (pre > now) {
                        downloader(pre);
                        String plugin = link.get("latest_plugin") + "";
                        List<String> messages = new ArrayList<>();
                        link.forEach((name, e) -> {
                            if (name.startsWith("InfoMessage_")) {
                                messages.add(Functions.instance.getAPI().replace(e, player, new String[]{"%plugin%", "%latest_version%", "%url%", "%version%", "local_version"}, new String[]{plugin, pre + "", "https://gitee.com/tianxiu2b2t/Functions-Beta/releases/" + pre, plugin_version, now + ""}));
                            }
                        });
                        messages.forEach(Functions.instance::print);
                    }
                    check = 20 * 60 * Functions.instance.getConfig().getLong("Updater.minutes", 5);
                }
                check--;
            }
        }
    }
    public int getNowVersion() {
        int version = -1;
        try {
            String input;
            InputStream is = Functions.instance.getResource("Version");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while ((input = br.readLine()) != null) {
                version = Integer.parseInt(input);
            }
        } catch (IOException e) {
            return -2;
        }
        return version;
    }
    public LinkedHashMap<String,Object> getAllInfo() {
        try {
            String website = "https://gitee.com/tianxiu2b2t/Functions-Beta/raw/master/Functions-bukkit/src/main/resources/";
            URL latest_version_url = new URL(website + "Version");
            URL InfoMessage_url = new URL(website + "InfoMessage");
            URL latest_plugin_url = new URL(website + "plugin.yml");
            URLConnection latest_version = latest_version_url.openConnection();
            URLConnection InfoMessage = InfoMessage_url.openConnection();
            URLConnection latest_plugin = latest_plugin_url.openConnection();
            latest_version.setUseCaches(true);
            InfoMessage.setUseCaches(true);
            latest_plugin.setUseCaches(true);
            latest_version.setDoOutput(true);
            InfoMessage.setDoOutput(true);
            latest_plugin.setDoOutput(true);
            InputStream local_version_inputStream = Functions.instance.getResource("Version");
            InputStream local_InfoMessage_inputStream = Functions.instance.getResource("InfoMessage");
            InputStream latest_version_inputStream = latest_version.getInputStream();
            InputStream latest_plugin_inputStream = latest_plugin.getInputStream();
            InputStream InfoMessage_inputStream = latest_version.getInputStream();
            BufferedReader local_InfoMessage_br = new BufferedReader(new InputStreamReader(local_InfoMessage_inputStream, StandardCharsets.UTF_8));
            BufferedReader local_version_br = new BufferedReader(new InputStreamReader(local_version_inputStream, StandardCharsets.UTF_8));
            BufferedReader latest_version_br = new BufferedReader(new InputStreamReader(latest_version_inputStream, StandardCharsets.UTF_8));
            BufferedReader latest_plugin_br = new BufferedReader(new InputStreamReader(latest_plugin_inputStream, StandardCharsets.UTF_8));
            BufferedReader InfoMessage_br = new BufferedReader(new InputStreamReader(InfoMessage_inputStream, StandardCharsets.UTF_8));
            LinkedHashMap<String,Object> objectList = new LinkedHashMap<>();
            //List<Object> objectList = new ArrayList<>();
            List<String> message = new ArrayList<>();
            objectList.put("latest_version", latest_version_br.readLine());
            objectList.put("local_version", local_version_br.readLine());
            String temp;
            while ((temp = latest_plugin_br.readLine())!=null) {
                if (temp.contains("version:")) {
                   objectList.put("latest_plugin",temp.split(": ")[1].replace("'","").replace("\"",""));
                }
            }
            while ((temp = local_InfoMessage_br.readLine()) != null) {
                    message.add(temp);
                    //System.out.println(temp);
            }
            //InfoMessage_br.lines().forEach(System.out::println);
            //while ((temp = InfoMessage_br.readLine()) != null) {
                    //message.add(temp);
                //System.out.println(temp);
            //}
            for (int i = 0; i < message.size(); i++) {
                objectList.put("InfoMessage_" + i, message.get(i));
            }
            message.clear();
            local_InfoMessage_br.close();
            local_version_br.close();
            latest_version_br.close();
            latest_plugin_br.close();
            InfoMessage_br.close();
            local_version_inputStream.close();
            local_InfoMessage_inputStream.close();
            latest_version_inputStream.close();
            latest_plugin_inputStream.close();
            InfoMessage_inputStream.close();
            return objectList;
        } catch (IOException e) {
            return null;
        }
    }
    public String getURL() {
        return "https://gitee.com/tianxiu2b2t/Functions-Beta/releases/";
    }
    public void downloader(int pre) {
        try {
            File dir = new File(Functions.instance.getFolder() + "/Downloader");
            File file = new File(dir,"Functions-Beta_" + pre + ".jar");
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    return;
                }
            }
            if (file.exists()) {
                if (file.length() != 0) {
                    return;
                }
            } else {
                file.createNewFile();
            }
            Functions.instance.print("For seconds be start downloader updater application","Updater Application");
            Functions.instance.getServer().getScheduler().runTaskAsynchronously(Functions.instance,new Runnable() {
                public void run() {
                    Functions.instance.print("Now let me try download this version file.","Updater Application");
                    try {
                        URL url = new URL("https://github.com/tianxiu2b2t/Functions-Beta/releases/download/" + pre + "/Functions-Beta.jar");
                        URLConnection connect = url.openConnection();
                        connect.setDoOutput(true);
                        connect.setUseCaches(true);
                        InputStream in = connect.getInputStream();
                        OutputStream out = new FileOutputStream(file);
                        byte[] buf = new byte[1024];
                        int len;
                        while ((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                            out.flush();
                        }
                        out.close();
                        in.close();
                        Functions.instance.print("All right! You can see \"" + file.getPath() + "\" folder file.","Updater Application");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void run() {
        test(null);
    }
}
