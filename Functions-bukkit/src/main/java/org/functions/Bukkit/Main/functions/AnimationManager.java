package org.functions.Bukkit.Main.functions;

import org.functions.Bukkit.Main.Functions;

import java.util.List;

public class AnimationManager {
    public static Animation getAnimation(String name) {
        return Functions.instance.getConfiguration().animations.get(name);
    }
    public static List<String> getAnimations() {
        return Functions.instance.getConfiguration().animations_Name;
    }
}
