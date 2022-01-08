package org.functions.Bukkit.Main.functions;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.functions.Bukkit.API.ClickPerSeconds;
import org.functions.Bukkit.API.SpeedPerSeconds;

import java.util.List;

public interface IUser {
    void setPermissions(List<String> permissions);
    boolean addPermissions(String name);
    boolean removePermissions(String name);
    List<String> getPermissions();
    List<String> getOtherPermissions();
    List<String> getFunctionsPermissions();
    void setPrefixes(List<String> prefixes);
    void setPrefix(String prefix);
    String getPrefix();
    void setSuffixes(List<String> Suffixes);
    List<String> getSuffixes();
    void setSuffix(String suffix);
    String getSuffix();
    boolean addPrefixes(String name);
    boolean addSuffixes(String name);
    @Deprecated
    boolean removePrefixes(String name);
    String removePrefixes(int number);
    @Deprecated
    boolean removeSuffixes(String name);
    Group getGroup();
    void setGroup(String name);
    Economy getEconomy();
    Bank getBank();
    Account getAccount();
    OfflinePlayer getOfflinePlayer();
    Player getPlayer();
    boolean exists();
    ClickPerSeconds getCPS();
    SpeedPerSeconds getSPS();
    boolean isHiding();
    void setInvisibility(boolean invisibility);
    String getDisplayName();
    void sendTellRaw(String text);
    void sendParseTellRaw(String text);
    String getJsonChatDisplayName();
    String getName();

}
