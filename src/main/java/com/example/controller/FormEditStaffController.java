package com.example.controller;

import com.example.dao.NhanVienDAO;
import com.example.model.NhanVien;
import com.example.view.FormEditStaff;

import javax.swing.*;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Map;
import java.util.regex.Pattern;

public class FormEditStaffController {
    private FormEditStaff view;
    private NhanVienDAO nhanVienDAO;
    private boolean successful = false;

    public FormEditStaffController(FormEditStaff view, NhanVienDAO nhanVienDAO) {
        this.view = view;
        this.nhanVienDAO = nhanVienDAO;
    }

    public void loadStaffData(String staffId) {
        try {
            NhanVien nhanVien = nhanVienDAO.getNhanVienById(staffId);
            if (nhanVien != null) {
                // Đổ dữ liệu vào form
                view.getIdField().setText(nhanVien.getId());
                view.getNameField().setText(nhanVien.getHoTen());
                view.getGenderComboBox().setSelectedItem(nhanVien.getGioiTinh());
                view.getPositionComboBox().setSelectedItem(nhanVien.getChucVu());
                view.getEmailField().setText(nhanVien.getEmail());
                view.getPhoneField().setText(nhanVien.getSoDienThoai());
                view.getStartDateSpinner().setValue(nhanVien.getNgayVaoLam());
                view.getStatusComboBox().setSelectedItem(nhanVien.getTrangThai());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view,
                    "Lỗi khi tải thông tin nhân viên: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void saveStaff() {
        if (!validateInputs()) {
            return;
        }

        try {
            String staffId = view.getIdField().getText().trim();
            String name = view.getNameField().getText().trim();
            String gender = view.getGenderComboBox().getSelectedItem().toString();
            String position = view.getPositionComboBox().getSelectedItem().toString();
            String email = view.getEmailField().getText().trim();
            String phone = view.getPhoneField().getText().trim();
            java.util.Date startDateUtil = (java.util.Date) view.getStartDateSpinner().getValue();
            Date startDate = new Date(startDateUtil.getTime());
            String status = view.getStatusComboBox().getSelectedItem().toString();

            // Kiểm tra xem email đã tồn tại với nhân viên khác chưa
            NhanVien existingWithEmail = nhanVienDAO.getNhanVienById(staffId);
            if (existingWithEmail != null && !existingWithEmail.getId().equals(staffId) && 
                nhanVienDAO.isEmailExists(email)) {
                JOptionPane.showMessageDialog(view,
                        "Email đã được sử dụng bởi nhân viên khác. Vui lòng sử dụng email khác.",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                view.getEmailField().requestFocus();
                return;
            }

            // Kiểm tra xem số điện thoại đã tồn tại với nhân viên khác chưa
            if (existingWithEmail != null && !existingWithEmail.getId().equals(staffId) && 
                nhanVienDAO.isPhoneExists(phone)) {
                JOptionPane.showMessageDialog(view,
                        "Số điện thoại đã được sử dụng bởi nhân viên khác. Vui lòng sử dụng số điện thoại khác.",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                view.getPhoneField().requestFocus();
                return;
            }

            // Tạo đối tượng NhanVien mới
            NhanVien nhanVien = new NhanVien(staffId, name, gender, position, email, phone, startDate, status);

            // Cập nhật nhân viên vào database
            nhanVienDAO.updateNhanVien(nhanVien);

            JOptionPane.showMessageDialog(view,
                    "Cập nhật thông tin nhân viên thành công!",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            successful = true;
            view.dispose();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view,
                    "Lỗi khi cập nhật thông tin nhân viên: " + ex.getMessage(),
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

    private boolean validateInputs() {
        // Validate ID
        if (view.getIdField().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "ID nhân viên không được để trống!",
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