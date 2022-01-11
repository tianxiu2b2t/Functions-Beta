package org.functions.Bukkit.Tasks;

import org.bukkit.Bukkit;
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
        if (!Utils.TPS.isHigh(15.0D)) {
            return;
        }
        for (Player p : Functions.instance.getAPI().getOnlinePlayers()) {
            StringBuilder Footer = new StringBuilder();
            if (Functions.instance.getConfiguration().getSettings().getString("Tab.Footer") != null) {
                if (!Functions.instance.getConfiguration().getSettings().getString("Tab.Footer").equals("none") && !Functions.instance.getConfiguration().getSettings().getString("Tab.Footer").equals("")) {
                    for (int i = 0; i < Functions.instance.getConfiguration().getSettings().getStringList("Tab.Footer").size(); ++i) {
                        if (i == Functions.instance.getConfiguration().getSettings().getStringList("Tab.Footer").size() - 1) {
                            Footer.append(Functions.instance.getConfiguration().getSettings().getStringList("Tab.Footer").get(i));
                        } else {
                            Footer.append(Functions.instance.getConfiguration().getSettings().getStringList("Tab.Footer").get(i)).append("\n");
                        }
                    }
                }
            }
            StringBuilder Header = new StringBuilder();
            if (Functions.instance.getConfiguration().getSettings().getString("Tab.Header") != null) {
                if (!Functions.instance.getConfiguration().getSettings().getString("Tab.Header").equals("none") && !Functions.instance.getConfiguration().getSettings().getString("Tab.Header").equals("")) {
                    for (int i = 0; i < Functions.instance.getConfiguration().getSettings().getStringList("Tab.Header").size(); ++i) {
                        if (i == Functions.instance.getConfiguration().getSettings().getStringList("Tab.Header").size() - 1) {
                            Header.append(Functions.instance.getConfiguration().getSettings().getStringList("Tab.Header").get(i));
                        } else {
                            Header.append(Functions.instance.getConfiguration().getSettings().getStringList("Tab.Header").get(i)).append("\n");
                        }
                    }
                }
            }
            tab = new Utils.Tab(p);
            tab.send(Header.toString(), Footer.toString(), Functions.instance.getConfiguration().getSettings().getString("PlayerList"));
            if (Functions.instance.getConfiguration().getSettings().get("ActionBar")!=null) {
                bar = new Utils.ActionBar(p);
                if (Functions.instance.getConfiguration().getSettings().get("ActionBar.Message")!=null) bar.send(Functions.instance.getConfiguration().getSettings().getString("ActionBar.Message"));
            }
            List<String> ls = new ArrayList<>();
            if (Functions.instance.getConfiguration().getSettings().get("ScoreBoard")!=null) {
                board = new Utils.ScoreBoard(p);
                ls.add(Functions.instance.getConfiguration().getSettings().getString("ScoreBoard.Lines",""));
                if (Functions.instance.getConfiguration().getSettings().getString("ScoreBoard.Lines").startsWith("[")) {
                    if (Functions.instance.getConfiguration().getSettings().getString("ScoreBoard.Lines").endsWith("]")) {
                        ls.clear();
                        ls = Functions.instance.getConfiguration().getSettings().getStringList("ScoreBoard.Lines");
                    }
                }
                board.sendAll(Functions.instance.getConfiguration().getSettings().getString("ScoreBoard.DisplayName", "Functions"), ls);
            }
            for (Player o : Bukkit.getOnlinePlayers()) {
                if (Functions.instance.getPlayerManager().getUser(p.getUniqueId()).isHiding()) o.hidePlayer(Functions.instance,p);
                else o.showPlayer(Functions.instance,p);
            }
        }
    }
}
