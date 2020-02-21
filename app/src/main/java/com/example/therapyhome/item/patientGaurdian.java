package com.example.therapyhome.item;

public class patientGaurdian {
    String name;
    String num;

    public patientGaurdian() {}

    public patientGaurdian(String name, String num) {
        this.name = name;
        this.num = num;
    }

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
}
