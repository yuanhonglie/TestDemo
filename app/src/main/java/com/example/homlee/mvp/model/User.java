package com.example.homlee.mvp.model;

import androidx.annotation.NonNull;

public class User {
    private String name;
    private int age;
    private String department;
    private String group;

    public User(String name, int age, String department, String group) {
        this.name = name;
        this.age = age;
        this.department = department;
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @NonNull
    @Override
    public String toString() {
        return name + ", " + age + " , " + department + "-" + group;
    }
}
