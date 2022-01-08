package org.functions.Bukkit.Main.functions.Messaging;


import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.functions.Bukkit.Main.Functions;
import org.functions.Bukkit.Main.functions.Accounts;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.regex.Pattern;

public class AccountMessaging implements Messaging , PluginMessageListener {
    Functions instance = Functions.instance;
    public void onEnable() {
        if (!instance.getServer().getMessenger().isOutgoingChannelRegistered(instance, "BungeeCord")) {
            instance.getServer().getMessenger().registerOutgoingPluginChannel(instance, "BungeeCord");
        }
        if (!instance.getServer().getMessenger().isIncomingChannelRegistered(instance, "BungeeCord")) {
            instance.getServer().getMessenger().registerIncomingPluginChannel(instance, "BungeeCord", this);
        }
    }

    public void onDisable() {
        if (instance.getServer().getMessenger().isOutgoingChannelRegistered(instance, "BungeeCord")) {
            instance.getServer().getMessenger().unregisterOutgoingPluginChannel(instance);
        }
        if (instance.getServer().getMessenger().isIncomingChannelRegistered(instance, "BungeeCord")) {
            instance.getServer().getMessenger().unregisterIncomingPluginChannel(instance);
        }

    }
    @SuppressWarnings("all")
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
        String subchannel = "";
        try {
            subchannel = in.readUTF();
        } catch (IOException e) {
        }
        if (subchannel.equalsIgnoreCase("login")) {
            String player_name_or_uuid = null;
            String uuid_format = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";
            try {
                player_name_or_uuid = in.readUTF();
            } catch (IOException e) {
            }
            if (player_name_or_uuid.equals("") || player_name_or_uuid == null) {
                return;
            }
            UUID uuid = UUID.randomUUID();
            if (player_name_or_uuid.matches(uuid_format)) {
                uuid = UUID.fromString(player_name_or_uuid);
                Accounts.getAccount(uuid);
            } else {
                String finalPlayer_name_or_uuid = player_name_or_uuid;
                Accounts.getAccounts().forEach(e -> {
                    if (e.getLowerName().equalsIgnoreCase(finalPlayer_name_or_uuid.toLowerCase())) {
                    try {
                        Method method = e.getClass().getDeclaredMethod("login");
                        method.setAccessible(true);
                        method.invoke(e);
                    } catch (IllegalAccessException ex) {
                        ex.printStackTrace();
                    } catch (InvocationTargetException ex) {
                        ex.printStackTrace();
                    } catch (NoSuchMethodException ex) {
                        ex.printStackTrace();
                    }
                }
                });
            }
        }
    }
}
