package org.functions.Bukkit.Main.functions.Messaging;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.functions.Bukkit.Main.Functions;
import org.functions.Bukkit.Main.functions.Messaging.BungeeCord.GetServer;
import org.functions.Bukkit.Main.functions.Messaging.BungeeCord.Teleport;

public class BungeeCordTeleport extends ListenerMessaging {
    GetServer getBcServer;
    Teleport BcTeleport;
    Functions instance = Functions.instance;
    public void onEnable() {
        Manager.manager.addClass("BungeeCordBetweenServers",this);
        if (!instance.getServer().getMessenger().isOutgoingChannelRegistered(instance, "BungeeCord")) {
            instance.getServer().getMessenger().registerOutgoingPluginChannel(instance, "BungeeCord");
        }
        if (!instance.getServer().getMessenger().isIncomingChannelRegistered(instance, "BungeeCord")) {
            instance.getServer().getMessenger().registerIncomingPluginChannel(instance, "BungeeCord", Manager.manager);
        }
        getBcServer = new GetServer();
        getBcServer.SendBCDataGetServerList();
        getBcServer.SendBCDataGetServerOnlinePlayers();
        BcTeleport = new Teleport();
    }

    public void onDisable() {
        if (instance.getServer().getMessenger().isOutgoingChannelRegistered(instance, "BungeeCord")) {
            instance.getServer().getMessenger().unregisterOutgoingPluginChannel(instance);
        }
        if (instance.getServer().getMessenger().isIncomingChannelRegistered(instance, "BungeeCord")) {
            instance.getServer().getMessenger().unregisterIncomingPluginChannel(instance);
        }
        getBcServer = null;
        BcTeleport = null;
    }

    public void ReceivePluginMessage(String channel, Player player, String[] message) {
        getBcServer.ReceivePluginMessage(channel, player, message);
    }

    public GetServer getGetBcServer() {
        return getBcServer;
    }

    public Teleport getBcTeleport() {
        return BcTeleport;
    }
}
