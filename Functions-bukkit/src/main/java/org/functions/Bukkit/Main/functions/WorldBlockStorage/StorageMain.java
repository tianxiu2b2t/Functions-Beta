package org.functions.Bukkit.Main.functions.WorldBlockStorage;

import java.io.*;

public class StorageMain {
    File dir;
    public StorageMain(File dir) {
        this.dir = dir;
    }
    public StorageRead getStorageRead() {
        return new StorageRead(dir);
    }
}
