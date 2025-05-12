package com.example.controller;

import com.example.dao.ThanhVienDAO;
import com.example.model.ThanhVien;
import com.example.view.FormEditMember;

import javax.swing.*;
import java.sql.Date;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class FormEditMemberController {
    private FormEditMember view;
    private ThanhVienDAO thanhVienDAO;
    private ThanhVien thanhVien;
    private boolean successful = false;

    public FormEditMemberController(FormEditMember view, ThanhVienDAO thanhVienDAO) {
        this.view = view;
        this.thanhVienDAO = thanhVienDAO;
    }

    public void loadMemberData(String maThanhVien) {
        try {
            thanhVien = thanhVienDAO.getThanhVienByMaThanhVien(maThanhVien);

            if (thanhVien == null) {
                JOptionPane.showMessageDialog(view,
                        "Không tìm thấy thành viên cần chỉnh sửa!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                view.dispose();
                return;
            }

            // Populate form fields
            view.getTxtMaThanhVien().setText(thanhVien.getMaThanhVien());
            view.getTxtHoTen().setText(thanhVien.getHoTen());

            String gioiTinh = thanhVien.getGioiTinh();
            view.getCmbGioiTinh().setSelectedIndex(gioiTinh.equals("Nữ") ? 1 : 0);

            view.getTxtSoDienThoai().setText(thanhVien.getSoDienThoai());
            view.getTxtEmail().setText(thanhVien.getEmail());

            // Set member type
            String loaiThanhVien = thanhVien.getLoaiThanhVien();
            if (loaiThanhVien.equals("Sinh viên")) {
                view.getCmbLoaiThanhVien().setSelectedIndex(0);
            } else if (loaiThanhVien.equals("Giảng viên")) {
                view.getCmbLoaiThanhVien().setSelectedIndex(1);
            } else if (loaiThanhVien.equals("Thường")) {
                view.getCmbLoaiThanhVien().setSelectedIndex(2);
            }

            // Set dates
            if (thanhVien.getNgayDangKy() != null) {
                view.getDtpNgayDangKy().setValue(thanhVien.getNgayDangKy());
            }

            if (thanhVien.getNgayHetHan() != null) {
                view.getDtpNgayHetHan().setValue(thanhVien.getNgayHetHan());
            }

            // Set status
            String trangThai = thanhVien.getTrangThai();
            switch (trangThai) {
                case "Hoạt động":
                    view.getCmbTrangThai().setSelectedIndex(0);
                    break;
                case "Hết hạn":
                    view.getCmbTrangThai().setSelectedIndex(1);
                    break;
                case "Khóa":
                    view.getCmbTrangThai().setSelectedIndex(2);
                    break;
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view,
                    "Lỗi khi tải thông tin thành viên: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            view.dispose();
        }
    }

    public void saveMember() {
        if (!validateInputs()) {
            return;
        }

        try {
            // Lấy dữ liệu từ form
            String maThanhVien = view.getTxtMaThanhVien().getText().trim();
            String hoTen = view.getTxtHoTen().getText().trim();
            String gioiTinh = view.getCmbGioiTinh().getSelectedItem().toString();
            String soDienThoai = view.getTxtSoDienThoai().getText().trim();
            String email = view.getTxtEmail().getText().trim();
            String loaiThanhVien = view.getCmbLoaiThanhVien().getSelectedItem().toString();
            Date ngayDangKy = new Date(((java.util.Date) view.getDtpNgayDangKy().getValue()).getTime());
            Date ngayHetHan = new Date(((java.util.Date) view.getDtpNgayHetHan().getValue()).getTime());
            String trangThai = view.getCmbTrangThai().getSelectedItem().toString();

            // Cập nhật đối tượng ThanhVien
            thanhVien.setHoTen(hoTen);
            thanhVien.setGioiTinh(gioiTinh);
            thanhVien.setSoDienThoai(soDienThoai);
            thanhVien.setEmail(email);
            thanhVien.setLoaiThanhVien(loaiThanhVien);
            thanhVien.setNgayDangKy(ngayDangKy);
            thanhVien.setNgayHetHan(ngayHetHan);
            thanhVien.setTrangThai(trangThai);

            // Cập nhật vào database
            thanhVienDAO.updateMember(thanhVien);

            JOptionPane.showMessageDialog(view,
                    "Cập nhật thông tin thành viên thành công!",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            
            successful = true;
            view.dispose();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view,
                    "Lỗi khi cập nhật thông tin thành viên: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateInputs() {
        // Validate name
        if (view.getTxtHoTen().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "Vui lòng nhập họ tên thành viên!",
                    "Lỗi",
                    JOptionPane.WARNING_MESSAGE);
            view.getTxtHoTen().requestFocus();
            return false;
        }

        // Validate phone number
        String soDienThoai = view.getTxtSoDienThoai().getText().trim();
        if (soDienThoai.isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "Vui lòng nhập số điện thoại!",
                    "Lỗi",
                    JOptionPane.WARNING_MESSAGE);
            view.getTxtSoDienThoai().requestFocus();
            return false;
        }

        // Validate phone number format
        String phonePattern = "^0\\d{9,10}$";
        if (!Pattern.matches(phonePattern, soDienThoai)) {
            JOptionPane.showMessageDialog(view,
                    "Số điện thoại không hợp lệ! Số điện thoại phải bắt đầu bằng số 0 và có 10-11 chữ số.",
                    "Lỗi",
                    JOptionPane.WARNING_MESSAGE);
            view.getTxtSoDienThoai().requestFocus();
            return false;
        }

        // Kiểm tra số điện thoại đã tồn tại chưa (nếu đã thay đổi)
        try {
            if (!soDienThoai.equals(thanhVien.getSoDienThoai()) && thanhVienDAO.isPhoneExists(soDienThoai)) {
                JOptionPane.showMessageDialog(view,
                        "Số điện thoại này đã được sử dụng bởi thành viên khác!",
                        "Lỗi",
                        JOptionPane.WARNING_MESSAGE);
                view.getTxtSoDienThoai().requestFocus();
                return false;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view,
                    "Lỗi khi kiểm tra số điện thoại: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate email
        String email = view.getTxtEmail().getText().trim();
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "Vui lòng nhập địa chỉ email!",
                    "Lỗi",
                    JOptionPane.WARNING_MESSAGE);
            view.getTxtEmail().requestFocus();
            return false;
        }

        // Validate email format
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (!Pattern.matches(emailPattern, email)) {
            JOptionPane.showMessageDialog(view,
                    "Địa chỉ email không hợp lệ!",
                    "Lỗi",
                    JOptionPane.WARNING_MESSAGE);
            view.getTxtEmail().requestFocus();
            return false;
        }

        // Kiểm tra email đã tồn tại chưa (nếu đã thay đổi)
        try {
            if (!email.equals(thanhVien.getEmail()) && thanhVienDAO.isEmailExists(email)) {
                JOptionPane.showMessageDialog(view,
                        "Email này đã được sử dụng bởi thành viên khác!",
                        "Lỗi",
                        JOptionPane.WARNING_MESSAGE);
                view.getTxtEmail().requestFocus();
                return false;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view,
                    "Lỗi khi kiểm tra email: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate expiration date
        java.util.Date ngayDangKy = (java.util.Date) view.getDtpNgayDangKy().getValue();
        java.util.Date ngayHetHan = (java.util.Date) view.getDtpNgayHetHan().getValue();
        if (ngayHetHan.before(ngayDangKy)) {
            JOptionPane.showMessageDialog(view,
                    "Ngày hết hạn không được nhỏ hơn ngày đăng ký!",
                    "Lỗi",
                    JOptionPane.WARNING_MESSAGE);
            view.getDtpNgayHetHan().requestFocus();
            return false;
        }
        return true;
    }

    public boolean isSuccessful() {
        return successful;
    }
} 