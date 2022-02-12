package org.functions.Bukkit.Main.functions.Messaging;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.functions.Bukkit.Main.Functions;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Manager implements PluginMessageListener {
    public static Manager manager;
    Functions instance = Functions.instance;
    public Manager() {
        setManager(this);
    }
    public void onEnable() {
        manager = this;
        if (!instance.getServer().getMessenger().isOutgoingChannelRegistered(instance, "Functions:" + Functions.instance.getVersion())) {
            instance.getServer().getMessenger().registerOutgoingPluginChannel(instance, "Functions:" + Functions.instance.getVersion());
        }
        if (!instance.getServer().getMessenger().isIncomingChannelRegistered(instance, "Functions:" + Functions.instance.getVersion())) {
            instance.getServer().getMessenger().registerIncomingPluginChannel(instance, "Functions:" + Functions.instance.getVersion(), this);
        }
        getAllClass().forEach(ListenerMessaging::onEnable);
    }

    public void setManager(Manager manager) {
        Manager.manager = manager;
    }

    public void onDisable() {
        if (instance.getServer().getMessenger().isOutgoingChannelRegistered(instance, "Functions:" + Functions.instance.getVersion())) {
            instance.getServer().getMessenger().unregisterOutgoingPluginChannel(instance);
        }
        if (instance.getServer().getMessenger().isIncomingChannelRegistered(instance, "Functions:" + Functions.instance.getVersion())) {
            instance.getServer().getMessenger().unregisterIncomingPluginChannel(instance);
        }
        getAllClass().forEach(ListenerMessaging::onDisable);
        manager = null;
    }

    List<ListenerMessaging> list = new ArrayList<>();
    LinkedHashMap<String,ListenerMessaging> link = new LinkedHashMap<>();
    public void addClass(String name , ListenerMessaging messaging) {
        list.add(messaging);
        link.put(name,messaging);
    }

    public List<ListenerMessaging> getAllClass() {
        return list;
    }

    public ListenerMessaging getClass(String name) {
        return link.get(name);
    }

    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
        String[] objects = new String[message.length];
        for (int i = 0; i < message.length; i++) {
            try {
                objects[i] = in.readUTF();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        getAllClass().forEach(e-> e.ReceivePluginMessage(channel,player,objects));
    }
}
