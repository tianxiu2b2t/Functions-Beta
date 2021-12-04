package org.functions.Bukkit.Main.Server;

import org.bukkit.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FServer {
    List<FWorld> lw = new ArrayList<>();
    long start = System.currentTimeMillis();
    Server server;
    public FServer(Server server) {
        this.server = server;
    }

    public Server getServer() {
        return server;
    }
    public long getServerTime() {
        return System.currentTimeMillis() - start;
    }
    public String getServerStringForTime() {
        long s = getServerTime() / 1000;
        long mins = 0;
        while (s!=0) {
            if (s >= 60) {
                s = s - 60;
                mins++;
            }
        }
        long hours = 0;
        while (mins!=0) {
            if (mins >= 60) {
                mins = mins - 60;
                hours++;
            }
        }
        long day = 0;
        while (hours!=0) {
            if (hours>= 24) {
                hours = hours - 24;
                day ++;
            }
        }
        return day + ":" + hours + ":" + mins + ":" + s;
    }
    public FWorld getWorld(UUID uuid) {
        for (FWorld world : lw) {
            if (world.getWorld().getUID().equals(uuid)) {
                return world;
            }
        }
        return null;
    }
    public List<FWorld> getWorlds() {
        return lw;
    }
}
