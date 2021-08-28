package com.example.cocaro;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.cocaro.Object.Acc;


public class frmChoose1 extends AppCompatActivity {

    ImageView btnPVP, btnPVE, detailPVP, detailPVE;
    ImageButton btnBack;

    ShowDialog helpDialog = new ShowDialog(this);

    Acc acc_logged = new Acc();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_frm_choose1);

        setControl();
        setEvent();
    }

    private void setControl() {
        btnPVP = (ImageView)findViewById(R.id.btnPVP);
        btnPVE = (ImageView)findViewById(R.id.btnPVE);
        detailPVP = (ImageView)findViewById(R.id.imagebtnPVP);
        detailPVE = (ImageView)findViewById(R.id.imagebtnPVE);
        btnBack = (ImageButton)findViewById(R.id.btnBack1);

        get_logged();
    }

    private void setEvent() {
        btnPVP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(frmChoose1.this, frmChoose2.class);
                intent.putExtra("model", "PVP");
                sent_logged(intent);
            }
        });
        btnPVE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(frmChoose1.this, frmChoose2.class);
                //gui thong tin logged di
                intent.putExtra("model", "PVE");
                sent_logged(intent);
            }
        });
        detailPVP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpDialog.showDialogIn4("PVP", "Chế độ chơi giữa 2 người với nhau!!");
            }
        });
        detailPVE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpDialog.showDialogIn4("PVE", "Chế độ chơi giữa người và máy!!!");
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(frmChoose1.this, HomeActivity.class);
                sent_logged(intent);
            }
        });
    }

    private void get_logged() {
        Intent intent = getIntent();
        acc_logged.setUsername(intent.getStringExtra("username"));
        acc_logged.setImg(intent.getStringExtra("urlImg"));
        acc_logged.setWon(Integer.parseInt(intent.getStringExtra("countWin")));
        acc_logged.setLose(Integer.parseInt(intent.getStringExtra("countLose")));
    }

    private void sent_logged(Intent intent) {
        intent.putExtra("username", acc_logged.getUsername());
        intent.putExtra("countWin", acc_logged.getWon() + "");
        intent.putExtra("countLose", acc_logged.getLose() + "");
        intent.putExtra("urlImg", acc_logged.getImg());
        startActivity(intent);
        finish();
    }
}