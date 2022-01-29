package org.functions.Bukkit.Main.functions.WorldBlockStorage;

import org.bukkit.entity.Player;
import org.functions.Bukkit.API.WorldBlock;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class Storage extends StorageFile {
    String name;
    List<WorldBlock> blocks;
    LinkedHashMap<String,String> link;
    private Storage(File file, String name, List<WorldBlock> blocks) {
        super(file);
        this.name = name;
        this.blocks = blocks;
    }
    public Storage(File file, String name, List<WorldBlock> blocks, LinkedHashMap<String,String> link) {
        super(file);
        this.name = name;
        this.blocks = blocks;
        this.link = link;
    }
    Player player;
    private Storage(Player player, File file, String name, List<WorldBlock> blocks) {
        this(file,name,blocks);
        this.player = player;
    }
    public Storage(Player player, File file, String name, List<WorldBlock> blocks, LinkedHashMap<String,String> link) {
        this(file,name,blocks);
        this.player = player;
        this.link = link;
    }
    public List<WorldBlock> getBlocks() {
        return blocks;
    }
    public List<String> getBlocksPosition() {
        return Arrays.asList(link.get("PositionAndBlocks").split("|"));
    }
}
