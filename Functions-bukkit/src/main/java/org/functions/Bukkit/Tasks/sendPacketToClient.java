package org.functions.Bukkit.Tasks;

import org.bukkit.entity.Player;
import org.functions.Bukkit.Main.Functions;
import org.functions.Bukkit.Main.functions.Utils;

import java.util.ArrayList;
import java.util.List;

public class sendPacketToClient implements Runnable {
    Utils.Tab tab;
    Utils.ActionBar bar;
    Utils.ScoreBoard board;
    public void run() {
        for (Player p : Functions.instance.getAPI().getOnlinePlayers()) {
            tab = new Utils.Tab(p);
            tab.send("awa","ewe");
            bar = new Utils.ActionBar(p);
            bar.send("Welcome");
            board = new Utils.ScoreBoard(p);
            List<String> ls = new ArrayList<>();
            ls.add("Welcome");
            ls.add("Hurry up");
            ls.add("Hurry up1");
            ls.add("Hurry up2");
            ls.add("Hurry up3");
            board.sendAll("Functions",ls);
        }
    }
}
