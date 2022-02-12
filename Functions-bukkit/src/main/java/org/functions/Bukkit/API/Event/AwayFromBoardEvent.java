package org.functions.Bukkit.API.Event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.functions.Bukkit.Main.Functions;
import org.functions.Bukkit.Main.functions.User;

public class AwayFromBoardEvent extends PlayerEvent implements Cancellable {
    private final HandlerList handlerList = new HandlerList();
    private boolean cancel = false;
    private User user = null;
    private final boolean afk;
    public AwayFromBoardEvent(Player player) {
        super(player);
        user = Functions.instance.getPlayerManager().getUser(player.getUniqueId());
        afk = !user.isAFK();
    }
    public HandlerList getHandlers() {
        return handlerList;
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public boolean isAFK() {
        return afk;
    }

    public User getUser() {
        return user;
    }
}
