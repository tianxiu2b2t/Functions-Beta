package org.functions.Bukkit.Main.functions.Messaging;

import java.util.LinkedHashMap;

public class RegisterMessaging {
    LinkedHashMap<String,ListenerMessaging> listenerMessaging = new LinkedHashMap<>();

    public LinkedHashMap<String, ListenerMessaging> getRegisterMessaging() {
        return listenerMessaging;
    }

    public RegisterMessaging() {
        listenerMessaging.put("ServerTeleport", (ListenerMessaging) new ServerTeleportMessaging());
    }
    public void run() {
        listenerMessaging.forEach((e,a)->{a.onEnable();});
    }
    public void end() {
        listenerMessaging.forEach((e,a)->{a.onDisable();});
        listenerMessaging.clear();
    }
}
