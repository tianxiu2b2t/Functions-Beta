package org.functions.Bungee.Messaging;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.protocol.Protocol;
import org.functions.Bungee.API.FPlayer;
import org.functions.Bungee.Main.Functions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class AccountMessaging implements Listener {
    @EventHandler
    public void onPlayerConnect(PostLoginEvent event) {
        System.out.println("a");
        event.getPlayer().getModList().forEach((modname,mod)->{
            System.out.println(modname + "|" + mod);
        });
        Functions.instance.fpi.temp.put(event.getPlayer().getUniqueId(),new FPlayer(event.getPlayer().getUniqueId()));
    }
    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent event) {
        Functions.instance.fpi.temp.remove(event.getPlayer().getUniqueId());
    }
    @EventHandler
    public void onAcceptConnectFromServer(PluginMessageEvent event) {
        if (!event.getTag().equals("BungeeCord")) {
            return;
        }
        if (!(event.getSender() instanceof Server)) {
            return;
        }
        final ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
        int i = 0;
        try {
            InputStream is = Functions.instance.getResourceAsStream("Version");
            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            i = Integer.parseInt(br.readLine());
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        if (!in.readUTF().equals("Functions." + i)) {
            return;
        }
        final short dataLength = in.readShort();
        final byte[] dataBytes = new byte[dataLength];
        in.readFully(dataBytes);
        final ByteArrayDataInput dataIn = ByteStreams.newDataInput(dataBytes);
        if ("isLogin".equals(dataIn.readUTF())) {
            Functions.instance.fpi.temp.get(UUID.fromString(in.readUTF())).a(in.readBoolean());
        }

    }
    @EventHandler
    public void onSendConnectFromAndToServer(ServerSwitchEvent event) {
        if (event.getFrom() == null) {
            return;
        }
    }

    @EventHandler
    public void onServerPing(ServerKickEvent event) {
    }

    @EventHandler
    public void onJoin(ServerConnectEvent event) {
    }


}
