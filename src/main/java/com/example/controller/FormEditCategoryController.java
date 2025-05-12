package com.example.controller;

import javax.swing.*;
import java.sql.SQLException;

import com.example.dao.DanhMucSachDAO;
import com.example.model.DanhMucSach;
import com.example.view.FormEditCategory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormEditCategoryController {
    private FormEditCategory view;
    private DanhMucSachDAO danhMucSachDAO;
    private DanhMucSach currentCategory;
    private List<DanhMucSach> allCategories;
    private boolean successful = false;

    public FormEditCategoryController(FormEditCategory view, DanhMucSachDAO danhMucSachDAO) {
        this.view = view;
        this.danhMucSachDAO = danhMucSachDAO;
    }

    public void loadCategoryData(String maDanhMuc) {
        try {
            // Lấy thông tin danh mục cần chỉnh sửa
            currentCategory = danhMucSachDAO.getDanhMucSach(maDanhMuc);
            if (currentCategory == null) {
                JOptionPane.showMessageDialog(view,
                        "Không tìm thấy danh mục cần chỉnh sửa!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                view.dispose();
                return;
            }

            // Lấy tất cả danh mục cho combobox danh mục cha
            allCategories = danhMucSachDAO.getAllDanhMucSach();

            // Hiển thị dữ liệu lên form
            view.getCategoryIdField().setText(currentCategory.getMaDanhMuc());
            view.getCategoryNameField().setText(currentCategory.getTenDanhMuc());
            view.getDescriptionField().setText(currentCategory.getMoTa());
            view.getBookCountField().setText(String.valueOf(currentCategory.getSoLuongSach()));
            
            // Thiết lập combobox trạng thái
            if ("Hoạt động".equals(currentCategory.getTrangThai())) {
                view.getStatusComboBox().setSelectedIndex(0);
            } else {
                view.getStatusComboBox().setSelectedIndex(1);
            }

            // Thêm các danh mục vào combobox
            populateParentCategoryComboBox();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view,
                    "Lỗi khi tải dữ liệu: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            view.dispose();
        }
    }

    private void populateParentCategoryComboBox() {
        JComboBox<String> comboBox = view.getParentCategoryComboBox();
        comboBox.removeAllItems();
        
        // Thêm lựa chọn "Không có danh mục cha"
        comboBox.addItem("-- Không có danh mục cha --");
        
        // Lưu trữ mapping giữa index và category ID để sử dụng khi lưu
        Map<Integer, String> indexToCategoryId = new HashMap<>();
        view.setIndexToCategoryId(indexToCategoryId);
        
        int selectedIndex = 0;
        int index = 1;
        
        // Thêm các danh mục vào combobox
        for (DanhMucSach category : allCategories) {
            // Bỏ qua danh mục hiện tại (không thể chọn chính nó làm cha)
            if (!category.getMaDanhMuc().equals(currentCategory.getMaDanhMuc())) {
                comboBox.addItem(category.getTenDanhMuc());
                indexToCategoryId.put(index, category.getMaDanhMuc());
                
                // Nếu là danh mục cha hiện tại, ghi nhớ index
                if (category.getMaDanhMuc().equals(currentCategory.getDanhMucCha())) {
                    selectedIndex = index;
                }
                
                index++;
            }
        }
        
        // Thiết lập lựa chọn danh mục cha
        if (currentCategory.getDanhMucCha() != null && !currentCategory.getDanhMucCha().isEmpty()) {
            comboBox.setSelectedIndex(selectedIndex);
        } else {
            comboBox.setSelectedIndex(0);
        }
    }

    public void saveCategory() {
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
            if (selectedIndex > 0) {
                parentCategoryId = view.getIndexToCategoryId().get(selectedIndex);
            }
            
            // Kiểm tra không thể chọn chính mình làm danh mục cha
            if (categoryId.equals(parentCategoryId)) {
                JOptionPane.showMessageDialog(view,
                        "Không thể chọn chính danh mục này làm danh mục cha!",
                        "Lỗi",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int bookCount = Integer.parseInt(view.getBookCountField().getText().trim());
            String status = view.getStatusComboBox().getSelectedItem().toString();

            // Cập nhật đối tượng danh mục
            currentCategory.setTenDanhMuc(categoryName);
            currentCategory.setMoTa(description);
            currentCategory.setDanhMucCha(parentCategoryId);
            currentCategory.setSoLuongSach(bookCount);
            currentCategory.setTrangThai(status);

            // Cập nhật xuống database
            danhMucSachDAO.updateCategory(currentCategory);

            JOptionPane.showMessageDialog(view,
                    "Cập nhật danh mục thành công!",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            successful = true;
            view.dispose();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view,
                    "Lỗi: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateInputs() {
        // Kiểm tra tên danh mục
        if (view.getCategoryNameField().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "Vui lòng nhập tên danh mục!",
                    "Lỗi",
                    JOptionPane.WARNING_MESSAGE);
            view.getCategoryNameField().requestFocus();
            return false;
        }

        // Kiểm tra độ dài tên danh mục
        if (view.getCategoryNameField().getText().trim().length() > 255) {
            JOptionPane.showMessageDialog(view,
                    "Tên danh mục không được vượt quá 255 ký tự!",
                    "Lỗi",
                    JOptionPane.WARNING_MESSAGE);
            view.getCategoryNameField().requestFocus();
            return false;
        }

        // Kiểm tra độ dài mô tả
        if (view.getDescriptionField().getText().trim().length() > 500) {
            JOptionPane.showMessageDialog(view,
                    "Mô tả không được vượt quá 500 ký tự!",
                    "Lỗi",
                    JOptionPane.WARNING_MESSAGE);
            view.getDescriptionField().requestFocus();
            return false;
        }

        // Kiểm tra số lượng sách
        try {
            int bookCount = Integer.parseInt(view.getBookCountField().getText().trim());
            if (bookCount < 0) {
                JOptionPane.showMessageDialog(view,
                        "Số lượng sách phải là số nguyên không âm!",
                        "Lỗi",
                        JOptionPane.WARNING_MESSAGE);
                view.getBookCountField().requestFocus();
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view,
                    "Số lượng sách phải là số nguyên!",
                    "Lỗi",
                    JOptionPane.WARNING_MESSAGE);
            view.getBookCountField().requestFocus();
            return false;
        }

        return true;
    }

    public boolean isSuccessful() {
        return successful;
    }
} 