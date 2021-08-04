package com.example.cocaro;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import android.widget.ImageView;


public class frmChoose1 extends AppCompatActivity {

    ImageView btnPVP, btnPVE, detailPVP, detailPVE;

    ShowDialog helpDialog = new ShowDialog(this);

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
    }

    private void setEvent() {
        btnPVP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(frmChoose1.this, MainActivity.class);
                startActivity(intent);
            }
        });
        btnPVE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(frmChoose1.this, frmChoose2.class);
                startActivity(intent);
            }
        });
        detailPVP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpDialog.showDialogIn4("PVP", "Chế độ chơi giữa 2 người với nhau!!! \nThời gian chơi của mỗi người là giới hạn" +
                        " ai dùng hết giờ trước sẽ thua");
            }
        });
        detailPVE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpDialog.showDialogIn4("PVE", "Chế độ chơi giữa người và máy!!! \nThời gian chơi của mỗi người là giới hạn" +
                        " ai dùng hết giờ trước sẽ thua");
            }
        });
    }
}