package com.example.custom_listview;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyArrayAdapter extends ArrayAdapter<Phone> {
    Activity context;
    int IdLayout;
    ArrayList<Phone> myList;
    // tạo constructor để MainActivity gọi đến và truyền các tham số vào

    public MyArrayAdapter( Activity context, int idLayout, ArrayList<Phone> myList) {
        super(context, idLayout,myList);
        this.context = context;
        IdLayout = idLayout;
        this.myList = myList;
    }
    // gọi hàm list view để tiến hành sắp xếp dữ liệu

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
     // Tạo đế chưa layout
        LayoutInflater myflacter = context.getLayoutInflater();
        // Đặt InputLayout lên đế để tạo thành View
        convertView = myflacter.inflate(IdLayout, null);
        // lấy 1 phần tử trong mảng
        Phone myphone = myList.get(position);
        // khai báo tham chiếu If và hiển thị lên ImagePhone
        ImageView img_phone = convertView.findViewById(R.id.imgphone);
        img_phone.setImageResource(myphone.getImage());
        // khai báo, tham chiếu id và hiển thị tên điện thoại lên TextView;
        TextView txt_phone = convertView.findViewById(R.id.txt_phone);
        txt_phone.setText(myphone.getName());
        return convertView;
    }
}
