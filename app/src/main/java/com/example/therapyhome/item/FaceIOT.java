package com.example.therapyhome.item;

public class FaceIOT {

    int no;
    float x;
    float y;
    int width;
    int height;

    public FaceIOT(int no, float x, float y, int width, int height) {
        this.no = no;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
