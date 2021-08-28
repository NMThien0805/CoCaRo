package com.example.cocaro;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cocaro.Object.UserOfRoom;
import com.squareup.picasso.Picasso;

public class PlayGameActivity extends AppCompatActivity {
    Button btnBack;
    TextView tVUsername1, tVusername2;

    ImageView imgLayout, avt1, avt2;

    String name_room;
    boolean chu_room;
    String model;

    Ve_quan_XO ve_quan_xo;
    ConnectFirebase cn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_play_game);

        setControl();
        setEvent();
    }

    void setControl(){
        btnBack = findViewById(R.id.btnBackEnd);
        tVUsername1 = findViewById(R.id.tVUsername11);
        tVusername2 = findViewById(R.id.tVUsername21);

        avt1 = findViewById(R.id.avt1);
        avt2 = findViewById(R.id.avt2);
        imgLayout = findViewById(R.id.imgtable);

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.viewTable);

        Intent intent = getIntent();
        name_room = intent.getStringExtra("name_room");
        model = intent.getStringExtra("model");
        String getchu = intent.getStringExtra("chu_room");

        cn = new ConnectFirebase(this, this, name_room);
        cn.Init();

        if(!name_room.equals("")){
            if(getchu.equals("true")){
                chu_room = true;
            }
            else{
                chu_room = false;
                reset_XO();
            }

            cn.load_for_play(chu_room);

            if(model.equals("3vs3")){
                ve_quan_xo = new Ve_quan_XO(chu_room, this, relativeLayout,
                        imgLayout.getLayoutParams().width, imgLayout.getLayoutParams().height,
                        3, 3, name_room);
                imgLayout.setImageResource(R.drawable.table_caro_3vs3);
            }
            else{
                ve_quan_xo = new Ve_quan_XO(chu_room, this, relativeLayout,
                        imgLayout.getLayoutParams().width, imgLayout.getLayoutParams().height,
                        22, 22, name_room);
                imgLayout.setImageResource(R.drawable.table_caro_free);
            }
            ve_quan_xo.create();
        }
        else{

        }
//        Toast.makeText(this, getchu, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("ClickableViewAccessibility")
    void setEvent(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder b = new AlertDialog.Builder(PlayGameActivity.this);
                b.setMessage("Khi thoát bạn sẽ bị xử thua, vẫn thoát?");
                b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Xu_li_back();
                    }
                });
                b.setNegativeButton("CANCLE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }) ;
                AlertDialog dialog = b.create();
                dialog.show();
            }
        });
        imgLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    int x=  (int)event.getX();
                    int y = (int)event.getY();

                    ve_quan_xo.tounch(x, y);

                    return true;
                }

                return false;
            }
        });
    }

    private void Xu_li_back() {
        cn.update_win_and_lose(chu_room, chu_room);
//        come_back();
    }

    public void come_back_home(UserOfRoom us){
//        Toast.makeText(this, us.toString(), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(PlayGameActivity.this, HomeActivity.class);
        intent.putExtra("username", us.getUsername());
        intent.putExtra("countWin", us.getWon() + "");
        intent.putExtra("countLose", us.getLose() + "");
        intent.putExtra("urlImg", us.getAvt());
        startActivity(intent);
    }

    String get_username_present(){
        return tVUsername1.getText().toString().trim();
    }

    private void reset_XO(){
        ImageView imgX, imgO;
        imgX = findViewById(R.id.imgX);
        imgO = findViewById(R.id.imgO);

        imgX.setImageResource(R.drawable.o);
        imgO.setImageResource(R.drawable.x);
    }

    public void setup_username_avt(String usN1, String usN2, String UrlAvt1, String UrlAvt2){
        tVUsername1.setText(usN1);
        tVusername2.setText(usN2);
        Picasso.get().load(UrlAvt1).into(avt1);
        Picasso.get().load(UrlAvt2).into(avt2);
    }
}