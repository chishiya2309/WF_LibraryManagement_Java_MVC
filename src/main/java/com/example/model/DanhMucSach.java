package com.example.model;

import java.util.Date;

public class DanhMucSach {
    private String maDanhMuc;
    private String tenDanhMuc;
    private String moTa;
    private String danhMucCha;
    private int soLuongSach;
    private Date ngayTao;
    private Date capNhatLanCuoi;
    private String trangThai;

    public DanhMucSach() {
    }

    public DanhMucSach(String maDanhMuc, String tenDanhMuc) {
        this.maDanhMuc = maDanhMuc;
        this.tenDanhMuc = tenDanhMuc;
    }

    public DanhMucSach(String maDanhMuc, String tenDanhMuc, String moTa, String danhMucCha, 
                       int soLuongSach, Date ngayTao, Date capNhatLanCuoi, String trangThai) {
        this.maDanhMuc = maDanhMuc;
        this.tenDanhMuc = tenDanhMuc;
        this.moTa = moTa;
        this.danhMucCha = danhMucCha;
        this.soLuongSach = soLuongSach;
        this.ngayTao = ngayTao;
        this.capNhatLanCuoi = capNhatLanCuoi;
        this.trangThai = trangThai;
    }

    public String getMaDanhMuc() {
        return maDanhMuc;
    }

    public void setMaDanhMuc(String maDanhMuc) {
        this.maDanhMuc = maDanhMuc;
    }

    public String getTenDanhMuc() {
        return tenDanhMuc;
    }

    public void setTenDanhMuc(String tenDanhMuc) {
        this.tenDanhMuc = tenDanhMuc;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getDanhMucCha() {
        return danhMucCha;
    }

    public void setDanhMucCha(String danhMucCha) {
        this.danhMucCha = danhMucCha;
    }

    public int getSoLuongSach() {
        return soLuongSach;
    }

    public void setSoLuongSach(int soLuongSach) {
        this.soLuongSach = soLuongSach;
    }

    public Date getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Date ngayTao) {
        this.ngayTao = ngayTao;
    }

    public Date getCapNhatLanCuoi() {
        return capNhatLanCuoi;
    }

    public void setCapNhatLanCuoi(Date capNhatLanCuoi) {
        this.capNhatLanCuoi = capNhatLanCuoi;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public String toString() {
        return tenDanhMuc;
    }
}
