package org.functions.Bungee.Main.functions;

import net.md_5.bungee.api.config.ServerInfo;
import org.functions.Bungee.Main.Functions;

import java.util.LinkedHashMap;

public class Servers {
    public static LinkedHashMap<String,Server> getServers() {
        LinkedHashMap<String, Server> link = new LinkedHashMap<>();
        if (Functions.instance.getProxy().getServersCopy().size() == 0) {
            return link;
        }
        Functions.instance.getProxy().getServersCopy().forEach((e,f)->{
            link.put(e,new Server(e,f));
        });
        return link;
    }
}
