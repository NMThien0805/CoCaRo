package com.example.cocaro;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class ShowDialog {

    Context context;

    public ShowDialog(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void showDialogIn4(String hinhthuc_game, String in4){
        final Dialog dialog =  new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_detail);

        Window window = dialog.getWindow();
        if(window==null){
            return;
        }

        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAtt = window.getAttributes();
        window.setAttributes(windowAtt);

        TextView tVAAA, tVOK, tVIn4;

        tVAAA = dialog.findViewById(R.id.tVAAA);
        tVOK = dialog.findViewById(R.id.tVOk);
        tVIn4 = dialog.findViewById(R.id.tVIn4);

        tVAAA.setText(hinhthuc_game);
        tVIn4.setText(in4);

        tVOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
