package org.functions.Bukkit.Tasks;

import org.functions.Bukkit.Main.Functions;
import org.functions.Bukkit.Main.functions.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BalanceTopRunnable implements Runnable{
    public static List<Economy> eco = new ArrayList<>();
    public static List<Bank> bank = new ArrayList<>();
    BalanceTop bp = new BalanceTop();
    public static double balance = 0.0;
    long time = 0;
    public void run() {
        balance = 0;
        List<Economy> tempe = bp.getEconomyBalances();
        tempe.sort(Comparator.comparingDouble(Economy::getBalance).reversed());
        eco = tempe;
        List<Bank> tempb = bp.getBankBalances();
        tempb.sort(Comparator.comparingDouble(Bank::getBalance).reversed());
        bank = tempb;
            for (Economy ecos : tempe) {
                if (ecos.getBalance() < 0.0) continue;
                balance = balance + ecos.getBalance();
            }
            for (Bank banks : tempb) {
                if (banks.getBalance() < 0.0) continue;
                balance = balance + banks.getBalance();

            }
    }
    public static List<Economy> getEconomyTop() {
        return eco;
    }
    public static List<Bank> getBankTop() {
        return bank;
    }
    public static double getDoubleAllBalance() {
        return balance;
    }
    public static String getAllBalance() {
        if (autoGetBalance()==-2) {
            return "MAX_BALANCE";
        }
        return display();
    }
    public static String display() {
        return autoGetBalance() + Functions.instance.getConfiguration().getSettings().getString("Money.Format","$");
    }
    public static double autoGetBalance() {
        if (getDoubleAllBalance()>=0.0) {
            if ((getDoubleAllBalance() + "").split("\\.")[1].length() >= (Functions.instance.getConfiguration().getSettings().getInt("Money.limitation", 4) + 1)) {
                return Utils.parseDoubleUpFromNumber(getDoubleAllBalance(), Functions.instance.getConfiguration().getSettings().getInt("Money.limitation", 4));
            }
            return getDoubleAllBalance();
        }
        return -1;
    }
}
