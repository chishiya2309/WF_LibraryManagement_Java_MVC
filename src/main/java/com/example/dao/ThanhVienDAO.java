package com.example.dao;

import com.example.model.ThanhVien;
import com.example.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ThanhVienDAO {
    public List<ThanhVien> getAllThanhViens() throws SQLException {
        List<ThanhVien> thanhViens = new ArrayList<>();
        String query = "Select * From ThanhVien";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                thanhViens.add(new ThanhVien(rs.getString("MaThanhVien"), rs.getNString("HoTen"),
                        rs.getNString("GioiTinh"), rs.getString("SoDienThoai"), rs.getString("Email"), rs.getNString("LoaiThanhVien"),
                        rs.getDate("NgayDangKy"), rs.getDate("NgayHetHan"), rs.getNString("TrangThai")));
            }
        }
        return thanhViens;
    }

    public void addMember(ThanhVien thanhVien) throws SQLException {
        String query = "INSERT INTO ThanhVien (MaThanhVien, HoTen, GioiTinh, SoDienThoai, Email, LoaiThanhVien, NgayDangKy, NgayHetHan, TrangThai) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, thanhVien.getMaThanhVien());
            stmt.setString(2, thanhVien.getHoTen());
            stmt.setString(3, thanhVien.getGioiTinh());
            stmt.setString(4, thanhVien.getSoDienThoai());
            stmt.setString(5, thanhVien.getEmail());
            stmt.setString(6, thanhVien.getLoaiThanhVien());
            stmt.setDate(7, thanhVien.getNgayDangKy());
            stmt.setDate(8, thanhVien.getNgayHetHan());
            stmt.setString(9, thanhVien.getTrangThai());
            stmt.executeUpdate();
        }
    }

    public boolean isMemberIdExists(String maThanhVien) throws SQLException {
        String query = "SELECT COUNT(*) FROM ThanhVien WHERE MaThanhVien = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, maThanhVien);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public boolean isPhoneExists(String soDienThoai) throws SQLException {
        String query = "SELECT COUNT(*) FROM ThanhVien WHERE SoDienThoai = ?";
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

    public boolean isEmailExists(String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM ThanhVien WHERE Email = ?";
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
}
