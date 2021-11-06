package org.functions.Bukkit.Main;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public class Updater {
    public static class CheckVersion {
        String url = "https://gitee.com/tianxiu2b2t/Functions-Beta/raw/master/Functions-bukkit/src/main/resources/Version";
        int version = 0;
        public CheckVersion() {
            try {
                URL u = new URL(url);
                InputStream is = u.openStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                version = Integer.parseInt(br.readLine());
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
            }
        }

        public int getVersion() {
            return version;
        }
    }
    public static class Localversion {
        int nowversion = 0;
        public Localversion() {
            if (Functions.instance!=null) {
                try {
                    InputStream is = Functions.instance.getResource("Version");
                    BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    nowversion = Integer.parseInt(br.readLine());
                } catch (IOException | NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        public int getVersion() {
            return nowversion;
        }
    }
    public static class scheduler implements Runnable {
        public void run() {
            if (Functions.instance.getConfig().getBoolean("Updater.Enable",true)) {
                if (new CheckVersion().getVersion() > new Localversion().getVersion()) {
                    Functions.instance.print("Fuck you need updater.");
                }
            }
        }
    }
}
