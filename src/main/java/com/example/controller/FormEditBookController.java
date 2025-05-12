package com.example.controller;

import com.example.dao.DanhMucSachDAO;
import com.example.dao.SachDAO;
import com.example.model.DanhMucSach;
import com.example.model.Sach;
import com.example.view.FormEditBook;

import javax.swing.*;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

public class FormEditBookController {
    private FormEditBook view;
    private SachDAO sachDAO;
    private DanhMucSachDAO danhMucSachDAO;

    public FormEditBookController(FormEditBook view, SachDAO sachDAO, DanhMucSachDAO danhMucSachDAO) {
        this.view = view;
        this.sachDAO = sachDAO;
        this.danhMucSachDAO = danhMucSachDAO;
    }

    public void saveBook() {
        // Kiểm tra tính hợp lệ của đầu vào
        if (!validateInputs()) {
            return;
        }

        // Lấy dữ liệu từ form
        String maSach = view.getBookIdField().getText().trim();
        String isbn = view.getIsbnField().getText().trim();
        String tenSach = view.getBookTitleField().getText().trim();
        String tacGia = view.getAuthorField().getText().trim();

        // Lấy danh mục sách đã chọn
        int selectedIndex = view.getCategoryComboBox().getSelectedIndex();
        DanhMucSach danhMucSach = view.getCategories().get(selectedIndex);

        // Lấy các thông tin khác
        int namXuatBan = Integer.parseInt(view.getPubYearField().getText().trim());
        String nxb = view.getPublisherField().getText().trim();
        int soBan = Integer.parseInt(view.getCopiesField().getText().trim());
        int khaDung = Integer.parseInt(view.getAvailableField().getText().trim());
        String viTri = view.getLocationField().getText().trim();

        // Cập nhật đối tượng Sach
        Sach updatedSach = view.getCurrentBook();
        updatedSach.setISBN(isbn);
        updatedSach.setTenSach(tenSach);
        updatedSach.setTacGia(tacGia);
        updatedSach.setDanhMucSach(danhMucSach);
        updatedSach.setNamXuatBan(namXuatBan);
        updatedSach.setNXB(nxb);
        updatedSach.setSoBan(soBan);
        updatedSach.setKhaDung(khaDung);
        updatedSach.setViTri(viTri);

        // Lưu vào database
        try {
            sachDAO.updateBook(updatedSach);
            JOptionPane.showMessageDialog(view, "Cập nhật sách thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            view.dispose(); // Đóng form sau khi lưu thành công
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Lỗi khi cập nhật sách: " + e.getMessage(), 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private boolean validateInputs() {
        // Kiểm tra các trường bắt buộc
        if (view.getBookIdField().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập mã sách.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            view.getBookIdField().requestFocus();
            return false;
        }

        if (view.getIsbnField().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập ISBN.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            view.getIsbnField().requestFocus();
            return false;
        }

        if (view.getBookTitleField().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập tên sách.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            view.getBookTitleField().requestFocus();
            return false;
        }

        if (view.getAuthorField().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập tên tác giả.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            view.getAuthorField().requestFocus();
            return false;
        }
        
        // Kiểm tra danh mục
        if (view.getCategoryComboBox().getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn danh mục sách", "Lỗi", JOptionPane.ERROR_MESSAGE);
            view.getCategoryComboBox().requestFocus();
            return false;
        }
        
        // Kiểm tra nhà xuất bản
        if (view.getPublisherField().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập thông tin nhà xuất bản.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            view.getPublisherField().requestFocus();
            return false;
        }
        
        // Kiểm tra vị trí
        if (view.getLocationField().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập vị trí sách.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            view.getLocationField().requestFocus();
            return false;
        }

        // Kiểm tra năm xuất bản
        try {
            String pubYearStr = view.getPubYearField().getText().trim();
            if (pubYearStr.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Vui lòng nhập năm xuất bản.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                view.getPubYearField().requestFocus();
                return false;
            }
            
            int pubYear = Integer.parseInt(pubYearStr);
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            if (pubYear <= 0 || pubYear > currentYear) {
                JOptionPane.showMessageDialog(view, 
                        "Năm xuất bản phải lớn hơn 0 và nhỏ hơn hoặc bằng năm hiện tại.", 
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                view.getPubYearField().requestFocus();
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập năm xuất bản hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            view.getPubYearField().requestFocus();
            return false;
        }

        // Kiểm tra số bản
        try {
            String soBanStr = view.getCopiesField().getText().trim();
            if (soBanStr.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Vui lòng nhập số bản.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                view.getCopiesField().requestFocus();
                return false;
            }
            
            int copies = Integer.parseInt(soBanStr);
            if (copies <= 0) {
                JOptionPane.showMessageDialog(view, "Số bản phải lớn hơn 0.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                view.getCopiesField().requestFocus();
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập số bản hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            view.getCopiesField().requestFocus();
            return false;
        }
        
        // Kiểm tra số lượng khả dụng
        try {
            String khaDungStr = view.getAvailableField().getText().trim();
            if (khaDungStr.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Vui lòng nhập số lượng khả dụng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                view.getAvailableField().requestFocus();
                return false;
            }
            
            int available = Integer.parseInt(khaDungStr);
            int copies = Integer.parseInt(view.getCopiesField().getText().trim());
            
            if (available < 0 || available > copies) {
                JOptionPane.showMessageDialog(view, 
                        "Số lượng khả dụng phải lớn hơn hoặc bằng 0 và nhỏ hơn hoặc bằng tổng số bản.", 
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                view.getAvailableField().requestFocus();
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập số lượng khả dụng hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            view.getAvailableField().requestFocus();
            return false;
        }

        return true;
    }
} 