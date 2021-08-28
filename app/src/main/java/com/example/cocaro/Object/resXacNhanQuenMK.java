package com.example.cocaro.Object;

import com.google.gson.annotations.SerializedName;

public class resXacNhanQuenMK {
    @SerializedName("email")
    String email;
    @SerializedName("result")
    String result = "";

    public resXacNhanQuenMK() {
    }

    public resXacNhanQuenMK(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "resXacNhanQuenMK{" +
                "email='" + email + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}
