package com.example.dao;

import com.example.model.DashboardStatistics;
import com.example.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DashboardDAO {

    public DashboardStatistics getOverviewStatistics() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        DashboardStatistics stats = new DashboardStatistics();

        try {
            conn = DatabaseConnection.getConnection();

            // Lấy tổng số sách khả dụng
            String query = "SELECT SUM(KhaDung) FROM Sach";
            pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                stats.setTotalAvailableBooks(rs.getInt(1));
            }
            DatabaseConnection.closeResources(rs, pstmt);

            // Lấy tổng số thành viên đang hoạt động
            query = "SELECT COUNT(*) FROM ThanhVien";
            pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                stats.setTotalMembers(rs.getInt(1));
            }
            DatabaseConnection.closeResources(rs, pstmt);

            // Lấy tổng số nhân viên đang làm việc
            query = "SELECT COUNT(*) FROM NhanVien";
            pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                stats.setTotalStaff(rs.getInt(1));
            }
            DatabaseConnection.closeResources(rs, pstmt);

            // Lấy số sách mượn hôm nay
            String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            query = "SELECT COUNT(*) FROM PhieuMuon WHERE NgayMuon = ?";
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, currentDate);
            rs = pstmt.executeQuery();
            if (rs.next() && rs.getObject(1) != null) {
                stats.setBooksBorrowedToday(rs.getInt(1));
            }
            DatabaseConnection.closeResources(rs, pstmt);

            // Lấy số sách trả hôm nay
            query = "SELECT COUNT(*) FROM PhieuMuon WHERE NgayTraThucTe = ?";
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, currentDate);
            rs = pstmt.executeQuery();
            if (rs.next() && rs.getObject(1) != null) {
                stats.setBooksReturnedToday(rs.getInt(1));
            }
            DatabaseConnection.closeResources(rs, pstmt);

            // Lấy số sách quá hạn
            query = "SELECT COUNT(*) FROM PhieuMuon WHERE TrangThai = 'Quá hạn'";
            pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();
            if (rs.next() && rs.getObject(1) != null) {
                stats.setOverDueBooks(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeResources(rs, pstmt);
        }
        return stats;
    }
}
