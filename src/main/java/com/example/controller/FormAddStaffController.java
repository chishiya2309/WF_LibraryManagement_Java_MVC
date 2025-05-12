package com.example.controller;

import com.example.dao.NhanVienDAO;
import com.example.model.NhanVien;
import com.example.view.FormAddStaff;

import javax.swing.*;
import java.sql.Date;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class FormAddStaffController {
    private FormAddStaff view;
    private NhanVienDAO nhanVienDAO;
    private boolean successful = false;

    public FormAddStaffController(FormAddStaff view, NhanVienDAO nhanVienDAO) {
        this.view = view;
        this.nhanVienDAO = nhanVienDAO;
    }

    public void saveStaff() {
        if (!validateInputs()) {
            return;
        }

        try {
            String id = view.getIdField().getText().trim();
            String name = view.getNameField().getText().trim();
            String gender = view.getGenderComboBox().getSelectedItem().toString();
            String position = view.getPositionComboBox().getSelectedItem().toString();
            String email = view.getEmailField().getText().trim();
            String phone = view.getPhoneField().getText().trim();
            java.util.Date startDateUtil = (java.util.Date) view.getStartDateSpinner().getValue();
            Date startDate = new Date(startDateUtil.getTime());
            String status = view.getStatusComboBox().getSelectedItem().toString();

            // Kiểm tra ID đã tồn tại chưa
            NhanVien existingStaff = nhanVienDAO.getNhanVienById(id);
            if (existingStaff != null) {
                JOptionPane.showMessageDialog(view,
                        "ID nhân viên đã tồn tại. Vui lòng chọn ID khác.",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                view.getIdField().requestFocus();
                return;
            }

            // Kiểm tra email đã tồn tại chưa
            if (nhanVienDAO.isEmailExists(email)) {
                JOptionPane.showMessageDialog(view,
                        "Email đã được sử dụng bởi nhân viên khác. Vui lòng sử dụng email khác.",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                view.getEmailField().requestFocus();
                return;
            }

            // Kiểm tra số điện thoại đã tồn tại chưa
            if (nhanVienDAO.isPhoneExists(phone)) {
                JOptionPane.showMessageDialog(view,
                        "Số điện thoại đã được sử dụng bởi nhân viên khác. Vui lòng sử dụng số điện thoại khác.",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                view.getPhoneField().requestFocus();
                return;
            }

            // Tạo đối tượng NhanVien mới
            NhanVien nhanVien = new NhanVien(id, name, gender, position, email, phone, startDate, status);

            // Thêm nhân viên vào database
            nhanVienDAO.addNhanVien(nhanVien);

            JOptionPane.showMessageDialog(view,
                    "Thêm nhân viên mới thành công!",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            successful = true;
            
            // Clear form để tiếp tục thêm nhân viên mới
            clearForm();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view,
                    "Lỗi khi thêm nhân viên: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view,
                    "Lỗi: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    // Phương thức để xóa dữ liệu trong form
    private void clearForm() {
        view.getIdField().setText("");
        view.getNameField().setText("");
        view.getGenderComboBox().setSelectedIndex(0);
        view.getPositionComboBox().setSelectedIndex(0);
        view.getEmailField().setText("");
        view.getPhoneField().setText("");
        view.getStartDateSpinner().setValue(new java.util.Date());
        view.getStatusComboBox().setSelectedIndex(0);
        
        // Focus vào trường ID để tiếp tục nhập
        view.getIdField().requestFocus();
    }

    private boolean validateInputs() {
        // Validate ID
        if (view.getIdField().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "Vui lòng nhập ID của nhân viên!",
                    "Lỗi",
                    JOptionPane.WARNING_MESSAGE);
            view.getIdField().requestFocus();
            return false;
        }

        // Validate name
        if (view.getNameField().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "Vui lòng nhập họ tên của nhân viên!",
                    "Lỗi",
                    JOptionPane.WARNING_MESSAGE);
            view.getNameField().requestFocus();
            return false;
        }

        // Validate email
        String email = view.getEmailField().getText().trim();
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "Vui lòng nhập địa chỉ email!",
                    "Lỗi",
                    JOptionPane.WARNING_MESSAGE);
            view.getEmailField().requestFocus();
            return false;
        }

        // Validate email format
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (!Pattern.matches(emailPattern, email)) {
            JOptionPane.showMessageDialog(view,
                    "Địa chỉ email không hợp lệ!",
                    "Lỗi",
                    JOptionPane.WARNING_MESSAGE);
            view.getEmailField().requestFocus();
            return false;
        }

        // Validate phone
        String phone = view.getPhoneField().getText().trim();
        if (phone.isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "Vui lòng nhập số điện thoại!",
                    "Lỗi",
                    JOptionPane.WARNING_MESSAGE);
            view.getPhoneField().requestFocus();
            return false;
        }

        // Validate phone format
        String phonePattern = "^0\\d{9,10}$";
        if (!Pattern.matches(phonePattern, phone)) {
            JOptionPane.showMessageDialog(view,
                    "Số điện thoại không hợp lệ! Số điện thoại phải bắt đầu bằng số 0 và có 10-11 chữ số.",
                    "Lỗi",
                    JOptionPane.WARNING_MESSAGE);
            view.getPhoneField().requestFocus();
            return false;
        }

        return true;
    }

    public boolean isSuccessful() {
        return successful;
    }
} 