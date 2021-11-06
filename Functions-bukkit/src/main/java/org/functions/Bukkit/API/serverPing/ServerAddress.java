package org.functions.Bukkit.API.serverPing;

public class ServerAddress {

    private String ip;
    private int port;

    public ServerAddress(String ip, int port) {
        this.ip = ip;
        if (port==0) {
            this.port = 25565;
        }
        this.port = port;
    }

    public String getAddress() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return ip + ":" + port;
    }

}
