package com.example.dao;

import com.example.model.LowStockBook;
import com.example.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp DAO để truy vấn sách có số lượng khả dụng thấp từ cơ sở dữ liệu
 */
public class LowStockBookDAO {
    
    /**
     * Lấy danh sách sách có số lượng khả dụng thấp hơn ngưỡng quy định
     * @param threshold Ngưỡng số lượng khả dụng
     * @return Danh sách sách có số lượng khả dụng thấp
     * @throws SQLException Nếu có lỗi khi truy vấn cơ sở dữ liệu
     */
    public List<LowStockBook> getLowStockBooks(int threshold) throws SQLException {
        List<LowStockBook> lowStockBooks = new ArrayList<>();
        String query = "SELECT s.*, d.TenDanhMuc " +
                       "FROM Sach s " +
                       "JOIN DanhMucSach d ON s.MaDanhMuc = d.MaDanhMuc " +
                       "WHERE s.KhaDung < ?";
        
        System.out.println("Thực hiện truy vấn với ngưỡng: " + threshold);
        System.out.println("Query: " + query);
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setInt(1, threshold);
            System.out.println("Đã thiết lập tham số, thực hiện truy vấn...");
            
            try (ResultSet rs = ps.executeQuery()) {
                int count = 0;
                while(rs.next()) {
                    count++;
                    LowStockBook book = new LowStockBook();
                    book.setMaSach(rs.getString("MaSach"));
                    book.setIsbn(rs.getString("ISBN"));
                    book.setTenSach(rs.getNString("TenSach"));
                    book.setTacGia(rs.getNString("TacGia"));
                    book.setMaDanhMuc(rs.getString("MaDanhMuc"));
                    book.setTenDanhMuc(rs.getNString("TenDanhMuc"));
                    book.setNamXuatBan(rs.getInt("NamXuatBan"));
                    book.setNxb(rs.getNString("NXB"));
                    book.setSoBan(rs.getInt("SoBan"));
                    book.setKhaDung(rs.getInt("KhaDung"));
                    book.setViTri(rs.getString("ViTri"));
                    
                    lowStockBooks.add(book);
                    System.out.println("Đã thêm sách: " + book.getTenSach() + " (Khả dụng: " + book.getKhaDung() + ")");
                }
                System.out.println("Đã đọc " + count + " bản ghi từ cơ sở dữ liệu");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi truy vấn sách có số lượng khả dụng thấp: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        
        return lowStockBooks;
    }
    
    /**
     * Lấy danh sách sách có số lượng khả dụng thấp hơn ngưỡng mặc định (3)
     * @return Danh sách sách có số lượng khả dụng thấp
     * @throws SQLException Nếu có lỗi khi truy vấn cơ sở dữ liệu
     */
    public List<LowStockBook> getLowStockBooks() throws SQLException {
        return getLowStockBooks(3); // Ngưỡng mặc định là 3
    }
    
    /**
     * Lấy tổng số đầu sách có số lượng khả dụng thấp
     * @param threshold Ngưỡng số lượng khả dụng
     * @return Tổng số đầu sách có số lượng khả dụng thấp
     * @throws SQLException Nếu có lỗi khi truy vấn cơ sở dữ liệu
     */
    public int countLowStockBooks(int threshold) throws SQLException {
        String query = "SELECT COUNT(*) AS total FROM Sach WHERE KhaDung < ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setInt(1, threshold);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        }
        
        return 0;
    }
    
    /**
     * Lấy tổng số đầu sách có số lượng khả dụng thấp hơn ngưỡng mặc định (3)
     * @return Tổng số đầu sách có số lượng khả dụng thấp
     * @throws SQLException Nếu có lỗi khi truy vấn cơ sở dữ liệu
     */
    public int countLowStockBooks() throws SQLException {
        return countLowStockBooks(3); // Ngưỡng mặc định là 3
    }
} 