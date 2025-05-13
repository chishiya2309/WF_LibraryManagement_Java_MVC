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
import java.sql.Date;
import java.sql.CallableStatement;

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
    
    public void addPhieuMuon(PhieuMuon pm) throws SQLException {
        String query = "INSERT INTO PhieuMuon (MaThanhVien, NgayMuon, HanTra, TrangThai, MaSach, SoLuong) VALUES (?, ?, ?, ?, ?, ?)";
        try(Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, pm.getThanhVien().getMaThanhVien());
            ps.setDate(2, pm.getNgayMuon());
            ps.setDate(3, pm.getHanTra());
            ps.setString(4, pm.getTrangThai());
            ps.setString(5, pm.getSach().getMaSach());
            ps.setInt(6, pm.getSoLuong());
            ps.executeUpdate();
        }
    }
    
    public int getSoSachDangMuon(String maThanhVien) throws SQLException {
        String query = "SELECT IFNULL(SUM(SoLuong), 0) AS TongSoLuong FROM PhieuMuon WHERE MaThanhVien = ? AND TrangThai = 'Đang mượn'";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, maThanhVien);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("TongSoLuong");
                }
                return 0;
            }
        }
    }
    
    public boolean hasQuaHan(String maThanhVien) throws SQLException {
        String query = "SELECT COUNT(*) AS SoPhieuQuaHan FROM PhieuMuon WHERE MaThanhVien = ? AND TrangThai = 'Quá hạn'";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, maThanhVien);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("SoPhieuQuaHan") > 0;
                }
                return false;
            }
        }
    }
    
    public PhieuMuon getPhieuMuonById(int maPhieu) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            String query = "SELECT pm.*, tv.HoTen, s.TenSach " +
                    "FROM PhieuMuon pm " +
                    "JOIN ThanhVien tv ON pm.MaThanhVien = tv.MaThanhVien " +
                    "JOIN Sach s ON pm.MaSach = s.MaSach " +
                    "WHERE pm.MaPhieu = ?";
                    
            ps = conn.prepareStatement(query);
            ps.setInt(1, maPhieu);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                ThanhVien thanhVien = new ThanhVien();
                thanhVien.setMaThanhVien(rs.getString("MaThanhVien"));
                thanhVien.setHoTen(rs.getNString("HoTen"));

                Sach sach = new Sach();
                sach.setMaSach(rs.getString("MaSach"));
                sach.setTenSach(rs.getNString("TenSach"));

                return new PhieuMuon(
                        rs.getInt("MaPhieu"),
                        thanhVien,
                        rs.getDate("NgayMuon"),
                        rs.getDate("HanTra"),
                        rs.getDate("NgayTraThucTe"),
                        rs.getNString("TrangThai"),
                        sach,
                        rs.getInt("SoLuong")
                );
            }
            return null;
        } finally {
            // Đóng các tài nguyên theo thứ tự ngược lại
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    // Log lỗi nhưng không ném ngoại lệ ở đây
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    // Log lỗi nhưng không ném ngoại lệ ở đây
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    // Log lỗi nhưng không ném ngoại lệ ở đây
                }
            }
        }
    }
    
    public void traSach(int maPhieu, Date ngayTra) throws SQLException {
        Connection conn = null;
        PreparedStatement psUpdatePhieuMuon = null;
        PreparedStatement psUpdateSach = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Lấy thông tin phiếu mượn để kiểm tra
            PhieuMuon phieuMuon = getPhieuMuonById(maPhieu);
            if (phieuMuon == null) {
                throw new SQLException("Phiếu mượn không tồn tại!");
            }
            
            if ("Đã trả".equals(phieuMuon.getTrangThai())) {
                throw new SQLException("Phiếu mượn này đã được trả trước đó!");
            }
            
            if (ngayTra.before(phieuMuon.getNgayMuon())) {
                throw new SQLException("Ngày trả thực tế phải lớn hơn hoặc bằng ngày mượn!");
            }
            
            // 1. Cập nhật trạng thái phiếu mượn thành 'Đã trả' và ghi nhận ngày trả thực tế
            String updatePhieuMuon = "UPDATE PhieuMuon SET TrangThai = 'Đã trả', NgayTraThucTe = ? WHERE MaPhieu = ?";
            psUpdatePhieuMuon = conn.prepareStatement(updatePhieuMuon);
            psUpdatePhieuMuon.setDate(1, ngayTra);
            psUpdatePhieuMuon.setInt(2, maPhieu);
            psUpdatePhieuMuon.executeUpdate();
            
            // 2. Cập nhật số lượng sách khả dụng
            String updateSach = "UPDATE Sach SET KhaDung = KhaDung + ? WHERE MaSach = ?";
            psUpdateSach = conn.prepareStatement(updateSach);
            psUpdateSach.setInt(1, phieuMuon.getSoLuong());
            psUpdateSach.setString(2, phieuMuon.getSach().getMaSach());
            psUpdateSach.executeUpdate();
            
            // Commit transaction
            conn.commit();
        } catch (SQLException e) {
            // Rollback transaction nếu có lỗi
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new SQLException("Lỗi khi rollback transaction: " + ex.getMessage());
                }
            }
            throw e;
        } finally {
            // Đóng các PreparedStatement trước
            if (psUpdatePhieuMuon != null) {
                try {
                    psUpdatePhieuMuon.close();
                } catch (SQLException e) {
                    // Log lỗi nhưng không ném ngoại lệ ở đây
                }
            }
            if (psUpdateSach != null) {
                try {
                    psUpdateSach.close();
                } catch (SQLException e) {
                    // Log lỗi nhưng không ném ngoại lệ ở đây
                }
            }
            
            // Đóng connection và reset autoCommit
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    throw new SQLException("Lỗi khi đóng kết nối: " + e.getMessage());
                }
            }
        }
    }
}
