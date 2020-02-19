package com.example.therapyhome.item;

public class PatientEditKeyWord {
    String Text;

    public PatientEditKeyWord(String text) {
        Text = text;
    }

    PatientEditKeyWord(){}

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }
}
