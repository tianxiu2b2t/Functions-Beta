package org.functions.Bukkit.API;

import org.bukkit.configuration.file.FileConfiguration;
import org.functions.Bukkit.Main.Functions;

import java.util.ArrayList;
import java.util.UUID;

public class ClickPerSeconds {
    UUID uuid;
    int max;
    int CPS;
    long cpsTime;
    long cpsMin;
    ArrayList<Long> cps = new ArrayList();
    public int getMax() {
        return this.max;
    }
    public ClickPerSeconds(UUID uuid) {
        this.uuid = uuid;
    }
    public void countCPS() {
        cps.add(System.currentTimeMillis());
        removeCPSTimeout();
        if (cps.size() > max) {
            max = cps.size();
        }

    }
    public void removeCPSTimeout() {
        while(!cps.isEmpty() && System.currentTimeMillis() - (Long)cps.get(0) > 1000L) {
            cps.remove(0);
        }

    }

    public void reset() {
        cps.clear();
        max = 0;
    }
    public void resetMax() {
        max = 0;
    }

    public int getMaxCPS() {
        return max;
    }

    public int getCountCPS() {
        removeCPSTimeout();
        return cps.size();
    }

}
