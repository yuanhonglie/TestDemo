package com.example.homlee.guanming;

import android.text.TextUtils;

/**
 * Created by homlee on 2018/11/30.
 */

public class SelectedRoom {

    private String roomId;
    private String candidateId;

    public SelectedRoom(String roomId) {
        this(roomId, "");
    }

    public SelectedRoom(String roomId, String candidateId) {
        this.roomId = roomId;
        this.candidateId = candidateId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(16);
        sb.append(roomId);
        if (!TextUtils.isEmpty(candidateId)) {
            sb.append("-").append(candidateId);
        }
        return sb.toString();
    }
}
