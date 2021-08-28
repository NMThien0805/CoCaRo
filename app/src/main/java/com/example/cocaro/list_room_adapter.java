package com.example.cocaro;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class list_room_adapter extends BaseAdapter {

    private List<String> listData;
    private Context context;
    private LayoutInflater layoutInflater;
    HomeActivity home;

    public list_room_adapter(List<String> listData, Context context, HomeActivity home) {
        this.listData = listData;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.home = home;
    }

    public List<String> getListData() {
        return listData;
    }

    public void setListData(List<String> listData) {
        this.listData = listData;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public LayoutInflater getLayoutInflater() {
        return layoutInflater;
    }

    public void setLayoutInflater(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewItemRoom viewItem;
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.item_list_room, null);
            viewItem = new ViewItemRoom();
            viewItem.roomName = (TextView)convertView.findViewById(R.id.tVNameRoom);
            viewItem.btnJoin = (Button)convertView.findViewById(R.id.btnJoin);
            convertView.setTag(viewItem);
        }
        else{
            viewItem = (ViewItemRoom) convertView.getTag();
        }

        String nameRoom = this.listData.get(position);
        viewItem.roomName.setText(nameRoom);

        viewItem.btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Đã chọn "+ nameRoom, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, frmBeforePlayGame.class);
                intent.putExtra("model1", "");
                intent.putExtra("model2", "");
                intent.putExtra("roomname", nameRoom.replaceAll(" ", ""));
                home.sent_logged(intent);
            }
        });

        return convertView;
    }

    private class ViewItemRoom{
        TextView roomName;
        Button btnJoin;
    }
}
