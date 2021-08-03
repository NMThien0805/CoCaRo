package com.example.cocaro;

import android.accounts.Account;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cocaro.Object.Acc;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Collections;

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

    public ConnectFirebase(Context context, int so_luong_o, Login frmLogin) {
        this.context = context;
        this.so_luong_o = so_luong_o;
        this.frmLogin = frmLogin;
    }

    public ConnectFirebase(Context context, Register frmRegister) {
        this.frmRegister = frmRegister;
        this.context = context;
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

    public void singnUp(String username, String pass, String img, int accpect, Uri mImageUri) {
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
                            Acc acc = new Acc(username, pass, false, 0, 0, img);
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
                                        Acc acc = new Acc(username, pass, false, 0, 0, downloadUri.toString());
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
                                frmLogin.go_to_home();
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

    public void play_a_game() {
        gamesRef.child(name_room).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(finish_game(snapshot.getValue().toString())){
                    Toast.makeText(context, "Finish game", Toast.LENGTH_SHORT).show();
                    gamesRef.child(name_room).removeValue();
                    update_names("", "-1");
                    return;
                }else{
//                    Toast.makeText(context, "Changed " + name_room, Toast.LENGTH_SHORT).show();
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
    }

    private boolean finish_game(String xau) {
        boolean res = false;
//        Toast.makeText(context, "xau: "+ xau, Toast.LENGTH_LONG).show();

        xau = xau.substring(1, xau.length()-1);
//        Toast.makeText(context, "xau: "+ xau, Toast.LENGTH_LONG).show();

        if(finish_caro_3x3(xau) && xau.length() == 25){
            res = true;
        }
        else if(finish_caro_free(xau)){
            res = true;
        }

        return res;
    }

    private boolean finish_caro_free(String xau) {
        return false;
    }

    private boolean finish_caro_3x3(String xau) {
        String s1, s2, s3;
        s1 = xau.substring(0, 8);
        s2 = xau.substring(9, 17);
        s3 = xau.substring(18, 25);

//        Toast.makeText(context, "s1: " + s1 + "|", Toast.LENGTH_SHORT).show();
//        Toast.makeText(context, "s2: " + s2 + "|", Toast.LENGTH_SHORT).show();
//        Toast.makeText(context, "s3: " + s3 + "|", Toast.LENGTH_SHORT).show();

        if(s1.equals("1, 1, 1,")){
            return true;
        }
        else if(s2.equals("1, 1, 1,")){
            return true;
        }
        else if(s3.equals("1, 1, 1")){
            return true;
        }
        else{
            return false;
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

    private void update_names(String old_list, String number_found) {
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
//                    Toast.makeText(context, value, Toast.LENGTH_SHORT).show();
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

        gamesRef.child(name_room).setValue(game_arr);
    }
}
