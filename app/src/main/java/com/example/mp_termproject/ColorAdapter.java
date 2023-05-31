package com.example.mp_termproject;

import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ColorAdapter extends BaseAdapter {

    private Context context;
    private List<Colors> colorList;

    public ColorAdapter(Context context,List<Colors> colorList){
        this.context = context;
        this.colorList = colorList;
    }
    @Override
    public int getCount() {
        return colorList != null ? colorList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return colorList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.color_spinner_layout,parent,false);
        TextView colorName = rootView.findViewById(R.id.colorName);
        ImageView colorImage = rootView.findViewById(R.id.colorImage);

        colorName.setText(colorList.get(position).getColor());
        colorImage.setImageResource(colorList.get(position).getImage());

        return rootView;
    }
}
