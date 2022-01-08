package org.functions.Bukkit.Main.functions.Messaging;

import org.bukkit.entity.Player;
import org.functions.Bukkit.Main.Functions;

import java.util.ArrayList;
import java.util.List;

public class Manager implements ListenerMessaging {
    Functions instance = Functions.instance;
    public void onEnable() {
        if (!instance.getServer().getMessenger().isOutgoingChannelRegistered(instance, "BungeeCord")) {
            instance.getServer().getMessenger().registerOutgoingPluginChannel(instance, "BungeeCord");
        }
        if (!instance.getServer().getMessenger().isIncomingChannelRegistered(instance, "BungeeCord")) {
            instance.getServer().getMessenger().registerIncomingPluginChannel(instance, "BungeeCord", this);
        }
        getAllClass().forEach(Messaging::onEnable);
    }

    public void onDisable() {
        if (instance.getServer().getMessenger().isOutgoingChannelRegistered(instance, "BungeeCord")) {
            instance.getServer().getMessenger().unregisterOutgoingPluginChannel(instance);
        }
        if (instance.getServer().getMessenger().isIncomingChannelRegistered(instance, "BungeeCord")) {
            instance.getServer().getMessenger().unregisterIncomingPluginChannel(instance);
        }
        getAllClass().forEach(Messaging::onDisable);
    }

    public List<ListenerMessaging> getAllClass() {
        List<ListenerMessaging> list = new ArrayList<>();
        list.add(new BungeeCordTeleport());
        return list;
    }

    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        getAllClass().forEach(e->onPluginMessageReceived(channel, player, message));
    }
}
