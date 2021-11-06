package org.functions.Bukkit.Tasks;

import org.functions.Bukkit.Main.BalanceTop;
import org.functions.Bukkit.Main.Economy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BalanceTopRunnable implements Runnable {
    public static List<Economy> list = new ArrayList<>();
    @Override
    public void run() {
        List<Economy> temp = new BalanceTop(10).getBalances();
        temp.sort(Comparator.comparingDouble(Economy::getBalance).reversed());
        list = temp;
    }
    public static List<Economy> getBalanceTop() {
        return list;
    }
}
