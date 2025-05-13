package com.example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.model.LoanHistoryItem;
import com.example.model.MemberItem;
import com.example.util.DatabaseConnection;

/**
 * DAO cho lịch sử mượn sách của thành viên
 */
public class MemberLoanHistoryDAO {
    
    /**
     * Lấy danh sách tất cả thành viên để hiển thị trong combobox
     * @return Danh sách thành viên
     * @throws SQLException Nếu có lỗi khi truy vấn
     */
    public List<MemberItem> getAllMembers() throws SQLException {
        List<MemberItem> members = new ArrayList<>();
        
        String query = "SELECT MaThanhVien, HoTen FROM ThanhVien ORDER BY HoTen";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                String id = rs.getString("MaThanhVien");
                String name = rs.getNString("HoTen");
                members.add(new MemberItem(id, name));
            }
        }
        
        return members;
    }
    
    /**
     * Lấy lịch sử mượn sách của một thành viên
     * @param memberId Mã thành viên
     * @return Danh sách lịch sử mượn sách
     * @throws SQLException Nếu có lỗi khi truy vấn
     */
    public List<LoanHistoryItem> getLoanHistoryByMember(String memberId) throws SQLException {
        List<LoanHistoryItem> loanHistory = new ArrayList<>();
        
        String query = "SELECT pm.MaPhieu, pm.MaThanhVien, tv.HoTen, pm.MaSach, s.TenSach, " +
                      "pm.NgayMuon, pm.HanTra, pm.NgayTraThucTe, pm.TrangThai, pm.SoLuong " +
                      "FROM PhieuMuon pm " +
                      "JOIN ThanhVien tv ON pm.MaThanhVien = tv.MaThanhVien " +
                      "JOIN Sach s ON pm.MaSach = s.MaSach " +
                      "WHERE pm.MaThanhVien = ? " +
                      "ORDER BY pm.NgayMuon DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setString(1, memberId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LoanHistoryItem item = new LoanHistoryItem(
                        rs.getInt("MaPhieu"),
                        rs.getString("MaThanhVien"),
                        rs.getNString("HoTen"),
                        rs.getString("MaSach"),
                        rs.getNString("TenSach"),
                        rs.getDate("NgayMuon"),
                        rs.getDate("HanTra"),
                        rs.getDate("NgayTraThucTe"),
                        rs.getNString("TrangThai"),
                        rs.getInt("SoLuong")
                    );
                    
                    loanHistory.add(item);
                }
            }
        }
        
        return loanHistory;
    }
} 