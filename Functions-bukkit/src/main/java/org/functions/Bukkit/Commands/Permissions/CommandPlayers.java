package org.functions.Bukkit.Commands.Permissions;

import com.google.common.base.Preconditions;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.functions.Bukkit.API.FPI;
import org.functions.Bukkit.API.WorldBlock;
import org.functions.Bukkit.Main.Functions;
import org.functions.Bukkit.Main.functions.*;

import java.util.*;

public class CommandPlayers implements TabExecutor {
    public void run() {
        Functions.instance.getAPI().getCommand(new String[]{"fly","spawn","show","invisibility","warp","ping","home"}, new CommandPlayers());
    }
    FPI fpi = Functions.instance.getAPI();

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (fpi.hasAliases("fly",label)) {
            if (sender instanceof Player) {
                Player p = (Player)sender;
                if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.fly")) {
                    return true;
                }
                p.setAllowFlight(!p.getAllowFlight());
                sender.sendMessage(fpi.putLanguage("PlayerFly","%player% 的飞行模式为 %enable%.",p,new Object[]{"player",p.getName(),"enable",fpi.changeBooleanToText(p.getAllowFlight())}));
                return true;
            }
            sender.sendMessage(fpi.noPlayer());
        } else if (fpi.hasAliases("spawn",label)) {
            if (sender instanceof Player) {
                Player p = (Player)sender;
                if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.spawn")) {
                    return true;
                }
                Location position = p.getWorld().getSpawnLocation();
                WorldBlock worldBlock = new WorldBlock(position);
                position.setY(worldBlock.onGroundY());
                p.teleport(position);
                sender.sendMessage(fpi.putLanguage("PlayerTeleportToSpawn","%player% 已传送至 %position%.",p,new Object[]{"player",p.getName(),"position",fpi.changeLocationToString(position)}));
                return true;
            }
            sender.sendMessage(fpi.noPlayer());
        } else if (fpi.hasAliases("invisibility",label)) {
            if (args.length == 0) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (!PermissionsUtils.hasPermissionsSendMessage(sender, "functions.permissions.command.invisibility")) {
                        return true;
                    }
                    IUser user = Functions.instance.getPlayerManager().getUser(p.getUniqueId());
                    user.setInvisibility(true);
                    sender.sendMessage(fpi.putLanguage("PlayerIsHide", "%player% 隐身为 %enable%.", p, new Object[]{"player", p.getName(), "enable", fpi.changeBooleanToText(user.isHiding())}));
                    return true;
                }
            }
            if (!PermissionsUtils.hasPermissionsSendMessage(sender, "functions.permissions.command.invisibility")) {
                return true;
            }
            User user = Functions.instance.getPlayerManager().getUser(args[0],true);
            if (user == null) {
                sender.sendMessage(Functions.instance.getPlayerManager().NotFound(args[0]));
                return true;
            }
            user.setInvisibility(true);
            sender.sendMessage(fpi.putLanguage("PlayerIsHide", "%player% 隐身为 %enable%.", null, new Object[]{"player", user.getOfflinePlayer().getName(), "enable", fpi.changeBooleanToText(user.isHiding())}));
            return true;
        } else if (fpi.hasAliases("show",label)) {
            if (args.length == 0) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (!PermissionsUtils.hasPermissionsSendMessage(sender, "functions.permissions.command.invisibility")) {
                        return true;
                    }
                    IUser user = Functions.instance.getPlayerManager().getUser(p.getUniqueId());
                    user.setInvisibility(false);
                    sender.sendMessage(fpi.putLanguage("PlayerIsHide", "%player% 隐身为 %enable%.", p, new Object[]{"player", p.getName(), "enable", fpi.changeBooleanToText(user.isHiding())}));
                    return true;
                }
            }
            if (!PermissionsUtils.hasPermissionsSendMessage(sender, "functions.permissions.command.invisibility")) {
                return true;
            }
            User user = Functions.instance.getPlayerManager().getUser(args[0],true);
            if (user == null) {
                sender.sendMessage(Functions.instance.getPlayerManager().NotFound(args[0]));
                return true;
            }
            user.setInvisibility(false);
            sender.sendMessage(fpi.putLanguage("PlayerIsHide", "%player% 隐身为 %enable%.", null, new Object[]{"player", user.getOfflinePlayer().getName(), "enable", fpi.changeBooleanToText(user.isHiding())}));
            return true;
        } else if (fpi.hasAliases("ping",label)) {
            if (sender instanceof Player) {
                Player p = (Player)sender;
                if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.ping")) {
                    return true;
                }
                sender.sendMessage(fpi.putLanguage("PlayerPing","%player% 的延迟为 %ping%ms",p,new Object[]{"player",p.getName(),"ping", new Utils.Ping(p).toString()}));
                return true;
            }
            sender.sendMessage(fpi.noPlayer());
            return true;
        } else if (fpi.hasAliases("warp",label)) {
            if (sender instanceof Player) {
                if (args.length == 0) {
                    sender.sendMessage(fpi.subcmd());
                    return true;
                }
                if (args[0].equalsIgnoreCase("add")) {
                    if (!PermissionsUtils.hasPermissionsSendMessage(sender, "functions.permissions.command.warp.add")) {
                        return true;
                    }
                    if (args.length == 1) {
                        sender.sendMessage(fpi.subcmd());
                        return true;
                    }
                    if (Warp.isWarp(args[1])) {
                        sender.sendMessage(fpi.putLanguage("WarpIsExists", "&c地标 %warp% 已存在!", null, new Object[]{"warp", Warp.getWarp(args[1])}));
                        return true;
                    }
                    if (args.length == 2) {
                        Warp.setWarpPos(args[1], ((Player) sender).getLocation());
                        sender.sendMessage(fpi.putLanguage("WarpSetYourPosition", "&a地标 %warp% 已成功设置成你的脚下(%position%)", null, new Object[]{"warp", args[1], "position", Warp.getWarpPosString(args[1])}));
                        return true;
                    }
                    if (args.length == 5) {
                        Location pos = ((Player) sender).getLocation();
                        double x = pos.getX();
                        double y = pos.getY();
                        double z = pos.getZ();
                        try {
                            x = Double.parseDouble(args[2]);
                            y = Double.parseDouble(args[3]);
                            z = Double.parseDouble(args[4]);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        Warp.setWarpPos(args[1], ((Player) sender).getLocation().getWorld(), x, y, z);
                        sender.sendMessage(fpi.putLanguage("WarpSetYourPrintPosition", "&a地标 %warp% 已成功设置成你输入的坐标(%position%)", null, new Object[]{"warp", args[1], "position", Warp.getWarpPosString(args[1])}));
                        return true;
                    }
                    if (args.length == 8) {
                        Location pos = ((Player) sender).getLocation();
                        World world = pos.getWorld();
                        double x = pos.getX();
                        double y = pos.getY();
                        double z = pos.getZ();
                        float yaw = pos.getYaw();
                        float pitch = pos.getPitch();
                        try {
                            x = Double.parseDouble(args[3]);
                            y = Double.parseDouble(args[4]);
                            z = Double.parseDouble(args[5]);
                            yaw = Float.parseFloat(args[6]);
                            pitch = Float.parseFloat(args[7]);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        for (World w : fpi.getServer().getWorlds()) {
                            if (w.getName().equalsIgnoreCase(args[2])) {
                                world = w;
                            }
                        }
                        Warp.setWarpPos(args[1], world, x, y, z, yaw, pitch);
                        sender.sendMessage(fpi.putLanguage("WarpSetYourPrintPosition", "&a地标 %warp% 已成功设置成你输入的坐标(%position%)", null, new Object[]{"warp", args[1], "position", Warp.getWarpPosString(args[1])}));
                        return true;
                    }
                    return true;
                } else if ("remove".equalsIgnoreCase(args[0])) {
                    if (!PermissionsUtils.hasPermissionsSendMessage(sender, "functions.permissions.command.warp.remove")) {
                        return true;
                    }
                    if (args.length == 1) {
                        sender.sendMessage(fpi.subcmd());
                        return true;
                    }
                    if (!Warp.isWarp(args[1])) {
                        sender.sendMessage(fpi.putLanguage("WarpIsNotExists", "&c地标 %warp% 不存在!", null, new Object[]{"warp", Warp.getWarp(args[1])}));
                        return true;
                    }
                    String location = Warp.getWarpPosString(args[1]);
                    Warp.removeWarp(args[1]);
                    sender.sendMessage(fpi.putLanguage("WarpIsRemoved","&a地标 %warp%(%position%) 已成功移除",null,new Object[]{"warp",args[1],"position",location}));
                    return true;
                } else if ("change".equalsIgnoreCase(args[0])) {
                    if (!PermissionsUtils.hasPermissionsSendMessage(sender, "functions.permissions.command.warp.change")) {
                        return true;
                    }
                    if (!Warp.isWarp(args[1])) {
                        sender.sendMessage(fpi.putLanguage("WarpIsNotExists", "&c地标 %warp% 不存在!", null, new Object[]{"warp", Warp.getWarp(args[1])}));
                        return true;
                    }
                    String location = Warp.getWarpPosString(args[1]);
                    Warp.removeWarp(args[1]);
                    sender.sendMessage(fpi.putLanguage("WarpIsRemoved","&a地标 %warp%(%position%) 已成功移除",null,new Object[]{"warp",args[1],"position",location}));
                    if (args.length == 2) {
                        Warp.setWarpPos(args[1], ((Player) sender).getLocation());
                        sender.sendMessage(fpi.putLanguage("WarpSetYourPosition", "&a地标 %warp% 已成功设置成你的脚下(%position%)", null, new Object[]{"warp", args[1], "position", Warp.getWarpPosString(args[1])}));
                        return true;
                    }
                    if (args.length == 5) {
                        Location pos = ((Player) sender).getLocation();
                        double x = pos.getX();
                        double y = pos.getY();
                        double z = pos.getZ();
                        try {
                            x = Double.parseDouble(args[2]);
                            y = Double.parseDouble(args[3]);
                            z = Double.parseDouble(args[4]);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        Warp.setWarpPos(args[1], ((Player) sender).getLocation().getWorld(), x, y, z);
                        sender.sendMessage(fpi.putLanguage("WarpSetYourPrintPosition", "&a地标 %warp% 已成功设置成你输入的坐标(%position%)", null, new Object[]{"warp", args[1], "position", Warp.getWarpPosString(args[1])}));
                        return true;
                    }
                    if (args.length == 8) {
                        Location pos = ((Player) sender).getLocation();
                        World world = pos.getWorld();
                        double x = pos.getX();
                        double y = pos.getY();
                        double z = pos.getZ();
                        float yaw = pos.getYaw();
                        float pitch = pos.getPitch();
                        try {
                            x = Double.parseDouble(args[3]);
                            y = Double.parseDouble(args[4]);
                            z = Double.parseDouble(args[5]);
                            yaw = Float.parseFloat(args[6]);
                            pitch = Float.parseFloat(args[7]);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        for (World w : fpi.getServer().getWorlds()) {
                            if (w.getName().equalsIgnoreCase(args[2])) {
                                world = w;
                            }
                        }
                        Warp.setWarpPos(args[1], world, x, y, z, yaw, pitch);
                        sender.sendMessage(fpi.putLanguage("WarpSetYourPrintPosition", "&a地标 %warp% 已成功设置成你输入的坐标(%position%)", null, new Object[]{"warp", args[1], "position", Warp.getWarpPosString(args[1])}));
                        return true;
                    }
                    return true;
                } else if ("list".equalsIgnoreCase(args[0])) {
                    if (!PermissionsUtils.hasPermissionsSendMessage(sender, "functions.permissions.command.warp.list")) {
                        return true;
                    }
                    String format = "%1$s(%2$s%3$s)";
                    StringBuilder list = new StringBuilder();
                    for (int i = 1; i <= Warp.getWarps().size();i++) {
                        String pos = Warp.getWarpPosString(Warp.getWarps().get(i-1));
                        if (i != Warp.getWarps().size()) {
                            list.append(String.format(format, Warp.getWarps().get(i-1), i,pos)).append(", ");
                            continue;
                        }
                        list.append(String.format(format, Warp.getWarps().get(i-1), i,pos));
                    }
                    sender.sendMessage(fpi.putLanguage("ListWarps","地标(%warp_size%): %warp_list%",null,new Object[]{"warp_size",Warp.getWarps().size(),"warp_list",list.toString()}));
                    return true;
                } else if ("teleport".equalsIgnoreCase(args[0])) {
                    if (!PermissionsUtils.hasPermissionsSendMessage(sender, "functions.permissions.command.warp.teleport")) {
                        return true;
                    }
                    if (!Warp.isWarp(args[1])) {
                        sender.sendMessage(fpi.putLanguage("WarpIsNotExists", "&c地标 %warp% 不存在!", null, new Object[]{"warp", Warp.getWarp(args[1])}));
                        return true;
                    }
                    ((Player) sender).teleport(Warp.getWarpPos(args[1]));
                    sender.sendMessage(fpi.putLanguage("TeleportedToWarp","&a已成功传送到 %warp%(%position%) 处！",null,new Object[]{"warp",args[1],"position",Warp.getWarpPosString(args[1])}));
                    return true;
                }
                if (!PermissionsUtils.hasPermissionsSendMessage(sender, "functions.permissions.command.warp.teleport")) {
                    return true;
                }
                if (!Warp.isWarp(args[0])) {
                    sender.sendMessage(fpi.putLanguage("WarpIsNotExists", "&c地标 %warp% 不存在!", null, new Object[]{"warp", Warp.getWarp(args[0])}));
                    return true;
                }
                ((Player) sender).teleport(Warp.getWarpPos(args[0]));
                sender.sendMessage(fpi.putLanguage("TeleportedToWarp","&a已成功传送到 %warp%(%position%) 处！",null,new Object[]{"warp",args[0],"position",Warp.getWarpPosString(args[0])}));
                return true;
            }
            sender.sendMessage(fpi.noPlayer());
            return true;
        } else if (fpi.hasAliases("home",label)) {
            if (sender instanceof Player) {
                if (args.length == 0) {
                    sender.sendMessage(fpi.subcmd());
                    return true;
                }
                UUID uuid = ((Player) sender).getUniqueId();
                if (args[0].equalsIgnoreCase("add")) {
                    if (!PermissionsUtils.hasPermissionsSendMessage(sender, "functions.permissions.command.home.add")) {
                        return true;
                    }
                    if (args.length == 1) {
                        sender.sendMessage(fpi.subcmd());
                        return true;
                    }
                    if (Home.isHome(uuid,args[1])) {
                        sender.sendMessage(fpi.putLanguage("HomeIsExists", "&c家 %home% 已存在!", null, new Object[]{"home", Home.getHome(uuid,args[1])}));
                        return true;
                    }
                    if (args.length == 2) {
                        Home.setHomePos(uuid,args[1], ((Player) sender).getLocation());
                        sender.sendMessage(fpi.putLanguage("HomeSetYourPosition", "&a家 %home% 已成功设置成你的脚下(%position%)", null, new Object[]{"home", args[1], "position", Home.getHomePosString(uuid,args[1])}));
                        return true;
                    }
                    if (args.length == 5) {
                        Location pos = ((Player) sender).getLocation();
                        double x = pos.getX();
                        double y = pos.getY();
                        double z = pos.getZ();
                        try {
                            x = Double.parseDouble(args[2]);
                            y = Double.parseDouble(args[3]);
                            z = Double.parseDouble(args[4]);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        Home.setHomePos(uuid,args[1], ((Player) sender).getLocation().getWorld(), x, y, z);
                        sender.sendMessage(fpi.putLanguage("HomeSetYourPrintPosition", "&a家 %home% 已成功设置成你输入的坐标(%position%)", null, new Object[]{"home", args[1], "position", Home.getHomePosString(uuid,args[1])}));
                        return true;
                    }
                    if (args.length == 8) {
                        Location pos = ((Player) sender).getLocation();
                        World world = pos.getWorld();
                        double x = pos.getX();
                        double y = pos.getY();
                        double z = pos.getZ();
                        float yaw = pos.getYaw();
                        float pitch = pos.getPitch();
                        try {
                            x = Double.parseDouble(args[3]);
                            y = Double.parseDouble(args[4]);
                            z = Double.parseDouble(args[5]);
                            yaw = Float.parseFloat(args[6]);
                            pitch = Float.parseFloat(args[7]);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        for (World w : fpi.getServer().getWorlds()) {
                            if (w.getName().equalsIgnoreCase(args[2])) {
                                world = w;
                            }
                        }
                        Home.setHomePos(uuid,args[1], world, x, y, z, yaw, pitch);
                        sender.sendMessage(fpi.putLanguage("HomeSetYourPrintPosition", "&a家 %home% 已成功设置成你输入的坐标(%position%)", null, new Object[]{"home", args[1], "position", Home.getHomePosString(uuid,args[1])}));
                        return true;
                    }
                    return true;
                } else if ("remove".equalsIgnoreCase(args[0])) {
                    if (!PermissionsUtils.hasPermissionsSendMessage(sender, "functions.permissions.command.home.remove")) {
                        return true;
                    }
                    if (args.length == 1) {
                        sender.sendMessage(fpi.subcmd());
                        return true;
                    }
                    if (!Home.isHome(uuid,args[1])) {
                        sender.sendMessage(fpi.putLanguage("HomeIsNotExists", "&c家 %home% 不存在!", null, new Object[]{"home", Home.getHome(uuid,args[1])}));
                        return true;
                    }
                    String location = Home.getHomePosString(uuid,args[1]);
                    Home.removeHome(uuid,args[1]);
                    sender.sendMessage(fpi.putLanguage("HomeIsRemoved","&a家 %home%(%position%) 已成功移除",null,new Object[]{"home",args[1],"position",location}));
                    return true;
                } else if ("change".equalsIgnoreCase(args[0])) {
                    if (!PermissionsUtils.hasPermissionsSendMessage(sender, "functions.permissions.command.home.changet")) {
                        return true;
                    }
                    if (args.length == 1) {
                        sender.sendMessage(fpi.subcmd());
                        return true;
                    }
                    if (!Home.isHome(uuid,args[1])) {
                        sender.sendMessage(fpi.putLanguage("HomeIsNotExists", "&c家 %home% 不存在!", null, new Object[]{"home", Home.getHome(uuid,args[1])}));
                        return true;
                    }
                    String location = Home.getHomePosString(uuid,args[1]);
                    Home.removeHome(uuid,args[1]);
                    sender.sendMessage(fpi.putLanguage("HomeIsRemoved","&a家 %home%(%position%) 已成功移除",null,new Object[]{"home",args[1],"position",location}));
                        if (Home.isHome(uuid,args[1])) {
                            sender.sendMessage(fpi.putLanguage("HomeIsExists", "&c家 %home% 已存在!", null, new Object[]{"home", Home.getHome(uuid,args[1])}));
                            return true;
                        }
                        if (args.length == 2) {
                            Home.setHomePos(uuid,args[1], ((Player) sender).getLocation());
                            sender.sendMessage(fpi.putLanguage("HomeSetYourPosition", "&a家 %home% 已成功设置成你的脚下(%position%)", null, new Object[]{"home", args[1], "position", Home.getHomePosString(uuid,args[1])}));
                            return true;
                        }
                        if (args.length == 5) {
                            Location pos = ((Player) sender).getLocation();
                            double x = pos.getX();
                            double y = pos.getY();
                            double z = pos.getZ();
                            try {
                                x = Double.parseDouble(args[2]);
                                y = Double.parseDouble(args[3]);
                                z = Double.parseDouble(args[4]);
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                            Home.setHomePos(uuid,args[1], ((Player) sender).getLocation().getWorld(), x, y, z);
                            sender.sendMessage(fpi.putLanguage("HomeSetYourPrintPosition", "&a家 %home% 已成功设置成你输入的坐标(%position%)", null, new Object[]{"home", args[1], "position", Home.getHomePosString(uuid,args[1])}));
                            return true;
                        }
                        if (args.length == 8) {
                            Location pos = ((Player) sender).getLocation();
                            World world = pos.getWorld();
                            double x = pos.getX();
                            double y = pos.getY();
                            double z = pos.getZ();
                            float yaw = pos.getYaw();
                            float pitch = pos.getPitch();
                            try {
                                x = Double.parseDouble(args[3]);
                                y = Double.parseDouble(args[4]);
                                z = Double.parseDouble(args[5]);
                                yaw = Float.parseFloat(args[6]);
                                pitch = Float.parseFloat(args[7]);
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                            for (World w : fpi.getServer().getWorlds()) {
                                if (w.getName().equalsIgnoreCase(args[2])) {
                                    world = w;
                                }
                            }
                            Home.setHomePos(uuid,args[1], world, x, y, z, yaw, pitch);
                            sender.sendMessage(fpi.putLanguage("HomeSetYourPrintPosition", "&a家 %home% 已成功设置成你输入的坐标(%position%)", null, new Object[]{"home", args[1], "position", Home.getHomePosString(uuid,args[1])}));
                            return true;
                        }
                        return true;
                } else if ("list".equalsIgnoreCase(args[0])) {
                    if (!PermissionsUtils.hasPermissionsSendMessage(sender, "functions.permissions.command.home.list")) {
                        return true;
                    }
                    String format = "%1$s(%2$s%3$s)";
                    StringBuilder list = new StringBuilder();
                    for (int i = 1; i <= Home.getHomes(uuid).size();i++) {
                        String pos = Home.getHomePosString(uuid,Home.getHomes(uuid).get(i-1));
                        if (i != Home.getHomes(uuid).size()) {
                            list.append(String.format(format, Home.getHomes(uuid).get(i-1), i,pos)).append(", ");
                            continue;
                        }
                        list.append(String.format(format, Home.getHomes(uuid).get(i-1), i,pos));
                    }
                    sender.sendMessage(fpi.putLanguage("ListHomes","家(%home_size%): %home_list%",null,new Object[]{"home_size",Home.getHomes(uuid).size(),"home_list",list.toString()}));
                    return true;
                } else if ("teleport".equalsIgnoreCase(args[0])) {
                    if (!PermissionsUtils.hasPermissionsSendMessage(sender, "functions.permissions.command.home.teleport")) {
                        return true;
                    }
                    if (!Home.isHome(uuid,args[1])) {
                        sender.sendMessage(fpi.putLanguage("HomeIsNotExists", "&c家 %home% 不存在!", null, new Object[]{"home", Home.getHome(uuid,args[1])}));
                        return true;
                    }
                    ((Player) sender).teleport(Home.getHomePos(uuid,args[1]));
                    sender.sendMessage(fpi.putLanguage("TeleportedToHome","&a已成功传送到 %home% 家(%position%) 处！",null,new Object[]{"home",args[1],"position",Home.getHomePosString(uuid,args[1])}));
                    return true;
                }
                if (!PermissionsUtils.hasPermissionsSendMessage(sender, "functions.permissions.command.home.teleport")) {
                    return true;
                }
                if (!Home.isHome(uuid,args[0])) {
                    sender.sendMessage(fpi.putLanguage("HomeIsNotExists", "&c家 %home% 不存在!", null, new Object[]{"home", Home.getHome(uuid,args[0])}));
                    return true;
                }
                ((Player) sender).teleport(Home.getHomePos(uuid,args[0]));
                sender.sendMessage(fpi.putLanguage("TeleportedToHome","&a已成功传送到 %home% 家(%position%) 处！",null,new Object[]{"home",args[0],"position",Home.getHomePosString(uuid,args[0])}));
                return true;
            }
            sender.sendMessage(fpi.noPlayer());
            return true;
        } else if (fpi.hasAliases("back",label)) {
            if (sender instanceof Player) {
                UUID uuid = ((Player) sender).getUniqueId();
                if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.warp.add")) {
                    return true;
                }
                if (!Back.isDeath(uuid)) {
                    sender.sendMessage(fpi.putLanguage("NoFoundDeath","&a找不到最后死亡地点",null));
                    return true;
                }
                ((Player) sender).teleport(Back.getDeathPos(uuid));
                sender.sendMessage(fpi.putLanguage("TeleportedToDeath","&a成功传送至死亡地点 (%position%)",null,new Object[]{"position",fpi.changeLocationToString(((Player) sender).getLocation())}));
                return true;
            }
            sender.sendMessage(fpi.noPlayer());
            return true;
        }
        return true;
    }

    //@SuppressWarnings("all")
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> ls = new ArrayList<>();
        if (fpi.hasAliases("warp",alias,true)) {
            if (PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.warp.add")) ls.add("add");
            if (PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.warp.remove")) ls.add("remove");
            if (PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.warp.change")) ls.add("change");
            if (PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.warp.teleport")) {
                ls.add("teleport");
                ls.addAll(Warp.getWarps());
            }
            if (args.length == 1) {
                if (args[0] != null) {
                    List<String> t = new ArrayList<>(ls);
                    Collections.sort(t);
                    ls.clear();
                    t.forEach(e->{if (e.contains(args[0])) ls.add(e);});
                }
            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("teleport") || args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("change")) {
                    ls.clear();
                    ls.addAll(Warp.getWarps());
                }
            }
        } else if (fpi.hasAliases("home",alias,true)) {
            UUID uuid = ((Player)sender).getUniqueId();
            if (PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.home.add")) ls.add("add");
            if (PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.home.remove")) ls.add("remove");
            if (PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.home.change")) ls.add("change");
            if (PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.home.teleport")) {
                ls.add("teleport");
                ls.addAll(Home.getHomes(uuid));
            }
            if (args.length == 1) {
                if (args[0] != null) {
                    List<String> t = new ArrayList<>(ls);
                    Collections.sort(t);
                    ls.clear();
                    t.forEach(e->{if (e.contains(args[0])) ls.add(e);});
                }
            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("teleport") || args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("change")) {
                    ls.clear();
                    ls.addAll(Home.getHomes(uuid));
                }
            }
        }
        Collections.sort(ls);
        return ls;
    }
}
