package org.functions.Bukkit.API;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.functions.Bukkit.Main.Functions;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Listeners {

    private final List<TabExecutor> commands = new ArrayList<>();
    private final Plugin plugin;
    private final String packageName;

    public Listeners(Plugin plugin, String packageName) {
        this.plugin = plugin;
        this.packageName = packageName;
    }
    public void register() {
        try {
            FPI fpi = Functions.instance.getAPI();
            String jar = URLDecoder.decode(this.plugin.getClass().getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
            ZipFile zip = new ZipFile(jar);
            // 遍历插件压缩包內所有内容
            Enumeration<? extends ZipEntry> files = zip.entries();
            while (files.hasMoreElements()) {
                String url = files.nextElement().getName().replace("\"", "/");
                // 只认 .class 文件
                if (url.toLowerCase().endsWith(".class")) {
                    // 去除 .class 后缀并替换分隔符为.
                    url = url.substring(0, url.toLowerCase().lastIndexOf(".class")).replace('/', '.').replace('\\', '.');
                    // 条件: 在commands包内，不是子类
                    if (url.startsWith(packageName + ".")) {
                        Class<?> c = Class.forName(url);
                        fpi.registerEvents((Listener) c.newInstance());
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
