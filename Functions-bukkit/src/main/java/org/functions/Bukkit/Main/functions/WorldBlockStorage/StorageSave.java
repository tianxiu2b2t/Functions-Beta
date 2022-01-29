package org.functions.Bukkit.Main.functions.WorldBlockStorage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class StorageSave extends StorageMain {
    File dir;
    public StorageSave(File dir) {
        super(dir);
        this.dir = dir;
    }
    public void saveFile(Storage storage) {
        try {
            File file = storage.file;
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            if (!file.exists()) {
                file.createNewFile();
            }
            writer.write("name:" + storage.name);
            writer.write("initPosition:" + storage.link.get("initPosition"));
            writer.write("PositionAndBlocks:" + storage.link.get("PositionAndBlocks"));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
