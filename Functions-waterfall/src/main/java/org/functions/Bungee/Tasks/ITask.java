package org.functions.Bungee.Tasks;

import net.md_5.bungee.api.ProxyServer;
import org.functions.Bungee.Main.Functions;

import java.util.concurrent.TimeUnit;

public abstract class ITask implements Runnable {
    ITask task;
    long delay;
    long period;
    TimeUnit unit;
    public ITask(ITask task) {
        this(task,0);
    }
    public ITask(ITask task,long delay) {
        this(task,delay,0);
    }
    public ITask(ITask task,long delay,long period) {
        this(task, delay,period,TimeUnit.SECONDS);
    }
    public ITask(ITask task,long delay,long period,TimeUnit unit) {
        this.task = task;
        this.delay = delay;
        this.period = period;
        this.unit = unit;
    }

    public ITask getTask() {
        return task;
    }

    public long getDelay() {
        return delay;
    }

    public long getPeriod() {
        return period;
    }

    public TimeUnit getUnit() {
        return unit;
    }
    public boolean equals(Object o) {
        if (o instanceof Runnable) {
            if (o.equals(this)) {
                return true;
            }
        }
        if (o instanceof ITask) {
            return ((ITask) o).task == task;
        }
        return false;
    }
}
