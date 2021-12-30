package org.functions.Bukkit.Main.functions.ServerInfo;

import org.functions.Bukkit.Main.Functions;
import org.functions.Bukkit.Main.Server.FServer;

public class Info {
    static FServer fServer = Functions.instance.getFServer();
    public static String getInfo() {
        return fServer.getInstance().getAPI().putLanguage("ServerInfo","服务器流畅度: %detail_tps%%lines%%plugin_prefix%服务器物品数: %item_size%%lines%%plugin_prefix%服务器实体数: %entities_size%",null,new Object[]{"item_size",fServer.getCountItems(),"entities_size",(fServer.getCountEntities() - fServer.getCountItems())});
    }
}
