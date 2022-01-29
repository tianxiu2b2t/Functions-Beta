package org.functions.Bukkit.Main.functions.Utitils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.functions.Bukkit.Main.Functions;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tab {
    public static String colorMsg(String msg) {
        if (msg == null) {
            return "";
        } else {
            if (ProtocolVersion.getServerVersion().getMinorVersion() >= 16 && msg.contains("#")) {
                msg = matchColorRegex(msg);
            }

            return ChatColor.translateAlternateColorCodes('&', msg);
        }
    }

    public static String matchColorRegex(String s) {
        String regex = "#[a-fA-F0-9]{6}";
        Matcher matcher = Pattern.compile(regex).matcher(s);

        while (matcher.find()) {
            String group = matcher.group(0);

            try {
                s = s.replace(group, ChatColor.valueOf(group) + "");
            } catch (Exception var5) {
                Functions.instance.print("The hex is a bad:" + group, "&c[ERROR] ");
            }
        }

        return s;
    }
    public static void send(Player player,String header,String footer) {
        header = Functions.instance.getAPI().replace(header,player);
        footer = Functions.instance.getAPI().replace(footer,player);
        try {
            Functions.instance.getClassesStorage().init();
            if (header == null) {
                header = "";
            }

            if (footer == null) {
                footer = "";
            }

            if (ProtocolVersion.getServerVersion().getMinorVersion() >= 15) {
                header = colorMsg(header);
                footer = colorMsg(footer);
            }
            Object packet = Functions.instance.getClassesStorage().PacketPlayOutPlayerListHeaderFooter.getConstructor().newInstance();
            Object tabHeader = Functions.instance.getClassesStorage().getAsIChatBaseComponent(header);
            Object tabFooter = Functions.instance.getClassesStorage().getAsIChatBaseComponent(footer);
            Functions.instance.getClassesStorage().setField(packet, Functions.instance.getClassesStorage().PacketPlayOutPlayerListHeaderFooter_HEADER, tabHeader);
            Functions.instance.getClassesStorage().setField(packet, Functions.instance.getClassesStorage().PacketPlayOutPlayerListHeaderFooter_FOOTER, tabFooter);
            Functions.instance.getClassesStorage().sendPacket(player,packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
