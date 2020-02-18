package com.example.bysj2020.event;

/**
 * Author : wxz
 * Time   : 2020/02/16
 * Desc   :
 */
public class BaseEvent {
    private int state;

    public BaseEvent(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
