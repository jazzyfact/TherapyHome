package com.example.therapyhome.item;

public class PhoneContactEdit {

    String name;
    String num;
    String emergency;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getEmergency() {
        return emergency;
    }

    public void setEmergency(String emergency) {
        this.emergency = emergency;
    }

    public PhoneContactEdit(String name, String num, String emergency) {
        this.name = name;
        this.num = num;
        this.emergency = emergency;
    }
}
