package org.functions.Bukkit.Main.functions;

import org.bukkit.configuration.file.FileConfiguration;
import org.functions.Bukkit.Main.Functions;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public class ServerTitle {
    FileConfiguration f = Functions.instance.getConfiguration().title;
    long changetime = 0;
    long time = 0;
    int l = 0;
    Random random = new Random();
    public boolean IsRandom() {
        return f.getBoolean("Random",false);
    }
    public void line() {
        if (IsRandom()) {
            l = random.nextInt(f.getStringList("Lines").size());
            return;
        }
        if (changetime > time) {
            time++;
        }
        if (f.getStringList("Lines").size()>=l) {
            l = 0;
        } else {
            l++;
        }
        time = 0;
    }
    public String getString() {
        return Functions.instance.getAPI().replace(f.getStringList("Lines").get(l),null);
    }
    public String getRawString() {
        return f.getStringList("Lines").get(l);
    }
}
