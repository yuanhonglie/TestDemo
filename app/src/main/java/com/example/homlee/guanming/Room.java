package com.example.homlee.guanming;

/**
 * Created by homlee on 2018/11/24.
 */

public class Room {
    private int building;
    private int out;
    private int level;
    private int num;
    private boolean selected;
    private final String id;

    public Room(int building, int level, int num) {
        this.building = building;
        this.level = level;
        this.num = num;
        this.id = generateRoomId();
    }

    private String generateRoomId() {
        return String.format("%02d-%02d%02d", building+1, level, num+1);
    }

    public String getRoomId() {
        return id;
    }

    public int getBuilding() {
        return building;
    }

    public void setBuilding(int building) {
        this.building = building;
    }

    public int getOut() {
        return out;
    }

    public void setOut(int out) {
        this.out = out;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return String.format("%d%02d", level, num+1);
    }
}
