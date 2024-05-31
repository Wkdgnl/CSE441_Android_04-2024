package com.example.gk_version_2.Product;

public class Product {
    private int img;
    private String tensp;
    private String mota;
    private int gia;

    public Product(int img, String tensp, String mota, int gia) {
        this.img = img;
        this.tensp = tensp;
        this.mota = mota;
        this.gia = gia;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getTensp() {
        return tensp;
    }

    public void setTensp(String tensp) {
        this.tensp = tensp;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }
}
