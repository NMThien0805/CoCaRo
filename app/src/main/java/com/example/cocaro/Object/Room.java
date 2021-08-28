package com.example.cocaro.Object;

import java.util.ArrayList;

public class Room {
    UserOfRoom user1;
    UserOfRoom user2;
    ArrayList<Integer> arr_game;
    String tt;

    public Room(UserOfRoom user1, UserOfRoom user2, ArrayList<Integer> arr_game) {
        this.user1 = user1;
        this.user2 = user2;
        this.arr_game = arr_game;
        this.tt = "Chua bat dau";
    }

    public Room() {
    }

    @Override
    public String toString() {
        return "Room{" +
                "user1=" + user1 +
                ", user2=" + user2 +
                ", arr_game=" + arr_game +
                '}';
    }

    public UserOfRoom getUser1() {
        return user1;
    }

    public void setUser1(UserOfRoom user1) {
        this.user1 = user1;
    }

    public UserOfRoom getUser2() {
        return user2;
    }

    public void setUser2(UserOfRoom user2) {
        this.user2 = user2;
    }

    public ArrayList<Integer> getArr_game() {
        return arr_game;
    }

    public void setArr_game(ArrayList<Integer> arr_game) {
        this.arr_game = arr_game;
    }

    public String isTt() {
        return tt;
    }

    public void setTt(String tt) {
        this.tt = tt;
    }
}
