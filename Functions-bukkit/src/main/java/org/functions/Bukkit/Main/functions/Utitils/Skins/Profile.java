package org.functions.Bukkit.Main.functions.Utitils.Skins;

import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class Profile {
    public Class<?> getGameProfile() throws ClassNotFoundException {
        return Class.forName("com.mojang.authlib.GameProfile");
    }
    public Object getPlayerGameProfile(Player player) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return getGameProfile().getConstructor(UUID.class,String.class).newInstance(player.getUniqueId(),player.getName());
    }
    public Class<?> getPropertyMap() throws ClassNotFoundException {
        return Class.forName("com.mojang.authlib.properties.PropertyMap");
    }
}
