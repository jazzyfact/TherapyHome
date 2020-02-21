package com.example.therapyhome.item;

public class docterPatient {
    Long name;
    String num;

    public docterPatient() {}

    public docterPatient(Long name, String num) {
        this.name = name;
        this.num = num;
    }

    public Long getName() {
        return name;
    }

    public void setName(Long name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
