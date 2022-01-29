package org.functions.Bukkit.Main.functions.Utitils;

import org.bukkit.entity.Player;
import org.functions.Bukkit.Main.Functions;

import java.lang.reflect.Constructor;
import java.util.UUID;

public class ActionBar {
    public static void send(Player player, Object message) {
        try {
            String msg = Functions.instance.getAPI().replace(message, player);
            Object iChat = Functions.instance.getClassesStorage().getAsIChatBaseComponent(msg);
            Object packet = null;
            Constructor<?> chat = Functions.instance.getClassesStorage().PacketPlayOutChat;
            if (ProtocolVersion.getServerVersion().getMinorVersion() >= 17) {
                packet = chat.newInstance(iChat,Functions.instance.getClassesStorage().ChatMessageType.getConstructor(Byte.class,Boolean.class).newInstance((byte)Functions.instance.getClassesStorage().getChatEnumType("c"),true),player.getUniqueId());
            } else if (ProtocolVersion.getServerVersion().getMinorVersion() >= 16) {
                packet = chat.newInstance(iChat,Functions.instance.getClassesStorage().getChatEnumType("GAME_INFO"),player.getUniqueId());
            } else if (ProtocolVersion.getServerVersion().getMinorVersion() >= 12) {
                packet = chat.newInstance(iChat,Functions.instance.getClassesStorage().getChatEnumType("GAME_INFO"));
            } else if (ProtocolVersion.getServerVersion().getMinorVersion() >= 8) {
                packet = chat.newInstance(iChat, 2);
            } else if (ProtocolVersion.getServerVersion().getMinorVersion() >= 7) {
                packet = chat.newInstance(iChat);
            }
            Functions.instance.getClassesStorage().sendPacket(player,packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
