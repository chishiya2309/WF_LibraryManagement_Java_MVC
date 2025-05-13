package com.example.model;

public class Sach {
    private String MaSach;
    private String ISBN;
    private String TenSach;
    private String TacGia;
    private DanhMucSach danhMucSach;
    private int NamXuatBan;
    private String NXB;
    private int SoBan;
    private int KhaDung;
    private String ViTri;

    public Sach() {
    }

    public Sach(String maSach, String ISBN, String tenSach, String tacGia, DanhMucSach danhMucSach, int namXuatBan, String NXB, int soBan, int khaDung, String viTri) {
        MaSach = maSach;
        this.ISBN = ISBN;
        TenSach = tenSach;
        TacGia = tacGia;
        this.danhMucSach = danhMucSach;
        NamXuatBan = namXuatBan;
        this.NXB = NXB;
        SoBan = soBan;
        KhaDung = khaDung;
        ViTri = viTri;
    }

    public String getMaSach() {
        return MaSach;
    }

    public void setMaSach(String maSach) {
        MaSach = maSach;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getTenSach() {
        return TenSach;
    }

    public void setTenSach(String tenSach) {
        TenSach = tenSach;
    }

    public String getTacGia() {
        return TacGia;
    }

    public void setTacGia(String tacGia) {
        TacGia = tacGia;
    }

    public DanhMucSach getDanhMucSach() {
        return danhMucSach;
    }

    public void setDanhMucSach(DanhMucSach danhMucSach) {
        this.danhMucSach = danhMucSach;
    }

    public int getNamXuatBan() {
        return NamXuatBan;
    }

    public void setNamXuatBan(int namXuatBan) {
        NamXuatBan = namXuatBan;
    }

    public String getNXB() {
        return NXB;
    }

    public void setNXB(String NXB) {
        this.NXB = NXB;
    }

    public int getSoBan() {
        return SoBan;
    }

    public void setSoBan(int soBan) {
        SoBan = soBan;
    }

    public int getKhaDung() {
        return KhaDung;
    }

    public void setKhaDung(int khaDung) {
        KhaDung = khaDung;
    }

    public String getViTri() {
        return ViTri;
    }

    public void setViTri(String viTri) {
        ViTri = viTri;
    }

    @Override
    public String toString() {
        return MaSach + " - " + TenSach;
    }
}
