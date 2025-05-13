package com.example.dao;

import com.example.model.ExpiringMember;
import com.example.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Lớp DAO để truy vấn thành viên sắp hết hạn từ cơ sở dữ liệu
 */
public class ExpiringMemberDAO {
    
    /**
     * Lấy danh sách thành viên sắp hết hạn trong khoảng thời gian tới
     * @param daysThreshold Số ngày tính từ hiện tại
     * @return Danh sách thành viên sắp hết hạn
     * @throws SQLException Nếu có lỗi khi truy vấn cơ sở dữ liệu
     */
    public List<ExpiringMember> getExpiringMembers(int daysThreshold) throws SQLException {
        List<ExpiringMember> expiringMembers = new ArrayList<>();
        
        // Tạo mốc thời gian hiện tại và thời gian sau daysThreshold ngày
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        
        calendar.add(Calendar.DAY_OF_MONTH, daysThreshold);
        Date expiryDate = calendar.getTime();
        
        // Truy vấn thành viên có ngày hết hạn nằm trong khoảng hiện tại đến ngày trong tương lai
        String query = "SELECT * FROM ThanhVien " +
                      "WHERE NgayHetHan BETWEEN CURRENT_DATE() AND DATE_ADD(CURRENT_DATE(), INTERVAL ? DAY) " +
                      "AND TrangThai = 'Hoạt động' " +
                      "ORDER BY NgayHetHan ASC";
        
        System.out.println("Thực hiện truy vấn thành viên sắp hết hạn trong " + daysThreshold + " ngày tới");
        System.out.println("Query: " + query);
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setInt(1, daysThreshold);
            System.out.println("Đã thiết lập tham số, thực hiện truy vấn...");
            
            try (ResultSet rs = ps.executeQuery()) {
                int count = 0;
                while(rs.next()) {
                    count++;
                    ExpiringMember member = new ExpiringMember();
                    member.setMaThanhVien(rs.getString("MaThanhVien"));
                    member.setHoTen(rs.getNString("HoTen"));
                    member.setGioiTinh(rs.getNString("GioiTinh"));
                    member.setSoDienThoai(rs.getString("SoDienThoai"));
                    member.setEmail(rs.getString("Email"));
                    member.setLoaiThanhVien(rs.getNString("LoaiThanhVien"));
                    member.setNgayDangKy(rs.getDate("NgayDangKy"));
                    member.setNgayHetHan(rs.getDate("NgayHetHan"));
                    member.setTrangThai(rs.getNString("TrangThai"));
                    
                    expiringMembers.add(member);
                    System.out.println("Đã thêm thành viên: " + member.getHoTen() + " (Ngày hết hạn: " + member.getNgayHetHan() + ")");
                }
                System.out.println("Đã đọc " + count + " bản ghi từ cơ sở dữ liệu");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi truy vấn thành viên sắp hết hạn: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        
        return expiringMembers;
    }
    
    /**
     * Lấy danh sách thành viên sắp hết hạn trong 30 ngày tới (mặc định)
     * @return Danh sách thành viên sắp hết hạn
     * @throws SQLException Nếu có lỗi khi truy vấn cơ sở dữ liệu
     */
    public List<ExpiringMember> getExpiringMembers() throws SQLException {
        return getExpiringMembers(30); // Ngưỡng mặc định là 30 ngày
    }
    
    /**
     * Lấy tổng số thành viên sắp hết hạn trong khoảng thời gian tới
     * @param daysThreshold Số ngày tính từ hiện tại
     * @return Tổng số thành viên sắp hết hạn
     * @throws SQLException Nếu có lỗi khi truy vấn cơ sở dữ liệu
     */
    public int countExpiringMembers(int daysThreshold) throws SQLException {
        String query = "SELECT COUNT(*) AS total FROM ThanhVien " +
                      "WHERE NgayHetHan BETWEEN CURRENT_DATE() AND DATE_ADD(CURRENT_DATE(), INTERVAL ? DAY) " +
                      "AND TrangThai = 'Hoạt động'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setInt(1, daysThreshold);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        }
        
        return 0;
    }
    
    /**
     * Lấy tổng số thành viên sắp hết hạn trong 30 ngày tới (mặc định)
     * @return Tổng số thành viên sắp hết hạn
     * @throws SQLException Nếu có lỗi khi truy vấn cơ sở dữ liệu
     */
    public int countExpiringMembers() throws SQLException {
        return countExpiringMembers(30); // Ngưỡng mặc định là 30 ngày
    }
} 