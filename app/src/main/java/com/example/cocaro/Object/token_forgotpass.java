package com.example.cocaro.Object;

import com.google.gson.annotations.SerializedName;

public class token_forgotpass {
    @SerializedName("result_token")
    String result_token;

    public token_forgotpass() {
    }

    public String getResult_token() {
        return result_token;
    }

    public void setResult_token(String result_token) {
        this.result_token = result_token;
    }

    @Override
    public String toString() {
        return "token_forgotpass{" +
                "result_token='" + result_token + '\'' +
                '}';
    }
}
