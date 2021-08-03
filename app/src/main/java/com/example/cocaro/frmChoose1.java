package com.example.cocaro;


import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import android.widget.ImageView;
import android.widget.TextView;


public class frmChoose1 extends AppCompatActivity {

    ImageView btnPVP, btnPVE, detailPVP, detailPVE;

    Dialog dialog;

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
                showDialog("PVP");
            }
        });
        detailPVE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog("PVE");
            }
        });
    }

    public void showDialog(String hinhthucchoi){
        dialog = new Dialog(this);
//        dialog.setTitle("You win!!!");
        dialog.setContentView(R.layout.dialog_detail);

        TextView tVAAA;
        TextView tVIn4;
        ImageView btnOk;

        tVAAA = dialog.findViewById(R.id.tVAAA);
        tVAAA.setText(hinhthucchoi);

        tVIn4 = dialog.findViewById(R.id.tVIn4);
        if(hinhthucchoi.equals("PVP")){
            tVIn4.setText("webjkwjebbwkejbvwe kwej kwefwkenfw,ekv wefwvlkwe w,ve" +
                    "wefwefwefwefwefwewfwefwefffffffffffffffffffffffffff" +
                    "wefwefwefweffffffffffffffffffffffffffffffffffffffffff" +
                    "wefwefweeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee" +
                    "ewfwefwefwefffffffffffffffffffffffffffffffffffffffffffffffff" +
                    "wefweeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
        }
        else{
            tVIn4.setText("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                    "ewfwefwefwefffffffffffffffffffffffffffffffffffffffffffffffff" +
                    "wefweeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
        }

        btnOk = dialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}