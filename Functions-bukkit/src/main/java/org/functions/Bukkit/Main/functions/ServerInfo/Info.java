package org.functions.Bukkit.Main.functions.ServerInfo;

import org.functions.Bukkit.Main.Functions;
import org.functions.Bukkit.Main.Server.FServer;
import org.functions.Bukkit.Main.functions.ServerInfo.Memory.Data;
import org.functions.Bukkit.Main.functions.ServerInfo.Memory.RAM;


public class Info {
    static FServer fServer = Functions.instance.getFServer();
    public static Data getData() {
        return RAM.update();
    }
    public static String getInfo() {
        Data data = getData();
        long max = data.getMax() / 1024L / 1024L;
        long total = data.getTotal() / 1024L / 1024L;
        long free = data.getFree() / 1024L / 1024L;
        long used = data.getUsedMem() / 1024L / 1024L;
        long free_display = data.getFree() * 100L / data.getMax();
        return fServer.getInstance().getAPI().putLanguage("ServerInfo","服务器流畅度: %detail_tps%" +
                "%lines%%plugin_prefix%服务器物品数: %item_size%" +
                "%lines%%plugin_prefix%服务器实体数: %entities_size%" +
                "%lines%%plugin_prefix%服务器物品堆叠数: %item_stack_size%" +
                "%lines%%plugin_prefix%服务器使用内存: %used_memory% / %max_memory%" +
                "%lines%%plugin_prefix%服务器内存堆: %total% / %max_memory%",null,new Object[]{"item_size",fServer.getCountItems(),"entities_size",(fServer.getCountEntities() - fServer.getCountItems()),"item_stack_size",fServer.getEntitiesItemStacks(),"max_memory",max,"used_memory",used,"free_memory",free,"free_memory_%",free_display,"total_memory",total});
    }
    public static Data[] gc() {
        Data before = getData();
        fServer.gc();
        Data ask = getData();
        return new Data[]{before,ask};
    }
}
