package com.example.model;

/**
 * Model class cho sách có số lượng khả dụng thấp
 */
public class LowStockBook {
    private String maSach;
    private String isbn;
    private String tenSach;
    private String tacGia;
    private String maDanhMuc;
    private String tenDanhMuc;
    private int namXuatBan;
    private String nxb;
    private int soBan;
    private int khaDung;
    private String viTri;
    
    public LowStockBook() {
    }
    
    public LowStockBook(String maSach, String isbn, String tenSach, String tacGia, 
                        String maDanhMuc, String tenDanhMuc, int namXuatBan, 
                        String nxb, int soBan, int khaDung, String viTri) {
        this.maSach = maSach;
        this.isbn = isbn;
        this.tenSach = tenSach;
        this.tacGia = tacGia;
        this.maDanhMuc = maDanhMuc;
        this.tenDanhMuc = tenDanhMuc;
        this.namXuatBan = namXuatBan;
        this.nxb = nxb;
        this.soBan = soBan;
        this.khaDung = khaDung;
        this.viTri = viTri;
    }
    
    // Getters and Setters
    public String getMaSach() {
        return maSach;
    }
    
    public void setMaSach(String maSach) {
        this.maSach = maSach;
    }
    
    public String getIsbn() {
        return isbn;
    }
    
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    
    public String getTenSach() {
        return tenSach;
    }
    
    public void setTenSach(String tenSach) {
        this.tenSach = tenSach;
    }
    
    public String getTacGia() {
        return tacGia;
    }
    
    public void setTacGia(String tacGia) {
        this.tacGia = tacGia;
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
    
    public int getNamXuatBan() {
        return namXuatBan;
    }
    
    public void setNamXuatBan(int namXuatBan) {
        this.namXuatBan = namXuatBan;
    }
    
    public String getNxb() {
        return nxb;
    }
    
    public void setNxb(String nxb) {
        this.nxb = nxb;
    }
    
    public int getSoBan() {
        return soBan;
    }
    
    public void setSoBan(int soBan) {
        this.soBan = soBan;
    }
    
    public int getKhaDung() {
        return khaDung;
    }
    
    public void setKhaDung(int khaDung) {
        this.khaDung = khaDung;
    }
    
    public String getViTri() {
        return viTri;
    }
    
    public void setViTri(String viTri) {
        this.viTri = viTri;
    }
    
    @Override
    public String toString() {
        return "LowStockBook{" +
                "maSach='" + maSach + '\'' +
                ", tenSach='" + tenSach + '\'' +
                ", khaDung=" + khaDung +
                '}';
    }
} 