package com.example.Alpinia;

public class Lamp extends Device {
    final static private String ID = "go46xmbqeomjrsjr";
    LampState state;
    public Lamp(String name) {
        super(name, new Type(ID));
    }

    @Override
    public LampState getState() {
        return state;
    }

    public void setState(LampState state) {
        this.state = state;
    }
}
