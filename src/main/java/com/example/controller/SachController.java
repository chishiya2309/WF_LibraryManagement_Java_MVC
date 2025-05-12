package com.example.controller;

import com.example.dao.DanhMucSachDAO;
import com.example.dao.SachDAO;
import com.example.model.Sach;
import com.example.view.AdminControlBooks;
import com.example.view.FormAddBook;
import com.example.view.FormEditBook;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
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
        
        // Đăng ký listener cho nút thêm sách
        initAddBookListener();
        
        // Đăng ký listener cho nút chỉnh sửa sách
        initEditBookListener();
        
        // Đăng ký listener cho nút xóa sách
        initDeleteBookListener();
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
    
    private void initAddBookListener() {
        // Thêm listener cho nút thêm sách
        mainView.getAddBookButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Luôn mở form thêm sách trống khi nhấn nút thêm sách
                FormAddBook formAddBook = new FormAddBook();
                formAddBook.setVisible(true);
                
                // Thêm WindowListener để reload dữ liệu khi form đóng
                formAddBook.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                        try {
                            loadBooks();
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(
                                mainView,
                                "Lỗi khi tải lại dữ liệu: " + ex.getMessage(),
                                "Lỗi",
                                JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                });
            }
        });
    }
    
    private void initEditBookListener() {
        // Thêm listener cho nút chỉnh sửa sách
        mainView.getEditBookButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lấy dòng đang chọn từ bảng
                int selectedRow = mainView.getBooksTable().getSelectedRow();
                
                if (selectedRow == -1) {
                    // Nếu không có dòng nào được chọn, hiển thị thông báo lỗi
                    JOptionPane.showMessageDialog(
                        mainView,
                        "Vui lòng chọn sách cần chỉnh sửa", 
                        "Thông báo", 
                        JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }
                
                // Lấy mã sách từ dòng đã chọn
                String maSach = mainView.getTableModel().getValueAt(selectedRow, 0).toString();
                
                try {
                    // Lấy thông tin sách từ cơ sở dữ liệu
                    List<Sach> sachs = sachDAO.getSachByMaSach(maSach);
                    
                    if (sachs.isEmpty()) {
                        JOptionPane.showMessageDialog(
                            mainView,
                            "Không tìm thấy thông tin sách", 
                            "Lỗi", 
                            JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }
                    
                    // Mở form chỉnh sửa với thông tin sách đã chọn
                    FormEditBook formEditBook = new FormEditBook(sachs.get(0));
                    formEditBook.setVisible(true);
                    
                    // Thêm WindowListener để reload dữ liệu khi form đóng
                    formEditBook.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                            try {
                                loadBooks();
                            } catch (SQLException ex) {
                                JOptionPane.showMessageDialog(
                                    mainView,
                                    "Lỗi khi tải lại dữ liệu: " + ex.getMessage(),
                                    "Lỗi",
                                    JOptionPane.ERROR_MESSAGE
                                );
                            }
                        }
                    });
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(
                        mainView,
                        "Lỗi khi lấy thông tin sách: " + ex.getMessage(), 
                        "Lỗi", 
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
    }
    
    private void initDeleteBookListener() {
        // Thêm listener cho nút xóa sách
        mainView.getDeleteBookButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteBook();
            }
        });
        
        // Thêm listener cho menu item xóa trong context menu
        mainView.getDeleteMenuItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteBook();
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
    
    private void deleteBook() {
        // Lấy dòng đang chọn từ bảng
        int selectedRow = mainView.getBooksTable().getSelectedRow();
        
        if (selectedRow == -1) {
            // Nếu không có dòng nào được chọn, hiển thị thông báo lỗi
            JOptionPane.showMessageDialog(
                mainView,
                "Vui lòng chọn sách để xóa.", 
                "Thông báo", 
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        // Lấy mã sách và tên sách từ dòng đã chọn
        String maSach = mainView.getTableModel().getValueAt(selectedRow, 0).toString();
        String tenSach = mainView.getTableModel().getValueAt(selectedRow, 2).toString();
        
        // Hiển thị hộp thoại xác nhận xóa
        int confirm = JOptionPane.showConfirmDialog(
                mainView,
                "Bạn có chắc chắn muốn xóa sách '" + tenSach + "' (Mã: " + maSach + ")?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        
        // Nếu người dùng xác nhận xóa
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Lấy thông tin sách từ database
                List<Sach> sachs = sachDAO.getSachByMaSach(maSach);
                
                if (sachs.isEmpty()) {
                    JOptionPane.showMessageDialog(
                        mainView,
                        "Không tìm thấy thông tin sách", 
                        "Lỗi", 
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                
                // Xóa sách từ database
                sachDAO.deleteBook(sachs.get(0));
                
                // Tải lại dữ liệu sau khi xóa
                loadBooks();
                
                // Hiển thị thông báo thành công
                JOptionPane.showMessageDialog(
                    mainView,
                    "Đã xóa sách thành công.", 
                    "Thông báo", 
                    JOptionPane.INFORMATION_MESSAGE
                );
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(
                    mainView,
                    "Lỗi khi xóa sách: " + ex.getMessage(), 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE
                );
                ex.printStackTrace();
            }
        }
    }
}
