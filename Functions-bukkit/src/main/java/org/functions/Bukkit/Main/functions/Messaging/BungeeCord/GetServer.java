package org.functions.Bukkit.Main.functions.Messaging.BungeeCord;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.functions.Bukkit.Main.Functions;
import org.functions.Bukkit.Main.functions.Messaging.BungeeCordTeleport;
import org.functions.Bukkit.Main.functions.Messaging.ListenerMessaging;
import org.functions.Bukkit.Main.functions.Messaging.Manager;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class GetServer extends ListenerMessaging {
    public List<String> ServerList = new ArrayList<>();
    public LinkedHashMap<String,String> OnlinePlayers = new LinkedHashMap<>();


    public List<String> getServerList() {
        return ServerList;
    }
    public LinkedHashMap<String,String> getServerOnlinePlayers() {
        return OnlinePlayers;
    }

    public void onEnable() {
        Manager.manager.addClass("Account",this);
    }

    public void onDisable() {

    }

    public void ReceivePluginMessage(String channel, Player player, String[] message) {
        String subchannel = message[0];
        String[] SL = null;

        String ts;
        if (subchannel.equals("GetServers")) {
            SL = message[1].split(", ");

            String[] var14 = SL;
            int var8 = SL.length;
            ServerList.clear();
            for(int var9 = 0; var9 < var8; ++var9) {
                ts = var14[var9];
                ServerList.add(ts);
            }

        }
        if (subchannel.equals("PlayerList")) {
            System.out.println(subchannel);
            String ServerName = "";
            ServerName = message[1];
            SL = message[2].split(", ");

            String[] var14 = SL;
            int var8 = SL.length;
            OnlinePlayers.clear();
            for(int var9 = 0; var9 < var8; ++var9) {
                ts = var14[var9];
                OnlinePlayers.put(ts,ServerName);
            }

        }

        if (subchannel.equals("Functions_ServerTeleportAll")) {
            ts = message[1];
            ((BungeeCordTeleport)Manager.manager.getClass("BungeeCordBetweenServers")).getBcTeleport().Run(ts);
        }

    }

    public void SendBCDataGetServerList() {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF("GetServers");
        } catch (IOException var4) {
        }

        Bukkit.getConsoleSender().getServer().sendPluginMessage(Functions.instance, "BungeeCord", b.toByteArray());
    }
    public void SendBCDataGetServerOnlinePlayers() {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF("PlayerList");
            //out.writeUTF(ServerName);
            out.writeUTF("ALL");
        } catch (IOException var4) {
        }

        Bukkit.getConsoleSender().getServer().sendPluginMessage(Functions.instance, "BungeeCord", b.toByteArray());
    }

    public void SendBCDateStpAll(String TargetServer) {
        sendMessage("BungeeCord", new Object[]{"Forward","ALL","Functions_ServerTeleportAll",TargetServer});
    }
}
