package com.example.controller;

import com.example.dao.PopularBooksDAO;
import com.example.model.PopularBookItem;
import com.example.view.AdminControlPopularBooks;

import javax.swing.JOptionPane;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Controller cho báo cáo sách phổ biến nhất
 */
public class PopularBooksController {
    private AdminControlPopularBooks view;
    private PopularBooksDAO dao;
    
    /**
     * Constructor với tham số view
     * @param view View hiển thị báo cáo sách phổ biến
     */
    public PopularBooksController(AdminControlPopularBooks view) {
        this.view = view;
        this.dao = new PopularBooksDAO();
    }
    
    /**
     * Lấy danh sách top 10 sách phổ biến nhất
     * @return Danh sách sách phổ biến
     */
    public List<PopularBookItem> getTop10PopularBooks() {
        try {
            return dao.getTop10PopularBooks();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view,
                "Lỗi khi truy vấn dữ liệu sách phổ biến: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Lấy danh sách sách phổ biến trong khoảng thời gian
     * @param startDate Ngày bắt đầu
     * @param endDate Ngày kết thúc
     * @return Danh sách sách phổ biến
     */
    public List<PopularBookItem> getPopularBooksByDateRange(Date startDate, Date endDate) {
        try {
            return dao.getTopPopularBooksByDateRange(startDate, endDate);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view,
                "Lỗi khi truy vấn dữ liệu sách phổ biến: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Tải dữ liệu top 10 sách phổ biến vào bảng
     */
    public void loadTop10PopularBooks() {
        List<PopularBookItem> books = getTop10PopularBooks();
        
        if (books == null || books.isEmpty()) {
            JOptionPane.showMessageDialog(view,
                "Không có dữ liệu sách phổ biến!",
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
            view.setNoDataVisible(true);
            return;
        }
        
        view.updateTable(books);
        view.setNoDataVisible(false);
    }
    
    /**
     * Tải dữ liệu sách phổ biến trong khoảng thời gian vào bảng
     * @param startDate Ngày bắt đầu
     * @param endDate Ngày kết thúc
     */
    public void loadPopularBooksByDateRange(Date startDate, Date endDate) {
        List<PopularBookItem> books = getPopularBooksByDateRange(startDate, endDate);
        
        if (books == null || books.isEmpty()) {
            JOptionPane.showMessageDialog(view,
                "Không có dữ liệu sách phổ biến trong khoảng thời gian đã chọn!",
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
            view.setNoDataVisible(true);
            return;
        }
        
        view.updateTable(books);
        view.setNoDataVisible(false);
    }
} 