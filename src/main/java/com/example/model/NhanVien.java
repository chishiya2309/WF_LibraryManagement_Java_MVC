package com.example.model;

import java.sql.Date;

public class NhanVien {
    private String ID;
    private String HoTen;
    private String GioiTinh;
    private String ChucVu;
    private String Email;
    private String SoDienThoai;
    private Date NgayVaoLam;
    private String TrangThai;

    public NhanVien() {
    }

    public NhanVien(String ID, String hoTen, String gioiTinh, String chucVu, String email, String soDienThoai, Date ngayVaoLam, String trangThai) {
        this.ID = ID;
        this.HoTen = hoTen;
        this.GioiTinh = gioiTinh;
        this.ChucVu = chucVu;
        this.Email = email;
        this.SoDienThoai = soDienThoai;
        this.NgayVaoLam = ngayVaoLam;
        this.TrangThai = trangThai;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getHoTen() {
        return HoTen;
    }

    public void setHoTen(String hoTen) {
        HoTen = hoTen;
    }

    public String getGioiTinh() {
        return GioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        GioiTinh = gioiTinh;
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

    public Date getNgayVaoLam() {
        return NgayVaoLam;
    }

    public void setNgayVaoLam(Date ngayVaoLam) {
        NgayVaoLam = ngayVaoLam;
    }

    public String getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(String trangThai) {
        TrangThai = trangThai;
    }
}
