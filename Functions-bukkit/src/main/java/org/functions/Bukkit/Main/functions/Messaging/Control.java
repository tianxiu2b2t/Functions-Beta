package org.functions.Bukkit.Main.functions.Messaging;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.functions.Bukkit.Main.Functions;
import org.functions.Bukkit.Main.functions.Messaging.BungeeCord.GetServer;
import org.functions.Bukkit.Main.functions.Messaging.BungeeCord.Teleport;

public class Control implements PluginMessageListener {
    GetServer getBcServer;
    Teleport BcTeleport;
    Functions instance = Functions.instance;
    public void onEnable() {
        if (!instance.getServer().getMessenger().isOutgoingChannelRegistered(instance, "BungeeCord")) {
            instance.getServer().getMessenger().registerOutgoingPluginChannel(instance, "BungeeCord");
        }
        if (!instance.getServer().getMessenger().isIncomingChannelRegistered(instance, "BungeeCord")) {
            instance.getServer().getMessenger().registerIncomingPluginChannel(instance, "BungeeCord", this);
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

    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        getBcServer.ReceivePluginMessage(channel, player, message);
    }

    public GetServer getGetBcServer() {
        return getBcServer;
    }

    public Teleport getBcTeleport() {
        return BcTeleport;
    }
}
