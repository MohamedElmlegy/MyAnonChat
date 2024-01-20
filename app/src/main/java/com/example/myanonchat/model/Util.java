package com.example.myanonchat.model;

public class Util {
    private int bubble_color;
    private int name_color;
    private int bubble_g;

    private int text_color;


    public int getBubble_color() {
        return bubble_color;
    }

    public void setBubble_color(int bubble_color) {
        this.bubble_color = bubble_color;
    }

    public int getName_color() {
        return name_color;
    }

    public void setName_color(int name_color) {
        this.name_color = name_color;
    }

    public int getBubble_g() {
        return bubble_g;
    }

    public void setBubble_g(int bubble_g) {
        this.bubble_g = bubble_g;
    }

    public int getText_color() {
        return text_color;
    }

    public void setText_color(int text_color) {
        this.text_color = text_color;
    }

    public Util(int bubble, int name, int bubble_g, int textColor) {
        this.bubble_color = bubble;
        this.name_color = name;
        this.bubble_g = bubble_g;
        text_color = textColor;
    }
}
