package org.functions.Bukkit.Main.functions.Utitils;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.functions.Bukkit.Main.Functions;
import org.functions.Bukkit.Main.functions.User;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ScoreBoard {
    static List<String> clear_lines = new ArrayList<>();
    static LinkedHashMap<UUID, Scoreboard> board = new LinkedHashMap<>();
    static LinkedHashMap<UUID, Boolean> enable_board = new LinkedHashMap<>();
    static LinkedHashMap<UUID, Objective> objective = new LinkedHashMap<>();
    static LinkedHashMap<UUID, Team> team = new LinkedHashMap<>();
    static LinkedHashMap<UUID, List<Team>> teams = new LinkedHashMap<>();
    static LinkedHashMap<UUID, List<String>> cache = new LinkedHashMap<>();
    private static void init(UUID uuid) {
        if (clear_lines.size() == 0) {
            clear_lines.addAll(Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"));
        }
        if (board.get(uuid) == null) {
            board.put(uuid,Functions.instance.getServer().getScoreboardManager().getNewScoreboard());
        }
        if (board.get(uuid).getObjective("Functions") == null) {
            objective.put(uuid,board.get(uuid).registerNewObjective("Functions","dummy"));
        }
        if (team.get(uuid) == null) {
            team.put(uuid,board.get(uuid).registerNewTeam(Bukkit.getOfflinePlayer(uuid).getName()));
        }
        cache.computeIfAbsent(uuid, k -> new ArrayList<>());
    }
    public static void board(Player player, List<String> scores,String displayname) {
        UUID uuid = player.getUniqueId();
        init(uuid);
        if (inBoard(uuid)) {
            if (inBoardCache(uuid)) {
                cache.get(uuid).forEach(e->{
                    board.get(uuid).resetScores(e);
                });
            }
            AtomicInteger i = new AtomicInteger(scores.size() - 1);
            scores.forEach(e->{
                objective.get(uuid).getScore(e).setScore(i.getAndDecrement());
            });
            objective.get(uuid).setDisplayName(displayname);
            objective.get(uuid).setDisplaySlot(DisplaySlot.SIDEBAR);
            if (enable_board.get(uuid) == null || !enable_board.get(uuid)) {
                enable_board.remove(uuid);
                enable_board.put(uuid,true);
            }
        } else {
            if (enable_board.get(uuid) != null || enable_board.get(uuid)) {
                board.get(uuid).clearSlot(DisplaySlot.SIDEBAR);
                enable_board.remove(uuid);
            }
        }
    }
    public static void submit(Player player) {
        player.setScoreboard(board.get(player.getUniqueId()));
    }
    private static Team setTeam(UUID uuid,Player player) {
        Team team;
        if (board.get(uuid).getTeam(player.getName())!=null) {
            team = board.get(uuid).getTeam(player.getName());
        } else {
            team = board.get(uuid).registerNewTeam(player.getName());
        }
        team.setPrefix(Functions.instance.getAPI().replace("%prefix%",player));
        team.setSuffix(Functions.instance.getAPI().replace("%suffix%",player));
        return team;
    }
    private static void teamOptions(Team team,Player player) {
        if (!team.hasEntry(player.getName())) {
            team.addEntry(player.getName());
        }
        team.setPrefix(Functions.instance.getAPI().replace("%prefix%",player));
        team.setSuffix(Functions.instance.getAPI().replace("%suffix%",player));
    }
    private static Team getTeam(Set<Team> team, Player player,UUID uuid) {
        for (Team team1 : team) {
            if (team1.getName().equalsIgnoreCase(player.getName())) {
                return team1;
            }
        }
        return board.get(uuid).registerNewTeam(player.getName());
    }
    public static void team(Player player) {
        UUID uuid = player.getUniqueId();
        init(uuid);
        for (OfflinePlayer e : Bukkit.getOfflinePlayers()) {
            if (e.isOnline()) {
                teamOptions(getTeam(board.get(uuid).getTeams(),e.getPlayer(),uuid), e.getPlayer());
            }
        }
        /*if (inTeam(uuid)) {
            if (!team.get(uuid).hasEntry(player.getName())) {
                team.get(uuid).addEntry(player.getName());
            }
            team.get(uuid).setPrefix(Functions.instance.getAPI().replace("%prefix%",player));
            team.get(uuid).setSuffix(Functions.instance.getAPI().replace("%suffix%",player));
        }*/
    }
    private static boolean inBoard(UUID uuid) {
        return board.get(uuid) != null;
    }
    private static boolean inTeam(UUID uuid) {
        return team.get(uuid) != null;
    }
    private static boolean inBoardCache(UUID uuid) {
        if (!inBoard(uuid)) {
            return false;
        }
        return cache.get(uuid) != null;
    }
}
