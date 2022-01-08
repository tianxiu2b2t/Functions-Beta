package org.functions.Application.irc.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.UUID;

public class Accept {
    Socket socket;
    Server server;
    DataOutputStream output;
    DataInputStream input;
    UUID uuid;
    public Accept(Socket socket, Server server,UUID uuid){
        this.uuid = uuid;
        this.socket = socket;
        this.server = server;
        try {
            output = new DataOutputStream(socket.getOutputStream());
            input = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void print() {
        try {
            input = new DataInputStream(socket.getInputStream());
            String text = input.readUTF();
            System.out.println(text);
            send(text);
        } catch (IOException e) {
            server.link.remove(uuid);
            e.printStackTrace();
        }
    }
    public void send(String[] message) {
        Arrays.asList(message).forEach(this::send);
    }
    public void send(String message) {
        try {
            output = new DataOutputStream(socket.getOutputStream());
            output.writeUTF(message);
        } catch (IOException e) {
            server.link.remove(uuid);
            e.printStackTrace();
        }
    }
}
