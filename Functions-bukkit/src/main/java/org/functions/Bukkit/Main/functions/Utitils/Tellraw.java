package org.functions.Bukkit.Main.functions.Utitils;

import org.bukkit.entity.Player;
import org.functions.Bukkit.Main.Functions;

public class Tellraw {
    public static void send(Player player,String text) {
        try {
            Functions.instance.getClassesStorage().sendPacket(player,Functions.instance.getClassesStorage().PacketPlayOutChatClass.getConstructor(Functions.instance.getClassesStorage().IChatBaseComponent).newInstance(Functions.instance.getClassesStorage().getAsIChatBaseComponent(text)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
