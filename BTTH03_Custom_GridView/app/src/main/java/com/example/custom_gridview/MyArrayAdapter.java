package com.example.custom_gridview;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
public class MyArrayAdapter extends ArrayAdapter<Phone>
{
    Activity context;
    int IdLayout;
    ArrayList<Phone> mylist;
//Tạo Constructor để MainActivity gọi đến và truyền 3 tham số vào
    public MyArrayAdapter(Activity context, int
            idLayout, ArrayList<Phone> mylist) {
        super(context, idLayout,mylist);
        this.context = context;
        IdLayout = idLayout;
        this.mylist = mylist;
    }
    //gọi hàm getView để tiến hành sắp xếp và hiển thị dữ liệu
    @NonNull
    @Override
    public View getView(int position, @Nullable View
            convertView, @NonNull ViewGroup parent) {
//Tạo đế chứa Layout
        LayoutInflater myflactor =
                context.getLayoutInflater();
//Đặt Layout lên đế để tạo thành View
        convertView = myflactor.inflate(IdLayout,null);
//Lấy 1 phần từ trong mảng ra
        Phone myphone = mylist.get(position);
//Khai báo và hiển thị hình ảnh
        ImageView img_phone = convertView.findViewById(R.id.imageView2);
        img_phone.setImageResource(myphone.getImage());
//Khai báo và hiển thị tên Điện thoại
        TextView txt_phone = convertView.findViewById(R.id.txt_tensp);
        txt_phone.setText(myphone.getName());
//Khai báo và hiển thị giá Điện thoại
        TextView txt_price = convertView.findViewById(R.id.txt_giasp);
        txt_price.setText(myphone.getPrice()+" VNĐ");
        return convertView;
    }
}