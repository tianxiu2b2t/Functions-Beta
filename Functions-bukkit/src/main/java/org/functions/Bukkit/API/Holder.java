package org.functions.Bukkit.API;

import org.bukkit.configuration.file.FileConfiguration;
import org.functions.Bukkit.Main.Functions;

import java.util.LinkedHashMap;
@SuppressWarnings("all"
)
public class Holder {
    LinkedHashMap<String, Object> holder = new LinkedHashMap<>();
    public Holder() {
        for (String s : Functions.instance.getConfiguration().getSettings().getConfigurationSection("Holder").getKeys(false)) {
            if (holder.get(s)!=null) {
                holder.remove(s);
            }
            holder.put(s,Functions.instance.getConfiguration().getSettings().get("Holder." + s));
        }
    }
    public LinkedHashMap getHolders() {
        return holder;
    }
    public Object getHolder(String key) {
        if (holder.get(key)!=null) {
            return holder.get(key);
        }
        return "";
    }
}
