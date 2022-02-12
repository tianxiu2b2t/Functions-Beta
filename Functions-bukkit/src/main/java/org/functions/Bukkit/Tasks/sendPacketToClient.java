package org.functions.Bukkit.Tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.functions.Bukkit.API.Event.AwayFromBoardEvent;
import org.functions.Bukkit.Main.Functions;
import org.functions.Bukkit.Main.functions.PermissionsUtils;
import org.functions.Bukkit.Main.functions.User;
import org.functions.Bukkit.Main.functions.Utils;
import org.functions.Bukkit.Main.functions.Utitils.ActionBar;
import org.functions.Bukkit.Main.functions.Utitils.ScoreBoard;
import org.functions.Bukkit.Main.functions.Utitils.TPS;
import org.functions.Bukkit.Main.functions.Utitils.Tab;

import java.util.ArrayList;
import java.util.List;

public class sendPacketToClient implements Runnable {
    StringBuilder Footer;
    StringBuilder Header;
    public void run() {
        if (!TPS.isHigh(Functions.instance.getConfiguration().getSettings().getDouble("TPSIsLower",16.0D))) {
            return;
        }
        Footer = new StringBuilder();
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
        Header = new StringBuilder();
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
        for (Player p : Functions.instance.getAPI().getOnlinePlayers()) {
            if (Functions.instance.getConfiguration().getSettings().getBoolean("Tab.Enable",false)) Tab.send(p,Header.toString(), Footer.toString());//, //Functions.instance.getConfiguration().getSettings().getString("PlayerList"));
            if (Functions.instance.getConfiguration().getSettings().get("ActionBar")!=null) {
                ActionBar.send(p,Functions.instance.getConfiguration().getSettings().getString("ActionBar"));
                if (Functions.instance.getConfiguration().getSettings().get("ActionBar")!=null) ActionBar.send(p,Functions.instance.getConfiguration().getSettings().getString("ActionBar.Message"));
            }
            List<String> ls = new ArrayList<>();
            if (Functions.instance.getConfiguration().getSettings().get("ScoreBoard")!=null) {
                ls.add(Functions.instance.getConfiguration().getSettings().getString("ScoreBoard.Lines",""));
                if (Functions.instance.getConfiguration().getSettings().getString("ScoreBoard.Lines").startsWith("[")) {
                    if (Functions.instance.getConfiguration().getSettings().getString("ScoreBoard.Lines").endsWith("]")) {
                        ls.clear();
                        ls = Functions.instance.getConfiguration().getSettings().getStringList("ScoreBoard.Lines");
                    }
                }
                ScoreBoard.board(p,ls,Functions.instance.getConfiguration().getSettings().getString("ScoreBoard.DisplayName", "Functions"));
            }
            ScoreBoard.team(p);
            ScoreBoard.submit(p);
            for (Player o : Bukkit.getOnlinePlayers()) {
                if (Functions.instance.getPlayerManager().getUser(p.getUniqueId()).isHiding()) o.hidePlayer(Functions.instance,p);
                else o.showPlayer(Functions.instance,p);
            }
            User user = Functions.instance.getPlayerManager().getUser(p.getUniqueId());
            if (user.getCPS() != null) {
                if (user.getCPS().getCountCPS() != 0) {
                    if (user.noClickers != 0) user.noClickers = 0;
                } else {
                    if (user.isAFK()) {
                        user.noClickers = 0;
                    } else if (user.noClickers >= Functions.instance.getConfiguration().getSettings().getInt("AFK.NoClickersOut", 15) * 20) {
                        AwayFromBoardEvent event = new AwayFromBoardEvent(p);
                        Bukkit.getPluginManager().callEvent(event);
                        if (!event.isCancelled()) {
                            Bukkit.getScheduler().runTaskAsynchronously(Functions.instance, () -> user.setAFK(true));
                        }
                    }
                }
            }
        }
    }
}
