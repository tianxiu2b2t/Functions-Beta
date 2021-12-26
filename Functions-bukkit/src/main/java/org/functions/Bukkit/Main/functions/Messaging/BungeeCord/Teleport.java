package org.functions.Bukkit.Main.functions.Messaging.BungeeCord;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.functions.Bukkit.Main.Functions;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;

public class Teleport {
    public void Run(Player p, String ServerName) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF("Connect");
            out.writeUTF(ServerName);
        } catch (IOException var6) {
        }

        p.sendPluginMessage(Functions.instance, "BungeeCord", b.toByteArray());
    }

    public void Run(String PName, String ServerName) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF("ConnectOther");
            out.writeUTF(PName);
            out.writeUTF(ServerName);
        } catch (IOException var6) {
        }

        Bukkit.getServer().sendPluginMessage(Functions.instance, "BungeeCord", b.toByteArray());
    }

    public void Run(String ServerName) {
        Functions.instance.getServer().getOnlinePlayers().forEach((e)->{Run(e,ServerName);});

    }
}
