package org.functions.Bukkit.API;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.functions.Bukkit.Main.Functions;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

public class SpeedPerSeconds {
    List<Long> speed = new ArrayList<>();
    //List<Location> speeds = new ArrayList<>();
    LinkedHashMap<Long,Location> speeds = new LinkedHashMap<>();
    ArrayList<Location> listPos = new ArrayList<>();
    UUID uuid;
    int max;
    double sp = 0.0D;
    public SpeedPerSeconds(UUID uuid) {
        this.uuid = uuid;
    }
    private Player getPlayer() {
        return Functions.instance.getServer().getPlayer(uuid);
    }
    public void count() {
        speeds.put(System.currentTimeMillis(),getPlayer().getLocation());//speed.add(System.currentTimeMillis());
        removeTimeout();
        listPos.clear();
        speeds.forEach((time,pos)->{
            listPos.add(pos);
        });
        Location pos1 = listPos.get(0);
        Location pos2 = listPos.get(listPos.size()-1);
        if (pos1.getX() != pos2.getX()) {
            sp = Math.max(pos1.getX(),pos2.getX()) - Math.min(pos1.getX(),pos2.getX());
        }
        if (pos1.getY() != pos2.getY()) {
            if (sp == 0) {
                sp = sp * Math.max(pos1.getY(),pos2.getY()) - Math.min(pos1.getY(),pos2.getY());
            }
        }
        if (pos1.getZ() != pos2.getZ()) {
            if (sp == 0) {
                sp = sp * Math.max(pos1.getZ(),pos2.getZ()) - Math.min(pos1.getZ(),pos2.getZ());
            }
        }
    }
    public void removeTimeout() {
        speeds.forEach((e,l)->{
            if ((System.currentTimeMillis() - e) > 1000L) {
                speeds.remove(e);
            }
        });
    }
    public void reset() {
        speeds.clear();
        max = 0;
    }
    public void resetMax() {
        removeTimeout();
        max = 0;
    }

    public int getMaxSPS() {
        removeTimeout();
        return max;
    }

    public double getCountSPS() {
        removeTimeout();
        return sp;
    }
}
