package org.functions.Bukkit.Main.functions;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;

public class security {
    static int count = 8;
    public security(int count) {
        security.count = count;
    }
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String cmd = scanner.nextLine();
            if (cmd != null) {
                String s = cmd;
                count = 1;

                long start = System.currentTimeMillis();
                s = security(cmd);
                long end = System.currentTimeMillis();
                System.out.print(s);
                System.out.print("\n");
                System.out.print("Need time ms: " + (end - start));
                System.out.print("\n");
                File file =new File("out-" + cmd + ".txt");
                System.out.print("Save file: " + file.getAbsolutePath());
                System.out.print("\n");
                System.out.print("done\n");
                if (file.exists()) {
                    file.deleteOnExit();
                }
                file.createNewFile();

                //true = append file
                String temp = "You print: " + cmd + "\n security: " + s;
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(temp);
                bw.close();

                //scanner.skip("> ");
            }
        }
    }
    static String[] new_all = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","~","`","!","@","#","$","%","^","&","*","(",")","-","_","+","=","{","[","]","}",";",":","\"","'","\\","|","<",",",">",".","?","/"};
    static String[] all = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
    public static String toNumber1(String text) {
        String temp = text;
        for (int i = 0; i < new_all.length; i++) {
            int is = new_all.length - i - 1;
            temp = temp.replace(new_all[is], i+"");
        }
        return subString(temp);
    }
    public static String base64(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }
    @SuppressWarnings("all")
    public static String security(String text) {
        text = security1(text);
        text = security2(text);
        int i = 1;
        i = Integer.parseInt(text);
        for (int f = 1; f > i; f++) {
            i = i * i * f;
        }
        String md = md5(text);
        String sha = SHA512(text);
        String temp = md5(SHA512(md + (md.length() * md.length()) + sha + (sha.length() * sha.length())+""));
        return subString(temp);
    }
    public static String byteHEX(String temp) {
        byte ib = temp.getBytes()[256];
        char[] Digit = { '0', '1', '2', '3', '4', '5','6','7', '8', '9','A', 'B', 'C', 'D','E','F' };
        char[] ob = new char[2];
        ob[0] = Digit[(ib >>> 4) & 0X0F];
        ob[1] = Digit[ib & 0X0F];
        return new String(ob);

    }
    public static String HexString(Object temps) {
        String tempa = temps.toString();
        byte[] b = tempa.getBytes();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tempa.length();i++) {
            //sb.append("a").append(0xFF & b[i]);
            if (Integer.toHexString(0xFF & b[i]).length() == 1) {
                sb.append("a").append(Integer.toHexString(0xFF & b[i]));
            } else {
                sb.append(Integer.toHexString(0xFF & b[i]));
            }
        }
        return sb.toString();
    }
    public static String subString(Object temps) {
        String temp = temps.toString();
        String max = (temp.length() / 4 * 2)+"";
        String min = (temp.length() / 4 / 2)+"";
        if (max.startsWith("-")) max = max.replace("-","");
        if (min.startsWith("-")) min = min.replace("-","");
        return temp.substring(Integer.parseInt(min),Integer.parseInt(max));
    }
    public static String toABC1(String text) {
        String temp = text;
        for (int i = 0; i < new_all.length; i++) {
            int is = new_all.length - i - 1;
            temp = temp.replace(i + "", new_all[is]);
        }
        return subString(temp);
    }
    public static String toNumber(String text) {
        String temp = text;
        for (int i = 0; i < all.length; i++) {
            int is = all.length - i - 1;
            temp = temp.replace(all[is], i+"");
        }
        return subString(temp);
    }
    public static String toABC(String text) {
        String temp = text;
        for (int i = 0; i < all.length; i++) {
            int is = all.length - i - 1;
            temp = temp.replace(i + "", all[is]);
        }
        return subString(temp);
    }
    public static String security1(String text) {
        String temp = text;
        temp = md5(temp) + SHA512(temp) + toABC(temp) + toNumber(temp);
        for (int i = 0; i < 4; i ++) {
            temp = md5(temp);
            temp = SHA512(temp);
            temp = toABC(temp);
            temp = toNumber(temp);
            temp = md5(SHA512(toABC(toNumber(temp))));
            temp = SHA512(toABC(toNumber(md5(temp))));
            temp = toABC(toNumber(md5(SHA512(temp))));
            temp = toNumber(md5(SHA512(toABC(temp))));
        }
        temp = md5(temp) + SHA512(temp) + toABC(temp) + toNumber(temp);
        temp = temp.hashCode()+"";
        return subString(temp);
    }
    public static String security2(String text) {
        String temp = text;
        temp = md5(temp) + SHA512(temp) + toABC(temp) + toNumber(temp);
        for (int i = 0; i < count; i ++) {
            temp = md5(temp);
            temp = SHA512(temp);
            temp = toABC(temp);
            temp = toNumber(temp);
            temp = md5(SHA512(toABC1(toNumber1(temp))));
            temp = SHA512(toABC1(toNumber1(md5(temp))));
            temp = toABC1(toNumber1(md5(SHA512(temp))));
            temp = toNumber1(md5(SHA512(toABC1(temp))));
        }
        temp = md5(temp) + SHA512(temp) + toABC(temp) + toNumber(temp);
        temp = temp.hashCode()+"";
        return subString(temp);
    }
    public static String SHA512(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("sha-512");
            List<String> ls = new ArrayList<>();
            for (int i = 2; i < 36; i++) {
                ls.add(new BigInteger(1,md.digest(text.getBytes(StandardCharsets.UTF_8))).toString(i));
            }
            StringBuilder temp = new StringBuilder();
            for (String s : ls) {
                temp.append(s);//.append(" |\n");
            }
            return subString(temp);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
    /*public static String md(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            return new BigInteger(1,md.digest(text.getBytes(StandardCharsets.UTF_8))).toString(36);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }*/
    public static String md5(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            List<String> ls = new ArrayList<>();
            for (int i = 2; i < 36; i++) {
                ls.add(new BigInteger(1,md.digest(text.getBytes(StandardCharsets.UTF_8))).toString(i));
            }
            StringBuilder temp = new StringBuilder();
            for (String s : ls) {
                temp.append(s);//.append(" |\n");
            }
            return subString(temp);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
