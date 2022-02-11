package org.functions.Bukkit.Main.functions.ServerInfo.Memory;

public class Data {
    private final long total;
    private final long free;
    private final long max;
    private final long usedMem;
    //private final int usedPercent;

    public Data(long total, long free, long max) {
        this.total = total;
        this.free = free;
        this.max = max;
        this.usedMem = total - free;
        //this.usedPercent = this.usedMem == 0L ? 0 : (int)(this.usedMem * 100L / max);
    }

    public long getTotal() {
        return this.total;
    }

    public long getFree() {
        return this.free;
    }

    public long getMax() {
        return this.max;
    }

    public long getUsedMem() {
        return this.usedMem;
    }

    //public int getUsedPercent() {
        //return this.usedPercent;
    //}
}
