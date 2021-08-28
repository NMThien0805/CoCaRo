package com.example.cocaro;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cocaro.Object.resXacNhanQuenMK;
import com.example.cocaro.Object.token_forgotpass;
import com.example.cocaro.basic.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText Edemail, password, confirm, token;
    TextView btnSendEmail, btnChangePass, linkComeBackLogin;

    String accept_token;
    String username_true;

    ConnectFirebase cn = new ConnectFirebase(this, this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forgot_password);

        setControl();
        setEvent();
    }

    void setControl(){
        Edemail = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirm = findViewById(R.id.confirmPass);
        token = findViewById(R.id.token);

        btnSendEmail = findViewById(R.id.btnSendEmail);
        btnChangePass = findViewById(R.id.btnChangePass);
        linkComeBackLogin = findViewById(R.id.comebacklogin);

        cn.Init();

        password.setEnabled(false);
        confirm.setEnabled(false);
        token.setEnabled(false);
        btnChangePass.setEnabled(false);
    }

    void setEvent(){
        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Edemail.getText().toString().trim().equals("")){
                    Toast.makeText(ForgotPasswordActivity.this, "Your email???", Toast.LENGTH_SHORT).show();
                }else{
                    Edemail.setEnabled(false);
                    btnSendEmail.setEnabled(false);
                    cn.search_email_for_forgot(Edemail.getText().toString().trim());
                }
            }
        });
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(token.getText().toString().equals("")){
                    Toast.makeText(ForgotPasswordActivity.this, "Token is not NULL!", Toast.LENGTH_SHORT).show();
                }
                else if(password.getText().toString().equals("") || confirm.getText().toString().equals("")){
                    Toast.makeText(ForgotPasswordActivity.this, "Password and Confirm Password are not NULL!", Toast.LENGTH_SHORT).show();
                }
                else if(!password.getText().toString().equals(confirm.getText().toString())){
                    Toast.makeText(ForgotPasswordActivity.this, "Password and confirm password are not the same!", Toast.LENGTH_SHORT).show();
                }
                else{
                    //change
                    if(token.getText().toString().trim().equals(accept_token.trim())){
                        cn.change_password(username_true, password.getText().toString());
                        Toast.makeText(ForgotPasswordActivity.this, "Đã cập nhật mật khẩu mới!!!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ForgotPasswordActivity.this, Login.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(ForgotPasswordActivity.this, "Token không đúng!!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        linkComeBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPasswordActivity.this, Login.class);
                startActivity(intent);
            }
        });
    }

    public void send_to_email(String email, String username){
//        System.out.println("resuslt: " + email);
        username_true = username;
        Call<token_forgotpass> res = ApiService.apiService.forgotPass(new resXacNhanQuenMK(email));
        res.enqueue(new Callback<token_forgotpass>() {
            @Override
            public void onResponse(Call<token_forgotpass> call, Response<token_forgotpass> response) {
                System.out.println(response.toString());
                if(response.code() == 200){
                    assert response.body() != null;
                    accept_token = response.body().getResult_token();
                    Toast.makeText(ForgotPasswordActivity.this, "Đã gửi token!!!", Toast.LENGTH_SHORT).show();
                    password.setEnabled(true);
                    confirm.setEnabled(true);
                    token.setEnabled(true);
                    btnChangePass.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<token_forgotpass> call, Throwable t) {
                System.out.println("Call api send email fail");
                restore_enable_for_btnEmail();
            }
        });
    }

    public void restore_enable_for_btnEmail(){
        Edemail.setEnabled(true);
        btnSendEmail.setEnabled(true);
    }
}