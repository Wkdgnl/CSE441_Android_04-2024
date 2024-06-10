package com.example.giuaky;

public class NhanVien {
    private int ID;
    private String HoTen;
    private String ChucVu;
    private  String Email;
    private String SoDienThoai;
    private byte[] AnhDaiDien;
    private int DonViID;

    public NhanVien(int ID, String hoTen, String chucVu, String email, String soDienThoai, byte[] anhDaiDien, int donViID) {
        this.ID = ID;
        HoTen = hoTen;
        ChucVu = chucVu;
        Email = email;
        SoDienThoai = soDienThoai;
        AnhDaiDien = anhDaiDien;
        DonViID = donViID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getHoTen() {
        return HoTen;
    }

    public void setHoTen(String hoTen) {
        HoTen = hoTen;
    }

    public String getChucVu() {
        return ChucVu;
    }

    public void setChucVu(String chucVu) {
        ChucVu = chucVu;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getSoDienThoai() {
        return SoDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        SoDienThoai = soDienThoai;
    }

    public byte[] getAnhDaiDien() {
        return AnhDaiDien;
    }

    public void setAnhDaiDien(byte[] anhDaiDien) {
        AnhDaiDien = anhDaiDien;
    }

    public int getDonViID() {
        return DonViID;
    }

    public void setDonViID(int donViID) {
        DonViID = donViID;
    }
}
