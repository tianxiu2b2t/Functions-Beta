package org.functions.Bukkit.API;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;

public class Speed {
    private static final long flush = 1000;
    private static final LinkedHashMap<UUID,LinkedHashMap<Long, Location>> speeds = new LinkedHashMap<>();
    private static final LinkedHashMap<Long,Location> temp = new LinkedHashMap<>();
    public static void count(Object object) {
        Player player;
        if (object instanceof OfflinePlayer) {
            OfflinePlayer offlinePlayer = (OfflinePlayer) object;
            if (!offlinePlayer.isOnline()) {
                if (speeds.size() == 0) {
                    return;
                }
                if (speeds.get(offlinePlayer.getUniqueId()) != null) {
                    speeds.remove(offlinePlayer.getUniqueId());
                }
            }
        }
        if (object instanceof Player) {
            player = (Player)object;
            temp.clear();
            temp.put(System.currentTimeMillis(),player.getLocation());
            speeds.put(player.getUniqueId(),temp);
            if (speeds.get(player.getUniqueId()).size() > 2) {
                speeds.get(player.getUniqueId()).forEach((time, pos)->{
                    if (time >= flush) {
                        speeds.get(player.getUniqueId()).remove(time);
                    }
                });
            }
        }
    }
    public static List<Location> getAll(UUID uuid) {
        if (speeds.size() != 0) {
            if (speeds.get(uuid) != null) {
                if (speeds.get(uuid).size() != 0) {
                    return new ArrayList<>(speeds.get(uuid).values());
                }
            }
        }
        return new ArrayList<>();
    }
    public static void countAll() {
        Arrays.asList(Bukkit.getOfflinePlayers()).forEach(Speed::count);
    }
    public static double get(Location a,Location b) {
        return get(a.getX(), b.getX()) + get(a.getZ(), b.getZ());
    }
    public static double get(double a,double b) {
        return Math.max(a, b) - Math.min(a, b);
    }
    public static double getSpeed(UUID uuid) {
        if (speeds.size() != 0) {
            if (speeds.get(uuid) != null) {
                if (speeds.get(uuid).size() >= 2) {
                    List<Location> poses = getAll(uuid);
                    Location first = poses.get(0);
                    Location last = poses.get(poses.size() - 1);
                    System.out.println(get(first.getX(), last.getX()) + get(first.getZ(), last.getZ()));
                    return get(first,last);
                }
            }
        }
        return 0.0D;
    }
}
