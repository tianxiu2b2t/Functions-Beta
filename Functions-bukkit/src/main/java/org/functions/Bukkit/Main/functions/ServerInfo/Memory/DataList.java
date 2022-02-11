package org.functions.Bukkit.Main.functions.ServerInfo.Memory;

import java.util.LinkedList;

public class DataList extends LinkedList<Data> {

    public boolean add(Data data) {
        if (this.size() >= 348) {
            this.remove();
        }

        return super.add(data);
    }
}
