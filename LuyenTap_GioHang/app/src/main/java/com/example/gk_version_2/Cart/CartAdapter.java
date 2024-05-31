package com.example.gk_version_2.Cart;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gk_version_2.R;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<Cart> cartList;
    private Context context;
    private byte[] img; // Mảng chứa ID của ảnh

    public CartAdapter(Context context, List<Cart> mylist, byte[] img) {
        this.context = context;
        this.cartList = mylist;
        this.img = img;
    }

    public CartAdapter(Context context, List<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item2, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Cart cart = cartList.get(position);

        // Hiển thị ảnh từ mảng img[]
        Bitmap bitmap = BitmapFactory.decodeByteArray(cart.getImg(), 0, cart.getImg().length);
        holder.img_gio_item.setImageBitmap(bitmap);
        // Đặt ảnh cho ImageView từ mảng img[] dựa vào vị trí của mục

        // Hiển thị ảnh từ mảng img[]
        holder.txt_gio_tensp.setText(cart.getTensp());
        holder.txt_gio_mota.setText(cart.getMota());
        holder.txt_gio_gia.setText(String.valueOf("Giá: "+cart.getGia()+" $"));
        holder.txt_gio_soluong.setText(String.valueOf("Số lượng: "+cart.getSoluong()));
        holder.txt_gio_tongtien.setText(String.valueOf("Tổng: "+cart.getThanhtien()));


    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        TextView txt_gio_tensp,txt_gio_mota,txt_gio_gia,txt_gio_soluong,txt_gio_tongtien;
         ImageView img_gio_item;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_gio_tensp = itemView.findViewById(R.id.txt_gio_tensp);
            txt_gio_mota = itemView.findViewById(R.id.txt_gio_mota);
            txt_gio_gia = itemView.findViewById(R.id.txt_gio_gia);
            txt_gio_soluong= itemView.findViewById(R.id.txt_gio_soluong);
            txt_gio_tongtien = itemView.findViewById(R.id.txt_gio_tong);
            img_gio_item = itemView.findViewById(R.id.imgv_gio_item);
        }
    }
}
