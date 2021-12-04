package org.functions.Bungee.API;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class FPlayer {
    boolean a = false;
    UUID e;
    public FPlayer(UUID e) {
        this.e = e;
    }
    public void a(boolean a) {
        this.a = a;
    }
    public boolean b() {
        return a;
    }
    public ProxiedPlayer p() {
        return ProxyServer.getInstance().getPlayer(e);
    }
}
