package com.example.homlee.guanming;

import android.text.TextUtils;

/**
 * Created by homlee on 2018/12/1.
 */

public class Candidate {
    private static final int ID_LEN = 6;
    private String id;
    private String name;
    private String extra;
    private String identity;
    private String birthday;

    public Candidate(String id, String name, String extra, String identity) {
        this.id = id;
        this.name = name;
        this.extra = extra;
        this.identity = identity;
        if (!TextUtils.isEmpty(identity) && identity.length() > ID_LEN) {
            this.birthday = identity.substring(ID_LEN);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    @Override
    public String toString() {
        return name + " " + birthday;
    }
}
