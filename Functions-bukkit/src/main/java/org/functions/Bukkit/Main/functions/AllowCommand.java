package org.functions.Bukkit.Main.functions;

import org.bukkit.configuration.file.FileConfiguration;
import org.functions.Bukkit.Main.Functions;

import java.util.List;

public class AllowCommand {
    String l = "";
    FileConfiguration set = null;
    public AllowCommand(String location) {
        l = location + ".";
        set = Functions.instance.getConfiguration().getCommands();
    }
    public List<String> getAllow() {
        if (set.getString(l+"Allow")!=null) {
            if (!set.getString(l + "Allow").equalsIgnoreCase("all")) {
                return set.getStringList(l + "Allow");
            }
        }
        return null;
    }
    public boolean getAllowAll() {
        return getAllow() == null;
    }
    public List<String> getDisallow() {
        if (set.getString(l+"Disallow")!=null) {
            if (!set.getString(l + "Disallow").equalsIgnoreCase("all")) {
                return set.getStringList(l + "Disallow");
            }
        }
        return null;
    }
    public boolean getDisallowAll() {
        return getDisallow() == null;
    }
    public boolean cmd(String cmd) {
        if (getAllowAll()) {
            return false;
        } else {
            boolean is = false;
            for (String s : getAllow()) {
                if (cmd.matches(s)) {
                    is = true;
                    break;
                }
            }
            if (is) {
                return false;
            }
        }
        if (!getDisallowAll()) {
            boolean is = false;
            for (String s : getDisallow()) {
                if (cmd.matches(s)) {
                    is = true;
                    break;
                }
            }
            return is;
        } else {
            return true;
        }
    }
}
