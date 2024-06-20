package com.example.project_btl_android;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/** @noinspection ALL*/
public class BillActivity extends AppCompatActivity {
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    ArrayList<Product> myProductListInBill;
    ArrayAdapterInBill myAdapterInBill;
    ListView lvProductInBill;
    Double totalMoney = 0.0;
    Cart cart;
    TextView txtBackFromBillToCart, txtOrder, txtTaxMoney, txtTotalMoneyWithTax, txtTotalMoneyProductInBill;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        //Mở database và ánh xạ ID
        txtTaxMoney = findViewById(R.id.txtTaxMoney);
        txtTotalMoneyWithTax = findViewById(R.id.txtTotalMoneyWithTax);
        txtTotalMoneyProductInBill = findViewById(R.id.txtTotalMoneyProductInBill);
        lvProductInBill = findViewById(R.id.lvProductInBill);
        myProductListInBill = new ArrayList<>();
        txtOrder = findViewById(R.id.txtToOrder);
        txtOrder.setText("Đặt hàng");

        // Xử lý Intent đến từ Detail
        if(getIntent().getAction() != null && getIntent().getAction().equals("FromDetail")){
//            Product productFromDetail = (Product) getIntent().getSerializableExtra("ProductFromDetail");
            String id = getIntent().getStringExtra("productId");
            cart = (Cart) getIntent().getSerializableExtra("Cart");
            db.getReference("Product").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Product productFromDetail = snapshot.getValue(Product.class);
                    productFromDetail.quantityInCart(cart, productFromDetail.getId()).thenAccept(quantitiCart -> {
                        totalMoney = productFromDetail.toMoney(quantitiCart);
                        productFromDetail.setQuantityInCart(quantitiCart);
                        myProductListInBill.add(productFromDetail);
                        display();
                    }).exceptionally(ex -> {
                        Toast.makeText(BillActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                        return null;
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
            });
        }
//        Intent đến từ Activity khác (Cart)
        else{
            ArrayList<String> arrIdProducts = (ArrayList<String>) getIntent().getStringArrayListExtra("selectProductList");
            totalMoney = getIntent().getDoubleExtra("Total", 1);
            cart = (Cart) getIntent().getSerializableExtra("Cart");

            List<CompletableFuture<Void>> futures = new ArrayList<>();
            for (String id : arrIdProducts) {
                CompletableFuture<Void> future = new CompletableFuture<>();
                futures.add(future);

                db.getReference("Product").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Product productFromDetail = snapshot.getValue(Product.class);
                        productFromDetail.quantityInCart(cart, productFromDetail.getId()).thenAccept(quantitiCart -> {
//                            totalMoney = productFromDetail.toMoney(quantitiCart);
                            productFromDetail.setQuantityInCart(quantitiCart);
                            myProductListInBill.add(productFromDetail);

                            // Đánh dấu future này đã hoàn thành
                            future.complete(null);
                        }).exceptionally(ex -> {
                            Toast.makeText(BillActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                            // Đánh dấu future này đã hoàn thành, ngay cả khi có lỗi
                            future.completeExceptionally(ex);
                            return null;
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        // Đánh dấu future này đã hoàn thành, ngay cả khi có lỗi
                        future.completeExceptionally(new Exception(error.getMessage()));
                    }
                });
            }
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).thenRun(() -> {
                // Tất cả các sản phẩm đã được xử lý, bây giờ hiển thị thông tin
                display();
            }).exceptionally(ex -> {
                Toast.makeText(BillActivity.this, "Failed to load all products: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                return null;
            });
        }
    }

    private void display(){
        // Đưa danh sách sản phẩm lên giao diện
        myAdapterInBill = new ArrayAdapterInBill(BillActivity.this, R.layout.layout_product_inbill, myProductListInBill);
        lvProductInBill.setAdapter(myAdapterInBill);

        //Đưa tổng tiền (cùng thuế) lên giao diện
        txtTotalMoneyProductInBill.setText(totalMoney+"");
        double tax = totalMoney*10.0/100;
        txtTaxMoney.setText(tax + "");
        txtTotalMoneyWithTax.setText((totalMoney+tax)+"");

        //Tạo dialog xác nhận đặt hàng
        AlertDialog.Builder dialog = new AlertDialog.Builder(BillActivity.this);
        dialog.setTitle("XÁC NHẬN ĐẶT HÀNG");

        //Cancel => huỷ dialog
        dialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        //OK => xử lý
        dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Toast.makeText(BillActivity.this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();

                //Đổi định dạng ngày
                LocalDate currentDate = LocalDate.now();
                int year = currentDate.getYear();
                int month = currentDate.getMonthValue();
                int dayOfMonth = currentDate.getDayOfMonth();
                String formattedDate = String.format("%04d-%02d-%02d", year, month, dayOfMonth);// Định dạng thành "YYYY-mm-dd"

                //Tạo bill mới với username, ngày, total
                Bill bill = new Bill(cart.getUserId(), formattedDate, Double.parseDouble(txtTotalMoneyWithTax.getText().toString()), "wait");
                String id = db.getReference("Bill").push().getKey();
                bill.setId(id);
                db.getReference("Bill").child(id).setValue(bill)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                List<CompletableFuture<Void>> futures = new ArrayList<>();
                                for (Product product : myProductListInBill) {
                                    // Thêm detail bill
                                    DetailBillProduct detailBillProduct = new DetailBillProduct(bill.getId(), product.getId(), product.getQuantityInCart());
                                    DetailBillProductService detailBillProductService = new DetailBillProductService();
                                    CompletableFuture<Void> futureAddDetail = detailBillProductService.addDetailProductBill(detailBillProduct);
                                    futures.add(futureAddDetail);
                                    // Lấy số lượng sản phẩm
                                    CompletableFuture<Integer> futureGetQuantity = product.quantityInCart(cart, product.getId());
                                    futures.add(futureGetQuantity.thenCompose(quantity -> {
                                        // Giảm số lượng sản phẩm có trong hệ thống
                                        return product.updateProductQuantity(product.getId(), product.getQuantity() - quantity);
                                    }).exceptionally(throwable -> {
                                        Toast.makeText(getApplicationContext(), "Failed to update product quantity: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                        return null;
                                    }));

                                    // Xóa sản phẩm ra khỏi giỏ hàng
                                    CompletableFuture<Void> futureDeleteCart = product.deleteProductFromCart(cart, product.getId());
                                    futures.add(futureDeleteCart);

                                    // Đặt lại quantityInCart
                                    product.setQuantityInCart(0);
                                }

                                // Chờ tất cả các tác vụ hoàn tất
                                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).thenRun(() -> {
                                    txtOrder.setText("Đặt hàng thành công");
                                    txtOrder.setEnabled(false);
                                    myAdapterInBill.notifyDataSetChanged();
                                    showMessage();
                                }).exceptionally(throwable -> {
                                    Toast.makeText(getApplicationContext(), "Failed to complete all tasks: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                    return null;
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Failed to add bill", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        txtOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.create().show();
            }
        });

        txtBackFromBillToCart = findViewById(R.id.txtBackFromBillToCart);
        txtBackFromBillToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //Thông báo đặt hàng thành công
    public void showMessage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(BillActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_success, null);
        builder.setView(dialogView);
        AlertDialog dialog2 = builder.create();
        dialog2.show();
        TextView txtDone = dialogView.findViewById(R.id.txtDone);
        txtDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}