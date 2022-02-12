package org.functions.Bukkit.Main.functions;

import org.functions.Bukkit.API.FunctionsSQL.SQLFile;
import org.functions.Bukkit.API.FunctionsSQL.SQLRead;
import org.functions.Bukkit.Main.DataBase;
import org.functions.Bukkit.Main.Functions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FunctionsRules {
    public enum Type {
        FALL(true),
        SICKS_ITEM(true),
        DISPENSER(true),
        WRONGPASSWORD(true);
        boolean isEnabled;
        Type(boolean isEnabled) {
            this.isEnabled = isEnabled;
        }

        public boolean isEnabled() {
            return isEnabled;
        }
    }
    SQLRead read;
    public FunctionsRules() {
        read = new SQLRead(new File(Functions.instance.getDataFolder(),"Rules"));
        AddRules(Type.FALL);
        AddRules(Type.SICKS_ITEM);
        AddRules(Type.DISPENSER);
        AddRules(Type.WRONGPASSWORD);
    }
    public FunctionsRules clone() {
        try {
            return (FunctionsRules) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return this;
    }
    public boolean isEnabled(Type type) {
        return read.getReader().setSelect(type.name()).getObjectAsBoolean();
    }
    public boolean enable(Type type) {
        read.getReader().setSelect(type.name()).setObjectAsBoolean(!read.getReader().setSelect(type.name()).getObjectAsBoolean());
        return read.getReader().setSelect(type.name()).getObjectAsBoolean();
    }
    private boolean RulesExists(Type type) {
        return read.getReader().setSelect(type.name()).existsSelect();
    }
    private void AddRules(Type type) {
        if (!RulesExists(type)) {
            read.getReader().setSelect(type.name()).setObjectAsBoolean(type.isEnabled());
        }
    }
}
