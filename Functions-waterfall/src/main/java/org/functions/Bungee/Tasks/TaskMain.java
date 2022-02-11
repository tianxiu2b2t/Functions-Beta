package org.functions.Bungee.Tasks;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import org.functions.Bungee.Main.Functions;

import java.util.ArrayList;
import java.util.List;

public class TaskMain {
    static List<ScheduledTask> tasks = new ArrayList<>();
    static List<ITask> Itasks = new ArrayList<>();
    public TaskMain() {

    }
    public static void addTask(ITask task) {
        Itasks.add(task);
        tasks.add(ProxyServer.getInstance().getScheduler().schedule(Functions.instance,task,task.getDelay(),task.getPeriod(), task.getUnit()));
    }
    public static void removeTask(ITask task) {
        getTask(task).cancel();
    }
    public static ScheduledTask getTask(ITask task) {
        for (int i = 0; i < Itasks.size(); i++) {
            if (Itasks.get(i).equals(task)) {
                return tasks.get(i);
            }
        }
        return null;
    }
}
