package com.example.gk_version_2.Cart;

public class Cart {
    private String tensp;
    private String mota;
    private int gia;
    private int soluong;
    private int thanhtien;
    private byte[] img ; // Thêm trường này để lưu ID của ảnh


    public Cart(String tensp, String mota, int gia, int soluong, int thanhtien, byte[] img) {
        this.tensp = tensp;
        this.mota = mota;
        this.gia = gia;
        this.soluong = soluong;
        this.thanhtien = thanhtien;
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

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

    public int getThanhtien() {
        return thanhtien;
    }

    public void setThanhtien(int thanhtien) {
        this.thanhtien = thanhtien;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }
}
