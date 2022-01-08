package org.functions.Application.irc.Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client1 {
    public Client1() {

    }
    public void run() {
        try {
            Socket socket = new Socket("localhost",8192);
            try {
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                DataInputStream input = new DataInputStream(socket.getInputStream());
                Scanner scanner = new Scanner(System.in);
                while (true) {
                    String send = scanner.nextLine();
                    //System.out.println(send);
                    output.writeUTF(send);
                    System.out.println(input.readUTF());
                }
            } catch (IOException ignored) {

            } finally {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws IOException {
        boolean noConnectBeWait = true;
        while (true) {
            new Client1().run();
        }
    }
}
