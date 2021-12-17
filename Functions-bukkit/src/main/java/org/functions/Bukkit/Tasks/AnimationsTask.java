package org.functions.Bukkit.Tasks;

import org.functions.Bukkit.Main.Functions;
import org.functions.Bukkit.Main.functions.AnimationManager;

public class AnimationsTask implements Runnable {
    public void run() {
        for (String s : AnimationManager.getAnimations()) {
            AnimationManager.getAnimation(s).line();
        }
        Functions.instance.title.line();
    }
}
