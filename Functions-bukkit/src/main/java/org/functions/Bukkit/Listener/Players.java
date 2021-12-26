package org.functions.Bukkit.Listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerListPingEvent;
import org.functions.Bukkit.API.ClickPerSeconds;
import org.functions.Bukkit.API.Event.FAsyncPlayerChatEvent;
import org.functions.Bukkit.API.FPI;
import org.functions.Bukkit.API.WorldBlock;
import org.functions.Bukkit.Main.*;
import org.functions.Bukkit.Main.functions.*;

import java.util.Collection;

public class Players implements Listener {
    FPI fpi = Functions.instance.getAPI();
    Account account;
    /** Bugs fix **/
    @EventHandler
    public void dispenser(BlockDispenseEvent b) {
        if ((b.getBlock().getLocation().getY() == (double)(b.getBlock().getWorld().getMaxHeight() - 1) || b.getBlock().getLocation().getY() == 0.0D || b.getBlock().getLocation().getY() == -64.0D) && b.getItem().getType().name().endsWith("SHULKER_BOX")) {
            b.setCancelled(true);
            Location loc = b.getBlock().getLocation();
            Collection<Entity> entities = loc.getWorld().getNearbyEntities(loc,256,256,256);
            Player player = null;
            for (Entity e : entities) {
                if (!(e instanceof Player)) {
                    continue;
                }
                player = (Player)e;
            }
            assert player != null;
            fpi.sendOperators("§c[ERROR] One of the players tried to crash the server! Player name: " + player.getName() + " Position: " + fpi.BlockLocationToString(loc));
            //fpi.sendConsole("§c[ERROR] One of the players tried to crash the server! Player name: " + player.getName() + " Position: " + api.changeLocationToString(loc));
        }
    }
    /** Player event **/
    @EventHandler(priority = EventPriority.HIGHEST)
    public void Command(PlayerCommandPreprocessEvent event) {
        Player p = event.getPlayer();
        account = new Account(p.getUniqueId());
        String cmd = event.getMessage().split(" ")[0].substring(1);
        AllowCommand login = new AllowCommand("NotLogin");
        if (!account.isLogin()) {
            if (login.cmd(cmd)) {
                p.sendMessage(fpi.onDisallowCommand(cmd));
                event.setCancelled(true);
            }
        } else {
            AllowCommand a = new AllowCommand("BeLogin");
            if (a.cmd(cmd)) {
                p.sendMessage(fpi.onDisallowCommand(cmd));
                event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void chat(AsyncPlayerChatEvent event) {
        FAsyncPlayerChatEvent fevent = new FAsyncPlayerChatEvent(event.getPlayer(),event.getMessage());
        event.setCancelled(true);
        event.setFormat("");
        event.setMessage("");
        chat(fevent);
    }
    public void chat(FAsyncPlayerChatEvent event) {
        Player p = event.getPlayer();
        account = new Account(p.getUniqueId());
        if (account.isLogin()) {
            p.getServer().getConsoleSender().sendMessage(event.getFormat());
            p.getServer().getOnlinePlayers().forEach(ps->{ps.sendMessage(event.getFormat());});
        } else {
            p.sendMessage(Functions.instance.getAPI().putLanguage("NoLoginChat","&c请登录再发言！",p));
        }
    }

    @EventHandler
    public void join(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        if (!Functions.instance.getPlayerManager().exists(p.getUniqueId())) Functions.instance.getPlayerManager().run();
        event.setJoinMessage(Functions.instance.getAPI().replace(Functions.instance.getPlayerManager().getUser(p.getUniqueId()).getGroup().getJoin(),p));
        if (fpi.cps.get(p.getUniqueId())==null) fpi.cps.put(p.getUniqueId(), new ClickPerSeconds(p.getUniqueId()));
        Functions.instance.print("Player: " + p.getName() + " Join the server. (Address: " + fpi.getPlayerAddress(p.getUniqueId()) + ")");
        account = Functions.instance.getPlayerManager().getUser(p.getUniqueId()).getAccount();
        if (account.exists()) {
            account.teleportSpawn();
            Accounts.login.put(p.getUniqueId(),false);
            if (account.autoLogin()) {
                p.sendMessage(fpi.putLanguage("AutoLogin","&a成功自动登陆！",p));
                Functions.instance.print("Player: " + p.getName() + " Auto login.");
            }
        }
        if (p.isOp()) {
            new Updater().test(p);
        }
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void move(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        account = new Account(p.getUniqueId());
        if (!account.isLogin()) {
            event.setTo(new Location(event.getFrom().getWorld(), event.getFrom().getX(),event.getFrom().getY(),event.getFrom().getZ(),event.getTo().getYaw(),event.getTo().getPitch()));
            return;
            //event.setTo(event.getFrom());
        }
        if (fpi.getRules().isEnabled(FunctionsRules.Type.FALL)) {
            if (event.getTo().getY() < -64) {
                FPI.fall.put(p.getUniqueId(), true);
                event.getPlayer().teleport(new Location(event.getTo().getWorld(), event.getTo().getX(), event.getPlayer().getWorld().getMaxHeight(), event.getTo().getZ(),event.getTo().getYaw(),event.getTo().getPitch()));
            }
        }
    }
    @EventHandler
    public void damage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            account = new Account(event.getEntity().getUniqueId());
            if (!account.isLogin()) {
                event.setCancelled(true);
            }
            Player e = ((Player) entity).getPlayer();
            if (fpi.getRules().isEnabled(FunctionsRules.Type.FALL)) {
                if (FPI.fall.get(e.getUniqueId()) != null) {
                    if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                        event.setCancelled(true);
                        FPI.fall.remove(e.getUniqueId());
                    }
                }
            }
        }
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void interact(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        account = new Account(p.getUniqueId());
        if (!account.isLogin()) {
            event.setCancelled(true);
        } else {
            if (fpi.cps.get(p.getUniqueId())==null) fpi.cps.put(p.getUniqueId(), new ClickPerSeconds(p.getUniqueId()));
            fpi.cps.get(p.getUniqueId()).countCPS();
            //event.getPlayer().sendMessage(fpi.cps.get(p.getUniqueId()).getCountCPS()+"");
        }

    }
    @EventHandler
    public void leave(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        event.setQuitMessage(Functions.instance.getAPI().replace(Functions.instance.getPlayerManager().getUser(p.getUniqueId()).getGroup().getQuit(),p));
        account = new Account(p.getUniqueId());
        if (account.exists() || account.isLogin()) {
            Accounts.login.remove(p.getUniqueId());
            if (fpi.cps.get(p.getUniqueId())!=null) fpi.cps.remove(p.getUniqueId());
            account.setPosition();
            account.teleportSpawn();
            account.setGameMode();
        }
    }
    @EventHandler
    public void title(ServerListPingEvent event) {

    }
}
