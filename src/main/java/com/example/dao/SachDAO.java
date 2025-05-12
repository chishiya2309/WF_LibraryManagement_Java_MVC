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

    public List<Sach> getSachByMaSach(String maSach) throws SQLException {
        List<Sach> sachs = new ArrayList<>();
        String query = "SELECT s.*, d.TenDanhMuc " +
                "FROM Sach s " +
                "JOIN DanhMucSach d ON s.MaDanhMuc = d.MaDanhMuc " +
                "WHERE s.MaSach = ?";
        try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, maSach);
            try (ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
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

    public void addBook(Sach sach) throws SQLException {
        String query = "INSERT INTO Sach (MaSach, ISBN, TenSach, TacGia, MaDanhMuc, NamXuatBan, NXB, SoBan, KhaDung, ViTri) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, sach.getMaSach());
            stmt.setString(2, sach.getISBN());
            stmt.setString(3, sach.getTenSach());
            stmt.setString(4, sach.getTacGia());
            stmt.setString(5, sach.getDanhMucSach().getMaDanhMuc());
            stmt.setInt(6, sach.getNamXuatBan());
            stmt.setString(7, sach.getNXB());
            stmt.setInt(8, sach.getSoBan());
            stmt.setInt(9, sach.getKhaDung());
            stmt.setString(10, sach.getViTri());
            stmt.executeUpdate();
        }
    }

    public void updateBook(Sach sach) throws SQLException {
        String query = "UPDATE Sach SET ISBN=?, TenSach=?, TacGia=?, MaDanhMuc=?, NamXuatBan=?, NXB=?, SoBan=?, KhaDung=?, ViTri=? WHERE MaSach=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, sach.getISBN());
            stmt.setString(2, sach.getTenSach());
            stmt.setString(3, sach.getTacGia());
            stmt.setString(4, sach.getDanhMucSach().getMaDanhMuc());
            stmt.setInt(5, sach.getNamXuatBan());
            stmt.setString(6, sach.getNXB());
            stmt.setInt(7, sach.getSoBan());
            stmt.setInt(8, sach.getKhaDung());
            stmt.setString(9, sach.getViTri());
            stmt.setString(10, sach.getMaSach());
            stmt.executeUpdate();
        }
    }

    public void deleteBook(Sach sach) throws SQLException {
        String query = "DELETE FROM Sach WHERE MaSach=?";
        try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, sach.getMaSach());
            stmt.executeUpdate();
        }
    }
}
