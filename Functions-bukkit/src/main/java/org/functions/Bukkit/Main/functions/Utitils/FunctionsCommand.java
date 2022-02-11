package org.functions.Bukkit.Main.functions.Utitils;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.command.defaults.PluginsCommand;
import org.bukkit.plugin.Plugin;
import org.functions.Bukkit.Main.Functions;
import sun.misc.Unsafe;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiConsumer;

public class FunctionsCommand {
    public static SimpleCommandMap getCommands() throws Exception {
        return (SimpleCommandMap) invokeMethod(Bukkit.getServer().getClass(),Bukkit.getServer(),"getCommandMap");
    }
    public static void register(Command command) throws Exception {
        getCommands().register("functions",command);
        commands.clear();
    }
    public static boolean contains(String cmd) {
        try {
            for (String s : getKnownCommands().keySet()) {
                if (s.equalsIgnoreCase(cmd)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        return false;
    }
    static List<Command> commands = new ArrayList<>();
    public static void ready_register(Command command) {
        commands.add(command);
    }
    static Class<?> pluginCommand;
    static Constructor<?> pluginCommand_Constructor;
    public static PluginCommand createPluginCommand(String cmd) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        if (pluginCommand == null) {
            pluginCommand = Class.forName("org.bukkit.command.PluginCommand");
        }
        if (pluginCommand_Constructor==null) {
            pluginCommand_Constructor = pluginCommand.getDeclaredConstructor(String.class,Plugin.class);
        }
        pluginCommand_Constructor.setAccessible(true);
        return (PluginCommand) pluginCommand_Constructor.newInstance(cmd,Functions.instance);
    }
    public static Command createCommand(String cmd,String[] aliases,String description,TabExecutor executor) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Command command = createPluginCommand(cmd);
        command.setName(cmd);
        command = command.setDescription(description);
        if (aliases != null) {
            if (aliases.length != 0) {
                command = command.setAliases(Arrays.asList(aliases));
            }
        }
        return command;
    }
    public static void register(String cmd,String[] aliases,String description,TabExecutor executor) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ready_register(createCommand(cmd, aliases, description, executor));
    }
    private static boolean isBlank(String text) {
        if (text == null) {
            return true;
        } else if (text.equals("")) {
            return true;
        } else if (text.equals(" ")) {
            return true;
        } else if (text.startsWith(" ")) {
            return true;
        } else {
            for (int i = 0; i < text.length(); i++) {
                if (text.startsWith(" ")) {
                    text = text.substring(1);
                }
            }
        }
        return false;
    }
    public static void registerAll() {
        commands.forEach(e->{
            try {
                getCommands().register("functions",e);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
    @SuppressWarnings("unchecked")
    public static Map<String, Command> getKnownCommands() throws Exception {
        return (Map<String, Command>) invokeMethod(getCommands().getClass(),getCommands(),"getKnownCommands");
    }
    private static Method getMethod(Class<?> clazz,String name,Class<?>... paramTypes) throws NoSuchMethodException {
        return setMethodAccessible(clazz.getDeclaredMethod(name,paramTypes));
    }
    private static Object invokeMethod(Class<?> clazz, Object object, String name) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return getMethod(clazz,name).invoke(object);
    }
    private static Method setMethodAccessible(Method method) {
        if (!method.isAccessible()) {
            method.setAccessible(true);
        }
        return method;
    }

}