package com.example.model;

import java.sql.Date;

public class ThanhVien {
    private String MaThanhVien;
    private String HoTen;
    private String GioiTinh;
    private String SoDienThoai;
    private String Email;
    private String LoaiThanhVien;
    private Date NgayDangKy;
    private Date NgayHetHan;
    private String TrangThai;

    public ThanhVien() {
    }

    public ThanhVien(String maThanhVien, String hoTen, String gioiTinh, String soDienThoai, String email, String loaiThanhVien, Date ngayDangKy, Date ngayHetHan, String trangThai) {
        MaThanhVien = maThanhVien;
        HoTen = hoTen;
        GioiTinh = gioiTinh;
        SoDienThoai = soDienThoai;
        Email = email;
        LoaiThanhVien = loaiThanhVien;
        NgayDangKy = ngayDangKy;
        NgayHetHan = ngayHetHan;
        TrangThai = trangThai;
    }

    public String getMaThanhVien() {
        return MaThanhVien;
    }

    public void setMaThanhVien(String maThanhVien) {
        MaThanhVien = maThanhVien;
    }

    public String getHoTen() {
        return HoTen;
    }

    public void setHoTen(String hoTen) {
        HoTen = hoTen;
    }

    public String getSoDienThoai() {
        return SoDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        SoDienThoai = soDienThoai;
    }

    public String getGioiTinh() {
        return GioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        GioiTinh = gioiTinh;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getLoaiThanhVien() {
        return LoaiThanhVien;
    }

    public void setLoaiThanhVien(String loaiThanhVien) {
        LoaiThanhVien = loaiThanhVien;
    }

    public Date getNgayDangKy() {
        return NgayDangKy;
    }

    public void setNgayDangKy(Date ngayDangKy) {
        NgayDangKy = ngayDangKy;
    }

    public Date getNgayHetHan() {
        return NgayHetHan;
    }

    public void setNgayHetHan(Date ngayHetHan) {
        NgayHetHan = ngayHetHan;
    }

    public String getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(String trangThai) {
        TrangThai = trangThai;
    }

    @Override
    public String toString() {
        return MaThanhVien + " - " + HoTen;
    }
}
