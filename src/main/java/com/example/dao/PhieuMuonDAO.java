package com.example.dao;

import com.example.model.PhieuMuon;
import com.example.model.Sach;
import com.example.model.ThanhVien;
import com.example.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PhieuMuonDAO {
    public List<PhieuMuon> getAllPhieuMuons() throws SQLException {
        List<PhieuMuon> phieuMuons = new ArrayList<>();
        String query = "SELECT pm.*, tv.HoTen, s.TenSach " +
                "FROM PhieuMuon pm " +
                "JOIN ThanhVien tv ON pm.MaThanhVien = tv.MaThanhVien " +
                "JOIN Sach s ON pm.MaSach = s.MaSach";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ThanhVien thanhVien = new ThanhVien();
                thanhVien.setMaThanhVien(rs.getString("MaThanhVien"));
                thanhVien.setHoTen(rs.getNString("HoTen"));

                Sach sach = new Sach();
                sach.setMaSach(rs.getString("MaSach"));
                sach.setTenSach(rs.getNString("TenSach"));

                PhieuMuon pm = new PhieuMuon(
                        rs.getInt("MaPhieu"),
                        thanhVien,
                        rs.getDate("NgayMuon"),
                        rs.getDate("HanTra"),
                        rs.getDate("NgayTraThucTe"),
                        rs.getNString("TrangThai"),
                        sach,
                        rs.getInt("SoLuong")
                );
                phieuMuons.add(pm);
            }
        }
        return phieuMuons;
    }
    
    public List<PhieuMuon> searchPhieuMuons(String keyword) throws SQLException {
        List<PhieuMuon> phieuMuons = new ArrayList<>();
        String query = "SELECT pm.*, tv.HoTen, s.TenSach " +
                "FROM PhieuMuon pm " +
                "JOIN ThanhVien tv ON pm.MaThanhVien = tv.MaThanhVien " +
                "JOIN Sach s ON pm.MaSach = s.MaSach " +
                "WHERE pm.MaPhieu LIKE ? OR pm.MaThanhVien LIKE ? OR tv.HoTen LIKE ? OR s.TenSach LIKE ? OR pm.TrangThai LIKE ? OR pm.MaSach LIKE ?";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query)) {
            
            String searchParam = "%" + keyword + "%";
            ps.setString(1, searchParam);
            ps.setString(2, searchParam);
            ps.setString(3, searchParam);
            ps.setString(4, searchParam);
            ps.setString(5, searchParam);
            ps.setString(6, searchParam);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ThanhVien thanhVien = new ThanhVien();
                thanhVien.setMaThanhVien(rs.getString("MaThanhVien"));
                thanhVien.setHoTen(rs.getNString("HoTen"));

                Sach sach = new Sach();
                sach.setMaSach(rs.getString("MaSach"));
                sach.setTenSach(rs.getNString("TenSach"));

                PhieuMuon pm = new PhieuMuon(
                        rs.getInt("MaPhieu"),
                        thanhVien,
                        rs.getDate("NgayMuon"),
                        rs.getDate("HanTra"),
                        rs.getDate("NgayTraThucTe"),
                        rs.getNString("TrangThai"),
                        sach,
                        rs.getInt("SoLuong")
                );
                phieuMuons.add(pm);
            }
        }
        return phieuMuons;
    }
    
    public boolean deletePhieuMuon(int maPhieu) throws SQLException {
        String query = "DELETE FROM PhieuMuon WHERE MaPhieu = ?";
        
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, maPhieu);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }
}
