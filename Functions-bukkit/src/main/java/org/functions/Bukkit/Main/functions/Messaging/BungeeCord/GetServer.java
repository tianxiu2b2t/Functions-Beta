package org.functions.Bukkit.Main.functions.Messaging.BungeeCord;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.functions.Bukkit.Main.Functions;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class GetServer {
    public List<String> ServerList = new ArrayList<>();
    public LinkedHashMap<String,String> OnlinePlayers = new LinkedHashMap<>();


    public List<String> getServerList() {
        return ServerList;
    }
    public LinkedHashMap<String,String> getServerOnlinePlayers() {
        return OnlinePlayers;
    }

    public void ReceivePluginMessage(String channel, Player player, byte[] message) {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
        String subchannel = "";
        String[] SL = null;

        try {
            subchannel = in.readUTF();
        } catch (IOException var13) {
        }

        String ts;
        if (subchannel.equals("GetServers")) {
            try {
                String ServerListStr = in.readUTF();
                SL = ServerListStr.split(", ");
            } catch (IOException var12) {
            }

            if (SL == null) {
                return;
            }

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
            try {
                ServerName = in.readUTF();
                SL = in.readUTF().split(", ");
            } catch (IOException var12) {
            }

            if (SL == null) {
                return;
            }

            String[] var14 = SL;
            int var8 = SL.length;
            OnlinePlayers.clear();
            for(int var9 = 0; var9 < var8; ++var9) {
                ts = var14[var9];
                OnlinePlayers.put(ts,ServerName);
            }

        }

        if (subchannel.equals("Functions_ServerTeleportAll")) {
            try {
                short len = in.readShort();
                byte[] msgbytes = new byte[len];
                in.readFully(msgbytes);
                DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
                ts = msgin.readUTF();
                Functions.instance.getMessaging().getBcTeleport().Run(ts);
            } catch (IOException var11) {
            }
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
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF("Forward");
            out.writeUTF("ALL");
            out.writeUTF("Functions_ServerTeleportAll");
            ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
            DataOutputStream msgout = new DataOutputStream(msgbytes);
            msgout.writeUTF(TargetServer);
            out.writeShort(msgbytes.toByteArray().length);
            out.write(msgbytes.toByteArray());
        } catch (IOException var6) {
        }

        Bukkit.getConsoleSender().getServer().sendPluginMessage(Functions.instance, "BungeeCord", b.toByteArray());
    }
}
