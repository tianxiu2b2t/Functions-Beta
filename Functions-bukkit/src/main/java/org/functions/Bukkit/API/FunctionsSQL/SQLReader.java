package org.functions.Bukkit.API.FunctionsSQL;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SQLReader extends SQLFile {
    String select;
    public SQLReader(File file, List<String> texts) {
        super(file, texts);
    }
    public SQLReader setSelect(String select) {
        this.select = select;
        return this;
    }
    public SQLReader clearSelect() {
        return setSelect("");
    }
    public boolean existsSelect() {
        if (select == null) {
            throwException("select is null");
            return false;
        }
        for (String s : texts) {
            if (split(s,0).equals(select)) {
                return true;
            }
        }
        return false;
    }
    public Object getObject() {
        if (select == null) {
            throwException("select");
            return null;
        }
        if (super.texts.size() == 0) {
            return null;
        }
        for (String text : super.texts) {
            if (split(text,0).equals(select)) {
                return split(text,1);
            }
        }
        return null;
    }
    public String getObjectAsString() {
        if (getObject() == null) {
            return "";
        }
        return getObject()+"";
    }
    public String[] getObjectAsStrings(String split) {
        if (getObject() == null) {
            return new String[]{};
        }
        return getObjectAsString().split(split);
    }
    public int getObjectAsInteger() {
        int i = Integer.MIN_VALUE;
        if (getObject() == null) {
            return i;
        }
        try {
            i = Integer.parseInt(getObjectAsString());
        } catch (NumberFormatException e) {
            throwException("Integer format exception");
        }
        return i;
    }
    public double getObjectAsDouble() {
        double i = Double.MIN_VALUE;
        if (getObject() == null) {
            return i;
        }
        try {
            i = Double.parseDouble(getObjectAsString());
        } catch (NumberFormatException e) {
            throwException("Double format exception");
        }
        return i;
    }
    public float getObjectAsFloat() {
        float i = Float.MIN_VALUE;
        if (getObject() == null) {
            return i;
        }
        try {
            i = Float.parseFloat(getObjectAsString());
        } catch (NumberFormatException e) {
            throwException("Float format exception");
        }
        return i;
    }
    public long getObjectAsLong() {
        long i = Long.MIN_VALUE;
        if (getObject() == null) {
            return i;
        }
        try {
            i = Long.parseLong(getObjectAsString());
        } catch (NumberFormatException e) {
            throwException("Long format exception");
        }
        return i;
    }
    public boolean getObjectAsBoolean() {
        if (getObject() == null) {
            return false;
        }
        return Objects.equals(getObjectAsString(), "true");
    }
    public void setObject(Object object) {
        if (select == null) {
            throwException("select");
            return;
        }
        if (super.texts.size() == 0) {
            texts.add(select + ":" + object);
        }
        for (int i = 0; i < super.texts.size(); i++) {
            if (split(texts.get(i), 0).equals(select)) {
                super.texts.set(i, super.texts.get(i).replace(split(super.texts.get(i), 1), String.valueOf(object)));
                return;
            }
        }
        texts.add(select + ":" + object);
    }
    @Deprecated
    /**
     * Because it split you do not know it split char.
     * So, it is @deprecated.
     * @param args args is you create String[] chars.
     * */
    public void setObjectAsStrings(String[] args) {
        setObject(Arrays.toString(args));
    }
    public void setObjectAsString(String string) {
        setObject(string);
    }
    public void setObjectAsInteger(int number) {
        setObject(number);
    }
    public void setObjectAsDouble(double number) {
        setObject(number);
    }
    public void setObjectAsFloat(float number) {
        setObject(number);
    }
    public void setObjectAsLong(long number) {
        setObject(number);
    }
    public void setObjectAsBoolean(boolean blob) {
        setObject(blob);
    }
    public List<String> getTexts() {
        return super.texts;
    }
    public void setTexts(List<String> texts) {
        super.texts = texts;
    }
    @SuppressWarnings("all")
    private String split(String string,String regex,int pos) {
        if (pos < -1 || pos > (string.split(regex).length - 1)) {
            throwException("outIndex " + string.split(regex).length + " " + pos);
            return null;
        }
        return string.split(regex)[pos];
    }
    private String split(String string,int pos) {
        return split(string,":",pos);
    }
    private void throwException(String type) {
        throw new SQLException(type);
    }
    public void save() {
        new SQLSave(file,texts);
    }
}
