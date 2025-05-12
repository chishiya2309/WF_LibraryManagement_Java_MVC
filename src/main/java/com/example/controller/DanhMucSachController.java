package com.example.controller;

import com.example.dao.DanhMucSachDAO;
import com.example.model.DanhMucSach;
import com.example.view.AdminControlCategories;
import com.example.view.FormAddCategory;
import com.example.view.FormEditCategory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.List;

public class DanhMucSachController {
    private AdminControlCategories mainView;
    private DanhMucSachDAO danhMucSachDAO;

    public DanhMucSachController() {}

    public DanhMucSachController(AdminControlCategories mainView, DanhMucSachDAO danhMucSachDAO) {
        this.mainView = mainView;
        this.danhMucSachDAO = danhMucSachDAO;
        
        // Đăng ký các listener cho các sự kiện tìm kiếm
        initSearchListeners();
        
        // Đăng ký listener cho nút thêm danh mục
        initAddCategoryListener();
        
        // Đăng ký listener cho nút chỉnh sửa danh mục
        initEditCategoryListener();
        
        // Đăng ký listener cho nút xóa danh mục
        initDeleteCategoryListener();
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
    
    private void initAddCategoryListener() {
        // Thêm listener cho nút thêm danh mục
        mainView.getAddCategoryButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddCategoryForm();
            }
        });
    }
    
    private void initEditCategoryListener() {
        // Thêm listener cho nút chỉnh sửa danh mục
        mainView.getEditCategoryButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editSelectedCategory();
            }
        });
    }
    
    private void initDeleteCategoryListener() {
        // Thêm listener cho nút xóa danh mục
        mainView.getDeleteCategoryButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedCategory();
            }
        });
        
        // Thêm listener cho menu item xóa trong context menu
        mainView.getDeleteMenuItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedCategory();
            }
        });
    }
    
    private void performSearch() {
        String searchTerm = mainView.getSearchField().getText().trim();
        
        if (searchTerm.isEmpty()) {
            try {
                loadCategories();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(mainView,
                    "Lỗi khi tải dữ liệu danh mục: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            }
            return;
        }
        
        try {
            // Sử dụng phương thức searchDanhMucSachs từ DAO để tìm kiếm trong database
            List<DanhMucSach> searchResults = danhMucSachDAO.searchDanhMucSachs(searchTerm);
            
            // Hiển thị kết quả tìm kiếm
            displayCategories(searchResults);
            
            // Show/hide no data label
            mainView.getNoDataLabel().setVisible(searchResults.isEmpty());
            mainView.getCategoriesTable().setVisible(!searchResults.isEmpty());
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(mainView,
                "Lỗi khi tìm kiếm danh mục: " + ex.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void loadCategories() throws SQLException {
        List<DanhMucSach> categories = danhMucSachDAO.getAllDanhMucSach();
        displayCategories(categories);
        
        // Show/hide no data label
        mainView.getNoDataLabel().setVisible(categories.isEmpty());
        mainView.getCategoriesTable().setVisible(!categories.isEmpty());
    }
    
    private void displayCategories(List<DanhMucSach> categories) {
        // Xóa dữ liệu cũ
        mainView.getTableModel().setRowCount(0);
        
        // Tạo map để tra cứu tên danh mục cha
        java.util.Map<String, String> categoryMap = new java.util.HashMap<>();
        for (DanhMucSach category : categories) {
            categoryMap.put(category.getMaDanhMuc(), category.getTenDanhMuc());
        }
        
        // Thêm dữ liệu mới vào bảng
        for (DanhMucSach category : categories) {
            Object[] row = new Object[8];
            row[0] = category.getMaDanhMuc();
            row[1] = category.getTenDanhMuc();
            row[2] = category.getMoTa();
            
            // Xử lý danh mục cha
            String parentId = category.getDanhMucCha();
            row[3] = (parentId != null && !parentId.isEmpty()) ? categoryMap.get(parentId) : "";
            
            row[4] = category.getSoLuongSach();
            row[5] = category.getNgayTao();
            row[6] = category.getCapNhatLanCuoi();
            row[7] = category.getTrangThai();
            
            mainView.getTableModel().addRow(row);
        }
    }
    
    private void showAddCategoryForm() {
        FormAddCategory formAddCategory = new FormAddCategory();
        formAddCategory.setVisible(true);
        formAddCategory.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                if (formAddCategory.isSuccessful()) {
                    try {
                        loadCategories();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(mainView,
                            "Lỗi khi tải lại dữ liệu: " + ex.getMessage(),
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }
    
    public void editSelectedCategory() {
        int selectedRow = mainView.getCategoriesTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(mainView,
                "Vui lòng chọn một danh mục để chỉnh sửa!",
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String categoryId = mainView.getTableModel().getValueAt(selectedRow, 0).toString();

        FormEditCategory formEditCategory = new FormEditCategory(categoryId);
        formEditCategory.setVisible(true);
        formEditCategory.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                if (formEditCategory.isSuccessful()) {
                    try {
                        loadCategories();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(mainView,
                            "Lỗi khi tải lại dữ liệu: " + ex.getMessage(),
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }
    
    private void deleteSelectedCategory() {
        int selectedRow = mainView.getCategoriesTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(mainView,
                "Vui lòng chọn danh mục cần xóa!",
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            String categoryId = mainView.getTableModel().getValueAt(selectedRow, 0).toString();
            String categoryName = mainView.getTableModel().getValueAt(selectedRow, 1).toString();

            int result = JOptionPane.showConfirmDialog(mainView,
                "Bạn có chắc muốn xóa danh mục '" + categoryName + "'?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

            if (result == JOptionPane.YES_OPTION) {
                boolean success = danhMucSachDAO.deleteCategory(categoryId);

                if (success) {
                    loadCategories();
                    JOptionPane.showMessageDialog(mainView,
                        "Đã xóa danh mục '" + categoryName + "' thành công!",
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(mainView,
                        "Không thể xóa danh mục. Danh mục có thể có ràng buộc dữ liệu khác!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(mainView,
                "Lỗi khi xóa danh mục: " + ex.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
