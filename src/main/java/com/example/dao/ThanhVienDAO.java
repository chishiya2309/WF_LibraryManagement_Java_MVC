package com.example.dao;

import com.example.model.ThanhVien;
import com.example.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
}
