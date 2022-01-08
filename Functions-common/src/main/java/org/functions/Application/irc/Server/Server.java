package org.functions.Application.irc.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.UUID;

public class Server {
    public LinkedHashMap<UUID,Accept> link = new LinkedHashMap<>();
    public Server() {

    }
    ServerSocket serverSocket;
    public void run() {
        try {
            serverSocket = new ServerSocket(8192);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void accept() {
        UUID uuid = UUID.randomUUID();
        try {
            link.put(uuid,new Accept(serverSocket.accept(),this,uuid));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws IOException {
        LinkedHashMap<UUID,Accept> link_old = new LinkedHashMap<>();
        boolean noConnectBeWait = true;
            Server server = new Server();
            server.run();
            while (true) {
                server.accept();
                if (link_old.size() == 0) {
                    link_old = server.link;
                }
                LinkedHashMap<UUID, Accept> finalLink_old = link_old;
                LinkedHashMap<UUID, Accept> finalLink_old1 = link_old;
                if (server.link.size() != link_old.size()) {
                    server.link.forEach(((uuid, accept) -> {
                        if (finalLink_old1.get(uuid)!=null) {
                            finalLink_old1.remove(uuid);
                        }
                    }));
                    finalLink_old1.forEach((uuid, accept) -> {
                        System.out.println(uuid.toString() + " is join channel");
                    });
                }
                server.link.forEach((uuid, accept) -> {
                if (finalLink_old.get(uuid)==null) {
                    System.out.println(uuid.toString() + " is leave channel irc.");
                }
                if (accept.socket.isClosed() || !accept.socket.isConnected()) {
                    server.link.remove(uuid);
                    System.out.println(uuid.toString() + " is leave channel irc.");
                }
                accept.print();
            });
                link_old = server.link;
        }
    }
}
