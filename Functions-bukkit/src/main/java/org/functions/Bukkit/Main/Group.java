package org.functions.Bukkit.Main;

import org.bukkit.configuration.file.FileConfiguration;

public class Group {
    FileConfiguration group;
    public Group(FileConfiguration group) {
        this.group = group;
    }

    public FileConfiguration getGroup() {
        return group;
    }
    public String getChat() {
        return group.getString("Format.Chat","<%player_display%> %message%");
    }
}
