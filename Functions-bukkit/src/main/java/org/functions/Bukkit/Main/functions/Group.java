package org.functions.Bukkit.Main.functions;

import org.bukkit.configuration.file.FileConfiguration;
import org.functions.Bukkit.Main.Functions;

import java.util.ArrayList;
import java.util.List;

public class Group {
    /*
    Group: Default
DisplayName: "&7默认组&r"
Prefix: "&7"
Suffix: none
Format:
  Join: "&7[&a+&7]%player_display% Join the %date% %time%"
  Quit: "&7[&c-&7]%player_display% Left the %date% %time%"
  Chat: "<%player_display%> %message%"
Hide: false
Permissions:
  - "functions.default.*"
atPlayer: "&2@%player%&r"

     */
    FileConfiguration group;
    public Group(FileConfiguration group) {
        this.group = group;
    }
    public Group(String group) {
        this.group = Functions.instance.getConfiguration().groups.get(group);
    }

    public FileConfiguration getGroup() {
        return group;
    }
    public String getChat() {
        return group.getString("Format.Chat","<%player_display%> %message%");
    }
    public String getJoin() {
        return group.getString("Format.Join","&7[&a+&7]%player_display% Join the %date% %time%");
    }
    public String getQuit() {
        return group.getString("Format.Quit","&7[&c-&7]%player_display% Left the %date% %time%");
    }
    public String getPrefix() {
        return group.getString("Prefix","");
    }
    public String getSuffix() {
        return group.getString("Suffix","");
    }
    public class Permissions {
        public List<String> getAllPermissions() {
            return group.getStringList("Permissions");
        }
        public List<String> getPermissions() {
            List<String> temp = new ArrayList<>();
            for (String s : getAllPermissions()) {
                if (s.startsWith("functions.")) {
                    continue;
                }
                temp.add(s);
            }
            return temp;
        }
        public List<String> getFunctionsPermissions() {
            List<String> temp = new ArrayList<>();
            for (String s : getAllPermissions()) {
                if (!s.startsWith("functions.")) {
                    continue;
                }
                temp.add(s);
            }
            return temp;
        }
    }
    public String atPlayer() {
        return group.getString("atPlayer","&2@%player%&r");
    }
}
