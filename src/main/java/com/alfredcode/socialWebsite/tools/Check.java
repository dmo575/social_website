package com.alfredcode.socialWebsite.tools;

public class Check {
    private boolean value = false;
    public Check(boolean value) {
        this.value = value;
    }

    public boolean getValue() {return value;}
    public void setValue(boolean newVal) {value = newVal;}
}
