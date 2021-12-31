package com.example.jsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<ListItem> listItems = new ArrayList<ListItem>();

    public ListViewAdapter(Context context){
        this.mContext = context;
    }
    @Override
    public int getCount() {
        return listItems.size();
    }
    @Override
    public Object getItem(int i) {
        return listItems.get(i);
    }
    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // item.xml 레이아웃을 inflate해서 참조획득
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, parent, false);
        }
        // item.xml 의 참조 획득
        TextView txt_id = (TextView)convertView.findViewById(R.id.txt_id);
        TextView txt_time = (TextView)convertView.findViewById(R.id.txt_time);
        TextView txt_content = (TextView)convertView.findViewById(R.id.txt_content);
        ListItem listItem = listItems.get(position);

        // 가져온 데이터를 텍스트뷰에 입력
        txt_id.setText(listItem.getId());
        txt_time.setText(listItem.getTime());
        txt_content.setText(listItem.getContent());

        // 리스트 아이템 삭제
//        btn_delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                listItems.remove(position);
//                notifyDataSetChanged();
//            }
//        });
        return convertView;
    }

    public void addItem(String id, String time, String content){
        ListItem listItem = new ListItem();

        listItem.setId(id);
        listItem.setTime(time);
        listItem.setContent(content);

        listItems.add(listItem);
    }
}

