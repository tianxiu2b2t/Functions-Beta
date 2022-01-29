package org.functions.Bukkit.Listener;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerListPingEvent;
import org.functions.Bukkit.API.ClickPerSeconds;
import org.functions.Bukkit.API.Event.FAsyncPlayerChatEvent;
import org.functions.Bukkit.API.FPI;
import org.functions.Bukkit.API.SpeedPerSeconds;
import org.functions.Bukkit.API.WorldBlock;
import org.functions.Bukkit.Main.*;
import org.functions.Bukkit.Main.Server.FList;
import org.functions.Bukkit.Main.functions.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class Players implements Listener {
    FPI fpi = Functions.instance.getAPI();
    Account account;
    /** Bugs fix **/
    @EventHandler
    public void dispenser(BlockDispenseEvent b) {
        if ((b.getBlock().getLocation().getY() == (double)(b.getBlock().getWorld().getMaxHeight() - 1) || b.getBlock().getLocation().getY() == 0.0D || b.getBlock().getLocation().getY() == -64.0D) && b.getItem().getType().name().endsWith("SHULKER_BOX")) {
            b.setCancelled(true);
            Location loc = b.getBlock().getLocation();
            Collection<Entity> entities = loc.getWorld().getNearbyEntities(loc, 256, 256, 256);
            Player player = null;
            for (Entity e : entities) {
                if (!(e instanceof Player)) {
                    continue;
                }
                player = (Player) e;
            }
            if (player != null) {
                fpi.sendOperators("§c[ERROR] One of the players tried to crash the server! Player name: " + player.getName() + " Position: " + fpi.BlockLocationToString(loc));
                fpi.getInstance().print("§c[ERROR] One of the players tried to crash the server! Player name: " + player.getName() + " Position: " + fpi.BlockLocationToString(loc));
            }
        }
    }
    /** Player event **/
    @EventHandler(priority = EventPriority.HIGHEST)
    public void Command(PlayerCommandPreprocessEvent event) {
        Player p = event.getPlayer();
        Functions.instance.print(event.getPlayer().getName() + "(" + p.getUniqueId().toString() + ") issued server command: " + event.getMessage());
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
        String message = event.getMessage();
        User user = Functions.instance.getPlayerManager().getUser(event.getPlayer().getUniqueId());
        String format = user.getGroup().getChat();
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
        }
        //Functions.instance.getAPI().replace(format.replace("%message%",message),event.getPlayer());
        event.setFormat(Functions.instance.getAPI().replace(format.replace("%message%",message),event.getPlayer()));
        //event.setMessage("");
        //chat(fevent);
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
        Functions.instance.getPlayerManager().run();
        if (!Functions.instance.getPlayerManager().exists(p.getUniqueId())) Functions.instance.getPlayerManager().run();
        event.setJoinMessage(Functions.instance.getAPI().replace(Functions.instance.getPlayerManager().getUser(p.getUniqueId()).getGroup().getJoin(),p));
        if (fpi.cps.get(p.getUniqueId())==null) fpi.cps.put(p.getUniqueId(), new ClickPerSeconds(p.getUniqueId()));
        Functions.instance.print("Player: " + p.getName() + " Join the server. (Address: " + fpi.getPlayerAddress(p.getUniqueId()) + ")");
        account = Functions.instance.getPlayerManager().getUser(p.getUniqueId()).getAccount();
        if (Accounts.enable()) {
            if (account.exists()) {
                account.teleportSpawn();
                Accounts.login.put(p.getUniqueId(), false);
                if (account.autoLogin()) {
                    p.sendMessage(fpi.putLanguage("AutoLogin", "&a成功自动登陆！", p));
                    Functions.instance.print("Player: " + p.getName() + " Auto login.");
                } else {
                    Functions.instance.getServer().getScheduler().runTaskTimerAsynchronously(Functions.instance,new Runnable() {
                        public void run() throws NullPointerException {
                            if (Accounts.enable()) {
                                    if (!(new Account(p.getUniqueId()).exists())) {
                                        p.sendMessage(fpi.putLanguage("RegisterAccount", "&c请使用/register <密码> <重复密码> 来注册账号！",p));
                                    }
                                    if (Accounts.getAccount(p.getUniqueId()).exists()) {
                                        if (!Accounts.getAccount(p.getUniqueId()).isLogin()) {
                                            Accounts.login.put(p.getUniqueId(), false);
                                        }
                                    }
                                    if (!Accounts.getAccount(p.getUniqueId()).isLogin()) {
                                        Accounts.login.put(p.getUniqueId(), false);
                                    }
                                    if (!Accounts.login.get(p.getUniqueId())) {
                                        for (Account account : Accounts.getAccounts()) {
                                            if (account.getUniqueID().equals(p.getUniqueId())) {
                                                if (account.isLogin()) {
                                                    continue;
                                                }
                                                p.sendMessage(fpi.putLanguage("LoginAccount", "&c请使用/login <密码> 或者使用/mailogin 来登录",p));
                                            }
                                        }
                                    }
                            }
                        }
                    },0,20 * Functions.instance.getConfig().getLong("Functions.RegisterLoginMessageInterval",5));
                }
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
            event.setTo(new Location(event.getFrom().getWorld(), event.getFrom().getX(), event.getFrom().getY(), event.getFrom().getZ(), event.getTo().getYaw(), event.getTo().getPitch()));
            return;
            //event.setTo(event.getFrom());
        }
        if (fpi.getRules().isEnabled(FunctionsRules.Type.FALL)) {
            if (event.getTo().getY() < -64) {
                FPI.fall.put(p.getUniqueId(), true);
                event.getPlayer().teleport(new Location(event.getTo().getWorld(), event.getTo().getX(), event.getPlayer().getWorld().getMaxHeight(), event.getTo().getZ(), event.getTo().getYaw(), event.getTo().getPitch()));
            }
        }
        //if (fpi.sps.get(p.getUniqueId())==null) fpi.sps.put(event.getPlayer().getUniqueId(),new SpeedPerSeconds(event.getPlayer().getUniqueId()));
        //fpi.sps.get(event.getPlayer().getUniqueId()).count();
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
        Functions.instance.getPlayerManager().run();
        Player p = event.getPlayer();
        if (!Objects.equals(event.getQuitMessage(), "") || event.getQuitMessage() != null) event.setQuitMessage(Functions.instance.getAPI().replace(Functions.instance.getPlayerManager().getUser(p.getUniqueId()).getGroup().getQuit(),p));
        account = new Account(p.getUniqueId());
        if (Accounts.enable()) {
            if (account.exists() || account.isLogin()) {
                Accounts.login.remove(p.getUniqueId());
                if (fpi.cps.get(p.getUniqueId()) != null) fpi.cps.remove(p.getUniqueId());
                //if (fpi.sps.get(p.getUniqueId())==null) fpi.sps.remove(event.getPlayer().getUniqueId());

                account.setPosition();
                account.setGameMode();
            }
        }
    }
    @EventHandler
    public void title(ServerListPingEvent event) {

    }
    @EventHandler
    public void death(PlayerDeathEvent event) {
        if (event.getEntity() != null) {
            Player p = event.getEntity();
            Back.setDeathPos(p.getUniqueId(), p.getLocation());
            event.setKeepInventory(Functions.instance.getConfiguration().getSettings().getBoolean("Death.Keep", false));
            event.setKeepLevel(Functions.instance.getConfiguration().getSettings().getBoolean("Death.Keep", false));
            if (Functions.instance.getConfiguration().getSettings().getBoolean("Death.Keep", false))
                event.setDroppedExp(0);
            Functions.instance.print("The player: " + p.getName() + " death is keep inventory and level");
        }
    }

    @EventHandler
    public void EntityExplode(EntityExplodeEvent event) {
        Entity entity = event.getEntity();
        List<String> list = new ArrayList<>();
        list.add("creeper");
        list.add("ender_dragon");
        list.add("fireball");
        list.add("small_fireball");
        list.add("wither");
        list.add("wither_skull");
        list.add("minecart_tnt");
        list.add("primed_tnt");
        if (Functions.instance.getConfiguration().getSettings().getStringList("EntityExplode.List").size() != 0) list = Functions.instance.getConfiguration().getSettings().getStringList("EntityExplode.List");
        if (!(entity instanceof Player)) {
            list.forEach(type->{
                if (entity.getType().name().toLowerCase().equalsIgnoreCase(type.toLowerCase())) {
                    event.setCancelled(true);
                    event.setYield(0);
                    Functions.instance.print("The entity: " + entity.getName() + " cancelled explosion");
                    return;
                }
                if (entity.getType().name().toLowerCase().contains(type.toLowerCase())) {
                    event.setCancelled(true);
                    event.setYield(0);
                    Functions.instance.print("Fuzzy entity: " + entity.getName() + " cancelled explosion");
                }
            });
        }
    }
    @EventHandler
    public void onServerStop(org.bukkit.event.server.PluginDisableEvent event) {
        if (event.getPlugin().getDescription().getName().equalsIgnoreCase("Functions")) {
            Functions.instance.print("Now execute all player quit event.");
            Functions.instance.onDisableSettings();
            Functions.instance.print("All right. Normal Stop Server.");
        }
    }
    @EventHandler
    public void onChangeGameMode(PlayerGameModeChangeEvent event) {
        if (event.getNewGameMode().equals(GameMode.CREATIVE)) {
            if (!PermissionsUtils.hasPermissionsSendMessage(event.getPlayer(),"functions.event.gamemode.change.creative")) {
                event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult().equals(AsyncPlayerPreLoginEvent.Result.ALLOWED)) {
            if (!FList.IsPlayerCanJoin(event.getName())) {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "§aYou is banned or not in white list can join this server!");
            }
        }
    }
}
