package org.functions.Bukkit.Main.functions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomCode {
    Type type;
    int length = 0;
    String[] integer = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    String[] big = new String[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
    String[] small = new String[]{"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
    String[] all = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
    public RandomCode(Type type,int length) {
        this.type = type;
        this.length = length;
    }
    public String out() {
        Random random = new Random();
        List<String> temp = new ArrayList();
        if (type==Type.ALL) {

            while (temp.size()!=length) {
                int i = all.length;
                int r = random.nextInt(i);
                temp.add(all[r]);
            }
        }
        if (type==Type.BIG_ABC) {

            while (temp.size()!=length) {
                int i = big.length;
                int r = random.nextInt(i);
                temp.add(big[r]);
            }
        }
        if (type==Type.INTEGER) {

            while (temp.size()!=length) {
                int i = integer.length;
                int r = random.nextInt(i);
                temp.add(integer[r]);
            }
        }
        if (type==Type.SMALL_ABC) {

            while (temp.size()!=length) {
                int i = small.length;
                int r = random.nextInt(i);
                temp.add(small[r]);
            }
        }
        StringBuilder code = new StringBuilder();
        for (String t : temp) {
            code.append(t);
        }
        return code.toString();
    }

    public enum Type {
        INTEGER,
        BIG_ABC,
        SMALL_ABC,
        ALL
    }
}
