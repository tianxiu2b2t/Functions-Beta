package org.functions.Bukkit.Main.functions.Messaging;


import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.functions.Bukkit.Main.Functions;
import org.functions.Bukkit.Main.functions.UserAccounts.*;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.UUID;
import java.util.regex.Pattern;

public class AccountMessaging extends ListenerMessaging {
    Functions instance = Functions.instance;
    public void onEnable() {
        Manager.manager.addClass("Account",this);
        if (!instance.getServer().getMessenger().isOutgoingChannelRegistered(instance, "BungeeCord")) {
            instance.getServer().getMessenger().registerOutgoingPluginChannel(instance, "BungeeCord");
        }
        if (!instance.getServer().getMessenger().isIncomingChannelRegistered(instance, "BungeeCord")) {
            instance.getServer().getMessenger().registerIncomingPluginChannel(instance, "BungeeCord", Manager.manager);
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
    public void ReceivePluginMessage(String channel, Player player, String[] message) {
        String player_name_or_uuid = message[0];
        String uuid_format = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";
        if (player_name_or_uuid.equals("") || player_name_or_uuid == null) {
            return;
        }
        UUID uuid = UUID.randomUUID();
        if (player_name_or_uuid.matches(uuid_format)) {
            uuid = UUID.fromString(player_name_or_uuid);
        } else {
            uuid = Accounts.getAccount(player_name_or_uuid).getUUID();
        }
        String subchannel = message[1];
        switch (subchannel){
            case "login": login(uuid);
            break;
            case "queryCanLogin":
                sendMessage(new Object[]{uuid,"CanTimeOut",Accounts.isBcLoginTimeOut(uuid)});
                break;
            case "isLogin":
                sendMessage(new Object[]{uuid,Accounts.getAccount(uuid).isLogin()});
                break;
            default:
                sendMessage(new Object[]{uuid,"No channel execute"});
        }
    }
    public void sendMessage(Object[] args) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        Arrays.asList(args).forEach(e->{
            try {
                out.writeUTF(e+"");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        Functions.instance.getServer().sendPluginMessage(Functions.instance,"BungeeCord",b.toByteArray());
    }
    private void login(UUID uuid) {
        Account e = Accounts.getAccount(uuid);
        try {
            Method method = e.getClass().getDeclaredMethod("login");
            method.setAccessible(true);
            method.invoke(e);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
            ex.printStackTrace();
        }
    }
}
