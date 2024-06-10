package com.example.giuaky;

public class DonVi {
    private int ID;
    private String TenDonVi;
    private String Email;
    private String Website;
    private byte[] Logo;
    private String DiaChi;
    private String DienThoai;
    private int DonViCha;

    public DonVi(int ID, String tenDonVi, String email, String website, byte[] logo, String diaChi, String dienThoai, int donViCha) {
        this.ID = ID;
        TenDonVi = tenDonVi;
        Email = email;
        Website = website;
        Logo = logo;
        DiaChi = diaChi;
        DienThoai = dienThoai;
        DonViCha = donViCha;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTenDonVi() {
        return TenDonVi;
    }

    public void setTenDonVi(String tenDonVi) {
        TenDonVi = tenDonVi;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(String diaChi) {
        DiaChi = diaChi;
    }

    public String getDienThoai() {
        return DienThoai;
    }

    public void setDienThoai(String dienThoai) {
        DienThoai = dienThoai;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getWebsite() {
        return Website;
    }

    public void setWebsite(String website) {
        Website = website;
    }

    public byte[] getLogo() {
        return Logo;
    }

    public void setLogo(byte[] logo) {
        Logo = logo;
    }

    public int getDonViCha() {
        return DonViCha;
    }

    public void setDonViCha(int donViCha) {
        DonViCha = donViCha;
    }
}
