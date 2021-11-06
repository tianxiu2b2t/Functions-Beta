package org.functions.Bukkit.Main;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

public class Updater {
    public Updater() {
        check();
        info();
        now();
    }
    long check = 0;
    int pre = 0;
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
    public void run() {
        if (check<=0) {
            if (pre > nowversion) {
                if (Functions.instance.getConfig().getBoolean("Updater.Enable", true)) {
                    for (String s : message) {
                        Functions.instance.print(Functions.instance.getAPI().replace(s).replace("%plugin%", Functions.instance.getDescription().getName()).replace("%url%", getURL() + pre).replace("%version%",Functions.instance.getDescription().getVersion()));
                        check = 20*60*Functions.instance.getConfig().getLong("Updater.minutes",5);
                    }
                }
            }
        }
        check--;
    }
}
