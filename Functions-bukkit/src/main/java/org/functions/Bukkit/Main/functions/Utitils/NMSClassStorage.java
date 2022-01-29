package org.functions.Bukkit.Main.functions.Utitils;

import com.google.gson.*;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.functions.Bukkit.Main.Functions;


import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class NMSClassStorage {
    public Class<?> IChatBaseComponent;
    public Class<?> Packet;
    public Class<?> PacketPlayOutPlayerListHeaderFooter;
    public Class<?> PacketPlayOutChatClass;
    public Class<?> ChatMessageType;
    public Class<?> MinecraftServer;
    public Class<?> ChatSerializer;
    public Class<?> EntityPlayer;
    public Class<?> CraftPlayer;
    public Constructor<?> PacketPlayOutChat;
    public Field PacketPlayOutPlayerListHeaderFooter_HEADER;
    public Field PacketPlayOutPlayerListHeaderFooter_FOOTER;
    public Field PING;
    public Object[] ChatMessageTypeValues;
    public LinkedHashMap<String,Object> IChatBaseComponents = new LinkedHashMap<>();
    public NMSClassStorage() {
        try {
            init();
        } catch (ClassNotFoundException | NoSuchMethodException | NoSuchFieldException e) {
        }
    }
    public void init() throws ClassNotFoundException, NoSuchMethodException, NoSuchFieldException {
        MinecraftServer = getNMSClass("server.MinecraftServer");
        IChatBaseComponent = getNMSClass("network.chat.IChatBaseComponent");
        if (IChatBaseComponent.getDeclaredClasses().length == 0) {
            ChatSerializer = getNMSClass("ChatSerializer");
        } else {
            ChatSerializer = IChatBaseComponent.getDeclaredClasses()[0];
        }
        Packet = getNMSClass("network.protocol.Packet");
        PacketPlayOutPlayerListHeaderFooter = getNMSClass("network.protocol.game.PacketPlayOutPlayerListHeaderFooter");
        PacketPlayOutPlayerListHeaderFooter_HEADER = (Field)getFields(PacketPlayOutPlayerListHeaderFooter, IChatBaseComponent).get(0);
        PacketPlayOutPlayerListHeaderFooter_FOOTER = (Field)getFields(PacketPlayOutPlayerListHeaderFooter, IChatBaseComponent).get(1);
        PacketPlayOutChatClass = getNMSClass("network.protocol.game.PacketPlayOutChat", "PacketPlayOutChat", "Packet3Chat");
        if (ProtocolVersion.getServerVersion().getMinorVersion() >= 12) {
            ChatMessageType = getNMSClass("net.minecraft.network.chat.ChatMessageType", "ChatMessageType");
            ChatMessageTypeValues = getEnumValues(ChatMessageType);
        }
        if (ProtocolVersion.getServerVersion().getMinorVersion() >= 16) {
            PacketPlayOutChat = PacketPlayOutChatClass.getConstructor(this.IChatBaseComponent, this.ChatMessageType, UUID.class);
        } else if (ProtocolVersion.getServerVersion().getMinorVersion() >= 12) {
            PacketPlayOutChat = PacketPlayOutChatClass.getConstructor(this.IChatBaseComponent, this.ChatMessageType);
        } else if (ProtocolVersion.getServerVersion().getMinorVersion() >= 8) {
            PacketPlayOutChat = PacketPlayOutChatClass.getConstructor(this.IChatBaseComponent, Byte.TYPE);
        } else if (ProtocolVersion.getServerVersion().getMinorVersion() >= 7) {
            PacketPlayOutChat = PacketPlayOutChatClass.getConstructor(this.IChatBaseComponent);
        }
        EntityPlayer = getNMSClass("server.level.EntityPlayer", "EntityPlayer");
        CraftPlayer = getCraftClass("entity.CraftPlayer");
        PING = getField(EntityPlayer, "ping", "latency", "field_71138_i", "field_13967", "e");
    }
    public Class<?> getNMSClass(String name) throws ClassNotFoundException {
        if (ProtocolVersion.getServerVersion().getMinorVersion() >= 17) {
            return Class.forName("net.minecraft." + name);
        }
        return Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + name.split("\\.")[name.split("\\.").length - 1]);
    }
    private Class<?> getLegacyClass(String name) throws ClassNotFoundException {
        try {
            return Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + name);
        } catch (NullPointerException | ClassNotFoundException var5) {
            try {
                Class<?> clazz = Functions.class.getClassLoader().loadClass("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + name);
                if (clazz != null) {
                    return clazz;
                } else {
                    throw new ClassNotFoundException(name);
                }
            } catch (NullPointerException | ClassNotFoundException var4) {
                return Class.forName(name);
            }
        }
    }
    public Class<?> getNMSClass(String name,String... names) throws ClassNotFoundException {
        if (ProtocolVersion.getServerVersion().getMinorVersion() >= 17) {
            return getNMSClass(name);
        } else {
            int var4 = names.length;
            int var5 = 0;

            while(var5 < var4) {
                String namef = names[var5];

                try {
                    return getLegacyClass(namef);
                } catch (ClassNotFoundException var8) {
                    ++var5;
                }
            }
            throw new ClassNotFoundException("No class found with possible names {modern = " + name + ", legacy = " + Arrays.toString(names) + "}");
        }
    }
    public Class<?> getCraftClass(String name) throws ClassNotFoundException {
        return Class.forName("org.bukkit.craftbukkit." +  Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + name);
    }
    public Object invokeMethod(Object obj, String name) throws Exception {
        return invokeMethod(obj, name, true, false);
    }
    public Object invokeMethod(Object obj, String name, boolean declared, boolean superClass) throws Exception {
        Class<?> c = superClass ? obj.getClass().getSuperclass() : obj.getClass();
        Method met = declared ? c.getDeclaredMethod(name) : c.getMethod(name);
        if (!isAccessible(met, obj)) {
            met.setAccessible(true);
        }

        return met.invoke(obj);
    }
    public Object getFieldObject(Object object, Field field) throws Exception {
        return field.get(object);
    }
    public Field getField(Object clazz, String name) throws Exception {
        return getField(clazz, name, true);
    }
    public Field getField(Object clazz, String name, boolean declared) throws Exception {
        return getField(clazz.getClass(), name, declared);
    }
    public Field getField(Class<?> clazz, String name, boolean declared) throws Exception {
        Field field = declared ? clazz.getDeclaredField(name) : clazz.getField(name);
        if (!isAccessible(field, (Object)null)) {
            field.setAccessible(true);
        }

        return field;
    }
    private List<Field> getFields(Class<?> clazz, Class<?> type) {
        if (clazz == null) {
            throw new IllegalArgumentException("Source class cannot be null");
        } else {
            List<Field> list = new ArrayList<>();
            Field[] var4 = clazz.getDeclaredFields();
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                Field field = var4[var6];
                if (field.getType() == type) {
                    list.add((Field)setAccessible(field));
                }
            }

            return list;
        }
    }
    public void setField(Object object, String fieldName, Object fieldValue) throws Exception {
        getField(object, fieldName).set(object, fieldValue);
    }
    public void setField(Object object, Field field, Object fieldValue) throws Exception {
        field.set(object,fieldValue);
    }
    private Method getMethod(Class<?> clazz, String[] names, Class<?>... parameterTypes) throws NoSuchMethodException {
        String[] var4 = names;
        int var5 = names.length;
        int var6 = 0;

        while(var6 < var5) {
            String name = var4[var6];

            try {
                return this.getMethod(clazz, name, parameterTypes);
            } catch (NoSuchMethodException var9) {
                ++var6;
            }
        }

        throw new NoSuchMethodException("No method found with possible names " + Arrays.toString(names) + " with parameters " + Arrays.toString(parameterTypes) + " in class " + clazz.getName());
    }
    private Method getMethod(Class<?> clazz, String name, Class<?>... parameterTypes) throws NoSuchMethodException {
        List<Method> list = new ArrayList<>();
        Method[] var5 = clazz.getMethods();
        int var6 = var5.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            Method m = var5[var7];
            if (m.getName().equals(name) && m.getParameterCount() == parameterTypes.length) {
                Class<?>[] types = m.getParameterTypes();
                boolean valid = true;

                for(int i = 0; i < types.length; ++i) {
                    if (types[i] != parameterTypes[i]) {
                        valid = false;
                        break;
                    }
                }

                if (valid) {
                    list.add(m);
                }
            }
        }

        if (!list.isEmpty()) {
            return (Method)list.get(0);
        } else {
            throw new NoSuchMethodException("No method found with name " + name + " in class " + clazz.getName() + " with parameters " + Arrays.toString(parameterTypes));
        }
    }
    private List<Method> getMethods(Class<?> clazz, Class<?> returnType, Class<?>... parameterTypes) {
        List<Method> list = new ArrayList<>();
        Method[] var5 = clazz.getDeclaredMethods();
        int var6 = var5.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            Method m = var5[var7];
            if (m.getReturnType() == returnType && m.getParameterCount() == parameterTypes.length && Modifier.isPublic(m.getModifiers())) {
                Class<?>[] types = m.getParameterTypes();
                boolean valid = true;

                for(int i = 0; i < types.length; ++i) {
                    if (types[i] != parameterTypes[i]) {
                        valid = false;
                        break;
                    }
                }

                if (valid) {
                    list.add(m);
                }
            }
        }

        return list;
    }
    private Field getField(Class<?> clazz, String name) throws NoSuchFieldException {
        Field[] var3 = clazz.getDeclaredFields();
        int var4 = var3.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            Field f = var3[var5];
            if (f.getName().equals(name) || f.getName().split("_").length == 3 && f.getName().split("_")[2].equals(name)) {
                return (Field)this.setAccessible(f);
            }
        }

        throw new NoSuchFieldException("Field \"" + name + "\" was not found in class " + clazz.getName());
    }
    private Field getField(Class<?> clazz, String... potentialNames) throws NoSuchFieldException {
        String[] var3 = potentialNames;
        int var4 = potentialNames.length;
        int var5 = 0;

        while(var5 < var4) {
            String name = var3[var5];

            try {
                return this.getField(clazz, name);
            } catch (NoSuchFieldException var8) {
                ++var5;
            }
        }

        throw new NoSuchFieldException("No field found in class " + clazz.getName() + " with potential names " + Arrays.toString(potentialNames));
    }
    public boolean isAccessible(Field field, Object target) {
        if (getCurrentVersion() >= 9 && target != null) {
            try {
                return (Boolean) field.getClass().getDeclaredMethod("canAccess", Object.class).invoke(field, target);
            } catch (NoSuchMethodException var3) {
            } catch (Exception var4) {
                var4.printStackTrace();
            }
        }

        return field.isAccessible();
    }
    public boolean isAccessible(Method method, Object target) {
        if (getCurrentVersion() >= 9 && target != null) {
            try {
                return (Boolean) method.getClass().getDeclaredMethod("canAccess", Object.class).invoke(method, target);
            } catch (NoSuchMethodException var3) {
            } catch (Exception var4) {
                var4.printStackTrace();
            }
        }

        return method.isAccessible();
    }
    public <T extends AccessibleObject> T setAccessible(T o) {
        o.setAccessible(true);
        return o;
    }
    public int getCurrentVersion() {
        String currentVersion = System.getProperty("java.version");
        if (currentVersion.contains("_")) {
            currentVersion = currentVersion.split("_")[0];
        }

        currentVersion = currentVersion.replaceAll("[^\\d]|_", "");

        for (int i = 8; i <= 18; ++i) {
            if (currentVersion.contains(Integer.toString(i))) {
                return i;
            }
        }

        return 0;
    }
    public Class<?> getClass(Class<?> inClass,String name,boolean Declared) throws ClassNotFoundException {
        for (Class<?> clazz : Declared ? inClass.getDeclaredClasses() : inClass.getClasses()) {
            if (clazz.getName().equalsIgnoreCase(name)) {
                return clazz;
            }
        }
        throw new ClassNotFoundException("Not name was not found in this class " + inClass.getName());
    }
    private static final Gson GSON = (new GsonBuilder()).create();
    private static final List<JsonObject> JSONLIST = new CopyOnWriteArrayList();
    public synchronized Object getAsIChatBaseComponent(String text) throws Exception {
        if (IChatBaseComponents.size() >= 10000) {
            IChatBaseComponents.clear();
        }
        Object object;
        if ((object = IChatBaseComponents.get(text)) != null) {
            return object;
        }
        if (getJSONType(text)) {
            object = getAsIChatBaseComponentJson(text);
            IChatBaseComponents.put(text,object);
            return object;
        }
        if (ProtocolMirrorVersion.getServerVersion().getMinorVersion() <= 81) {
            object = IChatBaseComponent.cast(ChatSerializer.getMethod("a", String.class).invoke(ChatSerializer, "{\"text\":\"" + text + "\"}"));
        } else {
            object = ChatSerializer.getMethod("a", String.class).invoke(IChatBaseComponent, "{\"text\":\"" + text + "\"}");
        }
        IChatBaseComponents.put(text,object);
        return object;
    }
    public synchronized Object getAsIChatBaseComponentJson(String text) throws Exception {
        if (ProtocolMirrorVersion.getServerVersion().getMinorVersion() <= 81) {
            return IChatBaseComponent.cast(ChatSerializer.getMethod("a", String.class).invoke(ChatSerializer, text));
        } else {
            return ChatSerializer.getMethod("a", String.class).invoke(IChatBaseComponent, text);
        }
    }
    public Object getHandle(Object obj) throws Exception {
        return invokeMethod(obj, "getHandle");
    }
    public void sendPacket(Player player, Object packet) {
        try {
            Object playerHandle = getHandle(player);
            // playerHandle.get(playerHandle.getClass().getDeclaredField("playerConnection"))
            Object playerConnection = getFieldObject(playerHandle, getField(playerHandle, "playerConnection"));
            playerConnection.getClass().getDeclaredMethod("sendPacket", Packet).invoke(playerConnection, packet);
        } catch (Exception var4) {
        }
    }
    public Object getEntityPlayer(Player player) throws Exception {
        return getHandle(player);
    }
    private Object[] getEnumValues(Class<?> enumClass) {
        if (enumClass == null) {
            throw new IllegalArgumentException("Class cannot be null");
        } else if (!enumClass.isEnum()) {
            throw new IllegalArgumentException(enumClass.getName() + " is not an enum class");
        } else {
            try {
                return (Enum[])enumClass.getMethod("values").invoke((Object)null);
            } catch (ReflectiveOperationException var3) {
                return new Enum[0];
            }
        }
    }
    public Object getChatEnumType(String type) {
        for (Object object : ChatMessageTypeValues) {
            if (object.toString().equalsIgnoreCase(type)) {
                return object;
            }
        }
        return null;
    }
    public static boolean getJSONType(String str) {
        if (StringUtils.isNotBlank(str)) {
            str = str.trim();
            if (str.startsWith("{") && str.endsWith("}")) {
                return true;
            } else return str.startsWith("[") && str.endsWith("]");
        }
        return false;
    }
}
