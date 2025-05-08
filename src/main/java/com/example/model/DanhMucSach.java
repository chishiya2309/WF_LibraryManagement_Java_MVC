package com.example.model;

import java.util.Date;

public class DanhMucSach {
    private String MaDanhMuc;
    private String TenDanhMuc;
    private String MoTa;
    private String DanhMucCha;
    private int SoLuongSach;
    private Date NgayTao;
    private Date CapNhatLanCuoi;
    private String TrangThai;

    public DanhMucSach() {
    }

    public DanhMucSach(String maDanhMuc, String tenDanhMuc, String moTa, String danhMucCha, int soLuongSach, Date ngayTao, Date capNhatLanCuoi, String trangThai) {
        MaDanhMuc = maDanhMuc;
        TenDanhMuc = tenDanhMuc;
        MoTa = moTa;
        DanhMucCha = danhMucCha;
        SoLuongSach = soLuongSach;
        NgayTao = ngayTao;
        CapNhatLanCuoi = capNhatLanCuoi;
        TrangThai = trangThai;
    }

    public String getMaDanhMuc() {
        return MaDanhMuc;
    }

    public void setMaDanhMuc(String maDanhMuc) {
        MaDanhMuc = maDanhMuc;
    }

    public String getTenDanhMuc() {
        return TenDanhMuc;
    }

    public void setTenDanhMuc(String tenDanhMuc) {
        TenDanhMuc = tenDanhMuc;
    }

    public String getMoTa() {
        return MoTa;
    }

    public void setMoTa(String moTa) {
        MoTa = moTa;
    }

    public String getDanhMucCha() {
        return DanhMucCha;
    }

    public void setDanhMucCha(String danhMucCha) {
        DanhMucCha = danhMucCha;
    }

    public int getSoLuongSach() {
        return SoLuongSach;
    }

    public void setSoLuongSach(int soLuongSach) {
        SoLuongSach = soLuongSach;
    }

    public Date getNgayTao() {
        return NgayTao;
    }

    public void setNgayTao(Date ngayTao) {
        NgayTao = ngayTao;
    }

    public Date getCapNhatLanCuoi() {
        return CapNhatLanCuoi;
    }

    public void setCapNhatLanCuoi(Date capNhatLanCuoi) {
        CapNhatLanCuoi = capNhatLanCuoi;
    }

    public String getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(String trangThai) {
        TrangThai = trangThai;
    }

    @Override
    public String toString() {
        return TenDanhMuc;
    }
}
