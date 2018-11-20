package com.homlee.rxbus2;

/**
 * Created by homlee on 2018/11/20.
 */

public class MessageEvent {
    public int type;
    public String message;
    public MessageEvent(int type, String message) {
        this.type = type;
        this.message = message;
    }
}
