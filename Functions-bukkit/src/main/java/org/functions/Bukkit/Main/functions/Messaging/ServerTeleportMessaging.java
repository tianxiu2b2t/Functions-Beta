package org.functions.Bukkit.Main.functions.Messaging;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.functions.Bukkit.Main.Functions;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ServerTeleportMessaging implements ListenerMessaging {
    public List<String> ServerList = new ArrayList<>();
    Functions instance = Functions.instance;
    public void onEnable() {
        if (!instance.getServer().getMessenger().isOutgoingChannelRegistered(instance, "BungeeCord")) {
            instance.getServer().getMessenger().registerOutgoingPluginChannel(instance, "BungeeCord");
        }
        if (!instance.getServer().getMessenger().isIncomingChannelRegistered(instance, "BungeeCord")) {
            instance.getServer().getMessenger().registerIncomingPluginChannel(instance, "BungeeCord", this);
        }
        getServers();
    }

    public void onDisable() {
        if (instance.getServer().getMessenger().isOutgoingChannelRegistered(instance, "BungeeCord")) {
            instance.getServer().getMessenger().unregisterOutgoingPluginChannel(instance);
        }
        if (instance.getServer().getMessenger().isIncomingChannelRegistered(instance, "BungeeCord")) {
            instance.getServer().getMessenger().unregisterIncomingPluginChannel(instance);
        }

    }

    public void Run(Player p, String ServerName) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF("Connect");
            out.writeUTF(ServerName);
        } catch (IOException var6) {
        }

        p.sendPluginMessage(instance, "BungeeCord", b.toByteArray());
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

        Bukkit.getServer().sendPluginMessage(instance, "BungeeCord", b.toByteArray());
    }

    public void Run(String ServerName) {
        Bukkit.getOnlinePlayers().forEach((p)->{Run(ServerName);});
    }

    public void getServers() {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Functions");
        out.writeUTF("GetServers");
        instance.getServer().sendPluginMessage(instance,"BungeeCord",out.toByteArray());

    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        ReceivePluginMessage(channel,player,message);
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

            for(int var9 = 0; var9 < var8; ++var9) {
                ts = var14[var9];
                ServerList.add(ts);
            }

        }

        if (subchannel.equals("Functions_ServerTeleportAll")) {
            try {
                short len = in.readShort();
                byte[] msgbytes = new byte[len];
                in.readFully(msgbytes);
                DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
                ts = msgin.readUTF();
                Run(ts);
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

        Bukkit.getConsoleSender().getServer().sendPluginMessage(instance, "BungeeCord", b.toByteArray());
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

        Bukkit.getConsoleSender().getServer().sendPluginMessage(instance, "BungeeCord", b.toByteArray());
    }
}
