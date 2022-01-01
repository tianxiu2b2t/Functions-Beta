package org.functions.Bukkit.Commands.Permissions;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.functions.Bukkit.API.FPI;
import org.functions.Bukkit.Main.Functions;
import org.functions.Bukkit.Main.functions.User;

import java.util.List;

public class CommandWorldControl implements TabExecutor {
    public void run() {
        Functions.instance.getAPI().getCommand("seed", new CommandWorldControl());
    }
    FPI fpi = Functions.instance.getAPI();

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (fpi.hasAliases("seed",label)) {
            if (sender instanceof Player) {
                Player p = ((Player) sender);
                User user = Functions.instance.getPlayerManager().getUser(p.getUniqueId());
                fpi.getInstance().getFServer().getWorlds().forEach(e->{
                    String text = fpi.putLanguage("ShowWorldsSeed","&a世界 %world% 的种子为 %seed%",null,new Object[]{"world",e.getWorld().getName(),"seed",e.getWorld().getSeed()});
                    user.sendTellRaw("[{\"text\":\"" + text + "\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"" + e.getWorld().getSeed() + "\"}}]");
                });
            }
            fpi.getInstance().getFServer().getWorlds().forEach(e->{
                sender.sendMessage(fpi.putLanguage("ShowWorldsSeed","&a世界 %world% 的种子为 %seed%",null,new Object[]{"world",e.getWorld().getName(),"seed",e.getWorld().getSeed()}));
            });
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
