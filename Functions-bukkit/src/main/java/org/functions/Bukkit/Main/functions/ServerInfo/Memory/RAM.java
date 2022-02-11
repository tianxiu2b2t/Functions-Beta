package org.functions.Bukkit.Main.functions.ServerInfo.Memory;

import java.util.LinkedList;

public class RAM {
    static LinkedList<Data> DATA = new LinkedList<>();
    public static Data update() {
        Runtime jvm = Runtime.getRuntime();
        DATA.add(new Data(jvm.totalMemory(),jvm.freeMemory(), jvm.maxMemory()));
        return getLastData();
    }
    public static Data getLastData() {
        return DATA.getLast();
    }
    static {
        Data data = new Data(0L,0L,0L);
        for (int i = 0; i < 350; i++) {
            DATA.add(data);
        }
    }
}
