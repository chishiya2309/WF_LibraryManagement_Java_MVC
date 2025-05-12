package com.example.controller;

import com.example.dao.DanhMucSachDAO;
import com.example.model.DanhMucSach;
import com.example.view.FormAddCategory;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

public class FormAddCategoryController {
    private FormAddCategory view;
    private DanhMucSachDAO danhMucSachDAO;
    private List<DanhMucSach> categories;

    public FormAddCategoryController(FormAddCategory view, DanhMucSachDAO danhMucSachDAO) {
        this.view = view;
        this.danhMucSachDAO = danhMucSachDAO;
    }
    
    public void loadCategories() {
        try {
            categories = danhMucSachDAO.getAllDanhMucSach();
            
            // Xóa tất cả các item hiện tại
            JComboBox<String> parentCategoryComboBox = view.getParentCategoryComboBox();
            parentCategoryComboBox.removeAllItems();
            
            // Thêm tùy chọn "Không có danh mục cha"
            parentCategoryComboBox.addItem("-- Không có danh mục cha --");
            
            // Thêm các danh mục từ database
            for (DanhMucSach category : categories) {
                parentCategoryComboBox.addItem(category.getTenDanhMuc());
            }
            
            // Chọn mục đầu tiên
            parentCategoryComboBox.setSelectedIndex(0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view,
                    "Lỗi khi tải danh mục: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    public void saveCategory() {
        // Kiểm tra dữ liệu đầu vào
        if (!validateInputs()) {
            return;
        }
        
        try {
            // Lấy dữ liệu từ form
            String categoryId = view.getCategoryIdField().getText().trim();
            String categoryName = view.getCategoryNameField().getText().trim();
            String description = view.getDescriptionField().getText().trim();
            
            // Xử lý danh mục cha
            String parentCategoryId = null;
            int selectedIndex = view.getParentCategoryComboBox().getSelectedIndex();
            if (selectedIndex > 0) { // Nếu không phải "Không có danh mục cha"
                parentCategoryId = categories.get(selectedIndex - 1).getMaDanhMuc();
            }
            
            int bookCount = Integer.parseInt(view.getBookCountField().getText().trim());
            String status = view.getStatusComboBox().getSelectedItem().toString();
            
            // Tạo đối tượng DanhMucSach mới
            DanhMucSach danhMucSach = new DanhMucSach();
            danhMucSach.setMaDanhMuc(categoryId);
            danhMucSach.setTenDanhMuc(categoryName);
            danhMucSach.setMoTa(description);
            danhMucSach.setDanhMucCha(parentCategoryId);
            danhMucSach.setSoLuongSach(bookCount);
            danhMucSach.setTrangThai(status);
            
            // Thêm danh mục vào cơ sở dữ liệu
            danhMucSachDAO.addCategory(danhMucSach);
            
            // Hiển thị thông báo thành công
            JOptionPane.showMessageDialog(view,
                    "Thêm danh mục mới thành công!",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            
            // Đặt cờ thành công
            view.setSuccessful(true);
            
            // Xóa dữ liệu trên form để nhập mới
            clearForm();
            
            // Tải lại danh sách danh mục để cập nhật combobox
            loadCategories();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view,
                    "Lỗi khi thêm danh mục: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view,
                    "Số lượng sách phải là số nguyên!",
                    "Lỗi",
                    JOptionPane.WARNING_MESSAGE);
            view.getBookCountField().requestFocus();
        }
    }
    
    private boolean validateInputs() {
        // Kiểm tra các trường bắt buộc
        if (view.getCategoryIdField().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "Vui lòng nhập mã danh mục!",
                    "Lỗi",
                    JOptionPane.WARNING_MESSAGE);
            view.getCategoryIdField().requestFocus();
            return false;
        }
        
        if (view.getCategoryNameField().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "Vui lòng nhập tên danh mục!",
                    "Lỗi",
                    JOptionPane.WARNING_MESSAGE);
            view.getCategoryNameField().requestFocus();
            return false;
        }
        
        // Kiểm tra số lượng sách
        try {
            if (!view.getBookCountField().getText().trim().isEmpty()) {
                int bookCount = Integer.parseInt(view.getBookCountField().getText().trim());
                if (bookCount < 0) {
                    JOptionPane.showMessageDialog(view,
                            "Số lượng sách phải là số nguyên không âm!",
                            "Lỗi",
                            JOptionPane.WARNING_MESSAGE);
                    view.getBookCountField().requestFocus();
                    return false;
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view,
                    "Số lượng sách phải là số nguyên!",
                    "Lỗi",
                    JOptionPane.WARNING_MESSAGE);
            view.getBookCountField().requestFocus();
            return false;
        }
        
        // Kiểm tra mã danh mục đã tồn tại chưa
        try {
            String categoryId = view.getCategoryIdField().getText().trim();
            for (DanhMucSach category : categories) {
                if (category.getMaDanhMuc().equals(categoryId)) {
                    JOptionPane.showMessageDialog(view,
                            "Mã danh mục đã tồn tại, vui lòng chọn mã khác!",
                            "Lỗi",
                            JOptionPane.WARNING_MESSAGE);
                    view.getCategoryIdField().requestFocus();
                    return false;
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view,
                    "Lỗi khi kiểm tra mã danh mục: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private void clearForm() {
        // Xóa dữ liệu trên các trường nhập liệu
        view.getCategoryIdField().setText("");
        view.getCategoryNameField().setText("");
        view.getDescriptionField().setText("");
        view.getParentCategoryComboBox().setSelectedIndex(0);
        view.getBookCountField().setText("0");
        view.getStatusComboBox().setSelectedIndex(0);
        
        // Đặt focus vào trường đầu tiên
        view.getCategoryIdField().requestFocus();
    }
} 