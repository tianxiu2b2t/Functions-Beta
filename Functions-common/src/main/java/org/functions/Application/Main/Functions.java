package org.functions.Application.Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class Functions {
    static JFrame gui;
    public static Functions instance;
    static boolean bukkit = false;
    static boolean spigot = false;
    static boolean paper = false;
    static boolean bungee = false;

    public static void isBukkit() {
        try {
            Class.forName("org.bukkit.craftbukkit.Main");
            bukkit = true;
        } catch (ClassNotFoundException e) {
            e.fillInStackTrace();
        }
    }
    public static void isSpigot() {
        try {
            Class.forName("org.spigotmc.SpigotConfig");
            spigot = true;
        } catch (ClassNotFoundException e) {
            e.fillInStackTrace();
        }
    }
    public static void isPaper() {
        try {
            Class.forName("com.destroystokyo.paper.PaperConfig");
            paper = true;
        } catch (ClassNotFoundException e) {
            e.fillInStackTrace();
        }
    }
    public static void isBungeeCord() {
        try {
            Class.forName("net.md_5.bungee.BungeeCord");
            bungee = true;
        } catch (ClassNotFoundException e) {
            e.fillInStackTrace();
        }
    }
    public static void run() {
        is();
        gui = new JFrame("Functions application");
        gui.setTitle("Functions Application");
        //gui.setUndecorated(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        gui.setBounds(screenSize.width/4,screenSize.height/4,screenSize.width/2,screenSize.height/2);
        gui.setBackground(Color.CYAN);
        Image image = new ImageIcon(Objects.requireNonNull(Functions.instance.getClass().getResource("/Icon.png"))).getImage();
        gui.setIconImage(image);
        //gui.setBackground(new Color(gui.getBackground().getRed(),gui.getBackground().getGreen(),gui.getBackground().getBlue(),100));
        gui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        if (bukkit) {
            gui.setTitle("Functions Bukkit Application");
        } else if (spigot) {
            gui.setTitle("Functions Spigot Application");
        } else if (paper) {
            gui.setTitle("Functions Paper Application");
        } else if (bungee) {
            gui.setTitle("Functions Bungee Application");
        }
        text.setBounds(50,50,50,50);
        gui.add(text);
        text.setText("Hello world");
        text.paintImmediately(text.getBounds());
        gui.setVisible(true);
    }
    static JTextArea text = new JTextArea();
    public static void change(String tps) {
        if (gui.getTitle().contains("Paper")) {
            text.setText(tps);
            text.paintImmediately(text.getBounds());
        }
    }
    public static void is() {
        isBukkit();
        isPaper();
        isBungeeCord();
        isSpigot();
        if (spigot) {
            bukkit = false;
        }
        if (paper) {
            spigot = false;
        }
    }
    public static void main(String[] args) /*throws IOException*/ {
        instance = new Functions();
        run();
    }
    public Functions getInstance() {
        return instance;
    }
}
