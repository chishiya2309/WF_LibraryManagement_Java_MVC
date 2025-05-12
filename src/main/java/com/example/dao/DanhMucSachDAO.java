package com.example.dao;

import com.example.model.DanhMucSach;
import com.example.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DanhMucSachDAO {
    public List<DanhMucSach> getAllDanhMucSach() throws SQLException {
        List<DanhMucSach> danhMucSachs = new ArrayList<>();
        String query = "SELECT * FROM DanhMucSach";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                danhMucSachs.add(new DanhMucSach(rs.getString("MaDanhMuc"), rs.getNString("TenDanhMuc"), rs.getNString("MoTa"), rs.getString("DanhMucCha"),
                        rs.getInt("SoLuongSach"), rs.getDate("NgayTao"), rs.getDate("CapNhatLanCuoi"), rs.getNString("TrangThai")));
            }
        }
        return danhMucSachs;
    }

    public DanhMucSach getDanhMucSach(String maDanhMuc) throws SQLException {
        String query = "SELECT * FROM DanhMucSach WHERE MaDanhMuc = ?";
        try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, maDanhMuc);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new DanhMucSach(
                        rs.getString("MaDanhMuc"), 
                        rs.getNString("TenDanhMuc"), 
                        rs.getNString("MoTa"), 
                        rs.getString("DanhMucCha"), 
                        rs.getInt("SoLuongSach"), 
                        rs.getDate("NgayTao"), 
                        rs.getDate("CapNhatLanCuoi"), 
                        rs.getNString("TrangThai"));
                }
            }
        }
        return null;
    }

    public DanhMucSach getDanhMucSachByMaDanhMuc(String maDanhMuc) throws SQLException {
        String query = "SELECT * FROM DanhMucSach WHERE MaDanhMuc = ?";
        try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, maDanhMuc);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new DanhMucSach(rs.getString("MaDanhMuc"), rs.getNString("TenDanhMuc"));
                }
            }
        }
        return null;
    }
    
    public List<DanhMucSach> searchDanhMucSachs(String keyword) throws SQLException {
        List<DanhMucSach> danhMucSachs = new ArrayList<>();
        String query = "SELECT * FROM DanhMucSach " +
                "WHERE MaDanhMuc LIKE ? OR TenDanhMuc LIKE ? OR MoTa LIKE ? OR DanhMucCha LIKE ? OR TrangThai LIKE ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            // Thiết lập tham số cho câu truy vấn
            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            stmt.setString(4, searchPattern);
            stmt.setString(5, searchPattern);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    danhMucSachs.add(new DanhMucSach(
                        rs.getString("MaDanhMuc"), 
                        rs.getNString("TenDanhMuc"), 
                        rs.getNString("MoTa"), 
                        rs.getString("DanhMucCha"),
                        rs.getInt("SoLuongSach"), 
                        rs.getDate("NgayTao"), 
                        rs.getDate("CapNhatLanCuoi"), 
                        rs.getNString("TrangThai")
                    ));
                }
            }
        }
        return danhMucSachs;
    }
    
    public boolean deleteCategory(String maDanhMuc) throws SQLException {
        String query = "DELETE FROM DanhMucSach WHERE MaDanhMuc = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, maDanhMuc);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new SQLException("Không thể xóa danh mục. Chi tiết lỗi: " + e.getMessage(), e);
        }
    }

    public void addCategory(DanhMucSach danhMucSach) throws SQLException {
        String query = "INSERT INTO DanhMucSach (MaDanhMuc, TenDanhMuc, MoTa, DanhMucCha, SoLuongSach, NgayTao, CapNhatLanCuoi, TrangThai) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            Timestamp now = new Timestamp(System.currentTimeMillis());

            stmt.setString(1, danhMucSach.getMaDanhMuc());
            stmt.setString(2, danhMucSach.getTenDanhMuc());
            stmt.setString(3, danhMucSach.getMoTa());
            stmt.setString(4, danhMucSach.getDanhMucCha());
            stmt.setInt(5, danhMucSach.getSoLuongSach());
            stmt.setTimestamp(6, now); // NgayTao
            stmt.setTimestamp(7, now); // CapNhatLanCuoi
            stmt.setString(8, danhMucSach.getTrangThai());
            stmt.executeUpdate();
        }
    }

    public void updateCategory(DanhMucSach danhMucSach) throws SQLException {
        String query = "UPDATE DanhMucSach SET TenDanhMuc = ?, MoTa = ?, DanhMucCha = ?, SoLuongSach = ?, CapNhatLanCuoi = ?, TrangThai = ? WHERE MaDanhMuc = ?";
        try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)) {
            Timestamp now = new Timestamp(System.currentTimeMillis());

            stmt.setString(1, danhMucSach.getTenDanhMuc());
            stmt.setString(2, danhMucSach.getMoTa());
            stmt.setString(3, danhMucSach.getDanhMucCha());
            stmt.setInt(4, danhMucSach.getSoLuongSach());
            stmt.setTimestamp(5, now);
            stmt.setString(6, danhMucSach.getTrangThai());
            stmt.setString(7, danhMucSach.getMaDanhMuc());
            stmt.executeUpdate();
        }
    }
}
