package com.example.dao;

import com.example.model.DanhMucSach;
import com.example.model.Sach;
import com.example.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SachDAO {
    private final DanhMucSachDAO danhMucSachDAO = new DanhMucSachDAO();
    
    public List<Sach> getAllSachs() throws SQLException {
        List<Sach> sachs = new ArrayList<>();
        String query = "SELECT s.*, d.TenDanhMuc " +
                       "FROM Sach s " +
                       "JOIN DanhMucSach d ON s.MaDanhMuc = d.MaDanhMuc";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while(rs.next()) {
                // Tạo đối tượng DanhMucSach
                DanhMucSach danhMucSach = new DanhMucSach();
                danhMucSach.setMaDanhMuc(rs.getString("MaDanhMuc"));
                danhMucSach.setTenDanhMuc(rs.getNString("TenDanhMuc"));
                
                // Tạo đối tượng Sach
                Sach sach = new Sach(
                        rs.getString("MaSach"),
                        rs.getString("ISBN"),
                        rs.getNString("TenSach"),
                        rs.getNString("TacGia"),
                        danhMucSach,
                        rs.getInt("NamXuatBan"),
                        rs.getNString("NXB"),
                        rs.getInt("SoBan"),
                        rs.getInt("KhaDung"),
                        rs.getString("ViTri")
                );
                sachs.add(sach);
            }
        }
        
        return sachs;
    }
    
    public List<Sach> searchSachs(String keyword) throws SQLException {
        List<Sach> sachs = new ArrayList<>();
        String query = "SELECT s.*, d.TenDanhMuc " +
                       "FROM Sach s " +
                       "JOIN DanhMucSach d ON s.MaDanhMuc = d.MaDanhMuc " +
                       "WHERE s.MaSach LIKE ? OR s.ISBN LIKE ? OR s.TenSach LIKE ? OR s.TacGia LIKE ? OR s.MaDanhMuc LIKE ? OR NamXuatBan LIKE ? OR s.NXB LIKE ? OR ViTri LIKE ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            // Thiết lập tham số cho câu truy vấn
            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            stmt.setString(4, searchPattern);
            stmt.setString(5, searchPattern);
            stmt.setString(6, searchPattern);
            stmt.setString(7, searchPattern);
            stmt.setString(8, searchPattern);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    // Tạo đối tượng DanhMucSach
                    DanhMucSach danhMucSach = new DanhMucSach();
                    danhMucSach.setMaDanhMuc(rs.getString("MaDanhMuc"));
                    danhMucSach.setTenDanhMuc(rs.getNString("TenDanhMuc"));
                    
                    // Tạo đối tượng Sach
                    Sach sach = new Sach(
                            rs.getString("MaSach"),
                            rs.getString("ISBN"),
                            rs.getNString("TenSach"),
                            rs.getNString("TacGia"),
                            danhMucSach,
                            rs.getInt("NamXuatBan"),
                            rs.getNString("NXB"),
                            rs.getInt("SoBan"),
                            rs.getInt("KhaDung"),
                            rs.getString("ViTri")
                    );
                    sachs.add(sach);
                }
            }
        }
        return sachs;
    }
}
