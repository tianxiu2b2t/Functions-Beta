package org.functions.Bukkit.Main.functions.ServerInfo;

import org.functions.Bukkit.Main.Functions;
import org.functions.Bukkit.Main.Server.FServer;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.math.BigDecimal;
import java.text.DecimalFormat;

public class Info {
    static MemoryUsage usage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
    static FServer fServer = Functions.instance.getFServer();
    public static String getInfo() {
        System.out.println(Runtime.getRuntime().totalMemory() + " | " + Runtime.getRuntime().freeMemory() + " | " + Runtime.getRuntime().maxMemory());
        return fServer.getInstance().getAPI().putLanguage("ServerInfo","服务器流畅度: %detail_tps%%lines%%plugin_prefix%服务器物品数: %item_size%%lines%%plugin_prefix%服务器实体数: %entities_size%%lines%%plugin_prefix%服务器物品堆叠数: %item_stack_size%",null,new Object[]{"item_size",fServer.getCountItems(),"entities_size",(fServer.getCountEntities() - fServer.getCountItems()),"item_stack_size",fServer.getEntitiesItemStacks(),"max_memory",getJVMMaxMemory(),"used_memory",getServerUsedMemory(),"free_memory",getServerFreeMemory(),"commit_memory",getServerCommittedMemory()});
    }
    public static long getJVMMinMemory() {
        return usage.getInit();
    }
    public static long getJVMMaxMemory() {
        return usage.getMax();
    }
    public static long getServerUsedMemory() {
        return getJVMMaxMemory() - getServerFreeMemory();
    }
    //1.052144659377628
    //1.056584716848338
    public static long getServerFreeMemory() {
        return usage.getUsed();
    }
    public static long getServerCommittedMemory() {
        return usage.getCommitted();
    }
    public static double changeBtoMB(long memory) {
        return change(((double)memory / (1024 * 1024)));
    }
    public static double change(long number) {
        return parseDoubleUpFromNumber(number,1);
    }
    public static double change(double number) {
        return parseDoubleUpFromNumber(number,1);
    }
    public static double parseDoubleUpFromNumber(Object d, int limit) {
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < limit; i++) {
            temp.append("0");
        }
        return parseDoubleUp(d, temp.toString());
    }
    public static double parseDouble(Object text) {
        return Double.parseDouble(text.toString());
    }
    public static String parseString(Object text) {
        return text.toString();
    }
    public static double parseDoubleUp(Object d,String limit) {
        String t = parseString(parseDouble(d));
        DecimalFormat df = new DecimalFormat("#." + limit);
        if (t.contains(".")) {
            if (t.split("\\.")[1].length() >= (limit.length() + 1)) {
                t = t.split("\\.")[0] + "." + t.split("\\.")[1].substring(0, limit.length() + 1);
            }
            return parseDouble(df.format(parseDouble(t)));
        }
        return parseDouble(d);
    }
}
