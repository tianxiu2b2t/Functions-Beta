package org.functions.Bukkit.API.Event;

import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.functions.Bukkit.Main.Functions;
import org.functions.Bukkit.Main.functions.User;

public class FAsyncPlayerChatEvent {
    Player p;
    private String message;
    private String format = "<%1$s> %2$s";
    User user;
    public FAsyncPlayerChatEvent(Player who, String message) {
        p = who;
        user = Functions.instance.getPlayerManager().getUser(p.getUniqueId());
        this.message = message;
        format = user.getGroup().getChat();
    }
    public Player getPlayer() {
        return p;
    }
    public String getMessage() {
        return message;
    }

    public String getFormat() {
        //List<String> name = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (message.contains("@everyone") || message.contains(user.getGroup().atPlayer().replace("%player%", "everyone"))) {
                message = message.replace("@" + "everyone",user.getGroup().atPlayer().replace("%player%","everyone"));
            } else if (!message.contains("@" + p.getName()) && !message.contains("@ " + p.getName())) {
                if (message.contains(p.getName())) {
                    message = message.replace(p.getName(), user.getGroup().atPlayer().replace("%player%", p.getName()));
                }
            } else {
                message = message.replace("@" + p.getName(), user.getGroup().atPlayer().replace("%player%", p.getName())).replace("@ " + p.getName(), user.getGroup().atPlayer().replace("%target%", p.getName()));
            }
            //name.add(p.getName());
        }
        return Functions.instance.getAPI().replace(format.replace("%message%",message),p);
    }
    public String getRawFormat() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (message.contains(user.getGroup().atPlayer().replace("%player%",p.getName()))) {
                message = message.replace("@" + p.getName(),user.getGroup().atPlayer().replace("%player%",p.getName()));
            } else if (message.contains("@" + p.getName())) {
                message = message.replace("@" + p.getName(),user.getGroup().atPlayer().replace("%player%",p.getName()));
            } else if (message.contains(p.getName())) {
                message = message.replace(p.getName(),user.getGroup().atPlayer().replace("%player%",p.getName()));
            }
        }
        return "{\"text\":\"" + format.replace("%player_display%",Functions.instance.getAPI().replaceJson(user.getJsonChatDisplayName())).replace("%message%",Functions.instance.getAPI().replace(message,p)) + "\"}";
    }
    public String getRawChat() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (message.contains(user.getGroup().atPlayer().replace("%player%",p.getName()))) {
                message = message.replace("@" + p.getName(),user.getGroup().atPlayer().replace("%player%",p.getName()));
            } else if (message.contains("@" + p.getName())) {
                message = message.replace("@" + p.getName(),user.getGroup().atPlayer().replace("%player%",p.getName()));
            } else if (message.contains(p.getName())) {
                message = message.replace(p.getName(),user.getGroup().atPlayer().replace("%player%",p.getName()));
            }
        }
        return "{\"text\":\"" + Functions.instance.getAPI().replace(format.replace("%message%",message),p) + "\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"" + Functions.instance.getAPI().replaceJson(user.getJsonChatDisplayName()) +"\"}}";
    }
}
