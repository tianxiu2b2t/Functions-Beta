package org.functions.Bungee.Main.functions;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.net.SocketAddress;
import java.util.*;

public class Server {
    final ServerInfo server;
    final String name;
    public Server(String name, ServerInfo server) {
        this.server = server;
        this.name = name;
    }

    public ServerInfo getServer() {
        return server;
    }

    public String getName() {
        return name;
    }

    public List<ProxiedPlayer> getPlayers() {
        return new ArrayList<>(getServer().getPlayers());
    }

    public SocketAddress getSocketAddress() {
        return getServer().getSocketAddress();
    }

}
