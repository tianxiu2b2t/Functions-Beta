package org.functions.Bukkit.Tasks;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import org.functions.Bukkit.Main.Functions;

import java.util.ArrayList;
import java.util.List;

public class Task {
    List<Integer> tasks = new ArrayList<>();
    public void runTask(Runnable runnable,long delay,long repeat) {
        int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Functions.instance,runnable,delay,repeat);
        tasks.add(id);
    }
    public void runTask(Runnable runnable,long repeat) {
        runTask(runnable,0,repeat);
    }
    public void cancelAll() {
        tasks.forEach(Bukkit.getScheduler()::cancelTask);
        tasks.clear();
    }
}
