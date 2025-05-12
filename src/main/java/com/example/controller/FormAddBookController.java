package com.example.controller;

import com.example.dao.DanhMucSachDAO;
import com.example.dao.SachDAO;
import com.example.model.DanhMucSach;
import com.example.model.Sach;
import com.example.view.FormAddBook;

import javax.swing.*;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

public class FormAddBookController {
    private FormAddBook view;
    private SachDAO sachDAO;
    private DanhMucSachDAO danhMucSachDAO;
    
    public FormAddBookController(FormAddBook view, SachDAO sachDAO, DanhMucSachDAO danhMucSachDAO) {
        this.view = view;
        this.sachDAO = sachDAO;
        this.danhMucSachDAO = danhMucSachDAO;
    }
    
    public void saveBook() {
        if (!validateInputs()) {
            return;
        }

        try {
            String bookId = view.getBookIdField().getText().trim();
            String isbn = view.getIsbnField().getText().trim();
            String title = view.getBookTitleField().getText().trim();
            String author = view.getAuthorField().getText().trim();
            
            // Lấy mã danh mục từ danh mục được chọn
            int selectedIndex = view.getCategoryComboBox().getSelectedIndex();
            List<DanhMucSach> categories = view.getCategories();
            DanhMucSach selectedCategory = categories.get(selectedIndex);
            
            int pubYear = Integer.parseInt(view.getPubYearField().getText().trim());
            String publisher = view.getPublisherField().getText().trim();
            int copies = Integer.parseInt(view.getCopiesField().getText().trim());
            int available = Integer.parseInt(view.getAvailableField().getText().trim());
            String location = view.getLocationField().getText().trim();

            // Tạo đối tượng Sách mới
            Sach sach = new Sach(
                bookId, 
                isbn, 
                title, 
                author, 
                selectedCategory,
                pubYear, 
                publisher, 
                copies, 
                available, 
                location
            );

            sachDAO.addBook(sach);
            JOptionPane.showMessageDialog(view, "Sách đã được thêm thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            
            // Xóa nội dung các trường sau khi thêm thành công
            clearFields();
            
            // Không đóng form tự động để người dùng có thể tiếp tục thêm sách khác
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập số hợp lệ cho 'Năm xuất bản', 'Số bản' và 'Khả dụng'.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Lỗi khi lưu sách: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
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
            int pubYear = Integer.parseInt(view.getPubYearField().getText().trim());
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            if (pubYear <= 0 || pubYear > currentYear) {
                JOptionPane.showMessageDialog(view, "Năm xuất bản phải lớn hơn 0 và nhỏ hơn hoặc bằng năm hiện tại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
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
            int copies = Integer.parseInt(view.getCopiesField().getText().trim());
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
            int available = Integer.parseInt(view.getAvailableField().getText().trim());
            int copies = Integer.parseInt(view.getCopiesField().getText().trim());
            if (available < 0 || available > copies) {
                JOptionPane.showMessageDialog(view, "Số lượng khả dụng phải lớn hơn hoặc bằng 0 và nhỏ hơn hoặc bằng tổng số bản.", "Lỗi", JOptionPane.ERROR_MESSAGE);
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
    
    /**
     * Xóa nội dung của tất cả các trường nhập liệu
     */
    private void clearFields() {
        view.getBookIdField().setText("");
        view.getIsbnField().setText("");
        view.getBookTitleField().setText("");
        view.getAuthorField().setText("");
        view.getPubYearField().setText("");
        view.getPublisherField().setText("");
        view.getCopiesField().setText("1");
        view.getAvailableField().setText("1");
        view.getLocationField().setText("");
        
        // Đặt focus vào trường đầu tiên để tiếp tục nhập
        view.getBookIdField().requestFocus();
    }
} 