package org.functions.Bukkit.Commands.Permissions;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.functions.Bukkit.API.FPI;
import org.functions.Bukkit.Main.Functions;
import org.functions.Bukkit.Main.functions.PermissionsUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class CommandForceLoad implements TabExecutor {
    public void run() {
        Functions.instance.getAPI().getCommand("forceload", new CommandForceLoad());
    }
    FPI fpi = Functions.instance.getAPI();

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(fpi.subcmd());
            return true;
        }
        if ("world".equalsIgnoreCase(args[0])) {
            AtomicReference<World> tempw = new AtomicReference<>();
            Bukkit.getWorlds().forEach(e->{
                if (e.getName().equalsIgnoreCase(args[1])) {
                    tempw.set(e);
                }
            });
            World world = null;
            if (tempw.get()!=null) {
                world = tempw.get();
            }
            if (world==null) {
                sender.sendMessage(fpi.putLanguage("WorldIsNotFound","&c世界 %world% 不存在？",null,new Object[]{"world",args[1]}));
                return true;
            }
            if (sender instanceof Player) {
                fpi.getInstance().getForceLoad().add(((Player) sender).getWorld());
                fpi.getInstance().getForceLoad().saveWorld();
                sender.sendMessage(fpi.putLanguage("SuccessfullyAddForceWorldLoad","&a成功添加强制加载世界(%world%)",null,new Object[]{"world",((Player) sender).getWorld().getName()}));
                return true;
            }
        } else if ("chunk".equalsIgnoreCase(args[0])) {
            if (sender instanceof Player) {
                if (!PermissionsUtils.hasPermissionsSendMessage(sender,"functions.permissions.command.forceload.chunk")) {
                    return true;
                }
                Chunk chunk = ((Player) sender).getLocation().getChunk();
                fpi.getInstance().getForceLoad().add(((Player) sender).getLocation().getChunk());
                fpi.getInstance().getForceLoad().saveChunk();
                sender.sendMessage(fpi.putLanguage("SuccessfullyAddForceChunkLoad","&a成功添加强制加载区块(%chunk_position%)",null,new Object[]{"chunk_position",fpi.changeLocationToString(new Location(chunk.getWorld(),chunk.getX(),0,chunk.getZ()))}));
                return true;
            }
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> ls = new ArrayList<>();
        if (args.length <= 1) {
            ls.add("chunk");
        }
        return ls;
    }
}
