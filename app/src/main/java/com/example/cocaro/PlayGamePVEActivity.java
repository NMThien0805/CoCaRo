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
import android.widget.Toast;

import com.example.cocaro.Object.Acc;
import com.example.cocaro.Object.UserOfRoom;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class PlayGamePVEActivity extends AppCompatActivity {

    int xMax;
    int yMax;
    int so_o_1_row;
    int so_o_1_column;

    boolean my_turn = true;
    int id_draw;
    int id_draw_other;
    int[][] arr_box;
    int w;
    int h;
    quan_XO[] arr_XO;
    ArrayList<theNext> arr_next = new ArrayList<>();
    ArrayList<theNext> arr_selected = new ArrayList<>();
    ArrayList<theNext> arr_opponent = new ArrayList<>();
    ArrayList<object_search_previous> arr_warning = new ArrayList<>();
    ArrayList<object_search_previous> arr_mart = new ArrayList<>();

    boolean ThirdQuan = false;
    int dem = 0;
    int cotOld = -1;
    int hangOld = -1;
    int time = 2; //seconds
    long time_choose = time * 100;
    Timer timer;

    int previous;
    int warning = -1;

    Button btnBack, btnA, btnB, btnC;
    ImageView imgLayout, imgAvt;
    TextView tVusername, tVTimer;
    RelativeLayout relativeLayout;

    Acc acc_logged = new Acc();

    String model2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_play_game_p_v_e);
        
        setControl();
        setEvent();
    }

    private void setControl() {
        btnA = findViewById(R.id.btnA);
        btnB = findViewById(R.id.btnB);
        btnC = findViewById(R.id.btnC);
        btnBack = findViewById(R.id.btnBackPVE);

        imgLayout = findViewById(R.id.imgtablePVE);
        imgAvt = findViewById(R.id.imgAVTPVE);

        tVusername = findViewById(R.id.tVUsernamePVE);
        tVTimer = findViewById(R.id.tVTimer);

        get_logged();
        tVusername.setText(acc_logged.getUsername());
        Picasso.get().load(acc_logged.getImg()).into(imgAvt);
//        Toast.makeText(this, model2, Toast.LENGTH_LONG).show();
        if(model2.equals("3vs3")){
            imgLayout.setImageResource(R.drawable.table_caro_3vs3);
            so_o_1_row = so_o_1_column = 3;
        }
        else if(model2.equals("free")){
            imgLayout.setImageResource(R.drawable.table_caro_free);
            so_o_1_row = so_o_1_column = 22;
        }
        relativeLayout = (RelativeLayout) findViewById(R.id.viewTablePVE);
        xMax = imgLayout.getLayoutParams().width;
        yMax = imgLayout.getLayoutParams().height;
        create();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setEvent() {
        btnA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder b = new AlertDialog.Builder(PlayGamePVEActivity.this);
                b.setMessage("Chức năng các nút:" +
                        "\nRESET: Reset lại ván game" +
                        "\nTIME: Đặt thời gian suy nghĩ (Thời gian suy nghĩ hiện tại: " + time + " giây)" +
                        "\nESC: Quay về Home");
                b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = b.create();
                dialog.show();
            }
        });
        btnB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder b = new AlertDialog.Builder(PlayGamePVEActivity.this);
                b.setMessage("Bắt đầu game!!!");
                b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reset_game();
                    }
                });
                AlertDialog dialog = b.create();
                dialog.show();
            }
        });
        btnC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(time < 8){
                    time = time + 2;
                }
                else if(time == 8){
                    time = 2;
                }
                Toast.makeText(PlayGamePVEActivity.this, "Thời gian suy nghĩ là: " + time + " giây", Toast.LENGTH_SHORT).show();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                come_back_home();
            }
        });
        imgLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    int x=  (int)event.getX();
                    int y = (int)event.getY();

                    tounch(x, y);
                    set_up_next();

                    if(!finish_game()){
                        if(!my_turn){
                            AI_turn();
                        }
                    }
                    else{
                        AlertDialog.Builder b = new AlertDialog.Builder(PlayGamePVEActivity.this);
                        b.setMessage("Đã kêt thúc ván đấu, bạn có muốn trở về Home?");
                        b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                come_back_home();
                            }
                        });
                        b.setNegativeButton("CANCLE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                my_turn = false;
                                dialog.cancel();
                            }
                        }) ;
                        AlertDialog dialog = b.create();
                        dialog.show();
                    }
                    return true;
                }

                return false;
            }
        });
    }

    private void reset_game() {
        for(quan_XO element: arr_XO){
            relativeLayout.removeView(findViewById(element.id_of_view));
            element.user = 0;
        }
        ThirdQuan = false;
        dem = 0;
        cotOld = -1;
        hangOld = -1;
        my_turn = true;
        warning = -1;
        arr_selected.clear();
        arr_opponent.clear();
        AI_turn();
    }

    private boolean Ktra_RightTop(int hang, int cot){
        boolean ktra = false;

        int vtri1 = hang*22+cot;
        int vtri2 = (hang-1)*22+cot+1;
        int vtri3 = (hang-2)*22+cot+2;
        int vtri4 = (hang-3)*22+cot+3;
        int vtri5 = (hang-4)*22+cot+4;
        if(arr_XO[vtri1].user == arr_XO[vtri2].user && arr_XO[vtri2].user == arr_XO[vtri3].user && arr_XO[vtri1].user != 0 &&
                arr_XO[vtri3].user == arr_XO[vtri4].user && arr_XO[vtri4].user == arr_XO[vtri5].user){
            ktra = true;
        }

        return  ktra;
    }
    private boolean Ktra_RightBottom(int hang, int cot){
        boolean ktra = false;

        int vtri1 = hang*22+cot;
        int vtri2 = (hang+1)*22+cot+1;
        int vtri3 = (hang+2)*22+cot+2;
        int vtri4 = (hang+3)*22+cot+3;
        int vtri5 = (hang+4)*22+cot+4;
        if(arr_XO[vtri1].user == arr_XO[vtri2].user && arr_XO[vtri2].user == arr_XO[vtri3].user && arr_XO[vtri1].user != 0 &&
                arr_XO[vtri3].user == arr_XO[vtri4].user && arr_XO[vtri4].user == arr_XO[vtri5].user){
            ktra = true;
        }

        return  ktra;
    }
    private boolean Ktra_Right(int hang, int cot){
        boolean ktra = false;

        int vtri1 = hang*22+cot;
        int vtri2 = hang*22+cot+1;
        int vtri3 = hang*22+cot+2;
        int vtri4 = hang*22+cot+3;
        int vtri5 = hang*22+cot+4;
        if(arr_XO[vtri1].user == arr_XO[vtri2].user && arr_XO[vtri2].user == arr_XO[vtri3].user && arr_XO[vtri1].user != 0 &&
                arr_XO[vtri3].user == arr_XO[vtri4].user && arr_XO[vtri4].user == arr_XO[vtri5].user){
            ktra = true;
        }

        return  ktra;
    }
    private boolean Ktra_Bottom(int hang, int cot){
        boolean ktra = false;

        int vtri1 = hang*22+cot;
        int vtri2 = (hang+1)*22+cot;
        int vtri3 = (hang+2)*22+cot;
        int vtri4 = (hang+3)*22+cot;
        int vtri5 = (hang+4)*22+cot;
        if(arr_XO[vtri1].user == arr_XO[vtri2].user && arr_XO[vtri2].user == arr_XO[vtri3].user && arr_XO[vtri1].user != 0 &&
                arr_XO[vtri3].user == arr_XO[vtri4].user && arr_XO[vtri4].user == arr_XO[vtri5].user){
            ktra = true;
        }
        return  ktra;
    }

    private boolean finish_game() {
        if(time_choose == 0){
            Toast.makeText(this, "Bạn đã hết thời gian suy nghĩ!!!", Toast.LENGTH_SHORT).show();
        }
        if(model2.equals("3vs3")){
            return finish_caro_3x3(false);
//            Toast.makeText(context, "3vs3", Toast.LENGTH_SHORT).show();
        }
        else if(model2.equals("free")) {
            return finish_caro_free(false);
//            Toast.makeText(context, "free", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    private boolean finish_caro_free(boolean is_test) {
        for (int h = 0; h < 22; h++){
            for(int c = 0; c < 22; c++){
                int vtri_in_arr = h*22+c;
                if(h >= 4 && c <=17){
                    //Ktra RightTop
                    if(Ktra_RightTop(h, c)){
                        if(arr_XO[h*so_o_1_row+c].user == 1 && !is_test){
                            Toast.makeText(this, "YOU WIN!!!", Toast.LENGTH_LONG).show();
                        }
                        else if(arr_XO[h*so_o_1_row+c].user == 2 && !is_test){
                            Toast.makeText(this, "YOU LOSE!!!", Toast.LENGTH_LONG).show();
                        }
                        return true;
                    }
                }
                if(h <= 17){
                    //Ktra Bottom
                    if(Ktra_Bottom(h, c)){
                        if(arr_XO[h*so_o_1_row+c].user == 1 && !is_test){
                            Toast.makeText(this, "YOU WIN!!!", Toast.LENGTH_LONG).show();
                        }
                        else if(arr_XO[h*so_o_1_row+c].user == 2 && !is_test){
                            Toast.makeText(this, "YOU LOSE!!!", Toast.LENGTH_LONG).show();
                        }
                        return true;
                    }
                    if(c <= 17){
                        //Ktra RigthBottom
                        if(Ktra_RightBottom(h, c)){
                            if(arr_XO[h*so_o_1_row+c].user == 1 && !is_test){
                                Toast.makeText(this, "YOU WIN!!!", Toast.LENGTH_LONG).show();
                            }
                            else if(arr_XO[h*so_o_1_row+c].user == 2 && !is_test){
                                Toast.makeText(this, "YOU LOSE!!!", Toast.LENGTH_LONG).show();
                            }
                            return true;
                        }
                    }
                }
                if(c <= 17){
                    //Ktra Right
                    if(Ktra_Right(h, c)){
                        if(arr_XO[h*so_o_1_row+c].user == 1 && !is_test){
                            Toast.makeText(this, "YOU WIN!!!", Toast.LENGTH_LONG).show();
                        }
                        else if(arr_XO[h*so_o_1_row+c].user == 2 && !is_test){
                            Toast.makeText(this, "YOU LOSE!!!", Toast.LENGTH_LONG).show();
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean finish_caro_3x3(boolean is_test) {
        for(int i = 0;i<3;i++){
            if(arr_XO[i*3].user == arr_XO[i*3+1].user && arr_XO[i*3+1].user == arr_XO[i*3+2].user && arr_XO[i*3].user !=0){
                if(arr_XO[i*3].user == 1 && !is_test){
                    Toast.makeText(this, "YOU WIN!!!", Toast.LENGTH_LONG).show();
                }
                else if(arr_XO[i*3].user == 2 && !is_test){
                    Toast.makeText(this, "YOU LOSE!!!", Toast.LENGTH_LONG).show();
                }
                return true;
            }
            if(arr_XO[i].user == arr_XO[i+3].user && arr_XO[i+3].user == arr_XO[i+6].user && arr_XO[i].user !=0){
                if(arr_XO[i].user == 1 && !is_test){
                    Toast.makeText(this, "YOU WIN!!!", Toast.LENGTH_LONG).show();
                }
                else if(arr_XO[i].user == 2 && !is_test){
                    Toast.makeText(this, "YOU LOSE!!!", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        }
        if(arr_XO[0].user == arr_XO[4].user && arr_XO[4].user == arr_XO[8].user && arr_XO[0].user !=0){
            if(arr_XO[0].user == 1 && !is_test){
                Toast.makeText(this, "YOU WIN!!!", Toast.LENGTH_LONG).show();
            }
            else if(arr_XO[0].user == 2 && !is_test){
                Toast.makeText(this, "YOU LOSE!!!", Toast.LENGTH_LONG).show();
            }
            return true;
        }
        if(arr_XO[2].user == arr_XO[4].user && arr_XO[4].user == arr_XO[6].user && arr_XO[2].user !=0){
            if(arr_XO[2].user == 1 && !is_test){
                Toast.makeText(this, "YOU WIN!!!", Toast.LENGTH_LONG).show();
            }
            else if(arr_XO[2].user == 2 && !is_test){
                Toast.makeText(this, "YOU LOSE!!!", Toast.LENGTH_LONG).show();
            }
            return true;
        }

        return false;
    }

    private void AI_turn() {
        int resX = 0;
        int resY = 0;
        int hang = 0;
        int cot = 0;

        if(so_o_1_row == 3){
            AI_3vs3();
        }
        else if(so_o_1_row == 22){
            AI_free();
        }
        setup_timer();
        my_turn = true;
        if(finish_game()){
            timer.cancel();
            AlertDialog.Builder b = new AlertDialog.Builder(PlayGamePVEActivity.this);
            b.setMessage("Đã kêt thúc ván đấu, bạn có muốn trở về Home?");
            b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    come_back_home();
                }
            });
            b.setNegativeButton("CANCLE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    my_turn = false;
                    dialog.cancel();
                }
            }) ;
            AlertDialog dialog = b.create();
            dialog.show();
        }
    }

    private void AI_free() {
        if(dem == 0){
            previous = 11*22+11;
            arr_selected.add(new theNext(11, 11));
        }
        else{
            search_vtri_AI();
        }
//        Toast.makeText(this, String.valueOf(previous), Toast.LENGTH_SHORT).show();
        set_XY_vtri_AI();
    }

    private void AI_3vs3() {
        if(dem == 0){
            previous = 0;
        }
        else{
            search_vtri_AI();
        }
        set_XY_vtri_AI();
    }

    private void set_XY_vtri_AI() {
        int hang = previous / so_o_1_row;
        int cot = previous % so_o_1_column;

        int resX = arr_box[cot][0];
        int resY = arr_box[hang][0];

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w, h);
        params.leftMargin = resX;
        params.topMargin = resY;

        ImageView iV = new ImageView(this);
        int i = findId();
        iV.setId(i);
//                Toast.makeText(context, iV.getId() + "", Toast.LENGTH_SHORT).show();
        relativeLayout.addView(iV, params);
        iV.setImageResource(id_draw_other);

        arr_XO[hang*so_o_1_row+cot] = new quan_XO(i, 2);
    }

    private void search_vtri_AI() {
        previous = -1;
        if(so_o_1_row == 3){
            if(dem == 1){
                if(arr_XO[4].user == 0){
                    previous = 1 * 3 + 1;
                }
                else{
                    previous = arr_next.get(0).hang * 3 + arr_next.get(0).cot;
                }
            }
            else if(dem == 2){
                if(arr_XO[4].user == 2 && arr_XO[1].user == 0){
                    previous = 1;
                }
                else if(arr_XO[4].user == 2 && arr_XO[3].user == 0){
                    previous = 3;
                }
                else{
                    for(theNext element_next: arr_next){
                        arr_XO[element_next.hang*3 + element_next.cot].user = 2;
                        if(finish_caro_3x3(true)){
                            previous = element_next.hang * 3 + element_next.cot;
                            arr_XO[element_next.hang*3 + element_next.cot].user = 0;
                            break;
                        }
                        else{
                            arr_XO[element_next.hang*3 + element_next.cot].user = 1;
                            if(finish_caro_3x3(true)){
                                previous = element_next.hang * 3 + element_next.cot;
                            }
                            arr_XO[element_next.hang*3 + element_next.cot].user = 0;
                        }
                        if(element_next.equals(arr_next.get(arr_next.size()-1)) && previous == -1){
                            previous = arr_next.get(0).hang * 3 + arr_next.get(0).cot;
                        }
                    }
                }
            }
            else{
                ArrayList<Integer> can_remove = new ArrayList<>();
                for(int i = 0; i < arr_XO.length; i++){
                    if(arr_XO[i].user == 2){
                        arr_XO[i].user = 1;
                        if(!finish_caro_3x3(true)){
                            can_remove.add(i);
                        }
                        arr_XO[i].user = 2;
                    }
                }
                for(theNext element_next: arr_next){
                    arr_XO[element_next.hang*3 + element_next.cot].user = 2;
                    if(finish_caro_3x3(true)){
                        previous = element_next.hang * 3 + element_next.cot;
                        for(int remove: can_remove){
                            arr_XO[remove].user = 0;
                            if(finish_caro_3x3(true)){
                                relativeLayout.removeView(findViewById(arr_XO[remove].id_of_view));
//                                quan_XO quan_xo = new quan_XO(0);
//                                arr_XO[remove] = quan_xo;
                            }
                            else{
                                arr_XO[remove].user = 2;
                            }
                        }
                        arr_XO[element_next.hang*3 + element_next.cot].user = 0;
                        break;
                    }
                    else{
                        arr_XO[element_next.hang*3 + element_next.cot].user = 1;
                        if(finish_caro_3x3(true)){
                            previous = element_next.hang * 3 + element_next.cot;
                        }
                        arr_XO[element_next.hang*3 + element_next.cot].user = 0;
                    }
                    if(element_next.equals(arr_next.get(arr_next.size()-1))){
                        if(previous == -1){
                            previous = arr_next.get(0).hang * 3 + arr_next.get(0).cot;
                        }
                        relativeLayout.removeView(findViewById(arr_XO[can_remove.get(0)].id_of_view));
                        quan_XO quan_xo = new quan_XO(0);
                        arr_XO[can_remove.get(0)] = quan_xo;
                    }
                }
            }
        }
        else if(so_o_1_row == 22){
            warning = -1;
            arr_warning.clear();
            arr_mart.clear();
            if(arr_selected.size() > 0){
                for(theNext element : arr_selected){
                    xu_li_search_previous_for_free(element, true);
                }
            }
            if(previous == -1 && arr_warning.size() > 0){
                warning = respone_warning(2);
            }
            if(arr_opponent.size() > 0 && previous == -1){
                for(theNext element : arr_opponent){
                    xu_li_search_previous_for_free(element, false);
                }
            }
            if(previous == -1 && arr_warning.size() > 0){
                warning = respone_warning(2);
            }
            if(previous == -1 && warning == -1 && arr_mart.size() > 0){
                warning = respone_mart(2);
            }
            if(previous == -1 && warning != -1){
                previous = warning;
            }
            else if(previous == -1 && warning == -1){
                ArrayList<object_search_previous> arr_temp = new ArrayList<>();
                for(theNext item_root : arr_selected){
                    int SubHang = item_root.hang - 1;
                    int SubCot = item_root.cot - 1;
                    int AddHang = item_root.cot - 1;
                    int AddCot = item_root.cot - 1;

                    if(SubHang >= 0){
                        theNext item = new theNext(SubHang, SubCot + 1);
                        update_arr_temp(item, arr_temp);
                        if(SubCot >= 0){
                            item = new theNext(SubHang, SubCot);
                            update_arr_temp(item, arr_temp);
                        }
                        if(AddCot <= 21){
                            item = new theNext(SubHang, AddCot);
                            update_arr_temp(item, arr_temp);
                        }
                    }
                    if(AddHang <= 21){
                        theNext item = new theNext(AddHang, SubCot + 1);
                        update_arr_temp(item, arr_temp);
                        if(SubCot >= 0){
                            item = new theNext(AddHang, SubCot);
                            update_arr_temp(item, arr_temp);
                        }
                        if(AddCot <= 21){
                            item = new theNext(AddHang, AddCot);
                            update_arr_temp(item, arr_temp);
                        }
                    }
                    if(SubCot >= 0){
                        theNext item = new theNext(SubHang + 1, SubCot);
                        update_arr_temp(item, arr_temp);
                    }
                    if(AddCot <= 21){
                        theNext item = new theNext(SubHang + 1, AddCot);
                        update_arr_temp(item, arr_temp);
                    }
                }
                int max = arr_temp.get(0).user;
                previous = arr_temp.get(0).toa_do.hang * 22 + arr_temp.get(0).toa_do.cot;
                for(object_search_previous temp : arr_temp){
                    if(temp.user > max){
                        max = temp.user;
                        previous = temp.toa_do.hang * 22 + temp.toa_do.cot;
                    }
                }
            }
            arr_selected.add(new theNext(previous/22, previous%22));
        }
    }

    private void update_arr_temp(theNext item, ArrayList<object_search_previous> arr_temp) {
        if(arr_XO[item.hang*22 + item.cot].user == 0){
            if(arr_temp.size() > 0){
                for(object_search_previous element : arr_temp){
                    if(item.equals(element.toa_do)){
                        element.user++;
                        break;
                    }
                    if(arr_temp.indexOf(element) == arr_temp.size()-1){
                        arr_temp.add(new object_search_previous(item, 1));
                    }
                }
            }
            else{
                arr_temp.add(new object_search_previous(item, 1));
            }
        }
    }

    private void xu_li_search_previous_for_free(theNext node_testing, boolean for_win){
        int presnetX = node_testing.cot;
        int presnetY = node_testing.hang;

        object_search_previous[] arr_RT = new object_search_previous[5];
        arr_RT[4] = new object_search_previous(new theNext(0,0), 0);
        object_search_previous[] arr_R = new object_search_previous[5];
        arr_R[4] = new object_search_previous(new theNext(0,0), 0);
        object_search_previous[] arr_RB = new object_search_previous[5];
        arr_RB[4] = new object_search_previous(new theNext(0,0), 0);
        object_search_previous[] arr_B = new object_search_previous[5];
        arr_B[4] = new object_search_previous(new theNext(0,0), 0);
        object_search_previous[] arr_LT = new object_search_previous[5];
        arr_LT[4] = new object_search_previous(new theNext(0,0), 0);
        object_search_previous[] arr_L = new object_search_previous[5];
        arr_L[4] = new object_search_previous(new theNext(0,0), 0);
        object_search_previous[] arr_LB = new object_search_previous[5];
        arr_LB[4] = new object_search_previous(new theNext(0,0), 0);
        object_search_previous[] arr_T = new object_search_previous[5];
        arr_T[4] = new object_search_previous(new theNext(0,0), 0);

        //luu lai toa do va gtri cho cac node thuoc cac mang tren
        for(int i = 1; i < 5; i++){
            if(presnetY >= 4){
                //Ktra Top
                object_search_previous temp = new object_search_previous(new theNext(presnetY-i, presnetX), 0);
                arr_T[i-1] = temp;
                arr_T[i-1].user = arr_XO[(presnetY-i)*22 + presnetX].user;
                if(arr_T[i-1].user == 2 && for_win){
                    arr_T[4].user++;
                }
                else if(arr_T[i-1].user == 1 && !for_win){
                    arr_T[4].user++;
                }
                if(presnetX <=17){
                    //Ktra RightTop
                    temp = new object_search_previous(new theNext(presnetY-i, presnetX+i), 0);
                    arr_RT[i-1] = temp;
                    arr_RT[i-1].user = arr_XO[(presnetY-i)*22 + presnetX+i].user;
                    if(arr_RT[i-1].user == 2 && for_win){
                        arr_RT[4].user++;
                    }
                    else if(arr_RT[i-1].user == 1 && !for_win){
                        arr_RT[4].user++;
                    }
                }
                if(presnetX >=4){
                    //Ktra LeftTop
                    temp = new object_search_previous(new theNext(presnetY-i, presnetX-i), 0);
                    arr_LT[i-1] = temp;
                    arr_LT[i-1].user = arr_XO[(presnetY-i)*22 + presnetX-i].user;
                    if(arr_LT[i-1].user == 2 && for_win){
                        arr_LT[4].user++;
                    }
                    else if(arr_LT[i-1].user == 1 && !for_win){
                        arr_LT[4].user++;
                    }
                }
            }
            if(presnetY <= 17){
                //Ktra Bottom
                object_search_previous temp = new object_search_previous(new theNext(presnetY+i, presnetX), 0);
                arr_B[i-1] = temp;
                arr_B[i-1].user = arr_XO[(presnetY+i)*22 + presnetX].user;
                if(arr_B[i-1].user == 2 && for_win){
                    arr_B[4].user++;
                }
                else if(arr_B[i-1].user == 1 && !for_win){
                    arr_B[4].user++;
                }
                if(presnetX <= 17){
                    //Ktra RigthBottom
                    temp = new object_search_previous(new theNext(presnetY+i, presnetX+i), 0);
                    arr_RB[i-1] = temp;
                    arr_RB[i-1].user = arr_XO[(presnetY+i)*22 + presnetX+i].user;
                    if(arr_RB[i-1].user == 2 && for_win){
                        arr_RB[4].user++;
                    }
                    else if(arr_RB[i-1].user == 1 && !for_win){
                        arr_RB[4].user++;
                    }
                }
                if(presnetX <= 17){
                    //Ktra LeftBottom
                    temp = new object_search_previous(new theNext(presnetY+i, presnetX-i), 0);
                    arr_LB[i-1] = temp;
                    arr_LB[i-1].user = arr_XO[(presnetY+i)*22 + presnetX-i].user;
                    if(arr_LB[i-1].user == 2 && for_win){
                        arr_LB[4].user++;
                    }
                    else if(arr_LB[i-1].user == 1 && !for_win){
                        arr_LB[4].user++;
                    }
                }
            }
            if(presnetX <= 17){
                //Ktra Right
                object_search_previous temp = new object_search_previous(new theNext(presnetY, presnetX+i), 0);
                arr_R[i-1] = temp;
                arr_R[i-1].user = arr_XO[presnetY*22 + presnetX+i].user;
                if(arr_R[i-1].user == 2 && for_win){
                    arr_R[4].user++;
                }
                else if(arr_R[i-1].user == 1 && !for_win){
                    arr_R[4].user++;
                }
            }
            if(presnetX >= 4){
                //Ktra Left
                object_search_previous temp = new object_search_previous(new theNext(presnetY, presnetX-i), 0);
                arr_L[i-1] = temp;
                arr_L[i-1].user = arr_XO[presnetY*22 + presnetX-i].user;
                if(arr_L[i-1].user == 2 && for_win){
                    arr_L[4].user++;
                }
                else if(arr_L[i-1].user == 1 && !for_win){
                    arr_L[4].user++;
                }
            }
        }

        //Xet huong co kha nang win(co them 3 node 2)
        if(arr_RT[4].user == 3){
            xac_dinh_previous_for_free(arr_RT);
        }
        else if(arr_R[4].user == 3){
            xac_dinh_previous_for_free(arr_R);
        }
        else if(arr_RB[4].user == 3){
            xac_dinh_previous_for_free(arr_RB);
        }
        else if(arr_B[4].user == 3){
            xac_dinh_previous_for_free(arr_B);
        }
        else if(arr_LB[4].user == 3){
            xac_dinh_previous_for_free(arr_LB);
        }
        else if(arr_L[4].user == 3){
            xac_dinh_previous_for_free(arr_L);
        }
        else if(arr_LT[4].user == 3){
            xac_dinh_previous_for_free(arr_LT);
        }
        else if(arr_T[4].user == 3){
            xac_dinh_previous_for_free(arr_T);
        }

        if(warning == -1){
            if(arr_RT[4].user == 2){
                if(arr_RT[0].toa_do.hang + 2 <= 21 && arr_RT[0].toa_do.cot - 2 >= 0){
                    if(arr_XO[(arr_RT[0].toa_do.hang + 2)*22+arr_RT[0].toa_do.cot - 2].user == 0){
                        xac_dinh_warning_for_free(arr_RT, for_win);
                    }
                }
            }
            if(arr_R[4].user == 2){
                if(arr_R[0].toa_do.cot - 2 >= 0){
                    if(arr_XO[arr_R[0].toa_do.hang*22+arr_R[0].toa_do.cot - 2].user == 0){
                        xac_dinh_warning_for_free(arr_R, for_win);
                    }
                }
            }
            if(arr_RB[4].user == 2){
                if(arr_RB[0].toa_do.hang - 2 >= 0 && arr_RB[0].toa_do.cot - 2 >= 0){
                    if(arr_XO[(arr_RB[0].toa_do.hang - 2)*22+arr_RB[0].toa_do.cot - 2].user == 0){
                        xac_dinh_warning_for_free(arr_RB, for_win);
                    }
                }
            }
            if(arr_B[4].user == 2){
                if(arr_B[0].toa_do.hang - 2 >= 0){
                    if(arr_XO[(arr_B[0].toa_do.hang - 2)*22+arr_B[0].toa_do.cot].user == 0){
                        xac_dinh_warning_for_free(arr_B, for_win);
                    }
                }
            }
            if(arr_LB[4].user == 2){
                if(arr_LB[0].toa_do.hang - 2 >= 0 && arr_LB[0].toa_do.cot + 2 <= 21){
                    if(arr_XO[(arr_LB[0].toa_do.hang - 2)*22+arr_LB[0].toa_do.cot + 2].user == 0){
                        xac_dinh_warning_for_free(arr_LB, for_win);
                    }
                }
            }
            if(arr_L[4].user == 2){
                if(arr_L[0].toa_do.cot + 2 <= 21){
                    if(arr_XO[arr_L[0].toa_do.hang*22+arr_L[0].toa_do.cot + 2].user == 0){
                        xac_dinh_warning_for_free(arr_L, for_win);
                    }
                }
            }
            if(arr_LT[4].user == 2){
                if(arr_LT[0].toa_do.hang + 2 <= 21 && arr_LT[0].toa_do.cot + 2 <= 21){
                    if(arr_XO[(arr_LT[0].toa_do.hang - 2)*22+arr_LT[0].toa_do.cot + 2].user == 0){
                        xac_dinh_warning_for_free(arr_LT, for_win);
                    }
                }
            }
            if(arr_T[4].user == 2){
                if(arr_T[0].toa_do.hang + 2 <= 21){
                    if(arr_XO[(arr_T[0].toa_do.hang + 2)*22+arr_T[0].toa_do.cot].user == 0){
                        xac_dinh_warning_for_free(arr_T, for_win);
                    }
                }
            }
        }

        if(for_win){
            if(arr_RT[4].user == 1){
                if(arr_RT[0].toa_do.hang + 2 <= 21 && arr_RT[0].toa_do.cot - 2 >= 0){
                    if(arr_XO[(arr_RT[0].toa_do.hang + 2)*22+arr_RT[0].toa_do.cot - 2].user == 0){
                        xac_dinh_mart_for_free(arr_RT);
//                        Toast.makeText(this, "a", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            if(arr_R[4].user == 1){
                if(arr_R[0].toa_do.cot - 2 >= 0){
                    if(arr_XO[arr_R[0].toa_do.hang*22+arr_R[0].toa_do.cot - 2].user == 0){
                        xac_dinh_mart_for_free(arr_R);
//                        Toast.makeText(this, "b", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            if(arr_RB[4].user == 1){
                if(arr_RB[0].toa_do.hang - 2 >= 0 && arr_RB[0].toa_do.cot - 2 >= 0){
                    if(arr_XO[(arr_RB[0].toa_do.hang - 2)*22+arr_RB[0].toa_do.cot - 2].user == 0){
                        xac_dinh_mart_for_free(arr_RB);
//                        Toast.makeText(this, "c", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            if(arr_B[4].user == 1){
                if(arr_B[0].toa_do.hang - 2 >= 0){
                    if(arr_XO[(arr_B[0].toa_do.hang - 2)*22+arr_B[0].toa_do.cot].user == 0){
                        xac_dinh_mart_for_free(arr_B);
//                        Toast.makeText(this, "d", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            if(arr_LB[4].user == 1){
                if(arr_LB[0].toa_do.hang - 2 >= 0 && arr_LB[0].toa_do.cot + 2 <= 21){
                    if(arr_XO[(arr_LB[0].toa_do.hang - 2)*22+arr_LB[0].toa_do.cot + 2].user == 0){
                        xac_dinh_mart_for_free(arr_LB);
//                        Toast.makeText(this, "e", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            if(arr_L[4].user == 1){
                if(arr_L[0].toa_do.cot + 2 <= 21){
                    if(arr_XO[arr_L[0].toa_do.hang*22+arr_L[0].toa_do.cot + 2].user == 0){
                        xac_dinh_mart_for_free(arr_L);
//                        Toast.makeText(this, "f", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            if(arr_LT[4].user == 1){
                if(arr_LT[0].toa_do.hang + 2 <= 21 && arr_LT[0].toa_do.cot + 2 <= 21){
                    if(arr_XO[(arr_LT[0].toa_do.hang - 2)*22+arr_LT[0].toa_do.cot + 2].user == 0){
                        xac_dinh_mart_for_free(arr_LT);
//                        Toast.makeText(this, "g", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            if(arr_T[4].user == 1){
                if(arr_T[0].toa_do.hang + 2 <= 21){
                    if(arr_XO[(arr_T[0].toa_do.hang + 2)*22+arr_T[0].toa_do.cot].user == 0){
                        xac_dinh_mart_for_free(arr_T);
//                        Toast.makeText(this, "h", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
//        if(for_win){
//            Toast.makeText(this, "Win: " + arr_T[4].user+"_"+arr_RT[4].user+"_"+arr_R[4].user+"_"+arr_RB[4].user+"_"+
//                    arr_B[4].user+"_"+arr_LB[4].user+"_"+arr_L[4].user+"_"+arr_LT[4].user, Toast.LENGTH_LONG).show();
//        }
//        else{
//            Toast.makeText(this, "Lose: " + arr_T[4].user+"_"+arr_RT[4].user+"_"+arr_R[4].user+"_"+arr_RB[4].user+"_"+
//                    arr_B[4].user+"_"+arr_LB[4].user+"_"+arr_L[4].user+"_"+arr_LT[4].user, Toast.LENGTH_LONG).show();
//        }
    }

    private void xac_dinh_previous_for_free(object_search_previous[] arr) {
        for(object_search_previous item : arr){
            if(item.user == 0){
                previous = item.toa_do.hang*22 + item.toa_do.cot;
            }
        }
    }

    private void xac_dinh_warning_for_free(object_search_previous[] arr, boolean for_win) {
        int thay_the = 1;
        if(for_win){
            thay_the = 2;
        }
        if(arr[0].user == thay_the && arr[1].user == 0 && arr[2].user == thay_the && arr[3].user == 0){
            arr_warning.add(new object_search_previous(new theNext(arr[1].toa_do.hang, arr[1].toa_do.cot),
                    count_dong_chat(arr[1].toa_do.hang, arr[1].toa_do.cot, thay_the, 1)));
//            Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
        }
        else if(arr[0].user == thay_the && arr[1].user == thay_the && arr[2].user == 0){
            arr_warning.add(new object_search_previous(new theNext(arr[2].toa_do.hang, arr[2].toa_do.cot),
                    count_dong_chat(arr[2].toa_do.hang, arr[2].toa_do.cot, thay_the, 1)));
//            Toast.makeText(this, arr[0].user+"_"+arr[1].user+"_"+arr[2].user+"_"+arr[3].user, Toast.LENGTH_SHORT).show();
        }
        else if(arr[0].user == 0 && arr[1].user == thay_the && arr[2].user == thay_the && arr[3].user == 0){
            arr_warning.add(new object_search_previous(new theNext(arr[3].toa_do.hang, arr[3].toa_do.cot),
                    count_dong_chat(arr[3].toa_do.hang, arr[3].toa_do.cot, thay_the, 1)));
//            Toast.makeText(this, "3", Toast.LENGTH_SHORT).show();
        }
    }

    private void xac_dinh_mart_for_free(object_search_previous[] arr){
        if(arr[0].user == 2 && arr[1].user == 0 && arr[2].user == 0){
            arr_mart.add(new object_search_previous(new theNext(arr[1].toa_do.hang, arr[1].toa_do.cot),
                    count_dong_chat(arr[1].toa_do.hang, arr[1].toa_do.cot, 2, 1)));
//            Toast.makeText(this, "1" + arr[1].toa_do.toString(), Toast.LENGTH_SHORT).show();
        }
        else if(arr[0].user == 0 && arr[1].user == 2 && arr[2].user == 0){
            arr_mart.add(new object_search_previous(new theNext(arr[0].toa_do.hang, arr[0].toa_do.cot),
                    count_dong_chat(arr[0].toa_do.hang, arr[0].toa_do.cot, 2, 1)));
//            Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
        }
        else if(arr[0].user == 0 && arr[1].user == 0 && arr[2].user == 2 && arr[3].user == 0){
            arr_mart.add(new object_search_previous(new theNext(arr[1].toa_do.hang, arr[1].toa_do.cot),
                    count_dong_chat(arr[1].toa_do.hang, arr[1].toa_do.cot, 2, 1)));
//            Toast.makeText(this, "3", Toast.LENGTH_SHORT).show();
        }
    }

    private int count_dong_chat(int hang, int cot, int thay_the, int pham_vi){
        int count = 0;

        for(int i = 1; i<=pham_vi; i++){
            if(hang - i >= 0){
                if(arr_XO[(hang-i)*22+cot].user == thay_the){
                    count++;
                }
                if(cot - i >=0){
                    for(int j = 1; j<i; j++){
                        if(arr_XO[(hang-i)*22+cot-j].user == thay_the){
                            count++;
                        }
                    }
                }
                if(cot + i <=21){
                    for(int j = 1; j<i; j++){
                        if(arr_XO[(hang-i)*22+cot+j].user == thay_the){
                            count++;
                        }
                    }
                }
            }
            if(hang + i <= 21){
                if(arr_XO[(hang+i)*22+cot].user == thay_the){
                    count++;
                }

                if(cot - i >=0){
                    for(int j = 1; j<i; j++){
                        if(arr_XO[(hang+i)*22+cot-j].user == thay_the){
                            count++;
                        }
                    }
                }
                if(cot + i <=21){
                    for(int j = 1; j<i; j++){
                        if(arr_XO[(hang+i)*22+cot+j].user == thay_the){
                            count++;
                        }
                    }
                }
            }
            if(cot - i >= 0 && arr_XO[hang*22+cot-i].user == thay_the){
                count++;
            }
            if(cot + i >= 0 && arr_XO[hang*22+cot+i].user == thay_the){
                count++;
            }
        }

        return count;
    }

    private int respone_warning(int user){
        int for_user = user;
        int max = arr_warning.get(0).user;
        theNext toado_max = arr_warning.get(0).toa_do;

//        System.out.println(arr_warning.get(0).toString());

        for(int j = 1; j<arr_warning.size(); j++){
//            System.out.println(arr_warning.get(j).toString());
            if(max < arr_warning.get(j).user){
                max = arr_warning.get(j).user;
                toado_max = arr_warning.get(j).toa_do;
            }
            else if(max == arr_warning.get(j).user){
                int pass = max;
                int present = arr_warning.get(j).user;
                int phamviNew = 2;
                while(pass == present && phamviNew < 5){
                    pass = count_dong_chat(toado_max.hang, toado_max.cot, for_user, phamviNew);
                    theNext object_present = arr_warning.get(j).toa_do;
                    present = count_dong_chat(object_present.hang, object_present.cot, for_user, phamviNew);
                    if(pass < present){
                        max = present;
                        toado_max = object_present;
                        break;
                    }
                    else{
                        phamviNew++;
                    }
                    if(for_user == user && phamviNew == 3){
                        for_user = Math.abs(for_user - 3);
                        phamviNew = 1;
                    }
                }
            }
        }
        return toado_max.hang*22 + toado_max.cot;
    }

    private int respone_mart(int user){
        int for_user = user;
        int max = arr_mart.get(0).user;
        theNext toado_max = arr_mart.get(0).toa_do;

//        System.out.println(arr_mart.get(0).toString());

        for(int j = 1; j<arr_mart.size(); j++){
            System.out.println(arr_mart.get(j).toString());
            if(max < arr_mart.get(j).user){
                max = arr_mart.get(j).user;
                toado_max = arr_mart.get(j).toa_do;
            }
            else if(max == arr_mart.get(j).user){
                int pass = max;
                int present = arr_mart.get(j).user;
                int phamviNew = 2;
                while(pass == present && phamviNew < 3){
                    pass = count_dong_chat(toado_max.hang, toado_max.cot, for_user, phamviNew);
                    theNext object_present = arr_mart.get(j).toa_do;
                    present = count_dong_chat(object_present.hang, object_present.cot, for_user, phamviNew);
                    if(pass < present){
                        max = present;
                        toado_max = object_present;
                        break;
                    }
                    else{
                        phamviNew++;
                    }
                }
            }
        }
        return toado_max.hang*22 + toado_max.cot;
    }

    private void set_up_next() {
        arr_next.clear();

        if(so_o_1_row == 3){
            for(int i = 0; i < 9; i++){
                if(arr_XO[i].user == 0){
                    arr_next.add(new theNext(i/3, i%3));
                }
            }
        }
        else if(so_o_1_row == 22){

        }
    }

    private void get_logged() {
        Intent intent = getIntent();
        acc_logged.setUsername(intent.getStringExtra("username"));
        acc_logged.setImg(intent.getStringExtra("urlImg"));
        acc_logged.setWon(Integer.parseInt(intent.getStringExtra("countWin")));
        acc_logged.setLose(Integer.parseInt(intent.getStringExtra("countLose")));

        model2 = intent.getStringExtra("model2");
    }

    public void create(){
        id_draw = R.drawable.x;
        id_draw_other = R.drawable.o;

        arr_box = new int[so_o_1_row][2];
        for(int i = 0; i<so_o_1_row; i++){
            arr_box[i][0] = xMax/so_o_1_row*i;
            arr_box[i][1] = arr_box[i][0] + xMax/so_o_1_row;

//            System.out.println("Row "+ i + ": " + arr_box[0][0] + "_" + arr_box[0][1]);
        }
        arr_XO = new quan_XO[so_o_1_row*so_o_1_column];
        for(int i = 0; i< so_o_1_row*so_o_1_column; i++){
            arr_XO[i] = new quan_XO(0);
        }
        w = xMax/so_o_1_row;
        h = yMax/so_o_1_column;

        AlertDialog.Builder b = new AlertDialog.Builder(PlayGamePVEActivity.this);
        b.setMessage("Bắt đầu game!!!");
        b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AI_turn();
            }
        });
        AlertDialog dialog = b.create();
        dialog.show();
    }

    public void tounch(int x, int y){
        if(my_turn){
            if(dem == 3 && so_o_1_row == 3){
                ThirdQuan = true;
                dem++;
            }

            if(ThirdQuan == false){
                my_turn = false;
                get_XY_vtri(x, y);
            }
            else{
                ThirdQuan = false;
                set_XY_vtri_to0(x, y);
            }
        }
    }

    private void get_XY_vtri(int x, int y){
        int resX = 0;
        int resY = 0;
        int hang = 0;
        int cot = 0;

        for(int i = 0; i < arr_box.length; i++){
            if(i == 0){
                if(x <= arr_box[0][1]){
                    resX = arr_box[0][0];
                    cot = 0;
                }
                if(y <= arr_box[0][1]){
                    resY = arr_box[0][0];
                    hang = 0;
                }
            }
            else{
                if(x <= arr_box[i][1] && x > arr_box[i-1][1]){
                    resX = arr_box[i][0];
                    cot = i;
                }
                if(y <= arr_box[i][1] && y > arr_box[i-1][1]){
                    resY = arr_box[i][0];
                    hang = i;
                }
            }
        }

//        Toast.makeText(getContext(), arr_XO[hang*3+cot].user + "_" + hang + "_" + cot + "_" + hangOld + "_" + cotOld
//                , Toast.LENGTH_LONG).show();

//        Toast.makeText(getContext(), "Zo 1", Toast.LENGTH_SHORT).show();
        if(arr_XO[hang*so_o_1_row+cot].user == 0 && (hang != hangOld || cot != cotOld)){
//            Toast.makeText(getContext(), "Zo 11", Toast.LENGTH_SHORT).show();

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w, h);
            params.leftMargin = resX;
            params.topMargin = resY;

            ImageView iV = new ImageView(this);
            int i = findId();
            iV.setId(i);
//                Toast.makeText(context, iV.getId() + "", Toast.LENGTH_SHORT).show();
            relativeLayout.addView(iV, params);
            iV.setImageResource(id_draw);

            arr_XO[hang*so_o_1_row+cot] = new quan_XO(i, 1);
            arr_opponent.add(new theNext(hang, cot));

            ThirdQuan = true;
            if(dem < 3 || so_o_1_row > 3){
                dem++;
                ThirdQuan = false;
            }
            
            timer.cancel();
        }
        else{
//            Toast.makeText(getContext(), "Zo 12", Toast.LENGTH_SHORT).show();
            my_turn = true;
        }
    }

    private void set_XY_vtri_to0(int x, int y){
        int hang, cot;

        if(x <= arr_box[0][1]){
            cot = 0;
        }
        else if(x <= arr_box[1][1]){
            cot = 1;
        }
        else{
            cot = 2;
        }

        if(y <= arr_box[0][1]){
            hang = 0;
        }
        else if(y <= arr_box[1][1]){
            hang = 1;
        }
        else{
            hang = 2;
        }

//        Toast.makeText(getContext(), "Zo 2", Toast.LENGTH_SHORT).show();
        if(arr_XO[hang*3+cot].user == 1){
//            Toast.makeText(getContext(), "Zo 21", Toast.LENGTH_SHORT).show();
            hangOld = hang;
            cotOld = cot;

            relativeLayout.removeView(findViewById(arr_XO[hang *  so_o_1_row + cot].id_of_view));

            quan_XO quan_xo = new quan_XO(0);
//            System.out.println("aaaaaaaaaa: " + quan_xo.toString());
            arr_XO[hang *  so_o_1_row + cot] = quan_xo;
        }
        else{
//            Toast.makeText(getContext(), "Zo 22", Toast.LENGTH_SHORT).show();
            ThirdQuan = true;
        }
    }

    public int findId(){
        int id = 1;
        View v = findViewById(id);
        while (v != null){
            v = findViewById(++id);
        }
        return id++;
    }

    public void come_back_home(){
//        Toast.makeText(this, us.toString(), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(PlayGamePVEActivity.this, HomeActivity.class);
        intent.putExtra("username", acc_logged.getUsername());
        intent.putExtra("countWin", acc_logged.getWon() + "");
        intent.putExtra("countLose", acc_logged.getLose() + "");
        intent.putExtra("urlImg", acc_logged.getImg());
        startActivity(intent);
    }

    private class quan_XO{
        int id_of_view;
        int user;

        public quan_XO(int user) {
            this.user = user;
        }

        public quan_XO(int id_of_view, int user) {
            this.id_of_view = id_of_view;
            this.user = user;
        }

        @Override
        public String toString() {
            return "quan_XO{" +
                    "id_of_view=" + id_of_view +
                    ", user=" + user +
                    '}';
        }
    }

    private class theNext{
        int hang;
        int cot;

        public theNext(int hang, int cot) {
            this.hang = hang;
            this.cot = cot;
        }

        @Override
        public String toString() {
            return "theNext{" +
                    "hang=" + hang +
                    ", cot=" + cot +
                    '}';
        }
    }

    private class object_search_previous{
        theNext toa_do;
        int user;

        public object_search_previous(theNext toa_do, int user) {
            this.toa_do = toa_do;
            this.user = user;
        }

        @Override
        public String toString() {
            return "object_search_previous{" +
                    "toa_do=" + toa_do +
                    ", user=" + user +
                    '}';
        }
    }

    private void setup_timer(){
        time_choose = time * 100;
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if(time_choose > 0){
                    time_choose--;
                    setText_for_tViewTime((time_choose/100) + ":" + (time_choose%100));
                }
                else{
                    timer.cancel();
                    //thua roi do tao gia win
                    arr_XO[0].user = arr_XO[1].user = arr_XO[2].user = arr_XO[3].user = arr_XO[4].user = 2;
                    my_turn = false;
                }
            }
        };
        long delay = 10L;
        timer = new Timer("Timer");
        timer.schedule(timerTask, 0, delay);
    }

    private void setText_for_tViewTime(final String s){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tVTimer.setText(s);
            }
        });
    }
}