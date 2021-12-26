package org.functions.Bukkit.API;

import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
@SuppressWarnings("all")
public class Commands {

    private final List<TabExecutor> commands = new ArrayList<>();
    private final Plugin plugin;
    private final String packageName;

    public Commands(Plugin plugin, String packageName) {
        this.plugin = plugin;
        this.packageName = packageName;
    }
        public void execute(Object obj, String ClassName, String MethodName) {
            Class cls = null;
            try {
                cls = Class.forName(ClassName);
            } catch (ClassNotFoundException e) {
                // 通过ClassName反射获取该类失败
                e.printStackTrace();
            }
            Method method = null;
            try {
                assert cls != null;
                method = cls.getDeclaredMethod(MethodName, String.class);
            } catch (SecurityException e) {
                // 通过MethodName反射获取该方法失败，SecurityManager校验失败
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                // 通过MethodName反射获取该方法失败，该方法不存在
                e.printStackTrace();
            }
            try {
                method.invoke(obj, null);
            } catch (IllegalArgumentException e) {
                // 反射执行该方法失败，参数不正确
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // 反射执行该方法失败，无法执行
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // 反射执行该方法失败，该方法本身抛出异常
                e.printStackTrace();
            }
        }

        public void register() {
        try {
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
                            if (!url.contains("AntiLoad")) {
                                if (!url.endsWith("anti.class")) {
                                    for (Method e : c.getMethods()) {
                                        if (e.getName().equals("run")) {
                                            c.getMethod("run", null).invoke(c.newInstance(), null);
                                        } else if (e.getName().startsWith("get")) {
                                            Object cmd = e.invoke(c.newInstance(), null);
                                        }
                                    }
                                }
                            }
                        }
                }
            }
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
