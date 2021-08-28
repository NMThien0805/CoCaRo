package com.example.cocaro;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cocaro.Object.Acc;
import com.example.cocaro.Object.Room;
import com.example.cocaro.Object.UserOfRoom;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Consumer;

public class ConnectFirebase {
    FirebaseDatabase database;
    DatabaseReference gamesRef; //include an array room
    DatabaseReference namesRef; //include an array number
    DatabaseReference accRef; //include an array accout

    FirebaseStorage storage;
    StorageReference mStorageRef;

    Context context;
    int so_luong_o;

    String name_room = "";
    String number_present = "";

    Login frmLogin;
    Register frmRegister;
    ForgotPasswordActivity frmForgot;
    HomeActivity homeActivity;
    frmBeforePlayGame frmBefore;
    Ve_quan_XO ve_quan_xo;


    PlayGameActivity playActivity;

    Acc acc_logged;

    String url = "https://firebasestorage.googleapis.com/v0/b/cocaro-f318d.appspot.com/o/avtmacdinhFB.jpg?alt=media&token=caf478e9-6ea6-4d29-8434-38a66b728345";


    public ConnectFirebase() {
    }

    public ConnectFirebase(Context context, Login frmLogin) {
        this.context = context;
        this.frmLogin = frmLogin;
    }

    public ConnectFirebase(Context context, Register frmRegister) {
        this.frmRegister = frmRegister;
        this.context = context;
    }

    public ConnectFirebase(Context context, ForgotPasswordActivity frmForgot) {
        this.frmForgot = frmForgot;
        this.context = context;
    }

    public ConnectFirebase(Context context, int so_luong_o, Acc acc_logged, frmBeforePlayGame frmBefore) {
        this.context = context;
        this.so_luong_o = so_luong_o;
        this.acc_logged = acc_logged;
        this.frmBefore = frmBefore;
    }

    public ConnectFirebase(Context context, HomeActivity homeActivity){
        this.homeActivity = homeActivity;
        this.context = context;
    }

    public ConnectFirebase(Context context, String name_room, Ve_quan_XO ve_quan_xo, PlayGameActivity playActivity) {
        this.context = context;
        this.name_room = name_room;
        this.ve_quan_xo = ve_quan_xo;
        this.playActivity = playActivity;
    }

    public ConnectFirebase(Context context, PlayGameActivity playActivity, String name_room){
        this.playActivity = playActivity;
        this.context = context;
        this.name_room = name_room;
    }

    void Init(){
        storage = FirebaseStorage.getInstance("gs://cocaro-f318d.appspot.com");

        mStorageRef = storage.getReference("accout");

        database = FirebaseDatabase.getInstance("https://cocaro-f318d-default-rtdb.firebaseio.com/");


        namesRef = database.getReference("names");
        gamesRef = database.getReference("games");

        accRef = database.getReference("accout");

//        namesRef.setValue("0");
//        namesRef.setValue("0 1 2 4 6");

//        create_room();
//        play_a_game();

//        singnUp("MinhThien", "bua123", "");
//        signIn("minhten", "bua123");
    }

    public void singnUp(String username, String email, String pass, String img, int accpect, Uri mImageUri) {
        accRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.isSuccessful()){
                    System.out.println("Error getting data" + task.getException());
//                    Toast.makeText(context, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
                else{
                    boolean check = false;
                    for(DataSnapshot item : task.getResult().getChildren()){
                        Acc acc = item.getValue(Acc.class);
//                        Toast.makeText(context, acc.getUsername(), Toast.LENGTH_SHORT).show();
                        if(username.equals(acc.getUsername())){
                            Toast.makeText(context, "Username already exist!!!", Toast.LENGTH_SHORT).show();
                            check = true;
                            break;
                        }
                    }
                    if(check == false){
                        if(accpect == 0){
                            Acc acc = new Acc(username, email, pass, false, 0, 0, img);
                            accRef.child(acc.getUsername()).setValue(acc);
                            Toast.makeText(context, "Added new account", Toast.LENGTH_SHORT).show();
                            frmRegister.go_to_Login();
                        }
                        else{
                            final StorageReference ref = mStorageRef.child(username);
                            UploadTask uploadTask = ref.putFile(mImageUri);

                            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!task.isSuccessful()) {
                                        throw task.getException();
                                    }

                                    // Continue with the task to get the download URL
                                    return ref.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        Uri downloadUri = task.getResult();
                                        Acc acc = new Acc(username, email, pass, false, 0, 0, downloadUri.toString());
                                        accRef.child(acc.getUsername()).setValue(acc);
                                        Toast.makeText(context, "Added new account", Toast.LENGTH_SHORT).show();
                                        frmRegister.go_to_Login();
                                    } else {
                                        Toast.makeText(context, "Fail: " + task.getException(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });
    }

    public void signIn(String username, String password) {
        if(username.equals("") || password.equals("")){
            Toast.makeText(context, "Username and Password please!!!", Toast.LENGTH_SHORT).show();
        }
        else{
            accRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(!task.isSuccessful()){
                        System.out.println("Error getting data" + task.getException());
//                        Toast.makeText(context, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        boolean check = false;
                        for(DataSnapshot item : task.getResult().getChildren()){
                            Acc acc = item.getValue(Acc.class);
//                        Toast.makeText(context, acc.getUsername(), Toast.LENGTH_SHORT).show();
                            if(username.equals(acc.getUsername()) && password.equals(acc.getPassword())){
                                Toast.makeText(context, "Logged", Toast.LENGTH_SHORT).show();
                                accRef.child(username).child("trangthai").setValue(true);
                                check = true;
                                frmLogin.go_to_home(acc);
                                break;
                            }
                        }
                        if(check == false){
                            Toast.makeText(context, "Username or Password is wrong!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }

    public void set_up_game_chu(){
        gamesRef.child(name_room).child("arr_game").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                finish_game(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        gamesRef.child(name_room).child("arr_game").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getValue().toString().equals("2")){
                    ve_quan_xo.get_XY_db(Integer.parseInt(snapshot.getKey()));
                }
                else if(snapshot.getValue().toString().equals("0")){
                    ve_quan_xo.set_XY_db_to0(Integer.parseInt(snapshot.getKey()));
                }
//                Toast.makeText(context, snapshot.getKey(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void set_up_game_join(){
        gamesRef.child(name_room).child("arr_game").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getValue().toString().equals("1")){
                    ve_quan_xo.get_XY_db(Integer.parseInt(snapshot.getKey()));
                }
                else if(snapshot.getValue().toString().equals("0")){
                    ve_quan_xo.set_XY_db_to0(Integer.parseInt(snapshot.getKey()));
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        gamesRef.child(name_room).child("tt").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(String.valueOf(snapshot.getValue()).equals("Ket thuc")){
                    gamesRef.child(name_room).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                System.out.println("firebase"+ "\nError getting data\n" +  task.getException());
                            }
                            else {
                                accRef.child(playActivity.get_username_present()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        if(!task.isSuccessful()){
                                            System.out.println("firebase"+ "\nError getting data\n" +  task.getException());
                                            Toast.makeText(context, task.getException() + "", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            String userN = String.valueOf(task.getResult().child("username").getValue());
                                            String lose = String.valueOf(task.getResult().child("lose").getValue());
                                            String won = String.valueOf(task.getResult().child("won").getValue());
                                            String urlImg = String.valueOf(task.getResult().child("img").getValue());

                                            UserOfRoom us = new UserOfRoom(urlImg, userN, won, lose);
                                            playActivity.come_back_home(us);
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void add_quan_xo_3vs3_to0(int hang, int cot){
        gamesRef.child(name_room).child("arr_game").child(String.valueOf(hang * 3 + cot)).setValue(0);
    }

    public void add_quan_xo_3vs3_chu(int hang, int cot){
        gamesRef.child(name_room).child("arr_game").child(String.valueOf(hang * 3 + cot)).setValue(1);
    }

    public void add_quan_xo_3vs3_join(int hang, int cot){
        gamesRef.child(name_room).child("arr_game").child(String.valueOf(hang * 3 + cot)).setValue(2);
    }

    public void add_quan_xo_free_chu(int hang, int cot){
        gamesRef.child(name_room).child("arr_game").child(String.valueOf(hang * 22 + cot)).setValue(1);
    }

    public void add_quan_xo_free_join(int hang, int cot){
        gamesRef.child(name_room).child("arr_game").child(String.valueOf(hang * 22 + cot)).setValue(2);
    }

    public void play_a_game(boolean chu_room, String model) {
        if(chu_room){
            gamesRef.child(name_room).child("tt").setValue("Bat dau");
            to_play_game(1, model);
        }
    }

    private void finish_game(String xau) {
//        Toast.makeText(context, "xau: "+ xau, Toast.LENGTH_LONG).show();

        xau = xau.substring(1, xau.length()-1);
//        Toast.makeText(context, "xau: "+ xau, Toast.LENGTH_LONG).show();

        if(xau.length() == 25){
            finish_caro_3x3(xau);
//            Toast.makeText(context, "3vs3", Toast.LENGTH_SHORT).show();
        }
        else {
            finish_caro_free(xau);
//            Toast.makeText(context, "free", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean Ktra_RightTop(int hang, int cot, String[] arr){
        boolean ktra = false;

        int vtri1 = hang*22+cot;
        int vtri2 = (hang-1)*22+cot+1;
        int vtri3 = (hang-2)*22+cot+2;
        int vtri4 = (hang-3)*22+cot+3;
        int vtri5 = (hang-4)*22+cot+4;
        if(arr[vtri1].equals(arr[vtri2]) && arr[vtri2].equals(arr[vtri3]) && !arr[vtri1].equals("0") &&
                arr[vtri3].equals(arr[vtri4]) && arr[vtri4].equals(arr[vtri5])){
            ktra = true;
        }

        return  ktra;
    }
    private boolean Ktra_RightBottom(int hang, int cot, String[] arr){
        boolean ktra = false;

        int vtri1 = hang*22+cot;
        int vtri2 = (hang+1)*22+cot+1;
        int vtri3 = (hang+2)*22+cot+2;
        int vtri4 = (hang+3)*22+cot+3;
        int vtri5 = (hang+4)*22+cot+4;
        if(arr[vtri1].equals(arr[vtri2]) && arr[vtri2].equals(arr[vtri3]) && !arr[vtri1].equals("0") &&
                arr[vtri3].equals(arr[vtri4]) && arr[vtri4].equals(arr[vtri5])){
            ktra = true;
        }

        return  ktra;
    }
    private boolean Ktra_Right(int hang, int cot, String[] arr){
        boolean ktra = false;

        int vtri1 = hang*22+cot;
        int vtri2 = hang*22+cot+1;
        int vtri3 = hang*22+cot+2;
        int vtri4 = hang*22+cot+3;
        int vtri5 = hang*22+cot+4;
        if(arr[vtri1].equals(arr[vtri2]) && arr[vtri2].equals(arr[vtri3]) && !arr[vtri1].equals("0") &&
                arr[vtri3].equals(arr[vtri4]) && arr[vtri4].equals(arr[vtri5])){
            ktra = true;
        }

        return  ktra;
    }
    private boolean Ktra_Bottom(int hang, int cot, String[] arr){
        boolean ktra = false;

        int vtri1 = hang*22+cot;
        int vtri2 = (hang+1)*22+cot;
        int vtri3 = (hang+2)*22+cot;
        int vtri4 = (hang+3)*22+cot;
        int vtri5 = (hang+4)*22+cot;
        if(arr[vtri1].equals(arr[vtri2]) && arr[vtri2].equals(arr[vtri3]) && !arr[vtri1].equals("0") &&
                arr[vtri3].equals(arr[vtri4]) && arr[vtri4].equals(arr[vtri5])){
            ktra = true;
        }
        return  ktra;
    }

    private void finish_caro_free(String xau) {
        //Ktra theo 4 huong:
        // - Bottom or Top
        //      + Top: start from row 4
        //      + Bottom: start until row 17
        // - Right or Left
        //      + Left: column between 4 to 21
        //      + Right: column between 0 to 17
        // - RightTop or LeftBottom
        // - RightBottom or LeftTop

        String[] arr = xau.split(", ");

        for (int h = 0; h < 22; h++){
            for(int c = 0; c < 22; c++){
                int vtri_in_arr = h*22+c;
                if(h >= 4 && c <=17){
                    //Ktra RightTop
                    if(Ktra_RightTop(h, c, arr)){
                        if(arr[h*22+c].equals("1")){
                            update_win_and_lose(true, false);
                        }
                        else{
                            update_win_and_lose(true, true);
                        }
                        return;
                    }
                }
                if(h <= 17){
                    //Ktra Bottom
                    if(Ktra_Bottom(h, c, arr)){
                        if(arr[h*22+c].equals("1")){
                            update_win_and_lose(true, false);
                        }
                        else{
                            update_win_and_lose(true, true);
                        }
                        return;
                    }
                    if(c <= 17){
                        //Ktra RigthBottom
                        if(Ktra_RightBottom(h, c, arr)){
                            if(arr[h*22+c].equals("1")){
                                update_win_and_lose(true, false);
                            }
                            else{
                                update_win_and_lose(true, true);
                            }
                            return;
                        }
                    }
                }
                if(c <= 17){
                    //Ktra Right
                    if(Ktra_Right(h, c, arr)){
                        if(arr[h*22+c].equals("1")){
                            update_win_and_lose(true, false);
                        }
                        else{
                            update_win_and_lose(true, true);
                        }
                        return;
                    }
                }
            }
        }
    }

    private void finish_caro_3x3(String xau) {
        String s1, s2, s3;
        s1 = xau.substring(0, 8);
        s2 = xau.substring(9, 17);
        s3 = xau.substring(18, 25);

//        Toast.makeText(context, "s1: " + s1 + "|", Toast.LENGTH_SHORT).show();
//        Toast.makeText(context, "s2: " + s2 + "|", Toast.LENGTH_SHORT).show();
//        Toast.makeText(context, "s3: " + s3 + "|", Toast.LENGTH_SHORT).show();

        if(s1.equals("1, 1, 1,") || s1.equals("2, 2, 2,")){
            if(s1.startsWith("1")){
                update_win_and_lose(true, false);
            }
            else{
                update_win_and_lose(true, true);
            }
        }
        else if(s2.equals("1, 1, 1,") || s2.equals("2, 2, 2,")){
            if(s2.startsWith("1")){
                update_win_and_lose(true, false);
            }
            else{
                update_win_and_lose(true, true);
            }
        }
        else if(s3.equals("1, 1, 1") || s1.equals("2, 2, 2")){
            if(s3.startsWith("1")){
                update_win_and_lose(true, false);
            }
            else{
                update_win_and_lose(true, true);
            }
        }
        else{
            String[] arr1 = s1.substring(0, s1.length()-1).split(", ");
            String[] arr2 = s2.substring(0, s2.length()-1).split(", ");
            String[] arr3 = s3.split(", ");
            for(int i = 0; i< 3; i++){
                if(arr1[i].equals(arr2[i]) && arr2[i].equals(arr3[i]) && arr1[i].equals("0") == false){
                    if(arr1[i].equals("1")){
                        update_win_and_lose(true, false);
                    }
                    else{
                        update_win_and_lose(true, true);
                    }
                }
            }
            if(arr1[0].equals(arr2[1]) && arr2[1].equals(arr3[2]) && arr1[0].equals("0") == false){
                if(arr1[0].equals("1")){
                    update_win_and_lose(true, false);
                }
                else{
                    update_win_and_lose(true, true);
                }
            }
            else if(arr1[2].equals(arr2[1]) && arr2[1].equals(arr3[0]) && arr1[2].equals("0") == false){
                if(arr1[2].equals("1")){
                    update_win_and_lose(true, false);
                }
                else{
                    update_win_and_lose(true, true);
                }
            }
        }
    }

    public void create_room(){
        namesRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.isSuccessful()){
                    System.out.println("Error getting data" + task.getException());
//                    Toast.makeText(context, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
                else{
                    String value = task.getResult().getValue().toString();
//                    System.out.println("value: "+ value);
//                    Toast.makeText(context, value, Toast.LENGTH_SHORT).show();
                    setup_room(value);
                }
            }
        });
    }

    private void setup_room(String names){
//        Toast.makeText(context, "names: "+ names, Toast.LENGTH_SHORT).show();
        String[] arr = names.split(" ");

        if(arr[0].equals("") || (arr[0].equals("0") && arr.length == 1)){
            update_names(names, "1");
            setup_name_of_game("1");
        }
        else{
            ArrayList<Integer> list_number = new ArrayList<>();
            for (int i = 0; i < arr.length; i++) {
                list_number.add(Integer.valueOf(arr[i]));
            }

            String number_found = find_number_room(list_number);
//        Toast.makeText(context, number_found, Toast.LENGTH_SHORT).show();
            update_names(names, number_found);
            setup_name_of_game(number_found);
        }
    }

    public void update_names(String old_list, String number_found) {
        String s = "";
        if(number_found == "-1"){
            namesRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(!task.isSuccessful()){
                        System.out.println("Error getting data" + task.getException());
//                        Toast.makeText(context, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        String value = task.getResult().getValue().toString();
//                    System.out.println("value: "+ value);
                        del_room_present(value);
                    }
                }
            });
        }
        else{
            s = old_list + " " + number_found;
            s = s.trim();
//        Toast.makeText(context, "aqweqweqwe" + s, Toast.LENGTH_SHORT).show();
            namesRef.setValue(s);
        }
    }

    private void del_room_present(String value) {
        String[] arr_temp = value.split(" ");
        String res = "";

        number_present = name_room.substring(4);
//        Toast.makeText(context, value + "\n" + name_room+"_"+ number_present, Toast.LENGTH_LONG).show();

        for (int i = 0; i < arr_temp.length; i++) {
            if(!arr_temp[i].equals(number_present)){
                res = res + arr_temp[i] + " ";
            }
        }
        res = res.trim();
        namesRef.setValue(res);
    }

    private String find_number_room(ArrayList<Integer> list_number) {
        String result = "";

        Collections.sort(list_number);
        for (int i = 0; i < list_number.size(); i++) {
            if(list_number.get(i+1) - list_number.get(i) > 1){
                result = String.valueOf(list_number.get(i) + 1);
//                Toast.makeText(context, "a", Toast.LENGTH_SHORT).show();
                break;
            }
            if(i == list_number.size() - 2){
                result = String.valueOf(list_number.get(list_number.size()-1) + 1);
//                Toast.makeText(context, "b", Toast.LENGTH_SHORT).show();
                break;
            }
        }

        return  result;
    }

    private void setup_name_of_game(String number_room){
        number_present = number_room;
        name_room = "Room" + number_room;
        ArrayList<Integer> game_arr = new ArrayList<Integer>();
        for (int j = 0; j < so_luong_o ; j++) {
            game_arr.add(0);
        }

        UserOfRoom us1 = new UserOfRoom(acc_logged.getImg(), acc_logged.getUsername(), acc_logged.getWon() + "", acc_logged.getLose() + "");
        UserOfRoom us2 = new UserOfRoom(url, "", "0", "0");
        Room r = new Room(us1, us2, game_arr);

        gamesRef.child(name_room).setValue(r);

        gamesRef.child(name_room).child("user2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String username = snapshot.child("username").getValue().toString();
                String url = snapshot.child("avt").getValue().toString();
                String won = snapshot.child("won").getValue().toString();
                String lose = snapshot.child("lose").getValue().toString();

                frmBefore.setup_chu_room(url, username, won, lose, false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        gamesRef.child(name_room).child("tt").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(String.valueOf(snapshot.getValue()).equals("San sang")){
                    frmBefore.setReady(true);
                }
                else if(String.valueOf(snapshot.getValue()).equals("Chua san sang")){
                    frmBefore.setReady(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setup_name_of_join_room(String roomname){
        name_room = roomname;

        UserOfRoom us = new UserOfRoom(acc_logged.getImg(), acc_logged.getUsername(), acc_logged.getWon() + "", acc_logged.getLose() + "");

        gamesRef.child(name_room).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    System.out.println("firebase"+ "\nError getting data\n" +  task.getException());
                }
                else {
                    String userN = String.valueOf(task.getResult().child("user1").child("username").getValue());
                    String url_user1 = String.valueOf(task.getResult().child("user1").child("avt").getValue());
                    String lose = String.valueOf(task.getResult().child("user1").child("lose").getValue());
                    String won = String.valueOf(task.getResult().child("user1").child("won").getValue());

                    String model;
                    if(task.getResult().child("arr_game").child("9").exists()){
                        model = "free";
                    }
                    else{
                        model = "3vs3";
                    }

                    gamesRef.child(name_room).child("tt").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(String.valueOf(snapshot.getValue()).equals("Bat dau")){
//                    Toast.makeText(context, "Vao roi nha", Toast.LENGTH_SHORT).show();
                                //Vao form play game
                                to_play_game(0, model);
                            }
                            else if(String.valueOf(snapshot.getValue()).equals("Huy room")){
//                    Toast.makeText(context, "Vao roi nha", Toast.LENGTH_SHORT).show();
                                //Vao form play game
                                thoat_room(true);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

//                    Toast.makeText(context, userN, Toast.LENGTH_SHORT).show();
                    frmBefore.setup_join_room(url_user1, userN, won, lose);
                }
            }
        });

        gamesRef.child(name_room).child("user2").setValue(us);
        gamesRef.child(name_room).child("tt").setValue("Chua san sang");
    }

    public void changeReadyRoom(boolean ready){
        String value;
        if(ready){
            value = "San sang";
        }
        else{
            value = "Chua san sang";
        }
        gamesRef.child(name_room).child("tt").setValue(value);
    }

    public void reset_room(){
        namesRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                String value = task.getResult().getValue().toString();
                gamesRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
//                            if(task.getResult().getChildren().iterator().hasNext()){
//                                String s = task.getResult().getChildren().iterator().next().getKey();
//                                Toast.makeText(context, s, Toast.LENGTH_LONG).show();
//                            }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            task.getResult().getChildren().forEach(new Consumer<DataSnapshot>() {
                                @Override
                                public void accept(DataSnapshot dataSnapshot) {
                                    String s = dataSnapshot.getKey();
                                    if(!value.contains(s.replace("Room", ""))){
//                                            Toast.makeText(context, s + " khong co trong danh sach name!!!", Toast.LENGTH_SHORT).show();
                                        gamesRef.child(s).removeValue();
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    public void setup_listRoom_forHome(ListView listView){
        namesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = String.valueOf(snapshot.getValue());
                String[] arr = value.split(" ");
                if(arr[0].equals("") || (arr[0].equals("0") && arr.length == 1)){
//                        Toast.makeText(context, "Chưa có phòng PVP nào!!!", Toast.LENGTH_SHORT).show();
                }
                else{
                    ArrayList<String> list_room = new ArrayList<String>();
                    for (int i = 1; i < arr.length; i++) {
                        list_room.add("Room " + arr[i]);
                    }
                    listView.setAdapter(new list_room_adapter(list_room, context, homeActivity));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void load_room_for_home(ListView listView, String search){
        namesRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.isSuccessful()){
                    System.out.println("Error getting data" + task.getException());
//                    Toast.makeText(context, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
                else{
                    String value = task.getResult().getValue().toString();
//                    System.out.println("value: "+ value);
//                    Toast.makeText(context, value, Toast.LENGTH_SHORT).show();
                    String[] arr = value.split(" ");

                    if(arr[0].equals("") || (arr[0].equals("0") && arr.length == 1)){
//                        Toast.makeText(context, "Chưa có phòng PVP nào!!!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        ArrayList<String> list_room = new ArrayList<String>();
                        for (int i = 1; i < arr.length; i++) {
                            if(search.equals("")){
                                list_room.add("Room " + arr[i]);
                            }
                            else{
                                String temp = "Room" + arr[i];
                                if(temp.contains(search)){
                                    list_room.add("Room " + arr[i]);
                                }
                            }
                        }
                        listView.setAdapter(new list_room_adapter(list_room, context, homeActivity));
                    }
                }
            }
        });
    }

    public void search_email_for_forgot(String email) {
        accRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.isSuccessful()){
                    System.out.println("Error getting data" + task.getException());
//                        Toast.makeText(context, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
                else{
                    boolean check = false;
                    for(DataSnapshot item : task.getResult().getChildren()){
                        Acc acc = item.getValue(Acc.class);
//                        Toast.makeText(context, acc.getUsername(), Toast.LENGTH_SHORT).show();
                        if(acc.getEmail().equals(email)){

                            frmForgot.send_to_email(email, acc.getUsername());

                            check = true;
                            break;
                        }
                    }
                    if(check == false){
                        Toast.makeText(context, "Email is not found!!!", Toast.LENGTH_SHORT).show();
                        frmForgot.restore_enable_for_btnEmail();
                    }
                }
            }
        });
    }

    public void change_password(String username, String password){
        accRef.child(username).child("password").setValue(password);
    }

    public void to_play_game(int BossRoom, String model){
        Intent intent = new Intent(context, PlayGameActivity.class);
        intent.putExtra("name_room", name_room);
        intent.putExtra("model", model);
//        Toast.makeText(context, model, Toast.LENGTH_LONG).show();
        if(BossRoom == 1){
            intent.putExtra("chu_room", "true");
        }else{
            intent.putExtra("chu_room", "false");
        }
        context.startActivity(intent);
    }

    public void update_win_and_lose(boolean is_chu_room, boolean chu_room_lose){
        gamesRef.child(name_room).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    System.out.println("firebase"+ "\nError getting data\n" +  task.getException());
                }
                else {
                    String userN1 = String.valueOf(task.getResult().child("user1").child("username").getValue());
                    String url_user1 = String.valueOf(task.getResult().child("user1").child("avt").getValue());
                    String lose1 = String.valueOf(task.getResult().child("user1").child("lose").getValue());
                    String won1 = String.valueOf(task.getResult().child("user1").child("won").getValue());

                    String userN2 = String.valueOf(task.getResult().child("user2").child("username").getValue());
                    String url_user2 = String.valueOf(task.getResult().child("user2").child("avt").getValue());
                    String lose2 = String.valueOf(task.getResult().child("user2").child("lose").getValue());
                    String won2 = String.valueOf(task.getResult().child("user2").child("won").getValue());

                    Toast.makeText(context, "Finish game", Toast.LENGTH_SHORT).show();

                    accRef.child(userN2).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if(!task.isSuccessful()){
                                System.out.println("firebase"+ "\nError getting data\n" +  task.getException());
                                Toast.makeText(context, task.getException() + "", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                int oldLose = Integer.parseInt(String.valueOf(task.getResult().child("lose").getValue()));
                                int oldWon = Integer.parseInt(String.valueOf(task.getResult().child("won").getValue()));

                                if(chu_room_lose){
                                    accRef.child(userN2).child("won").setValue(oldWon + 1);
                                }
                                else{
                                    accRef.child(userN2).child("lose").setValue(oldLose + 1);
                                }
                            }
                        }
                    });
                    accRef.child(userN1).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if(!task.isSuccessful()){
                                System.out.println("firebase"+ "\nError getting data\n" +  task.getException());
                                Toast.makeText(context, task.getException() + "", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                int oldLose = Integer.parseInt(String.valueOf(task.getResult().child("lose").getValue()));
                                int oldWon = Integer.parseInt(String.valueOf(task.getResult().child("won").getValue()));

                                if(chu_room_lose){
                                    accRef.child(userN1).child("lose").setValue(oldLose + 1);
                                    UserOfRoom us = new UserOfRoom(url_user1, userN1, won1, String.valueOf(oldLose + 1));
                                    playActivity.come_back_home(us);
                                }
                                else{
                                    accRef.child(userN1).child("won").setValue(oldWon + 1);
                                    UserOfRoom us = new UserOfRoom(url_user1, userN1, String.valueOf(oldWon + 1), lose1);
                                    playActivity.come_back_home(us);
                                }
                            }
                        }
                    });
                    gamesRef.child(name_room).child("tt").setValue("Ket thuc");
                    update_names("", "-1");
                }
            }
        });
    }

    public void huy_room(){
        gamesRef.child(name_room).child("tt").setValue("Huy room");
        update_names("", "-1");
        frmBefore.come_back_home();
    }

    public void thoat_room(boolean is_chu_room_thoat){
        if(!is_chu_room_thoat){
            gamesRef.child(name_room).child("user2").child("lose").setValue("0");
            gamesRef.child(name_room).child("user2").child("won").setValue("0");
            gamesRef.child(name_room).child("user2").child("username").setValue("");
        }
        frmBefore.come_back_home();
    }

    public void load_for_play(boolean chu_room){
        gamesRef.child(name_room).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot data1 = task.getResult().child("user1");
                DataSnapshot data2 = task.getResult().child("user2");
                String userN1 = data1.child("username").getValue().toString().trim();
                String userN2 = data2.child("username").getValue().toString().trim();
                String url1 = data1.child("avt").getValue().toString().trim();
                String url2 = data2.child("avt").getValue().toString().trim();
                if(chu_room){
                    playActivity.setup_username_avt(userN1, userN2, url1, url2);
                }
                else{
                    playActivity.setup_username_avt(userN2, userN1, url2, url1);
                }
            }
        });
    }
}
