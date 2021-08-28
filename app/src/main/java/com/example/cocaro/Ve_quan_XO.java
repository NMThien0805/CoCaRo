package com.example.cocaro;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Ve_quan_XO {
    //Constructor zo
    boolean is_chu_room;
    PlayGameActivity playGameActivity;
    RelativeLayout relativeLayout;
    int xMax;
    int yMax;
    int so_o_1_row;
    int so_o_1_column;
    String name_room;

    //Setup after create
    boolean my_turn = false;
    int id_draw;
    int id_draw_other;
    int[][] arr_box;
    ConnectFirebase cn;
    int w;
    int h;
    quan_XO[] arr_XO;
    Context context;

    //Default
    boolean ThirdQuan = false;
    int dem = 0;
    int cotOld = -1;
    int hangOld = -1;

    public Ve_quan_XO(boolean is_chu_room, PlayGameActivity playGameActivity, RelativeLayout relativeLayout, int xMax, int yMax, int so_o_1_row, int so_o_1_column, String name_room) {
        this.is_chu_room = is_chu_room;
        this.playGameActivity = playGameActivity;
        this.relativeLayout = relativeLayout;
        this.xMax = xMax;
        this.yMax = yMax;
        this.so_o_1_row = so_o_1_row;
        this.so_o_1_column = so_o_1_column;
        this.name_room = name_room;
    }

    public void create(){
        context = playGameActivity;
        cn = new ConnectFirebase(context, name_room, this, playGameActivity);
        cn.Init();
        if(is_chu_room){
            my_turn = false;
            id_draw = R.drawable.x;
            id_draw_other = R.drawable.o;
            cn.set_up_game_chu();
        }
        else{
            my_turn = true;
            id_draw = R.drawable.o;
            id_draw_other = R.drawable.x;
            cn.set_up_game_join();
        }
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
            if(dem == 0 && hang==1 && cot == 1 && is_chu_room == false){
                Toast.makeText(context, "Do not check this box for the first time!!!", Toast.LENGTH_SHORT).show();
                my_turn = true;
            }
            else{
                if(so_o_1_row == 3 && is_chu_room){
                    cn.add_quan_xo_3vs3_chu(hang, cot);
                }
                else if(so_o_1_row == 22 && is_chu_room){
                    cn.add_quan_xo_free_chu(hang, cot);
                }
                else if(so_o_1_row == 3 && !is_chu_room){
                    cn.add_quan_xo_3vs3_join(hang, cot);
                }
                else if(so_o_1_row == 22 && !is_chu_room){
                    cn.add_quan_xo_free_join(hang, cot);
                }

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w, h);
                params.leftMargin = resX;
                params.topMargin = resY;

                ImageView iV = new ImageView(context);
                int i = findId();
                iV.setId(i);
//                Toast.makeText(context, iV.getId() + "", Toast.LENGTH_SHORT).show();
                relativeLayout.addView(iV, params);
                iV.setImageResource(id_draw);

                if(is_chu_room){
                    arr_XO[hang*so_o_1_row+cot] = new quan_XO(i, 1);
                }
                else{
                    arr_XO[hang*so_o_1_row+cot] = new quan_XO(i, 2);
                }

                ThirdQuan = true;
                if(dem < 3 || so_o_1_row > 3){
                    dem++;
                    ThirdQuan = false;
                }
            }
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
        if((arr_XO[hang*3+cot].user == 1 && is_chu_room) || (arr_XO[hang*3+cot].user == 2 && is_chu_room == false)){
//            Toast.makeText(getContext(), "Zo 21", Toast.LENGTH_SHORT).show();
            hangOld = hang;
            cotOld = cot;

            if(so_o_1_row == 3){
                cn.add_quan_xo_3vs3_to0(hang, cot);
            }
        }
        else{
//            Toast.makeText(getContext(), "Zo 22", Toast.LENGTH_SHORT).show();
            ThirdQuan = true;
        }
    }

    public void set_XY_db_to0(int stt_of_box){
        int hang = stt_of_box / so_o_1_row;
        int cot = stt_of_box % so_o_1_column;

        relativeLayout.removeView(playGameActivity.findViewById(arr_XO[hang *  so_o_1_row + cot].id_of_view));

        quan_XO quan_xo = new quan_XO(0);
//            System.out.println("aaaaaaaaaa: " + quan_xo.toString());
        arr_XO[hang *  so_o_1_row + cot] = quan_xo;
    }

    public void get_XY_db(int stt_of_box){
        int resX, resY;

        int hang = stt_of_box / so_o_1_row;
        int cot = stt_of_box % so_o_1_row;

        resX = arr_box[cot][0];
        resY = arr_box[hang][0];

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w, h);
        params.leftMargin = resX;
        params.topMargin = resY;

        ImageView iV = new ImageView(context);
        int i = findId();
        iV.setId(i);
//                Toast.makeText(context, iV.getId() + "", Toast.LENGTH_SHORT).show();
        relativeLayout.addView(iV, params);
        iV.setImageResource(id_draw_other);

        quan_XO quan_xo;
        if(is_chu_room){
            quan_xo = new quan_XO(i, 2);
        }
        else{
            quan_xo = new quan_XO(i, 1);
        }
//            System.out.println("aaaaaaaaaa: " + quan_xo.toString());
        arr_XO[hang *  so_o_1_row + cot] = quan_xo;

        my_turn = true;
    }

    public int findId(){
        int id = 1;
        View v = playGameActivity.findViewById(id);
        while (v != null){
            v = playGameActivity.findViewById(++id);
        }
        return id++;
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
}
