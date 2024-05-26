package com.example.karaoke;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class myarrayAdapter extends ArrayAdapter<Item> {
    Activity context = null;
    ArrayList<Item> myArray = null;
    int LayoutId;
    public myarrayAdapter(Activity context, int LayoutId,ArrayList<Item>arr) {
        super(context, LayoutId,arr);
// TODO Auto-generated constructor stub
        this.context = context;
        this.LayoutId = LayoutId;
        this.myArray = arr;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
// TODO Auto-generated method stub
        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(LayoutId, null);
        Item myItem = myArray.get(position);
        ImageView btnlike = convertView.findViewById(R.id.btnlike);
        int thich = myItem.getThich();
        if (thich==1) {
            btnlike.setImageResource(R.drawable.like);
        } else {
            btnlike.setImageResource(R.drawable.unlike);
        }
        TextView tieude = convertView.findViewById(R.id.txttieude);
        tieude.setText(myItem.getTieude());
        TextView maso = convertView.findViewById(R.id.txtmaso);
        maso.setText(myItem.getMaso());
        return convertView;
    }
}