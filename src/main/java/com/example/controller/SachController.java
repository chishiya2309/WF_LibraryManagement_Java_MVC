package com.example.controller;

import com.example.dao.DanhMucSachDAO;
import com.example.dao.SachDAO;
import com.example.model.Sach;
import com.example.view.AdminControlBooks;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SachController {
    private AdminControlBooks mainView;
    private SachDAO sachDAO;
    private DanhMucSachDAO danhMucSachDAO;

    public SachController() {}

    public SachController(AdminControlBooks mainView, SachDAO sachDAO, DanhMucSachDAO danhMucSachDAO) {
        this.mainView = mainView;
        this.sachDAO = sachDAO;
        this.danhMucSachDAO = danhMucSachDAO;
        
        // Đăng ký các listener cho các sự kiện tìm kiếm
        initSearchListeners();
    }
    
    private void initSearchListeners() {
        // Thêm listener cho nút tìm kiếm
        mainView.getSearchButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });
        
        // Thêm listener cho ô tìm kiếm (Enter key)
        mainView.getSearchField().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performSearch();
                }
            }
        });
    }
    
    private void performSearch() {
        try {
            String searchTerm = mainView.getSearchField().getText();
            searchBooks(searchTerm);
            
            // Show/hide no data label
            mainView.getNoDataLabel().setVisible(mainView.getTableModel().getRowCount() == 0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                mainView, 
                "Lỗi khi tìm kiếm: " + ex.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        }
    }
    
    public void loadBooks() throws SQLException {
        List<Sach> books = sachDAO.getAllSachs();
        mainView.displaySachs(books);
    }
    
    public void searchBooks(String searchTerm) throws SQLException {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            // Nếu từ khóa tìm kiếm trống thì tải tất cả sách
            loadBooks();
            return;
        }
        
        // Sử dụng phương thức searchSachs từ DAO để tìm kiếm trong database
        List<Sach> searchResults = sachDAO.searchSachs(searchTerm.trim());
        
        // Hiển thị kết quả tìm kiếm
        mainView.displaySachs(searchResults);
    }
}
