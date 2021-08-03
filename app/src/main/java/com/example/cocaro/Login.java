package com.example.cocaro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    EditText username, password;
    CheckBox chechboxRemember;
    ImageView imgFb, imgGg;
    TextView forgot, signIn, signUp;

    String urlImg = "";

    ConnectFirebase cn = new ConnectFirebase(this, 9, this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        setControl();
        setEvent();
    }

    void setControl(){
        username = findViewById(R.id.usename);
        password = findViewById(R.id.password);
        chechboxRemember = findViewById(R.id.checkboxremember);
        imgFb = findViewById(R.id.btnFb);
        imgGg = findViewById(R.id.btnGg);
        forgot = findViewById(R.id.forgotPass);
        signIn = findViewById(R.id.btnSignin);
        signUp = findViewById(R.id.signup);

        cn.Init();
//        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
    }

    void setEvent(){
        imgFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        imgGg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cn.signIn(username.getText().toString(), password.getText().toString());
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Login.this, Register.class);
                startActivity(intent1);
            }
        });
    }

    void go_to_home(){
        Intent intent = new Intent(Login.this, frmChoose1.class);
        startActivity(intent);
    }
}