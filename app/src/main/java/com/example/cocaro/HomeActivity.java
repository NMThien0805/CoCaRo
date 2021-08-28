package com.example.cocaro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cocaro.Object.Acc;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity {

    ListView lVRoom;
    Button btnSearch, btnCreateRoom, btnBack;
    ImageView imgAVt;
    TextView tvUsername, tvSoWin, tvSoLose, tvSearch;

    Acc acc_logged = new Acc();

    ConnectFirebase cn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);

        setControl();
        setEvent();
    }

    void setControl(){
        cn = new ConnectFirebase(this, this);

        lVRoom = findViewById(R.id.lViewRoom);
        tvSearch = findViewById(R.id.tVSearch);
        btnSearch = findViewById(R.id.btnSearch);
        btnCreateRoom = findViewById(R.id.btnCreateRoom);
        btnBack = findViewById(R.id.btnBack);
        imgAVt = findViewById(R.id.imgAVT);
        tvUsername = findViewById(R.id.tVUsername);
        tvSoWin = findViewById(R.id.tVSoWin);
        tvSoLose = findViewById(R.id.tVSoLose);

        get_logged();
        tvUsername.setText(acc_logged.getUsername());
        tvSoWin.setText(acc_logged.getWon() + "");
        tvSoLose.setText(acc_logged.getLose() + "");
        Picasso.get().load(acc_logged.getImg()).into(imgAVt);

        cn.Init();
        cn.reset_room();
        cn.load_room_for_home(lVRoom, "");
        cn.setup_listRoom_forHome(lVRoom);
    }

    void setEvent(){
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cn.load_room_for_home(lVRoom, tvSearch.getText().toString().trim());
            }
        });
        btnCreateRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, frmChoose1.class);
                sent_logged(intent);
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, Login.class);
                startActivity(intent);
            }
        });
        lVRoom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = lVRoom.getItemAtPosition(position);
                String item = (String) o;
//                Toast.makeText(HomeActivity.this, "Room selected: " + item, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void get_logged() {
        Intent intent = getIntent();
        acc_logged.setUsername(intent.getStringExtra("username"));
        acc_logged.setImg(intent.getStringExtra("urlImg"));
        acc_logged.setWon(Integer.parseInt(intent.getStringExtra("countWin")));
        acc_logged.setLose(Integer.parseInt(intent.getStringExtra("countLose")));
//        Toast.makeText(this, acc_logged.toString(), Toast.LENGTH_LONG).show();
    }

    public void sent_logged(Intent intent) {
        intent.putExtra("username", acc_logged.getUsername());
        intent.putExtra("countWin", acc_logged.getWon() + "");
        intent.putExtra("countLose", acc_logged.getLose() + "");
        intent.putExtra("urlImg", acc_logged.getImg());
        startActivity(intent);
        finish();
    }

//    private List<String> getListData() {
//        List<String> list = new ArrayList<String>();
//
//        list.add("vietnam");
//        list.add("usa");
//        list.add("russia");
//
//        return list;
//    }
}