package com.example.cocaro;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.cocaro.Object.SlidePhoto;

import org.xmlpull.v1.XmlPullParser;

import java.util.List;

public class SlidePhotoAdapter extends PagerAdapter {

    private List<SlidePhoto> mListSlidePhoto;
    private Context context;
    frmChoose2 formChoose2;

    public SlidePhotoAdapter(List<SlidePhoto> mListSlidePhoto, Context context, frmChoose2 frm2) {
        this.mListSlidePhoto = mListSlidePhoto;
        this.context = context;
        this.formChoose2 = frm2;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view;
        if(position == 0){
            view = LayoutInflater.from(container.getContext()).inflate(R.layout.table_3vs3, container, false);
        }
        else if(position == 1) {
            view = LayoutInflater.from(container.getContext()).inflate(R.layout.table_free, container, false);
        }
        else{
            view = LayoutInflater.from(container.getContext()).inflate(R.layout.table_3vs3, container, false);
        }
        ImageView btnMain = view.findViewById(R.id.btnMain);
        ImageView btnDetail = view.findViewById(R.id.imagebtn);

        ShowDialog helpDialog = new ShowDialog(getContext());

        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position == 0){
                    helpDialog.showDialogIn4("3vs3", "Mỗi người sẽ được đi 3 quân và sau đó là đổi chổ các quân cho đến khi có người thắng.");
                }
                else if(position == 1){
                    helpDialog.showDialogIn4("FREE", "Những người chơi thay nhau ra quân cho đến khi có người thắng hoặc full bàn cờ.");
                }
            }
        });
        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formChoose2.to_next_form(position + "");
            }
        });
//        SlidePhoto slidePhoto = mListSlidePhoto.get(position);
//        if (slidePhoto != null) {
////            Glide.with(mfragment).load(slidePhoto.getResourceId()).into(imgSlidePhoto);
//        }
        container.addView(view);
        return view;
    }


    @Override
    public int getCount() {
        if (mListSlidePhoto != null) {
            return mListSlidePhoto.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}