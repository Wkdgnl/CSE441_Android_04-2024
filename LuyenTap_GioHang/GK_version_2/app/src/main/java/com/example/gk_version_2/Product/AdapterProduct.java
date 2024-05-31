package com.example.gk_version_2.Product;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gk_version_2.Donhang;
import com.example.gk_version_2.R;

import java.util.List;

public class AdapterProduct extends RecyclerView.Adapter<AdapterProduct.ProductViewHolder>{

    private List<Product> mylist;
    private Context context;
    public AdapterProduct(Context context){this.context = context;}



    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = mylist.get(position);
        if(product == null){
            return;
        }
        holder.imgv_item.setImageResource(product.getImg());
        holder.txt_tensp.setText(product.getTensp());
        holder.txt_mota.setText(product.getMota());
        holder.txt_gia.setText("Giá: "+product.getGia()+" $");
        holder.imgb_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Donhang.class);
                intent.putExtra("anh",product.getImg());
                intent.putExtra("ten",product.getTensp());
                intent.putExtra("mota",product.getMota());
                intent.putExtra("gia",product.getGia());

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(mylist!=null)return mylist.size();
        return 0;
    }
    public void setData(List<Product> list){
        this.mylist = list;
        notifyDataSetChanged();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgv_item;
        private TextView txt_tensp;
        private  TextView txt_mota;
        private  TextView txt_gia;
        private ImageButton imgb_add;


        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
           imgv_item = itemView.findViewById(R.id.imgv_item);
           txt_tensp = itemView.findViewById(R.id.txt_tensp);
           txt_mota = itemView.findViewById(R.id.txt_mota);
           txt_gia = itemView.findViewById(R.id.txt_gia);
           imgb_add = itemView.findViewById(R.id.imgb_add);
        }
    }


}
