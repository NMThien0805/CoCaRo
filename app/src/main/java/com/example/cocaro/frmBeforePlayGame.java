package com.example.cocaro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cocaro.Object.Acc;
import com.example.cocaro.Object.UserOfRoom;
import com.squareup.picasso.Picasso;

public class frmBeforePlayGame extends AppCompatActivity {
    String url = "https://firebasestorage.googleapis.com/v0/b/cocaro-f318d.appspot.com/o/avtmacdinhFB.jpg?alt=media&token=caf478e9-6ea6-4d29-8434-38a66b728345";

    Acc acc_logged = new Acc();
    String model1, model2;

    ImageView avtUser1, avtUser2, imgReady;
    TextView percent1, percent2, username1, username2, tVready;
    Button btnReady;
    ImageButton btnBack;

    boolean chu_room = false;
    boolean ready = false;

    public void setReady(boolean ready) {
        if(ready){
            imgReady.setImageResource(R.drawable.star);
        }
        else{
            imgReady.setImageResource(R.drawable.star1);
        }
        this.ready = ready;
    }

    String roomname;

    ConnectFirebase cn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_frm_before_play_game);

        get_logged();
//        Toast.makeText(this, "2 lua chon: " + model1 + "_" + model2 + "\n" + acc_logged.toString(), Toast.LENGTH_LONG).show();
        setControl();
        setEvent();
    }

    void setControl(){
        avtUser1 = findViewById(R.id.imgAVTuser1);
        avtUser2 = findViewById(R.id.imgAVTuser2);
        imgReady = findViewById(R.id.imgReady);

        percent1 = findViewById(R.id.tVPercent1);
        percent2 = findViewById(R.id.tVPercent2);
        username1 = findViewById(R.id.tVUsername1);
        username2 = findViewById(R.id.tVUsername2);
        tVready = findViewById(R.id.tVReady);

        btnReady = findViewById(R.id.btnReady);
        btnBack = findViewById(R.id.btnBack3);

        if(model1.length() > 0 && model2.length() > 0){
            chu_room = true;
            if(model2.trim().equals("3vs3")){
                cn = new ConnectFirebase(this, 9, acc_logged, this);
            }
            else if(model2.trim().equals("free")){
                cn = new ConnectFirebase(this, 22 * 22, acc_logged,this);
            }
        }
        else{
            chu_room = false;
            imgReady.setImageResource(R.drawable.star);
            roomname = getIntent().getStringExtra("roomname");
            cn = new ConnectFirebase(this, -1, acc_logged, this);
        }

        cn.Init();

        if(chu_room){
            setup_chu_room(url, "...", "0", "0", true);
            cn.create_room();
        }
        else{
//            Toast.makeText(this, roomname, Toast.LENGTH_SHORT).show();
            cn.setup_name_of_join_room(roomname);
        }
    }

    void setEvent(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chu_room){
                    cn.huy_room();
                }
                else{
                    cn.thoat_room(false);
                }
            }
        });
        btnReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chu_room == false){
                    if(ready){
                        btnReady.setBackgroundResource(R.drawable.btn_notready);
                        ready = false;
                        tVready.setText("NOT READY");
                    }
                    else{
                        btnReady.setBackgroundResource(R.drawable.btn_ready);
                        ready = true;
                        tVready.setText("READY");
                    }
                    cn.changeReadyRoom(ready);
                }
                else{
                    if(ready){
                        cn.play_a_game(chu_room, model2);
                    }
                    else{
                        Toast.makeText(frmBeforePlayGame.this, "Your opponent is not ready!!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void get_logged() {
        Intent intent = getIntent();
        acc_logged.setUsername(intent.getStringExtra("username"));
        acc_logged.setImg(intent.getStringExtra("urlImg"));
        acc_logged.setWon(Integer.parseInt(intent.getStringExtra("countWin")));
        acc_logged.setLose(Integer.parseInt(intent.getStringExtra("countLose")));

        model1 = intent.getStringExtra("model1");
        model2 = intent.getStringExtra("model2");
    }

    private void sent_logged(Intent intent) {
        intent.putExtra("username", acc_logged.getUsername());
        intent.putExtra("countWin", acc_logged.getWon() + "");
        intent.putExtra("countLose", acc_logged.getLose() + "");
        intent.putExtra("urlImg", acc_logged.getImg());
        startActivity(intent);
        finish();
    }

    private double percent_of_user(int won, int lose, boolean is_you){
        double res;

        if(is_you){
            res = (double) acc_logged.getWon() / (double) (acc_logged.getWon() + acc_logged.getLose()) * 100.00;
            res = Math.round(res*100.0)/100.0;
        }
        else{
            res = (double) won / (double) (won + lose) * 100.00;
            res = Math.round(res*100.0)/100.0;
        }

        return res;
    }

    public void setup_chu_room(String url_user2, String userN2, String won, String lose, boolean is_first){
        if(is_first){
            btnReady.setBackgroundResource(R.drawable.btn_start_game);
            Picasso.get().load(acc_logged.getImg()).into(avtUser1);
            username1.setText(acc_logged.getUsername());
            percent1.setText(percent_of_user(0,0, true) + " %");
            tVready.setText("START");
        }

        Picasso.get().load(url_user2).into(avtUser2);
        username2.setText(userN2);
        percent2.setText(percent_of_user(Integer.parseInt(won), Integer.parseInt(lose) , false) +" %");
    }
    public void setup_join_room(String url_user1, String userN1, String won, String lose){
        Picasso.get().load(acc_logged.getImg()).into(avtUser1);
        username1.setText(acc_logged.getUsername());
        percent1.setText(percent_of_user(0,0, true) + " %");
        tVready.setText("READY");

        Picasso.get().load(url_user1).into(avtUser2);
        username2.setText(userN1);
        percent2.setText(percent_of_user(Integer.parseInt(won), Integer.parseInt(lose), false) +" %");
    }

    public void come_back_home(){
//        Toast.makeText(this, us.toString(), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(frmBeforePlayGame.this, HomeActivity.class);
        intent.putExtra("username", acc_logged.getUsername());
        intent.putExtra("countWin", acc_logged.getWon() + "");
        intent.putExtra("countLose", acc_logged.getLose() + "");
        intent.putExtra("urlImg", acc_logged.getImg());
        startActivity(intent);
    }
}