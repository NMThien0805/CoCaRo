package com.example.cocaro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class Register extends AppCompatActivity {

    String url_img_default = "https://firebasestorage.googleapis.com/v0/b/cocaro-f318d.appspot.com/o/avtmacdinhFB.jpg?alt=media&token=caf478e9-6ea6-4d29-8434-38a66b728345";

    EditText username, password, confirmpassword, email;
    TextView btnFolder, btnSingUP;
    ImageView img;

    ConnectFirebase cn = new ConnectFirebase(Register.this, this);

    private Uri selectedUriImage;
    String picturePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_register);

        setControl();
        setEvent();
    }

    void setControl(){
        username = findViewById(R.id.usename);
        password = findViewById(R.id.password);
        confirmpassword = findViewById(R.id.confirmPass);
        email = findViewById(R.id.email);

        btnFolder = findViewById(R.id.btnFolder);
        btnSingUP = findViewById(R.id.btnSignup);

        img = findViewById(R.id.img);
    }

    void setEvent(){
        btnFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,100);
            }
        });
        btnSingUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().equals("")){
                    Toast.makeText(Register.this, "Username is not NULL!", Toast.LENGTH_SHORT).show();
                }
                else if(email.getText().toString().equals("")){
                    Toast.makeText(Register.this, "Email is not NULL!", Toast.LENGTH_SHORT).show();
                }
                else if(password.getText().toString().equals("") || confirmpassword.getText().toString().equals("")){
                    Toast.makeText(Register.this, "Password and Confirm Password are not NULL!", Toast.LENGTH_SHORT).show();
                }
                else if(!password.getText().toString().equals(confirmpassword.getText().toString())){
                    Toast.makeText(Register.this, "Password and confirm password are not the same!", Toast.LENGTH_SHORT).show();
                }
                else if(selectedUriImage == null){
                    showAlertDialog("Please choose an avatar or we will help you do it");
                }
                else{
                    cn.Init();
                    cn.singnUp(username.getText().toString().trim(), email.getText().toString().trim(), password.getText().toString().trim()
                            , "", 1, selectedUriImage);
                }
            }
        });
    }

    public void go_to_Login() {
        Intent intent1 = new Intent(Register.this, Login.class);
        startActivity(intent1);
    }

    public void showAlertDialog(String mess){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please choose an avatar or we will help you do it!!!");
        builder.setMessage(mess);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                cn.Init();
                cn.singnUp(username.getText().toString().trim(), email.getText().toString().trim(), password.getText().toString().trim()
                        , url_img_default, 0, null);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(Register.this, "Image!!!", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            //Lấy URI hình kết quả trả về
            selectedUriImage = data.getData();
//            System.out.println(selectedUriImage);
            //lấy đường dẫn hình
            picturePath=getPicturePath(selectedUriImage);
//            System.out.println(picturePath);
            InputStream inputStream = null;
            try {
                inputStream = getContentResolver().openInputStream(selectedUriImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream);
            img.setImageBitmap(selectedBitmap);
        }
    }

    private String getPicturePath(Uri uriImage) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uriImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String path = cursor.getString(columnIndex);
        cursor.close();
        return path;
    }
}