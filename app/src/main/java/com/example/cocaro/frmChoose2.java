package com.example.cocaro;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.cocaro.Object.Acc;
import com.example.cocaro.Object.SlidePhoto;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class frmChoose2 extends AppCompatActivity {

    ImageButton btnBack;
    ListView lVmodel;

    ViewPager viewPager;
    private SlidePhotoAdapter slidePhotoAdapter;
    private CircleIndicator circleIndicator;
    private List<SlidePhoto> listSlidePhoto;

    Acc acc_logged = new Acc();

    String model1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_frm_choose2);

        setControl();
        setEvent();
    }

    private void setControl() {
        btnBack = (ImageButton)findViewById(R.id.btnBack2);

        String[] arr = new String[2];
        arr[0] = "3x3";
        arr[1] = "Free";

        lVmodel = (ListView)findViewById(R.id.lVModel);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item_spinner_model_game, arr);
        adapter.setDropDownViewResource(R.layout.item_spinner_model_game);
        lVmodel.setAdapter(adapter);

        get_logged();

        circleIndicator = findViewById(R.id.circle_indicator);
        viewPager = (ViewPager)findViewById(R.id.vPager);

        listSlidePhoto = getListSlidePhoto();
        setDataSlidePhotoAdapter();
    }

    private void setEvent() {
        lVmodel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                viewPager.setCurrentItem(position);
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(frmChoose2.this, frmChoose1.class);
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

        model1 = intent.getStringExtra("model");
    }

    private void sent_logged(Intent intent) {
        intent.putExtra("username", acc_logged.getUsername());
        intent.putExtra("countWin", acc_logged.getWon() + "");
        intent.putExtra("countLose", acc_logged.getLose() + "");
        intent.putExtra("urlImg", acc_logged.getImg());
        startActivity(intent);
    }

    private void setDataSlidePhotoAdapter(){
        slidePhotoAdapter = new SlidePhotoAdapter(listSlidePhoto, this, this);
        viewPager.setAdapter(slidePhotoAdapter);
        circleIndicator.setViewPager(viewPager);
        slidePhotoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());

        if(listSlidePhoto == null || listSlidePhoto.isEmpty() || viewPager == null){
            return;
        }
    }

    private List<SlidePhoto> getListSlidePhoto(){
        List<SlidePhoto> listSlidePhoto = new ArrayList<>();
        listSlidePhoto.add(new SlidePhoto(R.layout.table_3vs3));
        listSlidePhoto.add(new SlidePhoto(R.layout.table_free));
        return listSlidePhoto;
    }

    public void to_next_form(String model2){
        if(model1.equals("PVP")){
            Intent intent = new Intent(frmChoose2.this, frmBeforePlayGame.class);

            String s = "";
            if(model2.equals("0")){
                s = "3vs3";
            }else if(model2.equals("1")){
                s = "free";
            }

            intent.putExtra("model1", model1);
            intent.putExtra("model2", s);
            sent_logged(intent);
            finish();
        }
        else{
            //Choi voi may
            Intent intent = new Intent(frmChoose2.this, PlayGamePVEActivity.class);

            String s = "";
            if(model2.equals("0")){
                s = "3vs3";
            }else if(model2.equals("1")){
                s = "free";
            }
            intent.putExtra("model2", s);
            sent_logged(intent);
            finish();
        }
    }
}