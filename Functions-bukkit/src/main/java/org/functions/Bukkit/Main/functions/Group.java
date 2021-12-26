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
    public String getGroupName() {
        return group.getString("Group","");
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
    public void setPrefix(String name) {
        group.set("Prefix",name);
    }
    public void setSuffix(String name) {
        group.set("Suffix",name);
    }
    public List<String> getPrefixes() {
        return group.getStringList("Prefixes");
    }
    public List<String> getSuffixes() {
        return group.getStringList("Suffixes");
    }
    public void setPrefixes(List<String> prefixes) {
        group.set("Prefixes",prefixes);
        save();
    }
    public void setSuffixes(List<String> suffixes) {
        group.set("Suffixes",suffixes);
        save();
    }
    public boolean addPrefixes(String name) {
        //boolean is = false;
        for (String s : getPrefixes()) {
            if (s.equalsIgnoreCase(name)) {
                return false;
            }
        }
            List<String> ls = getPrefixes();
            ls.add(name);
            setPrefixes(ls);
            return true;
    }
    public boolean addSuffixes(String name) {
        boolean is = false;
        for (String s : getSuffixes()) {
            if (s.equalsIgnoreCase(name)) {
                is = true;
                break;
            }
        }
        if (is) {
            List<String> ls = getSuffixes();
            ls.add(name);
            setPrefixes(ls);
        }
        return is;
    }
    public boolean removePrefixes(String name) {
        boolean is = true;
        for (String s : getPrefixes()) {
            if (!s.equalsIgnoreCase(name)) {
                is = false;
                break;
            }
        }
        if (is) {
            List<String> ls = getPrefixes();
            ls.add(name);
            setPrefixes(ls);
        }
        return is;
    }
    public String removePrefixes(int number) {
        boolean is = true;
        String s = null;
        if (getPrefixes().size() >= number) {
            List<String> ls = getPrefixes();
            s = ls.remove(number);
            setPrefixes(ls);
        }
        return s;
    }
    public boolean removeSuffixes(String name) {
        boolean is = true;
        for (String s : getSuffixes()) {
            if (!s.equalsIgnoreCase(name)) {
                is = false;
                break;
            }
        }
        if (is) {
            List<String> ls = getSuffixes();
            ls.add(name);
            setPrefixes(ls);
        }
        return is;
    }
    public String removeSuffixes(int number) {
        boolean is = true;
        String s = null;
        if (getSuffixes().size() >= number) {
            List<String> ls = getSuffixes();
            s = ls.remove(number);
            setSuffixes(ls);
        }
        return s;
    }
    public List<String> getAllPermissions() {
            return group.getStringList("Permissions");
        }
    public void setAllPermissions(List<String> permissions) {
        group.set("Permissions",permissions);
        save();
    }
    public boolean addAllPermissions(String name) {
        boolean is = true;
        for (String e : getAllPermissions()) {
            if (e.equalsIgnoreCase(name)) {
                is = false;
                break;
            }
        }
        if (is) {
            List<String> ls = getAllPermissions();
            ls.add(name);
            setAllPermissions(ls);
        }
        return is;
    }
    public boolean removeAllPermissions(String name) {
        boolean is = false;
        for (String e : getAllPermissions()) {
            if (e.equalsIgnoreCase(name)) {
                is = true;
                break;
            }
        }
        if (is) {
            List<String> ls = getAllPermissions();
            ls.remove(name);
            setAllPermissions(ls);
        }
        return is;
    }
    public void save() {
        Functions.instance.getConfiguration().saveGroups();
    }

    public List<String> getPermissions() {
            List<String> temp = new ArrayList<>();
            for (String s : getAllPermissions()) {
                if (s.startsWith("functions")) {
                    continue;
                }
                temp.add(s);
            }
            return temp;
        }
        public List<String> getFunctionsPermissions() {
            List<String> temp = new ArrayList<>();
            for (String s : getAllPermissions()) {
                if (s.startsWith("*")) {
                    temp.add("*");
                }
                if (!s.startsWith("functions")) {
                    continue;
                }
                temp.add(s);
            }
            return temp;
        }
    public String atPlayer() {
        return group.getString("atPlayer","&2@%player%&r");
    }
}
