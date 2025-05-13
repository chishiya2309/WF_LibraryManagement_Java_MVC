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
import java.util.Calendar;

/**
 * DAO cho phiếu mượn
 */
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
    
    public boolean updatePhieuMuon(int maPhieu, Date ngayMuon, Date hanTra, 
                               Date ngayTraThucTe, String trangThai, int soLuong) throws SQLException {
        Connection conn = null;
        PreparedStatement psGetOldInfo = null;
        PreparedStatement psUpdatePhieuMuon = null;
        PreparedStatement psUpdateSach = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Lấy thông tin phiếu mượn hiện tại để tính toán số lượng sách cần cập nhật
            String queryGetOldInfo = "SELECT SoLuong, TrangThai, MaSach FROM PhieuMuon WHERE MaPhieu = ?";
            psGetOldInfo = conn.prepareStatement(queryGetOldInfo);
            psGetOldInfo.setInt(1, maPhieu);
            rs = psGetOldInfo.executeQuery();
            
            if (!rs.next()) {
                throw new SQLException("Phiếu mượn không tồn tại!");
            }
            
            int oldSoLuong = rs.getInt("SoLuong");
            String oldTrangThai = rs.getString("TrangThai");
            String maSach = rs.getString("MaSach");
            
            // Kiểm tra nếu phiếu đã ở trạng thái "Đã trả" thì không cho phép cập nhật
            if ("Đã trả".equals(oldTrangThai)) {
                throw new SQLException("Không thể cập nhật phiếu mượn đã ở trạng thái 'Đã trả'!");
            }
            
            // Cập nhật phiếu mượn
            String updatePhieuMuon = "UPDATE PhieuMuon SET NgayMuon = ?, HanTra = ?, " +
                                   "NgayTraThucTe = ?, TrangThai = ?, SoLuong = ? " +
                                   "WHERE MaPhieu = ?";
            psUpdatePhieuMuon = conn.prepareStatement(updatePhieuMuon);
            psUpdatePhieuMuon.setDate(1, ngayMuon);
            psUpdatePhieuMuon.setDate(2, hanTra);
            psUpdatePhieuMuon.setDate(3, ngayTraThucTe);
            psUpdatePhieuMuon.setString(4, trangThai);
            psUpdatePhieuMuon.setInt(5, soLuong);
            psUpdatePhieuMuon.setInt(6, maPhieu);
            psUpdatePhieuMuon.executeUpdate();
            
            // Tính toán sự thay đổi số lượng sách
            int sachKhaDungDiff = 0;
            
            // Tính toán sự thay đổi số lượng sách
            if (trangThai.equals("Đã trả")) {
                // Đang mượn/Quá hạn -> Đã trả: tăng sách khả dụng
                sachKhaDungDiff = oldSoLuong;
                System.out.println("DEBUG: Trạng thái từ Đang mượn/Quá hạn -> Đã trả. Tăng KhaDung: " + oldSoLuong);
            } else {
                // Đang mượn/Quá hạn -> Đang mượn/Quá hạn: điều chỉnh theo sự thay đổi số lượng
                sachKhaDungDiff = oldSoLuong - soLuong;
                System.out.println("DEBUG: Trạng thái vẫn Đang mượn/Quá hạn. Thay đổi KhaDung: " + sachKhaDungDiff);
            }
            
            if (sachKhaDungDiff != 0) {
                String updateSach = "UPDATE Sach SET KhaDung = KhaDung + ? WHERE MaSach = ?";
                psUpdateSach = conn.prepareStatement(updateSach);
                psUpdateSach.setInt(1, sachKhaDungDiff);
                psUpdateSach.setString(2, maSach);
                psUpdateSach.executeUpdate();
                System.out.println("DEBUG: Đã cập nhật KhaDung trong DB, thay đổi: " + sachKhaDungDiff);
            } else {
                System.out.println("DEBUG: Không cập nhật KhaDung trong DB vì sachKhaDungDiff = 0");
            }
            
            // Commit transaction
            conn.commit();
            return true;
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
            // Đóng tất cả các tài nguyên
            if (rs != null) try { rs.close(); } catch (SQLException e) { }
            if (psGetOldInfo != null) try { psGetOldInfo.close(); } catch (SQLException e) { }
            if (psUpdatePhieuMuon != null) try { psUpdatePhieuMuon.close(); } catch (SQLException e) { }
            if (psUpdateSach != null) try { psUpdateSach.close(); } catch (SQLException e) { }
            
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) { }
            }
        }
    }
    
    public int getSoSachDangMuonCuaThanhVien(String maThanhVien, int maPhieuLoaiTru) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            String query = "SELECT IFNULL(SUM(SoLuong), 0) AS TongSachDangMuon " +
                         "FROM PhieuMuon " +
                         "WHERE MaThanhVien = ? AND MaPhieu != ? " +
                         "AND TrangThai IN ('Đang mượn', 'Quá hạn')";
            ps = conn.prepareStatement(query);
            ps.setString(1, maThanhVien);
            ps.setInt(2, maPhieuLoaiTru);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("TongSachDangMuon");
            }
            return 0;
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { }
            if (ps != null) try { ps.close(); } catch (SQLException e) { }
            if (conn != null) try { conn.close(); } catch (SQLException e) { }
        }
    }

    /**
     * Lấy danh sách phiếu mượn quá hạn
     * @return Danh sách phiếu mượn quá hạn
     * @throws SQLException Nếu có lỗi khi truy vấn cơ sở dữ liệu
     */
    public List<PhieuMuon> getPhieuMuonQuaHan() throws SQLException {
        List<PhieuMuon> phieuMuons = new ArrayList<>();
        
        String query = "SELECT pm.*, tv.HoTen, s.TenSach " +
                "FROM PhieuMuon pm " +
                "JOIN ThanhVien tv ON pm.MaThanhVien = tv.MaThanhVien " +
                "JOIN Sach s ON pm.MaSach = s.MaSach " +
                "WHERE pm.TrangThai = N'Quá hạn'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                // Tạo đối tượng ThanhVien
                ThanhVien thanhVien = new ThanhVien();
                thanhVien.setMaThanhVien(rs.getString("MaThanhVien"));
                thanhVien.setHoTen(rs.getNString("HoTen"));
                
                // Tạo đối tượng Sach
                Sach sach = new Sach();
                sach.setMaSach(rs.getString("MaSach"));
                sach.setTenSach(rs.getNString("TenSach"));
                
                // Tạo đối tượng PhieuMuon
                PhieuMuon phieuMuon = new PhieuMuon(
                        rs.getInt("MaPhieu"),
                        thanhVien,
                        rs.getDate("NgayMuon"),
                        rs.getDate("HanTra"),
                        rs.getDate("NgayTraThucTe"),
                        rs.getNString("TrangThai"),
                        sach,
                        rs.getInt("SoLuong")
                );
                
                phieuMuons.add(phieuMuon);
            }
        }
        
        return phieuMuons;
    }
}
