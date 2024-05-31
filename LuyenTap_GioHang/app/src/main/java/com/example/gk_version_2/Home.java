package com.example.gk_version_2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import com.example.gk_version_2.Product.AdapterProduct;
import com.example.gk_version_2.Product.Product;

public class Home extends AppCompatActivity {
    int img[] = {R.drawable.anh1,R.drawable.anh2,R.drawable.anh3,R.drawable.anh4,R.drawable.anh5,R.drawable.anh6,R.drawable.anh7,R.drawable.anh8,R.drawable.anh9, R.drawable.anh10};
    String tensp[] = {"1","1","1","1","1","1","1","1","1","1","1"};
    String mota[] = {"Chứa rất nhiều hợp chất polyphenol được gọi là epigallocatechin galatte (EGCG) chất chống oxy hóa chống lại các gốc tự do và lão hóa, có tác dụng trẻ hóa làn da","Một truyền thuyết từ Trung Quốc nói rằng, Hoàng đế Cheng-Nung đã phát hiện ra trà một cách tình cờ vào năm 2374 Trước Công nguyên. Ông đang ngồi dưới gốc cây và chuẩn bị một ly nước nóng để uống. Vô tình một số lá trà rơi vào cốc và trà được phát hiện từ đó"," Một truyền thuyết khác từ Ấn Độ cho hay, một vị tu sĩ Bodhidharma đã phát hiện ra trà khi ông thực hiện một chuyến thiền dài đến Trung Quốc. Đó là khi ông buồn ngủ và chợp mắt. Trong một khoảnh khắc vị tu sĩ này đã lấy một con dao và cắt đi mi mắt. Cây chè mọc lên trên mảnh đất nơi mi mắt ông rơi xuống. Về sau, các nhà sư Phật giáo uống trà để có thể tỉnh táo trong thời gian thiền định dài.","Giúp tinh thần tỉnh táo, thư thái","Chống oxy hóa, tăng sức đề kháng để chống lại bệnh tật","Thanh lọc cơ thể, hỗ trợ giảm cân, dùng kết hợp khi ăn kiêng","Chứa caffeine nên rất tốt để uống vào buổi sáng giúp tỉnh táo, một lựa chọn khác thay vì uống cà phê","Hỗ trợ giảm cân, tăng khả năng đốt cháy chất béo đến 17%"," Bảo vệ bạn khỏi các bệnh như ung thư phổi, ruột và gan"," Trấn an tinh thần, giảm căng thẳng"};
    int gia[] = {3000,4560,3450,4032,5204,6040,7300,8000,9000,10000};
    RecyclerView recy;
    ImageButton btngiohang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recy = findViewById(R.id.recy);
        btngiohang = findViewById(R.id.btngiohang);

        btngiohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Giohang.class);
                startActivity(intent);
            }
        });

        AdapterProduct myadapter = new AdapterProduct(this);
        LinearLayoutManager line = new LinearLayoutManager(this);
        recy.setLayoutManager(line);
        myadapter.setData(getData());
        recy.setAdapter(myadapter);

    }
    private List<Product> getData(){
        List<Product> list = new ArrayList<>();
        list.add(new Product(img[0],"Ipone",mota[0],gia[0]));
        list.add(new Product(img[1],tensp[1],mota[1],gia[1]));
        list.add(new Product(img[2],tensp[2],mota[2],gia[2]));
        list.add(new Product(img[3],tensp[3],mota[3],gia[3]));
        list.add(new Product(img[4],tensp[4],mota[4],gia[4]));
        list.add(new Product(img[5],tensp[5],mota[5],gia[5]));
        list.add(new Product(img[6],tensp[6],mota[6],gia[6]));
        list.add(new Product(img[7],tensp[7],mota[7],gia[7]));
        list.add(new Product(img[8],tensp[8],mota[8],gia[8]));
        list.add(new Product(img[9],tensp[9],mota[9],gia[9]));
        return list;
    }
}