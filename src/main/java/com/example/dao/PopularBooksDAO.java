package com.example.dao;

import com.example.model.PopularBookItem;
import com.example.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho danh sách sách phổ biến nhất
 */
public class PopularBooksDAO {
    
    /**
     * Lấy danh sách top 10 sách được mượn nhiều nhất
     * @return Danh sách sách phổ biến
     * @throws SQLException Nếu có lỗi khi truy vấn
     */
    public List<PopularBookItem> getTop10PopularBooks() throws SQLException {
        List<PopularBookItem> popularBooks = new ArrayList<>();
        
        String query = "SELECT s.MaSach, s.TenSach, COUNT(pm.MaSach) AS SoLanMuon " +
                       "FROM PhieuMuon pm " +
                       "JOIN Sach s ON pm.MaSach = s.MaSach " +
                       "GROUP BY s.MaSach, s.TenSach " +
                       "ORDER BY SoLanMuon DESC " +
                       "LIMIT 10";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            int orderNumber = 1;
            
            while (rs.next()) {
                PopularBookItem item = new PopularBookItem(
                    orderNumber++,
                    rs.getString("MaSach"),
                    rs.getNString("TenSach"),
                    rs.getInt("SoLanMuon")
                );
                
                popularBooks.add(item);
            }
        }
        
        return popularBooks;
    }
    
    /**
     * Lấy danh sách top sách phổ biến trong khoảng thời gian
     * @param startDate Ngày bắt đầu
     * @param endDate Ngày kết thúc
     * @return Danh sách sách phổ biến
     * @throws SQLException Nếu có lỗi khi truy vấn
     */
    public List<PopularBookItem> getTopPopularBooksByDateRange(java.util.Date startDate, java.util.Date endDate) throws SQLException {
        List<PopularBookItem> popularBooks = new ArrayList<>();
        
        String query = "SELECT s.MaSach, s.TenSach, COUNT(pm.MaSach) AS SoLanMuon " +
                       "FROM PhieuMuon pm " +
                       "JOIN Sach s ON pm.MaSach = s.MaSach " +
                       "WHERE pm.NgayMuon BETWEEN ? AND ? " +
                       "GROUP BY s.MaSach, s.TenSach " +
                       "ORDER BY SoLanMuon DESC " +
                       "LIMIT 10";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setDate(1, new java.sql.Date(startDate.getTime()));
            ps.setDate(2, new java.sql.Date(endDate.getTime()));
            
            try (ResultSet rs = ps.executeQuery()) {
                int orderNumber = 1;
                
                while (rs.next()) {
                    PopularBookItem item = new PopularBookItem(
                        orderNumber++,
                        rs.getString("MaSach"),
                        rs.getNString("TenSach"),
                        rs.getInt("SoLanMuon")
                    );
                    
                    popularBooks.add(item);
                }
            }
        }
        
        return popularBooks;
    }
} 