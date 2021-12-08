package org.functions.Bukkit.API.Event;

import java.util.IllegalFormatException;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.functions.Bukkit.Main.Functions;
import org.functions.Bukkit.Main.functions.User;

public class FAsyncPlayerChatEvent implements Cancellable {
    Player p;
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;
    private final String message;
    private String format = "<%1$s> %2$s";
    User user;
    public FAsyncPlayerChatEvent(Player who, String message) {
        p = who;
        user = new User(who.getUniqueId());
        this.message = message;
        format = user.getGroup().getChat();
    }
    public Player getPlayer() {
        return p;
    }
    public String getMessage() {
        return message;
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public String getFormat() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            format = format.replace("%message%",message);
            if (message.contains(user.getGroup().atPlayer().replace("%player%",p.getName()))) {
                format = format.replace("@" + p.getName(),user.getGroup().atPlayer().replace("%player%",p.getName()));
            } else if (message.contains("@" + p.getName())) {
                format = format.replace("@" + p.getName(),user.getGroup().atPlayer().replace("%player%",p.getName()));
            } else if (message.contains(p.getName())) {
                    format = format.replace(p.getName(),user.getGroup().atPlayer().replace("%player%",p.getName()));
                }
            }
        return Functions.instance.getAPI().replace(format,p);
    }
}
