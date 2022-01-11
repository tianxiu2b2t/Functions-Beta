package org.functions.Bukkit.Main.functions;

import com.google.gson.*;
import org.apache.commons.lang.StringUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.*;
import org.functions.Bukkit.API.WorldBlock;
import org.functions.Bukkit.Main.Functions;
import org.functions.Bukkit.Main.PlayerManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@SuppressWarnings("all")
public class Utils {
    public static int parseInteger(Object text) {
        return Integer.parseInt(text.toString());
    }
    public static double parseDouble(Object text) {
        return Double.parseDouble(text.toString());
    }
    public static String parseString(Object text) {
        return text.toString();
    }
    public static double parseDoubleUpFromNumber(Object d, int limit) {
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < limit; i++) {
            temp.append("0");
        }
        return parseDoubleUp(d, temp.toString());
    }
    public static double parseDoubleUp(Object d,String limit) {
        /*String temp = parseString(d);
        if (temp.length() == 1) {
            return parseInteger(temp);
        }
        int t = temp.length() - 1;
            System.out.println(t);
            if (parseInteger(temp.substring(t)) >= 5) {
                System.out.println(temp.substring(0,temp.length()-1));
                temp = parseString(parseInteger(temp.substring(0,temp.length()-1)) + (parseInteger(temp.substring(t)) + 5));
            }
        return parseInteger(temp);
        double t = parseDouble(temp.substring(temp.length() - 1));
        if (t >= 5.0) {
            return Math.ceil(parseDouble(d));
        } else {
            return Math.floor(parseDouble(d));
        }*/
        String t = d.toString();
        DecimalFormat df = new DecimalFormat("#." + limit);
        if (parseString(d).split("\\.")[1].length() >= (limit.length()+1)) {
            t = parseString(d).split("\\.")[0] + "." + parseString(d).split("\\.")[1].substring(0,limit.length()+1);
        }
        return parseDouble(df.format(parseDouble(t)));
    }
    public static Location parseBlockPositionToPlayerPosition(Location location) {
        return null;
    }
    public static double autoCon(double value) {
        double values = value;
        if (values != value) {
            return values;
        }
        if (values >= 0) {
            values = values + 0.5D;
        } else {
            values = values + -0.5D;
        }
        return values;
    }
    public static PlayerManager getPlayerManager() {
        return Functions.instance.getPlayerManager();
    }
    public static class Tab {
        Player player;
        public Tab(Player player) {
            this.player = player;
        }
        public static String colorMsg(String msg) {
            if (msg == null) {
                return "";
            } else {
                if (Version.isCurrentEqualOrHigher(Version.v1_16_R1) && msg.contains("#")) {
                    msg = matchColorRegex(msg);
                }

                return ChatColor.translateAlternateColorCodes('&', msg);
            }
        }

        public static String matchColorRegex(String s) {
            String regex = "#[a-fA-F0-9]{6}";
            Matcher matcher = Pattern.compile(regex).matcher(s);

            while(matcher.find()) {
                String group = matcher.group(0);

                try {
                    s = s.replace(group, ChatColor.valueOf(group) + "");
                } catch (Exception var5) {
                    Functions.instance.print("The hex is a bad:" + group, "&c[ERROR] ");
                }
            }

            return s;
        }
        public void send(String header, String footer) {
            if (Functions.instance.getAPI().send_packet.get("Tab")!=null) {
                if (System.currentTimeMillis() < Functions.instance.getAPI().send_packet.get("Tab")) {
                    return;
                }
            }
            Functions.instance.getAPI().send_packet.put("Tab",System.currentTimeMillis() + 50 * Functions.instance.getConfiguration().getSettings().getLong("Tab.sendTime",5));
                if (header == null) {
                    header = "";
                }

                if (footer == null) {
                    footer = "";
                }

                if (Version.isCurrentEqualOrLower(Version.v1_15_R2)) {
                    header = colorMsg(header);
                    footer = colorMsg(footer);
                }
                header = Functions.instance.getAPI().replace(header,player);
                footer = Functions.instance.getAPI().replace(footer,player);
                header = Functions.instance.getAPI().replaceJson(header);
                footer = Functions.instance.getAPI().replaceJson(footer);
                try {
                    Class packetPlayOutPlayerListHeaderFooter = getNMSClass("PacketPlayOutPlayerListHeaderFooter");

                    try {
                        Object packet = packetPlayOutPlayerListHeaderFooter.getConstructor().newInstance();
                        Object tabHeader = getAsIChatBaseComponent(header);
                        Object tabFooter = getAsIChatBaseComponent(footer);
                        if (Version.isCurrentEqualOrHigher(Version.v1_13_R2)) {
                            setField(packet, "header", tabHeader);
                            setField(packet, "footer", tabFooter);
                        } else {
                            setField(packet, "a", tabHeader);
                            setField(packet, "b", tabFooter);
                        }

                        sendPacket(player, packet);
                    } catch (Exception var8) {
                        Constructor<?> titleConstructor = null;
                        if (Version.isCurrentEqualOrHigher(Version.v1_12_R1)) {
                            titleConstructor = packetPlayOutPlayerListHeaderFooter.getConstructor();
                        } else if (Version.isCurrentLower(Version.v1_12_R1)) {
                            titleConstructor = packetPlayOutPlayerListHeaderFooter.getConstructor(getAsIChatBaseComponent(header).getClass());
                        }

                        if (titleConstructor != null) {
                            setField(titleConstructor, "b", getAsIChatBaseComponent(footer));
                            sendPacket(player, titleConstructor);
                        }
                    }
                } catch (Throwable var9) {
                    var9.printStackTrace();
                }

        }
        public void send(String header, String footer,String ListPlayer) {
            if (Functions.instance.getAPI().send_packet.get("Tab")!=null) {
                if (System.currentTimeMillis() < Functions.instance.getAPI().send_packet.get("Tab")) {
                    return;
                }
            }
            Functions.instance.getAPI().send_packet.put("Tab",System.currentTimeMillis() + 50 * Functions.instance.getConfiguration().getSettings().getLong("Tab.sendTime",5));
            if (header == null) {
                header = "";
            }

            if (footer == null) {
                footer = "";
            }

            if (Version.isCurrentEqualOrLower(Version.v1_15_R2)) {
                header = colorMsg(header);
                footer = colorMsg(footer);
            }
            header = Functions.instance.getAPI().replace(header,player);
            footer = Functions.instance.getAPI().replace(footer,player);
            try {
                Class packetPlayOutPlayerListHeaderFooter = getNMSClass("PacketPlayOutPlayerListHeaderFooter");

                try {
                    Object packet = packetPlayOutPlayerListHeaderFooter.getConstructor().newInstance();
                    Object tabHeader = getAsIChatBaseComponent(header);
                    Object tabFooter = getAsIChatBaseComponent(footer);
                    if (Version.isCurrentEqualOrHigher(Version.v1_13_R2)) {
                        setField(packet, "header", tabHeader);
                        setField(packet, "footer", tabFooter);
                    } else {
                        setField(packet, "a", tabHeader);
                        setField(packet, "b", tabFooter);
                    }

                    sendPacket(player, packet);
                } catch (Exception var8) {
                    Constructor<?> titleConstructor = null;
                    if (Version.isCurrentEqualOrHigher(Version.v1_12_R1)) {
                        titleConstructor = packetPlayOutPlayerListHeaderFooter.getConstructor();
                    } else if (Version.isCurrentLower(Version.v1_12_R1)) {
                        titleConstructor = packetPlayOutPlayerListHeaderFooter.getConstructor(getAsIChatBaseComponent(header).getClass());
                    }

                    if (titleConstructor != null) {
                        setField(titleConstructor, "b", getAsIChatBaseComponent(footer));
                        sendPacket(player, titleConstructor);
                    }
                }
            } catch (Throwable var9) {
                var9.printStackTrace();
            }
            if (ListPlayer==null || ListPlayer == "") {
                player.setPlayerListName(Functions.instance.getAPI().replace("%player_display%",player));
                return;
            }
            player.setPlayerListName(Functions.instance.getAPI().replace(ListPlayer,player));
        }
        public static class JavaAccessibilities {
            public static boolean isAccessible(Field field, Object target) {
                if (getCurrentVersion() >= 9 && target != null) {
                    try {
                        return (Boolean)field.getClass().getDeclaredMethod("canAccess", Object.class).invoke(field, target);
                    } catch (NoSuchMethodException var3) {
                    } catch (Exception var4) {
                        var4.printStackTrace();
                    }
                }

                return field.isAccessible();
            }

            public static boolean isAccessible(Method method, Object target) {
                if (getCurrentVersion() >= 9 && target != null) {
                    try {
                        return (Boolean)method.getClass().getDeclaredMethod("canAccess", Object.class).invoke(method, target);
                    } catch (NoSuchMethodException var3) {
                    } catch (Exception var4) {
                        var4.printStackTrace();
                    }
                }

                return method.isAccessible();
            }

            public static int getCurrentVersion() {
                String currentVersion = System.getProperty("java.version");
                if (currentVersion.contains("_")) {
                    currentVersion = currentVersion.split("_")[0];
                }

                currentVersion = currentVersion.replaceAll("[^\\d]|_", "");

                for(int i = 8; i <= 18; ++i) {
                    if (currentVersion.contains(Integer.toString(i))) {
                        return i;
                    }
                }

                return 0;
            }
        }
        private static final Gson GSON = (new GsonBuilder()).create();
        private static final List<JsonObject> JSONLIST = new CopyOnWriteArrayList();
        public static Object getHandle(Object obj) throws Exception {
            return invokeMethod(obj, "getHandle");
        }
        public static synchronized Object getAsIChatBaseComponent(String text) throws Exception {
            Class<?> iChatBaseComponent = getNMSClass("IChatBaseComponent");
            if (!Version.isCurrentEqualOrHigher(Version.v1_16_R1)) {
                if (Version.isCurrentLower(Version.v1_8_R2)) {
                    Class<?> chatSerializer = getNMSClass("ChatSerializer");
                    return iChatBaseComponent.cast(chatSerializer.getMethod("a", String.class).invoke(chatSerializer, "{\"text\":\"" + text + "\"}"));
                } else {
                    Method m = iChatBaseComponent.getDeclaredClasses()[0].getMethod("a", String.class);
                    return m.invoke(iChatBaseComponent, "{\"text\":\"" + text + "\"}");
                }
            } else {
                JSONLIST.clear();
                JsonObject obj = new JsonObject();
                StringBuilder builder = new StringBuilder();
                String res = text;
                String font = "";
                String colorName = "";
                char charBefore = ' ';

                for(int i = 0; i < res.length() && i < res.length(); ++i) {
                    if (charBefore == '&') {
                        charBefore = ' ';
                    } else {
                        char charAt = res.charAt(i);
                        if (charAt == '{') {
                            int closeIndex = -1;
                            if (res.regionMatches(true, i, "{font=", 0, 6) && (closeIndex = res.indexOf(125, i + 6)) >= 0) {
                                font = NamespacedKey.minecraft(res.substring(i + 6, closeIndex)).toString();
                            } else if (res.regionMatches(true, i, "{/font", 0, 6) && (closeIndex = res.indexOf(125, i + 6)) >= 0) {
                                font = NamespacedKey.minecraft("default").toString();
                            }

                            if (closeIndex >= 0) {
                                if (builder.length() > 0) {
                                    obj.addProperty("text", builder.toString());
                                    JSONLIST.add(obj);
                                    builder = new StringBuilder();
                                }

                                obj = new JsonObject();
                                obj.addProperty("font", font);
                                i += closeIndex - i;
                            }
                        } else if (charAt == '#') {
                            colorName = res.substring(i, i + 7);
                            if (builder.length() > 0) {
                                obj.addProperty("text", builder.toString());
                                JSONLIST.add(obj);
                                builder = new StringBuilder();
                            }

                            obj = new JsonObject();
                            obj.addProperty("color", colorName);
                            i += 6;
                        } else if (charAt == '&') {
                            charBefore = charAt;
                            char colorCode = res.charAt(i + 1);
                            if (Character.isDigit(colorCode) || colorCode >= 'a' && colorCode <= 'f' || colorCode == 'k' || colorCode == 'l' || colorCode == 'm' || colorCode == 'n' || colorCode == 'o' || colorCode == 'r') {
                                obj.addProperty("text", builder.toString());
                                JSONLIST.add(obj);
                                obj = new JsonObject();
                                builder = new StringBuilder();
                                if (!colorName.isEmpty()) {
                                    obj.addProperty("color", colorName);
                                }

                                if (!font.isEmpty()) {
                                    obj.addProperty("font", font);
                                }

                                switch(colorCode) {
                                    case 'k':
                                        obj.addProperty("obfuscated", true);
                                        break;
                                    case 'l':
                                        obj.addProperty("bold", true);
                                        break;
                                    case 'm':
                                        obj.addProperty("strikethrough", true);
                                        break;
                                    case 'n':
                                        obj.addProperty("underlined", true);
                                        break;
                                    case 'o':
                                        obj.addProperty("italic", true);
                                        break;
                                    case 'p':
                                    case 'q':
                                    default:
                                        colorName = ChatColor.getByChar(colorCode).name().toLowerCase();
                                        obj.addProperty("color", colorName);
                                        break;
                                    case 'r':
                                        colorName = "white";
                                        obj.addProperty("color", colorName);
                                }
                            }
                        } else {
                            builder.append(charAt);
                        }
                    }
                }

                obj.addProperty("text", builder.toString());
                JSONLIST.add(obj);
                Method m = iChatBaseComponent.getDeclaredClasses()[0].getMethod("a", String.class);
                return m.invoke(iChatBaseComponent, GSON.toJson(JSONLIST));
            }
        }
        public static Object invokeMethod(Object obj, String name) throws Exception {
            return invokeMethod(obj, name, true, false);
        }
        public static Object invokeMethod(Object obj, String name, boolean declared, boolean superClass) throws Exception {
            Class<?> c = superClass ? obj.getClass().getSuperclass() : obj.getClass();
            Method met = declared ? c.getDeclaredMethod(name) : c.getMethod(name);
            if (!JavaAccessibilities.isAccessible(met, obj)) {
                met.setAccessible(true);
            }

            return met.invoke(obj);
        }

        public static Field getField(Object clazz, String name) throws Exception {
            return getField(clazz, name, true);
        }
        public static Field getField(Object clazz, String name, boolean declared) throws Exception {
            return getField(clazz.getClass(), name, declared);
        }
        public static Field getField(Class<?> clazz, String name, boolean declared) throws Exception {
            Field field = declared ? clazz.getDeclaredField(name) : clazz.getField(name);
            if (!JavaAccessibilities.isAccessible(field, (Object)null)) {
                field.setAccessible(true);
            }

            return field;
        }
        public static Object getFieldObject(Object object, Field field) throws Exception {
            return field.get(object);
        }
        public static void setField(Object object, String fieldName, Object fieldValue) throws Exception {
            getField(object, fieldName).set(object, fieldValue);
        }
        public static Class<?> getNMSClass(String name) throws ClassNotFoundException {
            return Class.forName("net.minecraft.server." + getPackageVersion() + "." + name);
        }
        public static String getPackageVersion() {
            return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        }
        public static void sendPacket(Player player, Object packet) {
            try {
                Object playerHandle = getHandle(player);
                Object playerConnection = getFieldObject(playerHandle, getField(playerHandle, "playerConnection"));
                playerConnection.getClass().getDeclaredMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
            } catch (Exception var4) {
            }

        }
    }
    public static class ActionBar {
        Player player;
        public ActionBar(Player player) {
            this.player = player;
        }
        public void send(Object Message) {
            if (Functions.instance.getAPI().send_packet.get("ActionBar")!=null) {
                if (System.currentTimeMillis() < Functions.instance.getAPI().send_packet.get("ActionBar")) {
                    return;
                }
            }
            Functions.instance.getAPI().send_packet.put("ActionBar",System.currentTimeMillis() + 50 * Functions.instance.getConfiguration().getSettings().getLong("ActionBar.sendTime",5));
            if (Message == null || Message.equals("")) {
                return;
            }
            String msg = Functions.instance.getAPI().replace(Message.toString(),player);
            if (msg == null || msg.equals("")) {
                return;
            }
            String nms = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            UUID uuid = player.getUniqueId();
            Player p = player;
            Object packet;
            boolean high = false;
            boolean useOldMethods = false;
            if (msg==null || msg.equals("")) {
                return;
            }
            if (nms.startsWith("v_1_1") || nms.contains("v_1_16")) {
                high = true;
            }
            if (nms.equalsIgnoreCase("v1_8_R1") || nms.startsWith("v1_7_")) {
                useOldMethods = true;
            }

            try {
                Class<?> packetPlayOutChatClass = Class.forName("net.minecraft.server." + nms + ".PacketPlayOutChat");
                Class chatComponentTextClass;
                Class iChatBaseComponentClass;
                Object chatCompontentText;
                if (useOldMethods) {
                    chatComponentTextClass = Class.forName("net.minecraft.server." + nms + ".ChatSerializer");
                    iChatBaseComponentClass = Class.forName("net.minecraft.server." + nms + ".IChatBaseComponent");
                    Method m3 = chatComponentTextClass.getDeclaredMethod("a", String.class);
                    chatCompontentText = iChatBaseComponentClass.cast(m3.invoke(chatComponentTextClass, "{\"text\": \"" + Message + "\"}"));
                    packet = packetPlayOutChatClass.getConstructor(iChatBaseComponentClass, Byte.TYPE).newInstance(chatCompontentText, 2);
                } else {
                    if (high) {
                        try {
                            Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + nms + ".entity.CraftPlayer");
                            Object craftPlayer = craftPlayerClass.cast(p);
                            Class<?> packetClass = Class.forName("net.minecraft.server." + nms + ".Packet");
                            packetPlayOutChatClass = Class.forName("net.minecraft.server." + nms + ".PacketPlayOutChat");
                            chatComponentTextClass = Class.forName("net.minecraft.server." + nms + ".ChatComponentText");
                            iChatBaseComponentClass = Class.forName("net.minecraft.server." + nms + ".IChatBaseComponent");
                            try {
                                Class<?> chatMessageTypeClass = Class.forName("net.minecraft.server." + nms + ".ChatMessageType");
                                Object[] chatMessageTypes = chatMessageTypeClass.getEnumConstants();
                                Object chatMessageType = null;
                                Object[] var13 = chatMessageTypes;
                                int var14 = chatMessageTypes.length;

                                for (int var15 = 0; var15 < var14; ++var15) {
                                    Object obj = var13[var15];
                                    if (obj.toString().equals("GAME_INFO")) {
                                        chatMessageType = obj;
                                    }
                                }

                                chatCompontentText = chatComponentTextClass.getConstructor(String.class).newInstance(msg);
                                packet = packetPlayOutChatClass.getConstructor(iChatBaseComponentClass, chatMessageTypeClass, uuid.getClass()).newInstance(chatCompontentText, chatMessageType, uuid);
                            } catch (ClassNotFoundException var16) {
                                chatCompontentText = chatComponentTextClass.getConstructor(String.class).newInstance(msg);
                                packet = packetPlayOutChatClass.getConstructor(iChatBaseComponentClass, Byte.TYPE, uuid.getClass()).newInstance(chatCompontentText, 2, uuid);
                            }

                            Method craftPlayerHandleMethod = craftPlayerClass.getDeclaredMethod("getHandle");
                            Object craftPlayerHandle = craftPlayerHandleMethod.invoke(craftPlayer);
                            Field playerConnectionField = craftPlayerHandle.getClass().getDeclaredField("playerConnection");
                            chatCompontentText = playerConnectionField.get(craftPlayerHandle);
                            Method sendPacketMethod = chatCompontentText.getClass().getDeclaredMethod("sendPacket", packetClass);
                            sendPacketMethod.invoke(chatCompontentText, packet);
                        } catch (NoSuchMethodException | ClassNotFoundException var17) {
                            var17.printStackTrace();
                        } catch (IllegalAccessException var18) {
                            var18.printStackTrace();
                        } catch (InstantiationException var19) {
                            var19.printStackTrace();
                        } catch (InvocationTargetException var20) {
                            var20.printStackTrace();
                        } catch (NoSuchFieldException var21) {
                            var21.printStackTrace();
                        }
                    }
                    chatComponentTextClass = Class.forName("net.minecraft.server." + nms + ".ChatComponentText");
                    iChatBaseComponentClass = Class.forName("net.minecraft.server." + nms + ".IChatBaseComponent");

                    try {
                        Class<?> chatMessageTypeClass = Class.forName("net.minecraft.server." + nms + ".ChatMessageType");
                        Object[] chatMessageTypes = chatMessageTypeClass.getEnumConstants();
                        Object chatMessageType = null;
                        Object[] var13 = chatMessageTypes;
                        int var14 = chatMessageTypes.length;

                        for (int var15 = 0; var15 < var14; ++var15) {
                            Object obj = var13[var15];
                            if (obj.toString().equals("GAME_INFO")) {
                                chatMessageType = obj;
                            }
                        }

                        chatCompontentText = chatComponentTextClass.getConstructor(String.class).newInstance(msg);
                        packet = packetPlayOutChatClass.getConstructor(iChatBaseComponentClass, chatMessageTypeClass).newInstance(chatCompontentText, chatMessageType);
                    } catch (ClassNotFoundException var14) {
                        chatCompontentText = chatComponentTextClass.getConstructor(String.class).newInstance(msg);
                        packet = packetPlayOutChatClass.getConstructor(iChatBaseComponentClass, Byte.TYPE).newInstance(chatCompontentText, 2);
                    }
                }
                Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + nms + ".entity.CraftPlayer");
                Object craftPlayer = craftPlayerClass.cast(p);
                Class<?> packetClass = Class.forName("net.minecraft.server." + nms + ".Packet");
                Method craftPlayerHandleMethod = craftPlayerClass.getDeclaredMethod("getHandle");
                Object craftPlayerHandle = craftPlayerHandleMethod.invoke(craftPlayer);
                Field playerConnectionField = craftPlayerHandle.getClass().getDeclaredField("playerConnection");
                chatCompontentText = playerConnectionField.get(craftPlayerHandle);
                Method sendPacketMethod = chatCompontentText.getClass().getDeclaredMethod("sendPacket", packetClass);
                sendPacketMethod.invoke(chatCompontentText, packet);
            } catch (NoSuchMethodException | ClassNotFoundException var15) {
                var15.printStackTrace();
            } catch (IllegalAccessException var16) {
                var16.printStackTrace();
            } catch (InstantiationException var17) {
                var17.printStackTrace();
            } catch (InvocationTargetException var18) {
                var18.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }
    public static class ScoreBoard {
        Player player;
        Scoreboard scoreboard;
        public ScoreBoard(Player player) {
            this.player = player;
        }
        public void board(String display,List<String> ls) {
            ScoreboardManager scoreboardManager = Functions.instance.getServer().getScoreboardManager();
            scoreboard = scoreboardManager.getNewScoreboard();
            Team team = scoreboard.registerNewTeam(player.getName());
            String prefix = getPlayerManager().getUser(player.getUniqueId()).getPrefix();
            String suffix = getPlayerManager().getUser(player.getUniqueId()).getSuffix();
            if (prefix!=null) team.setPrefix(prefix);
            if (suffix!=null) team.setSuffix(suffix);
            team.addEntry(player.getName());
            Objective objective = scoreboard.registerNewObjective("Functions", "dummy");
            if (display == null || ls == null || display.equals("")) {
                scoreboard.clearSlot(DisplaySlot.SIDEBAR);
                player.setScoreboard(scoreboard);
                return;
            }
            objective.setDisplayName(display);
            int i = ls.size();
            int continue_count = 0;
            for (int s = 0; s < i; ++s) {
                if (ls.get(s) == null || ls.get(s).equals("")) {
                    continue_count++;
                    continue;
                }
                objective.getScore(Functions.instance.getAPI().replace(ls.get(s),player)).setScore(i - s - 1 - continue_count);
            }
            scoreboard.clearSlot(DisplaySlot.SIDEBAR);
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            player.setScoreboard(scoreboard);
        }
        public void display() {
            Team team = scoreboard.registerNewTeam(player.getName());
            String prefix = getPlayerManager().getUser(player.getUniqueId()).getPrefix();
            String suffix = getPlayerManager().getUser(player.getUniqueId()).getSuffix();
            if (prefix!=null) team.setPrefix(prefix);
            if (suffix!=null) team.setSuffix(suffix);
            team.addEntry(player.getName());
            player.setScoreboard(scoreboard);
        }
        public void sendAll(String display,List<String> ls) {
            if (Functions.instance.getAPI().send_packet.get("ScoreBoard")!=null) {
                if (System.currentTimeMillis() < Functions.instance.getAPI().send_packet.get("ScoreBoard")) {
                    return;
                }
            }
            Functions.instance.getAPI().send_packet.put("ScoreBoard",System.currentTimeMillis() + 50 * Functions.instance.getConfiguration().getSettings().getLong("ScoreBoard.sendTime",5));
            //display();
            board(display,ls);
            //player.setScoreboard(scoreboard);
        }
    }
    public static class Ping {
        int i = -1;
        public Ping(Player player) {
            String nms = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            if (!player.getClass().getName().equals("org.bukkit.craftbukkit." + nms + ".entity.CraftPlayer")) {
                player = Bukkit.getPlayer(player.getUniqueId());
            }

            try {
                Class<?> CraftPlayerClass = Class.forName("org.bukkit.craftbukkit." + nms + ".entity.CraftPlayer");
                Object CraftPlayer = CraftPlayerClass.cast(player);
                Method getHandle = CraftPlayer.getClass().getMethod("getHandle");
                Object EntityPlayer = getHandle.invoke(CraftPlayer);
                Field ping = EntityPlayer.getClass().getDeclaredField("ping");
                i = ping.getInt(EntityPlayer);
            } catch (Exception var7) {
                var7.printStackTrace();
            }
        }
        public String toString() {
            return i+"";
        }
    }
    public static class TPS {
        public static double[] recentTPS() {
            double[] d = new double[3];
            try {
                String nms = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
                Class MinecraftServerClass = Class.forName("net.minecraft.server."+ nms + ".MinecraftServer");
                Object obj = MinecraftServerClass.getMethod("getServer").invoke((Object)null);
                Field f = MinecraftServerClass.getDeclaredField("recentTps");
                return (double[])obj.getClass().getField("recentTps").get(obj);
            } catch (ClassNotFoundException var6) {
                var6.printStackTrace();
            } catch (NoSuchFieldException var7) {
                var7.printStackTrace();
            } catch (IllegalAccessException var8) {
                var8.printStackTrace();
            } catch (NoSuchMethodException var9) {
                var9.printStackTrace();
            } catch (InvocationTargetException var10) {
                var10.printStackTrace();
            }

            return new double[]{-1.0D, -2.0D, -3.0D};
        }
        public static String details_tps() {
            double[] tps = recentTPS();
            String[] tpsAvg = new String[tps.length];

            for(int i = 0; i < tps.length; ++i) {
                tpsAvg[i] = format(tps[i]);
            }
            return StringUtils.join(tpsAvg, ", ");
        }
        public static String getTPS() {
            return details_tps().split(", ")[0];
        }

        public static String format(double tps) {
            return (tps > 18.0D ? ChatColor.GREEN : (tps > 16.0D ? ChatColor.YELLOW : ChatColor.RED)).toString() + (tps > 21.0D ? "*" : "") + Math.min((double)Math.round(tps * 100.0D) / 100.0D, 20.0D);
        }
    }
    public static class sendTellRaw {
        Player player;
        public static Object getHandle(Object obj) throws Exception {
            return invokeMethod(obj, "getHandle");
        }
        public static Object invokeMethod(Object obj, String name) throws Exception {
            return invokeMethod(obj, name, true, false);
        }
        public static Object invokeMethod(Object obj, String name, boolean declared, boolean superClass) throws Exception {
            Class<?> c = superClass ? obj.getClass().getSuperclass() : obj.getClass();
            Method met = declared ? c.getDeclaredMethod(name) : c.getMethod(name);
            if (!Tab.JavaAccessibilities.isAccessible(met, obj)) {
                met.setAccessible(true);
            }

            return met.invoke(obj);
        }

        public static Field getField(Object clazz, String name) throws Exception {
            return getField(clazz, name, true);
        }
        public static Field getField(Object clazz, String name, boolean declared) throws Exception {
            return getField(clazz.getClass(), name, declared);
        }
        public static Field getField(Class<?> clazz, String name, boolean declared) throws Exception {
            Field field = declared ? clazz.getDeclaredField(name) : clazz.getField(name);
            if (!Tab.JavaAccessibilities.isAccessible(field, (Object)null)) {
                field.setAccessible(true);
            }

            return field;
        }
        public static Object getFieldObject(Object object, Field field) throws Exception {
            return field.get(object);
        }
        public static void setField(Object object, String fieldName, Object fieldValue) throws Exception {
            getField(object, fieldName).set(object, fieldValue);
        }
        public static void sendPacket(Player player, Object packet) {
            try {
                Object playerHandle = getHandle(player);
                Object playerConnection = getFieldObject(playerHandle, getField(playerHandle, "playerConnection"));
                playerConnection.getClass().getDeclaredMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
            } catch (Exception var4) {
            }

        }
        public boolean isExists(String name) {
            try {
                getNMSClass(name);
                return true;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return false;
        }
        public void send(String text) {
            text = Functions.instance.getAPI().replace(text,player);
            try {
                Class<?> ChatSerializerClass = null;
                Class<?> IClass = getNMSClass("IChatBaseComponent");
                Class<?> PacketPlayOutChatClass = getNMSClass("PacketPlayOutChat");
                if (Version.isCurrentLower(Version.v1_8_R1)) {
                    ChatSerializerClass = getNMSClass("ChatSerializer");
                } else {
                    ChatSerializerClass = getNMSClass("IChatBaseComponent$ChatSerializer");
                }
                Object chatjson = PacketPlayOutChatClass.getConstructor(getNMSClass("IChatBaseComponent")).newInstance(ChatSerializerClass.getDeclaredMethod("a",String.class).invoke(IClass,text));
                sendPacket(player,chatjson);
            } catch (ClassNotFoundException | NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        public JsonElement parse(String parse) {
            try {
                return new JsonParser().parse(Functions.instance.getAPI().replace(parse, player));
            } catch (JsonParseException e) {
                e.printStackTrace();
                Functions.instance.print(parse,"[Utils.JsonParse]");
            }
            return null;
        }
        public static void main(String[] args) {
            Scanner a = new Scanner(System.in);
            System.out.print("> ");
            while (a.hasNext()) {
                final String s = a.nextLine();
                System.out.println(IsJson(s));
                System.out.println(joinJson(s));
                System.out.print("> ");
            }
        }
        public static boolean IsJson(String text) {
            try {
                new JsonParser().parse(text);
                return true;
            } catch (JsonParseException e) {
                e.fillInStackTrace();
            }
            return false;
        }
        private static String joinJson(String text) {
            return text.replace("\\","\\\\").replace("\"","\\\"");
        }
        public void send(JsonElement text) {
            Functions.instance.print(text.toString(),"[Utils.JsonSend]");
            try {
                Class<?> ChatSerializerClass = null;
                Class<?> IClass = getNMSClass("IChatBaseComponent");
                Class<?> PacketPlayOutChatClass = getNMSClass("PacketPlayOutChat");
                if (Version.isCurrentLower(Version.v1_8_R1)) {
                    ChatSerializerClass = getNMSClass("ChatSerializer");
                } else {
                    ChatSerializerClass = getNMSClass("IChatBaseComponent$ChatSerializer");
                }
                Object chatjson = PacketPlayOutChatClass.getConstructor(getNMSClass("IChatBaseComponent")).newInstance(ChatSerializerClass.getDeclaredMethod("a",JsonElement.class).invoke(IClass,text));
                Object ichat = IClass.cast(chatjson);

                sendPacket(player,chatjson);
            } catch (ClassNotFoundException | NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }

        public sendTellRaw(Player player) {
            this.player = player;
        }

        public static Class<?> getNMSClass(String name) throws ClassNotFoundException {
            return Class.forName("net.minecraft.server." + getPackageVersion() + "." + name);
        }

        public static String getPackageVersion() {
            return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        }
    }
    public static class Fill {
        public static String replace(World world,int x, int y, int z, int dx, int dy, int dz,List<Material> anti,Material need, Material replace) {
            String s = "";
            WorldBlock t1 = new WorldBlock(world.getBlockAt(x,y,z));
            WorldBlock t2 = new WorldBlock(world.getBlockAt(dx,dy,dz));
            WorldBlock s1 = new WorldBlock(Math.min(t1.getX(),t2.getX()),Math.min(t1.getY(),t2.getY()),Math.min(t1.getZ(),t2.getZ()),world);
            WorldBlock s2 = new WorldBlock(Math.max(t1.getX(),t2.getX()),Math.max(t1.getY(),t2.getY()),Math.max(t1.getZ(),t2.getZ()),world);
            int size = (s2.getX() - s1.getX() + 1) * (s2.getY() - s1.getY() + 1) * (s2.getZ() - s1.getZ() + 1);
            int status = 0;
            int air = 0;
            if (s1.getY() >= -64 && s2.getY() < world.getMaxHeight()) {
                World w = world;
                for(int var15 = s1.getZ(); var15 <= s2.getZ(); ++var15) {
                    for (int var16 = s1.getY(); var16 <= s2.getY(); ++var16) {
                        for (int var17 = s1.getX(); var17 <= s2.getX(); ++var17) {
                            WorldBlock wb = new WorldBlock(var17, var16, var15, w);
                            boolean is = true;
                            for (Material f : anti) {
                                if (wb.getBlock() == null || wb.getBlock().getType() == f || wb.getBlock().getType() == replace) {
                                    air++;
                                    is = false;
                                }
                            }
                            if (is) {
                                if (wb.getBlock().getType() == need) {
                                    status = status + wb.set(replace);
                                }
                            }
                        }
                    }
                }
            }
            s = "{\"pos1\":\"" + t1.getX() + "," + t1.getY() + "," + t1.getZ() + "\"},{\"pos2\":\"" + t2.getX() + "," + t2.getY() + "," + t2.getZ() + "\"},{\"fillsize\":\"" + size + "\"},{\"successfill\"" + status + "\"},{\"air\"" + air + "\"}";
            return s;
        }
        public static String destroy(World world,int x, int y, int z, int dx, int dy, int dz,List<Material> anti) {
            String s = "";
            WorldBlock t1 = new WorldBlock(world.getBlockAt(x,y,z));
            WorldBlock t2 = new WorldBlock(world.getBlockAt(dx,dy,dz));
            WorldBlock s1 = new WorldBlock(Math.min(t1.getX(),t2.getX()),Math.min(t1.getY(),t2.getY()),Math.min(t1.getZ(),t2.getZ()),world);
            WorldBlock s2 = new WorldBlock(Math.max(t1.getX(),t2.getX()),Math.max(t1.getY(),t2.getY()),Math.max(t1.getZ(),t2.getZ()),world);
            int size = (s2.getX() - s1.getX() + 1) * (s2.getY() - s1.getY() + 1) * (s2.getZ() - s1.getZ() + 1);
            int status = 0;
            int air = 0;
            if (s1.getY() >= -64 && s2.getY() < world.getMaxHeight()) {
                World w = world;
                for(int var15 = s1.getZ(); var15 <= s2.getZ(); ++var15) {
                    for (int var16 = s1.getY(); var16 <= s2.getY(); ++var16) {
                        for (int var17 = s1.getX(); var17 <= s2.getX(); ++var17) {
                            WorldBlock wb = new WorldBlock(var17, var16, var15, w);
                            boolean is = true;
                            for (Material f : anti) {
                                if (wb.getBlock() == null || wb.getBlock().getType() == f || wb.getBlock().getType() == Material.AIR) {
                                    air++;
                                    is = false;
                                }
                            }
                            if (is) {
                                status = status + wb.destroy();
                            }
                        }
                    }
                }
            }
            s = "{\"pos1\":\"" + t1.getX() + "," + t1.getY() + "," + t1.getZ() + "\"},{\"pos2\":\"" + t2.getX() + "," + t2.getY() + "," + t2.getZ() + "\"},{\"fillsize\":\"" + size + "\"},{\"successfill\"" + status + "\"},{\"air\"" + air + "\"}";
            return s;
        }
        public static String Quietdestroy(World world,int x, int y, int z, int dx, int dy, int dz,List<Material> anti) {
            String s = "";
            WorldBlock t1 = new WorldBlock(world.getBlockAt(x,y,z));
            WorldBlock t2 = new WorldBlock(world.getBlockAt(dx,dy,dz));
            WorldBlock s1 = new WorldBlock(Math.min(t1.getX(),t2.getX()),Math.min(t1.getY(),t2.getY()),Math.min(t1.getZ(),t2.getZ()),world);
            WorldBlock s2 = new WorldBlock(Math.max(t1.getX(),t2.getX()),Math.max(t1.getY(),t2.getY()),Math.max(t1.getZ(),t2.getZ()),world);
            int size = (s2.getX() - s1.getX() + 1) * (s2.getY() - s1.getY() + 1) * (s2.getZ() - s1.getZ() + 1);
            int status = 0;
            int air = 0;
            if (s1.getY() >= -64 && s2.getY() < world.getMaxHeight()) {
                World w = world;
                for(int var15 = s1.getZ(); var15 <= s2.getZ(); ++var15) {
                    for (int var16 = s1.getY(); var16 <= s2.getY(); ++var16) {
                        for (int var17 = s1.getX(); var17 <= s2.getX(); ++var17) {
                            WorldBlock wb = new WorldBlock(var17, var16, var15, w);
                            boolean is = true;
                            for (Material f : anti) {
                                if (wb.getBlock() == null || wb.getBlock().getType() == f || wb.getBlock().getType() == Material.AIR) {
                                    air++;
                                    is = false;
                                }
                            }
                            if (is) {
                                status = status + wb.QuietDestroy();
                            }
                        }
                    }
                }
            }
            s = "{\"pos1\":\"" + t1.getX() + "," + t1.getY() + "," + t1.getZ() + "\"},{\"pos2\":\"" + t2.getX() + "," + t2.getY() + "," + t2.getZ() + "\"},{\"fillsize\":\"" + size + "\"},{\"successfill\"" + status + "\"},{\"air\"" + air + "\"}";
            return s;
        }
        public static String destroy(World world,int x, int y, int z, int dx, int dy, int dz,List<Material> anti,List<Material> can) {
            String s = "";
            WorldBlock t1 = new WorldBlock(world.getBlockAt(x,y,z));
            WorldBlock t2 = new WorldBlock(world.getBlockAt(dx,dy,dz));
            WorldBlock s1 = new WorldBlock(Math.min(t1.getX(),t2.getX()),Math.min(t1.getY(),t2.getY()),Math.min(t1.getZ(),t2.getZ()),world);
            WorldBlock s2 = new WorldBlock(Math.max(t1.getX(),t2.getX()),Math.max(t1.getY(),t2.getY()),Math.max(t1.getZ(),t2.getZ()),world);
            int size = (s2.getX() - s1.getX() + 1) * (s2.getY() - s1.getY() + 1) * (s2.getZ() - s1.getZ() + 1);
            int status = 0;
            int air = 0;
            if (s1.getY() >= -64 && s2.getY() < world.getMaxHeight()) {
                World w = world;
                for(int var15 = s1.getZ(); var15 <= s2.getZ(); ++var15) {
                    for (int var16 = s1.getY(); var16 <= s2.getY(); ++var16) {
                        for (int var17 = s1.getX(); var17 <= s2.getX(); ++var17) {
                            WorldBlock wb = new WorldBlock(var17, var16, var15, w);
                            boolean is = true;
                            for (Material f : anti) {
                                if (wb.getBlock() == null || wb.getBlock().getType() == f || wb.getBlock().getType() == Material.AIR) {
                                    air++;
                                    is = false;
                                }
                            }
                            if (is) {
                                for (Material f : can) {
                                    if (f.equals(wb.getBlock().getType())) {
                                        status = status + wb.destroy();
                                    }
                                }
                                air++;
                            }
                        }
                    }
                }
            }
            s = "{\"pos1\":\"" + t1.getX() + "," + t1.getY() + "," + t1.getZ() + "\"},{\"pos2\":\"" + t2.getX() + "," + t2.getY() + "," + t2.getZ() + "\"},{\"fillsize\":\"" + size + "\"},{\"successfill\"" + status + "\"},{\"air\"" + air + "\"}";
            return s;
        }
        public static String Quietdestroy(World world,int x, int y, int z, int dx, int dy, int dz,List<Material> anti,List<Material> can) {
            String s = "";
            WorldBlock t1 = new WorldBlock(world.getBlockAt(x,y,z));
            WorldBlock t2 = new WorldBlock(world.getBlockAt(dx,dy,dz));
            WorldBlock s1 = new WorldBlock(Math.min(t1.getX(),t2.getX()),Math.min(t1.getY(),t2.getY()),Math.min(t1.getZ(),t2.getZ()),world);
            WorldBlock s2 = new WorldBlock(Math.max(t1.getX(),t2.getX()),Math.max(t1.getY(),t2.getY()),Math.max(t1.getZ(),t2.getZ()),world);
            int size = (s2.getX() - s1.getX() + 1) * (s2.getY() - s1.getY() + 1) * (s2.getZ() - s1.getZ() + 1);
            int status = 0;
            int air = 0;
            if (s1.getY() >= -64 && s2.getY() < world.getMaxHeight()) {
                World w = world;
                for(int var15 = s1.getZ(); var15 <= s2.getZ(); ++var15) {
                    for (int var16 = s1.getY(); var16 <= s2.getY(); ++var16) {
                        for (int var17 = s1.getX(); var17 <= s2.getX(); ++var17) {
                            WorldBlock wb = new WorldBlock(var17, var16, var15, w);
                            boolean is = true;
                            for (Material f : anti) {
                                if (wb.getBlock() == null || wb.getBlock().getType() == f || wb.getBlock().getType() == Material.AIR) {
                                    air++;
                                    is = false;
                                }
                            }
                            if (is) {
                                for (Material f : can) {
                                    if (f.equals(wb.getBlock().getType())) {
                                        status = status + wb.QuietDestroy();
                                    }
                                }
                                air++;
                            }
                        }
                    }
                }
            }
            s = "{\"pos1\":\"" + t1.getX() + "," + t1.getY() + "," + t1.getZ() + "\"},{\"pos2\":\"" + t2.getX() + "," + t2.getY() + "," + t2.getZ() + "\"},{\"fillsize\":\"" + size + "\"},{\"successfill\"" + status + "\"},{\"air\"" + air + "\"}";
            return s;
        }
        public static String destroy(World world,int x, int y, int z, int dx, int dy, int dz,List<Material> anti,boolean quiet) {
            return quiet ? Quietdestroy(world, x, y, z, dx, dy, dz, anti) : destroy(world, x, y, z, dx, dy, dz, anti);
        }
        public static String destroy(World world,int x, int y, int z, int dx, int dy, int dz,List<Material> anti,List<Material> can,boolean quiet) {
            return quiet ? Quietdestroy(world, x, y, z, dx, dy, dz, anti,can) : destroy(world, x, y, z, dx, dy, dz, anti,can);
        }
        public static String keep(World world,int x, int y, int z, int dx, int dy, int dz,List<Material> anti,Material keep) {
            String s = "";
            WorldBlock t1 = new WorldBlock(world.getBlockAt(x,y,z));
            WorldBlock t2 = new WorldBlock(world.getBlockAt(dx,dy,dz));
            WorldBlock s1 = new WorldBlock(Math.min(t1.getX(),t2.getX()),Math.min(t1.getY(),t2.getY()),Math.min(t1.getZ(),t2.getZ()),world);
            WorldBlock s2 = new WorldBlock(Math.max(t1.getX(),t2.getX()),Math.max(t1.getY(),t2.getY()),Math.max(t1.getZ(),t2.getZ()),world);
            int size = (s2.getX() - s1.getX() + 1) * (s2.getY() - s1.getY() + 1) * (s2.getZ() - s1.getZ() + 1);
            int status = 0;
            int air = 0;
            if (s1.getY() >= -64 && s2.getY() < world.getMaxHeight()) {
                World w = world;
                for(int var15 = s1.getZ(); var15 <= s2.getZ(); ++var15) {
                    for (int var16 = s1.getY(); var16 <= s2.getY(); ++var16) {
                        for (int var17 = s1.getX(); var17 <= s2.getX(); ++var17) {
                            WorldBlock wb = new WorldBlock(var17, var16, var15, w);
                            boolean is = true;
                            for (Material f : anti) {
                                if (wb.getBlock() != null || wb.getBlock().getType() == f || wb.getBlock().getType() == keep) {
                                    air++;
                                    is = false;
                                }
                            }
                            if (is) {
                                status = status + wb.set(keep);
                            }
                        }
                    }
                }
            }
            s = "{\"pos1\":\"" + t1.getX() + "," + t1.getY() + "," + t1.getZ() + "\"},{\"pos2\":\"" + t2.getX() + "," + t2.getY() + "," + t2.getZ() + "\"},{\"fillsize\":\"" + size + "\"},{\"successfill\"" + status + "\"},{\"air\"" + air + "\"}";
            return s;
        }
        public static String fill(World world,int x, int y, int z, int dx, int dy, int dz,List<Material> anti,String mode,Material block) {
            String s = "";
            WorldBlock t1 = new WorldBlock(world.getBlockAt(x,y,z));
            WorldBlock t2 = new WorldBlock(world.getBlockAt(dx,dy,dz));
            WorldBlock s1 = new WorldBlock(Math.min(t1.getX(),t2.getX()),Math.min(t1.getY(),t2.getY()),Math.min(t1.getZ(),t2.getZ()),world);
            WorldBlock s2 = new WorldBlock(Math.max(t1.getX(),t2.getX()),Math.max(t1.getY(),t2.getY()),Math.max(t1.getZ(),t2.getZ()),world);
            int size = (s2.getX() - s1.getX() + 1) * (s2.getY() - s1.getY() + 1) * (s2.getZ() - s1.getZ() + 1);
            int status = 0;
            int air = 0;
            if (s1.getY() >= -64 && s2.getY() < world.getMaxHeight()) {
                World w = world;
                for(int var15 = s1.getZ(); var15 <= s2.getZ(); ++var15) {
                    for (int var16 = s1.getY(); var16 <= s2.getY(); ++var16) {
                        for (int var17 = s1.getX(); var17 <= s2.getX(); ++var17) {
                            WorldBlock wb = new WorldBlock(var17, var16, var15, w);
                            boolean is = true;
                            for (Material f : anti) {
                                if (wb.getBlock() == null || wb.getBlock().getType() == f || wb.getBlock().getType() == block) {
                                    air++;
                                    is = false;
                                }
                            }
                            if (is) {
                                status = status + wb.set(block);
                            }
                        }
                    }
                }
            }
            s = "{\"pos1\":\"" + t1.getX() + "," + t1.getY() + "," + t1.getZ() + "\"},{\"pos2\":\"" + t2.getX() + "," + t2.getY() + "," + t2.getZ() + "\"},{\"fillsize\":\"" + size + "\"},{\"successfill\"" + status + "\"},{\"air\"" + air + "\"}";
            return s;
        }
        public static String fill(World world, int x, int y, int z, int dx, int dy, int dz, List<Material> anti, Material block, Player player) {
            String s = "";
            WorldBlock t1 = new WorldBlock(world.getBlockAt(x,y,z));
            WorldBlock t2 = new WorldBlock(world.getBlockAt(dx,dy,dz));
            WorldBlock s1 = new WorldBlock(Math.min(t1.getX(),t2.getX()),Math.min(t1.getY(),t2.getY()),Math.min(t1.getZ(),t2.getZ()),world);
            WorldBlock s2 = new WorldBlock(Math.max(t1.getX(),t2.getX()),Math.max(t1.getY(),t2.getY()),Math.max(t1.getZ(),t2.getZ()),world);
            int size = (s2.getX() - s1.getX() + 1) * (s2.getY() - s1.getY() + 1) * (s2.getZ() - s1.getZ() + 1);
            int status = 0;
            int air = 0;
            int airblock = 0;
            if (s1.getY() >= -64 && s2.getY() < world.getMaxHeight()) {
                World w = world;
                for(int var15 = s1.getZ(); var15 <= s2.getZ(); ++var15) {
                    for (int var16 = s1.getY(); var16 <= s2.getY(); ++var16) {
                        for (int var17 = s1.getX(); var17 <= s2.getX(); ++var17) {
                            WorldBlock wb = new WorldBlock(var17, var16, var15, w);
                            boolean is = true;
                            for (Material f : anti) {
                                if (wb.isBlock()) {
                                    air++;
                                    is = false;
                                } else if (wb.getBlock().getType() == f) {
                                    air++;
                                    is = false;
                                } else if (wb.getBlock().getType() == block) {
                                    air++;
                                    is = false;
                                }
                            }
                            if (is) {
                                PlayerInventory inventory = player.getInventory();
                                for (int i = 0; i < inventory.getSize(); i++) {
                                    ItemStack istack = inventory.getItem(i);
                                    if (istack!=null) {
                                        if (istack.getType().equals(block)) {
                                            if (wb.getBlock().getType().equals(istack.getType())) {
                                                continue;
                                            }
                                            istack.setAmount(istack.getAmount() - 1);
                                            status = status + wb.set(block);
                                            inventory.setItem(i, istack);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            s = "{\"pos1\":\"" + t1.getX() + "," + t1.getY() + "," + t1.getZ() + "\"},{\"pos2\":\"" + t2.getX() + "," + t2.getY() + "," + t2.getZ() + "\"},{\"fillsize\":\"" + size + "\"},{\"successfill\"" + status + "\"},{\"air\"" + air + "\"},{\"airblock\":\"" + airblock + "\"}";
            return s;
        }
    }
    public enum Version {
            v1_8_R1,
            v1_8_R2,
            v1_8_R3,
            v1_9_R1,
            v1_9_R2,
            v1_10_R1,
            v1_11_R1,
            v1_12_R1,
            v1_13_R1,
            v1_13_R2,
            v1_14_R1,
            v1_14_R2,
            v1_15_R1,
            v1_15_R2,
            v1_16_R1,
            v1_16_R2,
            v1_16_R3,
            v1_17_R1,
            v1_17_R2,
            v1_18_R1,
            v1_18_R2;

            private Integer value = Integer.valueOf(this.name().replaceAll("[^\\d.]", ""));
            private String shortVersion = this.name().substring(0, this.name().length() - 3);
            private static Version current;

            private Version() {
            }

            public Integer getValue() {
                return this.value;
            }

            public static Version getCurrent() {
                if (current != null) {
                    return current;
                } else {
                    String[] v = Bukkit.getServer().getClass().getPackage().getName().split("\\.");
                    String vv = v[v.length - 1];
                    Version[] var2 = values();
                    int var3 = var2.length;

                    for (int var4 = 0; var4 < var3; ++var4) {
                        Version one = var2[var4];
                        if (one.name().equalsIgnoreCase(vv)) {
                            current = one;
                            break;
                        }
                    }

                    return current;
                }
            }

            public static boolean isCurrentEqualOrHigher(Version v) {
                return getCurrent().getValue() >= v.getValue();
            }

            public static boolean isCurrentLower(Version v) {
                return getCurrent().getValue() < v.getValue();
            }

            public static boolean isCurrentEqualOrLower(Version v) {
                return getCurrent().getValue() <= v.getValue();
            }

            public static boolean isCurrentEqual(Version v) {
                return getCurrent().getValue().equals(v.getValue());
            }
        }

}
