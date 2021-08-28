package com.example.cocaro.Object;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Acc {
    String username;
    String email;
    String password;
    boolean trangthai;
    int won;
    int lose;
    String img;

    public Acc() {
    }

    public Acc(String username, String email, String password, boolean trangthai, int won, int lose, String img) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.trangthai = trangthai;
        this.won = won;
        this.lose = lose;
        this.img = img;
    }

    @Override
    public String toString() {
        return "Acc{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", trangthai=" + trangthai +
                ", won=" + won +
                ", lose=" + lose +
                ", img='" + img + '\'' +
                '}';
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isTrangthai() {
        return trangthai;
    }

    public void setTrangthai(boolean trangthai) {
        this.trangthai = trangthai;
    }

    public int getWon() {
        return won;
    }

    public void setWon(int won) {
        this.won = won;
    }

    public int getLose() {
        return lose;
    }

    public void setLose(int lose) {
        this.lose = lose;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
