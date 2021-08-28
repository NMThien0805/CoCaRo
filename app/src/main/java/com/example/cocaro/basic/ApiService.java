package com.example.cocaro.basic;

import com.example.cocaro.Object.resXacNhanQuenMK;
import com.example.cocaro.Object.token_forgotpass;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    //https://api-cuoiky.herokuapp.com/api/select/user?fbclid=IwAR0n5neTeuF99x5Gpy0Qd2PZgW2iwmj3hz_0XtIthlyVr4So0E4nOmiUwXQ
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    ApiService apiService = new Retrofit.Builder()
            .baseUrl("https://api-cuoiky.herokuapp.com/")
            .client(new OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);

    @POST("api/support/forgotpass")
    Call<token_forgotpass> forgotPass(@Body resXacNhanQuenMK res);
}
