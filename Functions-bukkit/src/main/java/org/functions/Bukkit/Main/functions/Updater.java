package org.functions.Bukkit.Main.functions;

import org.bukkit.entity.Player;
import org.functions.Bukkit.Main.Functions;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Updater {
    public Updater() {
        check();
        info();
        now();
        getVersion();
    }
    long check = 0;
    int pre = 0;
    String plugin_version = Functions.instance.getDescription().getVersion();
    String vurl = "https://gitee.com/tianxiu2b2t/Functions-Beta/raw/master/Functions-bukkit/src/main/resources/plugin.yml";
    String curl = "https://gitee.com/tianxiu2b2t/Functions-Beta/raw/master/Functions-bukkit/src/main/resources/Version";
    String iurl = "https://gitee.com/tianxiu2b2t/Functions-Beta/raw/master/Functions-bukkit/src/main/resources/InfoMessage";
    public void check() {
        try {
            URL u = new URL(curl);
            InputStream is = u.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            pre = Integer.parseInt(br.readLine());
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }
    public String getURL() {
        return "https://gitee.com/tianxiu2b2t/Functions-Beta/releases/";
    }
    ArrayList<String> message = new ArrayList<>();
    public void getVersion() {
        try {
            URL u = new URL(vurl);
            InputStream is = u.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            LinkedHashMap<String, String> t = new LinkedHashMap<>();
            String temp;
            while ((temp = br.readLine())!=null) {
                if (temp.contains("version:")) {
                    plugin_version = temp.split(": ")[1].replace("'","").replace("\"","");
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void info() {
        try {
            InputStream is = Functions.instance.getResource("Version");
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String temp;
            while ((temp = br.readLine()) != null) {
                message.add(temp);
            }
            URL u = new URL(iurl);
            is = u.openStream();
            br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            message.clear();
            while ((temp = br.readLine()) != null) {
                message.add(temp);
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }
    int nowversion = 0;

    public void now() {
        if (Functions.instance != null) {
            try {
                InputStream is = Functions.instance.getResource("Version");
                BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                nowversion = Integer.parseInt(br.readLine());
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }
    public void sendOp(Player p) {
        check();
        info();
        now();
        getVersion();
        if (pre > nowversion) {
            if (Functions.instance.getConfig().getBoolean("Updater.Enable", true)) {
                for (String s : message) {
                    p.sendMessage(Functions.instance.Prefix() + Functions.instance.getAPI().replace(s,p).replace("%plugin%", Functions.instance.getDescription().getName()).replace("%url%", getURL() + pre).replace("%latest_version%", plugin_version).replace("%version%", Functions.instance.getDescription().getVersion()));
                }
            }
        }
    }
    public void run() {
        if (check<=0) {
            check();
            info();
            now();
            getVersion();
            if (pre > nowversion) {
                if (Functions.instance.getConfig().getBoolean("Updater.Enable", true)) {
                    for (String s : message) {
                        Functions.instance.print(Functions.instance.getAPI().replace(s,null).replace("%plugin%", Functions.instance.getDescription().getName()).replace("%url%", getURL() + pre).replace("%latest_version%",plugin_version).replace("%version%",Functions.instance.getDescription().getVersion()));
                        check = 20*60*Functions.instance.getConfig().getLong("Updater.minutes",5);
                    }
                }
            }
        }
        check--;
    }
}
