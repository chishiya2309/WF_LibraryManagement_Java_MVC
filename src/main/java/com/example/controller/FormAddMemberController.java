package com.example.controller;

import com.example.dao.ThanhVienDAO;
import com.example.model.ThanhVien;
import com.example.view.FormAddMember;

import javax.swing.*;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.regex.Pattern;

public class FormAddMemberController {
    private FormAddMember view;
    private ThanhVienDAO thanhVienDAO;
    private boolean successful = false;

    public FormAddMemberController(FormAddMember view, ThanhVienDAO thanhVienDAO) {
        this.view = view;
        this.thanhVienDAO = thanhVienDAO;
    }

    public void saveMember() {
        if (!validateInputs()) {
            return;
        }

        try {
            String memberId = view.getMemberIdField().getText().trim();
            String name = view.getNameField().getText().trim();
            String gender = view.getGenderComboBox().getSelectedItem().toString();
            String phone = view.getPhoneField().getText().trim();
            String email = view.getEmailField().getText().trim();
            String memberType = view.getMemberTypeComboBox().getSelectedItem().toString();
            java.util.Date registerDateUtil = (java.util.Date) view.getRegisterDateSpinner().getValue();
            java.util.Date expiryDateUtil = (java.util.Date) view.getExpiryDateSpinner().getValue();
            String status = view.getStatusComboBox().getSelectedItem().toString();

            // Kiểm tra trùng lặp mã thành viên
            if (thanhVienDAO.isMemberIdExists(memberId)) {
                JOptionPane.showMessageDialog(view,
                        "Mã thành viên đã tồn tại trong hệ thống!",
                        "Lỗi",
                        JOptionPane.WARNING_MESSAGE);
                view.getMemberIdField().requestFocus();
                return;
            }

            // Kiểm tra trùng lặp số điện thoại
            if (thanhVienDAO.isPhoneExists(phone)) {
                JOptionPane.showMessageDialog(view,
                        "Số điện thoại đã được đăng ký cho thành viên khác!",
                        "Lỗi",
                        JOptionPane.WARNING_MESSAGE);
                view.getPhoneField().requestFocus();
                return;
            }

            // Kiểm tra trùng lặp email
            if (thanhVienDAO.isEmailExists(email)) {
                JOptionPane.showMessageDialog(view,
                        "Email đã được đăng ký cho thành viên khác!",
                        "Lỗi",
                        JOptionPane.WARNING_MESSAGE);
                view.getEmailField().requestFocus();
                return;
            }

            // Chuyển đổi từ java.util.Date sang java.sql.Date
            Date registerDate = new Date(registerDateUtil.getTime());
            Date expiryDate = new Date(expiryDateUtil.getTime());

            // Tạo đối tượng ThanhVien
            ThanhVien thanhVien = new ThanhVien(
                memberId, name, gender, phone, email, memberType, registerDate, expiryDate, status
            );

            // Thêm thành viên vào database
            thanhVienDAO.addMember(thanhVien);

            JOptionPane.showMessageDialog(view,
                    "Thêm thành viên mới thành công!",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            successful = true;
            
            // Clear form để tiếp tục nhập thay vì đóng form
            clearForm();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view,
                    "Lỗi khi thêm thành viên: " + ex.getMessage(),
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

    private void clearForm() {
        // Xóa dữ liệu đã nhập
        view.getMemberIdField().setText("");
        view.getNameField().setText("");
        view.getPhoneField().setText("");
        view.getEmailField().setText("");
        
        // Đặt lại các giá trị mặc định
        view.getGenderComboBox().setSelectedIndex(0);
        view.getMemberTypeComboBox().setSelectedIndex(0);
        view.getStatusComboBox().setSelectedIndex(0);
        
        // Đặt lại ngày đăng ký là ngày hiện tại
        java.util.Date today = new java.util.Date();
        view.getRegisterDateSpinner().setValue(today);
        
        // Đặt lại ngày hết hạn là 2 năm sau
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.YEAR, 2);
        view.getExpiryDateSpinner().setValue(calendar.getTime());
        
        // Focus vào trường nhập liệu đầu tiên
        view.getMemberIdField().requestFocus();
    }

    private boolean validateInputs() {
        // Check member ID
        if (view.getMemberIdField().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "Vui lòng nhập mã thành viên!",
                    "Lỗi",
                    JOptionPane.WARNING_MESSAGE);
            view.getMemberIdField().requestFocus();
            return false;
        }

        // Check name
        if (view.getNameField().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "Vui lòng nhập họ tên thành viên!",
                    "Lỗi",
                    JOptionPane.WARNING_MESSAGE);
            view.getNameField().requestFocus();
            return false;
        }

        // Check phone
        if (view.getPhoneField().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "Vui lòng nhập số điện thoại!",
                    "Lỗi",
                    JOptionPane.WARNING_MESSAGE);
            view.getPhoneField().requestFocus();
            return false;
        }

        // Check phone format
        String phonePattern = "^0\\d{9,10}$";
        if (!Pattern.matches(phonePattern, view.getPhoneField().getText().trim())) {
            JOptionPane.showMessageDialog(view,
                    "Số điện thoại không hợp lệ! Số điện thoại phải bắt đầu bằng số 0 và có 10-11 chữ số.",
                    "Lỗi",
                    JOptionPane.WARNING_MESSAGE);
            view.getPhoneField().requestFocus();
            return false;
        }

        // Check email
        if (view.getEmailField().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "Vui lòng nhập địa chỉ email!",
                    "Lỗi",
                    JOptionPane.WARNING_MESSAGE);
            view.getEmailField().requestFocus();
            return false;
        }

        // Check email format
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (!Pattern.matches(emailPattern, view.getEmailField().getText().trim())) {
            JOptionPane.showMessageDialog(view,
                    "Địa chỉ email không hợp lệ!",
                    "Lỗi",
                    JOptionPane.WARNING_MESSAGE);
            view.getEmailField().requestFocus();
            return false;
        }

        // Check expiry date is after register date
        java.util.Date registerDate = (java.util.Date) view.getRegisterDateSpinner().getValue();
        java.util.Date expiryDate = (java.util.Date) view.getExpiryDateSpinner().getValue();

        if (!expiryDate.after(registerDate)) {
            JOptionPane.showMessageDialog(view,
                    "Ngày hết hạn phải lớn hơn ngày đăng ký!",
                    "Lỗi",
                    JOptionPane.WARNING_MESSAGE);
            view.getExpiryDateSpinner().requestFocus();
            return false;
        }

        return true;
    }

    public boolean isSuccessful() {
        return successful;
    }
} 