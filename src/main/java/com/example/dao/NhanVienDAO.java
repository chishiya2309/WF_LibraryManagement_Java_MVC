package com.example.dao;

import com.example.model.NhanVien;
import com.example.model.ThanhVien;
import com.example.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NhanVienDAO {
    public List<NhanVien> getAllNhanViens() throws SQLException {
        List<NhanVien> nhanViens = new ArrayList<>();
        String query = "SELECT * From NhanVien";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                nhanViens.add(new NhanVien(rs.getString("ID"), rs.getNString("HoTen"),
                        rs.getNString("GioiTinh"), rs.getNString("ChucVu"), rs.getString("Email"), rs.getString("SoDienThoai"),
                        rs.getDate("NgayVaoLam"), rs.getNString("TrangThai")));
            }
        }
        return nhanViens;
    }
    
    public List<NhanVien> searchNhanViens(String keyword) throws SQLException {
        List<NhanVien> nhanViens = new ArrayList<>();
        String query = "SELECT * FROM NhanVien WHERE ID LIKE ? OR HoTen LIKE ? OR GioiTinh LIKE ? OR ChucVu LIKE ? OR Email LIKE ? OR SoDienThoai LIKE ? OR TrangThai LIKE ?";
        
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
            
            try (ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    nhanViens.add(new NhanVien(rs.getString("ID"), rs.getNString("HoTen"),
                            rs.getNString("GioiTinh"), rs.getNString("ChucVu"), rs.getString("Email"), rs.getString("SoDienThoai"),
                            rs.getDate("NgayVaoLam"), rs.getNString("TrangThai")));
                }
            }
        }
        return nhanViens;
    }
    
    public NhanVien getNhanVienById(String id) throws SQLException {
        String query = "SELECT * FROM NhanVien WHERE ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new NhanVien(rs.getString("ID"), rs.getNString("HoTen"),
                            rs.getNString("GioiTinh"), rs.getNString("ChucVu"), rs.getString("Email"), rs.getString("SoDienThoai"),
                            rs.getDate("NgayVaoLam"), rs.getNString("TrangThai"));
                }
            }
        }
        return null;
    }
    
    public void addNhanVien(NhanVien nhanVien) throws SQLException {
        String query = "INSERT INTO NhanVien (ID, HoTen, GioiTinh, ChucVu, Email, SoDienThoai, NgayVaoLam, TrangThai) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nhanVien.getID());
            stmt.setString(2, nhanVien.getHoTen());
            stmt.setString(3, nhanVien.getGioiTinh());
            stmt.setString(4, nhanVien.getChucVu());
            stmt.setString(5, nhanVien.getEmail());
            stmt.setString(6, nhanVien.getSoDienThoai());
            stmt.setDate(7, nhanVien.getNgayVaoLam());
            stmt.setString(8, nhanVien.getTrangThai());
            stmt.executeUpdate();
        }
    }
    
    public void updateNhanVien(NhanVien nhanVien) throws SQLException {
        String query = "UPDATE NhanVien SET HoTen = ?, GioiTinh = ?, ChucVu = ?, Email = ?, SoDienThoai = ?, NgayVaoLam = ?, TrangThai = ? WHERE ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nhanVien.getHoTen());
            stmt.setString(2, nhanVien.getGioiTinh());
            stmt.setString(3, nhanVien.getChucVu());
            stmt.setString(4, nhanVien.getEmail());
            stmt.setString(5, nhanVien.getSoDienThoai());
            stmt.setDate(6, nhanVien.getNgayVaoLam());
            stmt.setString(7, nhanVien.getTrangThai());
            stmt.setString(8, nhanVien.getID());
            stmt.executeUpdate();
        }
    }
    
    public boolean deleteNhanVien(String id) throws SQLException {
        String query = "DELETE FROM NhanVien WHERE ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    public boolean isEmailExists(String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM NhanVien WHERE Email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
    public boolean isPhoneExists(String soDienThoai) throws SQLException {
        String query = "SELECT COUNT(*) FROM NhanVien WHERE SoDienThoai = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, soDienThoai);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}
