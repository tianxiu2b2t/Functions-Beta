package org.functions.Bukkit.API.Hook;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderHook;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.functions.Bukkit.API.FPI;
import org.functions.Bukkit.Main.Functions;
import org.functions.Bukkit.Main.PlayerManager;
import org.functions.Bukkit.Main.functions.Bank;
import org.functions.Bukkit.Main.functions.Economy;

public class PlaceholderAPIHook extends PlaceholderExpansion {
    Functions plugin = Functions.instance;
    public String getAuthor() {
        return "2b2ttianxiu";
    }
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }
    public String getIdentifier() {
        return "functions";
    }
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }
    public String getRequiredPlugin() {
        return "Functions";
    }

    @Override
    public boolean canRegister() {
        return (plugin = (Functions) Bukkit.getPluginManager().getPlugin(getRequiredPlugin())) != null;
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        return new FPI().onRequest(player,params);
    }
}
