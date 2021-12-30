package org.functions.Bukkit.API.serverPing;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SpeedPerSeconds {
    List<Long> speed = new ArrayList<>();
    UUID uuid;
    int max;
    public SpeedPerSeconds(UUID uuid) {
        this.uuid = uuid;
    }
    public void count() {
        speed.add(System.currentTimeMillis());
        removeTimeout();
        if (speed.size() > max) {
            max = speed.size();
        }

    }
    public void removeTimeout() {
        while(!speed.isEmpty() && System.currentTimeMillis() - (Long)speed.get(0) > 1000L) {
            speed.remove(0);
        }
    }
    public void reset() {
        speed.clear();
        max = 0;
    }
    public void resetMax() {
        removeTimeout();
        max = 0;
    }

    public int getMaxCPS() {
        removeTimeout();
        return max;
    }

    public int getCountCPS() {
        removeTimeout();
        return speed.size();
    }
}
