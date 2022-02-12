package org.functions.Bukkit.Main.functions.UserAccounts;

import org.apache.commons.lang.StringUtils;
import org.bukkit.*;
import org.functions.Bukkit.API.FunctionsSQL.SQLRead;
import org.functions.Bukkit.API.FunctionsSQL.SQLReader;
import org.functions.Bukkit.API.WorldBlock;
import org.functions.Bukkit.Main.Functions;
import org.functions.Bukkit.Main.functions.FunctionsRules;
import org.functions.Bukkit.Main.functions.Utils;
import org.functions.Bukkit.Main.functions.security;

import java.io.File;
import java.util.UUID;

public class Account extends SQLRead {
    final UUID uuid;
    final SQLReader reader;
    public Account(UUID uuid) {
        super(new File(Accounts.dir,uuid.toString()));
        reader = super.getReader();
        this.uuid = uuid;
    }
    public boolean exists() {
        return isRegister();
    }
    public void save() {
        reader.save();
    }
    public UUID getUUID() {
        return uuid;
    }
    public OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(uuid);
    }
    public String getName() {
        if (reader.setSelect("Name").existsSelect()) {
            return reader.setSelect("Name").getObjectAsString();
        }
        return getOfflinePlayer().getName();
    }
    public void setName() {
        reader.setSelect("Name").setObjectAsString(getName());
    }
    public long getRegisterTime() {
        if (!reader.setSelect("RegisterTime").existsSelect()) {
            return System.currentTimeMillis();
        }
        return reader.setSelect("RegisterTime").getObjectAsLong();
    }
    public void setRegisterTime() {
        reader.setSelect("RegisterTime").setObjectAsLong(getRegisterTime());
        save();
    }
    public boolean getAllowFight() {
        if (!reader.setSelect("AllowFight").existsSelect()) {
            return getOfflinePlayer().isOnline() && getOfflinePlayer().getPlayer().getAllowFlight();
        }
        return false;
    }
    public void setAllowFight() {
        reader.setSelect("AllowFight").setObjectAsBoolean(getAllowFight());
        save();
    }
    private Location AutoCon(Location loc) {
        WorldBlock block = new WorldBlock(loc);
        return new Location(loc.getWorld(),Utils.autoCon(loc.getX()),block.onGroundY(),Utils.autoCon(loc.getZ()));
    }
    public Location getSpawnLocation() {
        World world = Bukkit.getWorld(Functions.instance.getConfiguration().getSettings().getString("Login.DefaultWorld",Bukkit.getWorlds().get(0).getName()));
        return AutoCon(world.getSpawnLocation());
    }
    public void setPosition() {
        if (isLogin()) {
            reader.setSelect("Position").setObjectAsString(Functions.instance.getAPI().changeLocationToString(getOfflinePlayer().getPlayer().getLocation()));
            save();
        }
    }
    public boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (str.charAt(i) != ' ') {
                return false;
            }
            if ((!Character.isWhitespace(str.charAt(i)))) {
                return false;
            }
        }
        return true;
    }
    public boolean isRegister() {
        if (reader.setSelect("Password").existsSelect()) {
            return !isBlank(reader.setSelect("Password").getObjectAsString());
        }
        return false;
    }
    public boolean logout() {
        if (isLogin()) {
            reader.setSelect("Position").setObjectAsString(Functions.instance.getAPI().changeLocationToString(getOfflinePlayer().getPlayer().getLocation()));
            getOfflinePlayer().getPlayer().teleport(getSpawnLocation());
            reader.setSelect("AllowFight").setObjectAsBoolean(getOfflinePlayer().getPlayer().getAllowFlight());
            reader.setSelect("GameMode").setObjectAsString(getOfflinePlayer().getPlayer().getGameMode().toString());
            getOfflinePlayer().getPlayer().setGameMode(GameMode.valueOf(Functions.instance.getConfiguration().getConfig().getString("Functions.NotLoginGameMode", Bukkit.getDefaultGameMode().toString())));
            setLogin(false);
            reader.setSelect("LastTime").setObjectAsLong(System.currentTimeMillis());
            save();
            return true;
        }
        setLogin(false);
        getOfflinePlayer().getPlayer().teleport(getSpawnLocation());
        getOfflinePlayer().getPlayer().setGameMode(GameMode.valueOf(Functions.instance.getConfiguration().getConfig().getString("Functions.NotLoginGameMode", Bukkit.getDefaultGameMode().toString())));
        return false;
    }
    public boolean isLogin() {
        if (!Accounts.enable()) {
            return true;
        }
        return reader.setSelect("isLogin").getObjectAsBoolean();
    }
    private void setLogin(boolean login) {
        reader.setSelect("isLogin").setObjectAsBoolean(login);
        save();
    }
    public void setGameMode() {
        reader.setSelect("GameMode").setObjectAsString(getOfflinePlayer().getPlayer().getGameMode().toString());
        save();
    }
    public GameMode getGameMode() {
        if (reader.setSelect("GameMode").existsSelect()) {
            return GameMode.valueOf(reader.setSelect("GameMode").getObjectAsString());
        }
        return getOfflinePlayer().getPlayer().getGameMode();
    }
    public boolean existsMail() {
        return reader.setSelect("Mail").existsSelect();
    }
    public String getMail() {
        if (existsMail()) {
            return reader.setSelect("Mail").getObjectAsString();
        }
        return null;
    }
    String matches = "([a-zA-Z0-9]*)@([a-zA-Z0-9]*)\\.([a-zA-Z0-9]*)";
    public boolean setMail(String mail) {
        if (!mail.matches(matches)) {
            return false;
        }
        reader.setSelect("Mail").setObjectAsString(mail);
        return true;
    }
    public String getPassword() {
        return reader.setSelect("Password").getObjectAsString() != null ? reader.setSelect("Password").getObjectAsString() : "";
    }
    public void setAddress() {
        reader.setSelect("Address").setObjectAsString(address());
        save();
    }
    public String getAddress() {
        if (!reader.setSelect("Address").existsSelect()) {
            return address();
        }
        return reader.setSelect("Address").getObjectAsString();
    }
    public String address() {
        return getOfflinePlayer().getPlayer().getAddress().getAddress().getHostAddress();
    }
    public void setPassword(String password) {
        reader.setSelect("Password").setObjectAsString(password);
        save();
    }
    public boolean Register(String password) {
        setPassword(security.security(password));
        setName();
        setRegisterTime();
        setLogin(true);
        setAddress();
        save();
        return reader.setSelect("Password").existsSelect();
    }
    private void login() {
        setLogin(true);
        getOfflinePlayer().getPlayer().teleport(getPosition());
        getOfflinePlayer().getPlayer().setGameMode(getGameMode());
        getOfflinePlayer().getPlayer().setAllowFlight(getAllowFight());
    }

    public Location getPosition() {
        if (!reader.setSelect("Position").existsSelect()) {
            Functions.instance.getAPI().changeStringToLocation(reader.setSelect("Position").getObjectAsString());
        }
        return getSpawnLocation();
    }

    public boolean login(String password) {
        if (!isLogin()) {
            password = security.security(password);
            System.out.println(password);
            System.out.println(getPassword());
            if (password.equals(getPassword())) {
                login();
                return true;
            }
            //WrongPassword();
        }
        return false;
    }
    public boolean WrongPassword() {
        if (Functions.instance.getAPI().getRules().isEnabled(FunctionsRules.Type.WRONGPASSWORD)) {
            getOfflinePlayer().getPlayer().sendMessage(Functions.instance.getAPI().putLanguage("AccountPrintWrongPassword","&c您输入的的密码错误%lines%请找管理员！",getOfflinePlayer().getPlayer()));
            getOfflinePlayer().getPlayer().kickPlayer(Functions.instance.getAPI().putLanguage("AccountPrintWrongPassword","&c您输入的的密码错误%lines%请找管理员！",getOfflinePlayer().getPlayer()));
            return true;
        }
        return false;
    }
    public boolean mailLogin() {
        if (!isLogin()) {
            login();
            return true;
        }
        return false;
    }
    public boolean changePassword(String password) {
        password = security.security(password);
        if (!getPassword().equals(password)) {
            setPassword(password);
            logout();
            return true;
        }
        return false;
    }
    public boolean delete() {
        if (getOfflinePlayer().isOnline()) logout();
        super.getReader().getFile().deleteOnExit();
        return !exists();
    }
    public boolean setAutoLogin() {
        reader.setSelect("AutoLogin").setObjectAsBoolean(!getAutoLogin());
        return true;
    }
    public boolean getAutoLogin() {
        if (reader.setSelect("AutoLogin").existsSelect()) {
            return reader.setSelect("AutoLogin").getObjectAsBoolean();
        }
        return false;
    }
    public boolean autoLogin() {
        if (exists()) {
            if (!isLogin()) {
                if (getAutoLogin()) {
                    if (getAddress().equalsIgnoreCase(address())) {
                        login();
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
