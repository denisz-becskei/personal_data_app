package com.deniszbecskei.personaldata.person;

public class PositiveInt {
    private int integer;

    public PositiveInt(int integer) {
        this.integer = Math.max(integer, 0);
    }

    public int getInteger() {
        return integer;
    }
}
