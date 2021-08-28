package com.example.cocaro.Object;

import java.util.ArrayList;

public class UserOfRoom {
    String avt;
    String username;
    String won;
    String lose;

    public UserOfRoom() {
    }

    public UserOfRoom(String avt, String username, String won, String lose) {
        this.avt = avt;
        this.username = username;
        this.won = won;
        this.lose = lose;
    }

    @Override
    public String toString() {
        return "UserOfRoom{" +
                "avt='" + avt + '\'' +
                ", username='" + username + '\'' +
                ", won='" + won + '\'' +
                ", lose='" + lose + '\'' +
                '}';
    }

    public String getAvt() {
        return avt;
    }

    public void setAvt(String avt) {
        this.avt = avt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWon() {
        return won;
    }

    public void setWon(String won) {
        this.won = won;
    }

    public String getLose() {
        return lose;
    }

    public void setLose(String lose) {
        this.lose = lose;
    }
}
