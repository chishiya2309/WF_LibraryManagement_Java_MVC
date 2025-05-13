package com.example.model;

import java.sql.Date;

public class PhieuMuon {
    private int maPhieu;
    private ThanhVien thanhVien;
    private Date ngayMuon;
    private Date hanTra;
    private Date ngayTraThucTe;
    private String trangThai;
    private Sach sach;
    private int soLuong;

    public PhieuMuon() {
    }

    public PhieuMuon(int maPhieu, ThanhVien thanhVien, Date ngayMuon, Date hanTra, Date ngayTraThucTe, String trangThai, Sach sach, int soLuong) {
        this.maPhieu = maPhieu;
        this.thanhVien = thanhVien;
        this.ngayMuon = ngayMuon;
        this.hanTra = hanTra;
        this.ngayTraThucTe = ngayTraThucTe;
        this.trangThai = trangThai;
        this.sach = sach;
        this.soLuong = soLuong;
    }

    public int getMaPhieu() {
        return maPhieu;
    }

    public void setMaPhieu(int maPhieu) {
        this.maPhieu = maPhieu;
    }

    public ThanhVien getThanhVien() {
        return thanhVien;
    }

    public void setThanhVien(ThanhVien thanhVien) {
        this.thanhVien = thanhVien;
    }

    public Date getNgayMuon() {
        return ngayMuon;
    }

    public void setNgayMuon(Date ngayMuon) {
        this.ngayMuon = ngayMuon;
    }

    public Date getHanTra() {
        return hanTra;
    }

    public void setHanTra(Date hanTra) {
        this.hanTra = hanTra;
    }

    public Date getNgayTraThucTe() {
        return ngayTraThucTe;
    }

    public void setNgayTraThucTe(Date ngayTraThucTe) {
        this.ngayTraThucTe = ngayTraThucTe;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public Sach getSach() {
        return sach;
    }

    public void setSach(Sach sach) {
        this.sach = sach;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
}
