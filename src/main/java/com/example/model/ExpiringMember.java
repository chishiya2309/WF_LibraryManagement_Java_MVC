package com.example.model;

import java.util.Date;

/**
 * Model class cho thành viên sắp hết hạn
 */
public class ExpiringMember {
    private String maThanhVien;
    private String hoTen;
    private String gioiTinh;
    private String soDienThoai;
    private String email;
    private String loaiThanhVien;
    private Date ngayDangKy;
    private Date ngayHetHan;
    private String trangThai;
    
    public ExpiringMember() {
    }
    
    public ExpiringMember(String maThanhVien, String hoTen, String gioiTinh, String soDienThoai, 
                         String email, String loaiThanhVien, Date ngayDangKy, 
                         Date ngayHetHan, String trangThai) {
        this.maThanhVien = maThanhVien;
        this.hoTen = hoTen;
        this.gioiTinh = gioiTinh;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.loaiThanhVien = loaiThanhVien;
        this.ngayDangKy = ngayDangKy;
        this.ngayHetHan = ngayHetHan;
        this.trangThai = trangThai;
    }
    
    // Getters and Setters
    public String getMaThanhVien() {
        return maThanhVien;
    }
    
    public void setMaThanhVien(String maThanhVien) {
        this.maThanhVien = maThanhVien;
    }
    
    public String getHoTen() {
        return hoTen;
    }
    
    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }
    
    public String getGioiTinh() {
        return gioiTinh;
    }
    
    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }
    
    public String getSoDienThoai() {
        return soDienThoai;
    }
    
    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getLoaiThanhVien() {
        return loaiThanhVien;
    }
    
    public void setLoaiThanhVien(String loaiThanhVien) {
        this.loaiThanhVien = loaiThanhVien;
    }
    
    public Date getNgayDangKy() {
        return ngayDangKy;
    }
    
    public void setNgayDangKy(Date ngayDangKy) {
        this.ngayDangKy = ngayDangKy;
    }
    
    public Date getNgayHetHan() {
        return ngayHetHan;
    }
    
    public void setNgayHetHan(Date ngayHetHan) {
        this.ngayHetHan = ngayHetHan;
    }
    
    public String getTrangThai() {
        return trangThai;
    }
    
    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
    
    @Override
    public String toString() {
        return "ExpiringMember{" +
                "maThanhVien='" + maThanhVien + '\'' +
                ", hoTen='" + hoTen + '\'' +
                ", ngayHetHan=" + ngayHetHan +
                '}';
    }
} 