package org.functions.Bungee.Main;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import org.functions.Bungee.API.FPI;
import org.functions.Bungee.Messaging.AccountMessaging;

public final class Functions extends Plugin {
    public static Functions instance;
    public FPI fpi;
    @Override
    public void onEnable() {
        //instance = this;
        //fpi = new FPI();
        //ProxyServer.getInstance().getPluginManager().registerListener(this, new AccountMessaging());
        // Plugin startup logic
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
