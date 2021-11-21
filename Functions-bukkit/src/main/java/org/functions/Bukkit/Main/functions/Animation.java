package org.functions.Bukkit.Main.functions;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.Random;

public class Animation {
    FileConfiguration f;
    int l = 0;
    long changetime = 0;
    long time = 0;
    Random random = new Random();
    public Animation(FileConfiguration config) {
        f = config;
        changetime = config.getLong("ChangeTick",20);
    }
    public boolean IsRandom() {
        return f.getBoolean("Random",false);
    }
    public void line() {
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
    public String getAnimation() {
        if (IsRandom()) {
            return f.getStringList("Lines").get(random.nextInt(f.getStringList("Lines").size()));
        }
        return f.getStringList("Lines").get(l);
    }
}
