package org.functions.Bukkit.Main.functions.Messaging;

import org.bukkit.entity.Player;
import org.functions.Bukkit.Main.Functions;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

public abstract class ListenerMessaging {
    public abstract void onEnable();
    public abstract void onDisable();
    public abstract void ReceivePluginMessage(String channel, Player player, String[] message);
    public ByteArrayOutputStream asDataOutputStream(Object[] message) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream in = new DataOutputStream(baos);
        for (Object o : message) {
            try {
                in.writeUTF(o+"");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return baos;
    }
    public void sendMessage(String channel, Object[] message) {
        Functions.instance.getServer().sendPluginMessage(Functions.instance,channel,asDataOutputStream(message).toByteArray());
    }
}
