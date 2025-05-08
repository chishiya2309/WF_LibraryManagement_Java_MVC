package com.example.dao;

import com.example.model.DanhMucSach;
import com.example.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
}
