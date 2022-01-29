package org.functions.Bukkit.API.FunctionsSQL;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SQLReader extends SQLFile {
    String select;
    public SQLReader(File file, List<String> texts) {
        super(file, texts);
    }
    public SQLReader(File file, List<String> texts,String select) {
        this(file, texts);
        this.select = select;
    }
    public SQLReader setSelect(String select) {
        return new SQLReader(file,texts,select);
    }
    public Object getObject() {
        if (select == null) {
            throwException("select");
            return null;
        }
        List<Object> t = new ArrayList<>();
        super.texts.forEach(e->{
            if (split(e,0).equals(select)) {
                t.add(split(e,1));
            }
        });
        return t.get(t.size()-1);
    }
    public String getObjectAsString() {
        return getObject().toString();
    }
    public int getObjectAsInteger() {
        int i = Integer.MIN_VALUE;
        try {
            i = Integer.parseInt(getObjectAsString());
        } catch (NumberFormatException e) {
            throwException("Integer format exception");
        }
        return i;
    }
    public double getObjectAsDouble() {
        double i = Double.MIN_VALUE;
        try {
            i = Double.parseDouble(getObjectAsString());
        } catch (NumberFormatException e) {
            throwException("Double format exception");
        }
        return i;
    }
    public float getObjectAsFloat() {
        float i = Float.MIN_VALUE;
        try {
            i = Float.parseFloat(getObjectAsString());
        } catch (NumberFormatException e) {
            throwException("Float format exception");
        }
        return i;
    }
    public long getObjectAsLong() {
        long i = Long.MIN_VALUE;
        try {
            i = Long.parseLong(getObjectAsString());
        } catch (NumberFormatException e) {
            throwException("Long format exception");
        }
        return i;
    }
    public boolean getObjectAsBoolean() {
        boolean i = false;
        try {
            i = Boolean.parseBoolean(getObjectAsString());
        } catch (NumberFormatException e) {
            throwException("Boolean format exception");
        }
        return i;
    }
    public void setObject(Object object) {
        if (select == null) {
            throwException("select");
            return;
        }
        List<Boolean> is = new ArrayList<>();
        super.texts.forEach(e->{
            if (split(e,0).equals(select)) {
                if (is.get(0) == null || is.get(0)) {
                    texts.remove(e);
                    texts.add(e.replace(split(e,1),object.toString()));
                    is.add(true);
                }
            }
        });
        if (!is.get(0)) {
            texts.add(select + ":" + object.toString());
        }
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
