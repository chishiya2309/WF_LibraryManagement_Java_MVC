package com.example.controller;

import com.example.dao.NhanVienDAO;
import com.example.model.NhanVien;
import com.example.view.AdminControlStaff;
import com.example.view.FormAddStaff;
import com.example.view.FormEditStaff;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.List;

public class NhanVienController {
    private AdminControlStaff mainView;
    private NhanVienDAO nhanVienDAO;
    
    public NhanVienController() {
        this.nhanVienDAO = new NhanVienDAO();
    }
    
    public NhanVienController(AdminControlStaff mainView, NhanVienDAO nhanVienDAO) {
        this.mainView = mainView;
        this.nhanVienDAO = nhanVienDAO;
    }
    
    public void loadStaff() throws SQLException {
        List<NhanVien> staffList = nhanVienDAO.getAllNhanVien();
        updateTable(staffList);
    }
    
    private void updateTable(List<NhanVien> staffList) {
        DefaultTableModel model = mainView.getTableModel();
        model.setRowCount(0);
        
        JLabel noDataLabel = mainView.getNoDataLabel();
        if (staffList.isEmpty()) {
            noDataLabel.setVisible(true);
        } else {
            noDataLabel.setVisible(false);
            
            for (NhanVien staff : staffList) {
                model.addRow(new Object[]{
                    staff.getId(),
                    staff.getHoTen(),
                    staff.getGioiTinh(),
                    staff.getChucVu(),
                    staff.getEmail(),
                    staff.getSoDienThoai(),
                    staff.getNgayVaoLam(),
                    staff.getTrangThai()
                });
            }
        }
    }
    
    public void searchNhanVien() {
        String keyword = mainView.getSearchField().getText().trim();
        
        try {
            List<NhanVien> results;
            if (keyword.isEmpty()) {
                results = nhanVienDAO.getAllNhanVien();
            } else {
                results = nhanVienDAO.searchNhanVien(keyword);
            }
            
            updateTable(results);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(mainView,
                    "Lỗi khi tìm kiếm: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void deleteNhanVien() {
        JTable staffTable = mainView.getStaffTable();
        int row = staffTable.getSelectedRow();
        
        if (row == -1) {
            JOptionPane.showMessageDialog(mainView,
                    "Vui lòng chọn nhân viên để xóa",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String staffId = staffTable.getValueAt(row, 0).toString();
        
        int confirm = JOptionPane.showConfirmDialog(mainView,
                "Bạn có chắc chắn muốn xóa nhân viên này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean success = nhanVienDAO.deleteNhanVien(staffId);
                
                if (success) {
                    JOptionPane.showMessageDialog(mainView,
                            "Xóa nhân viên thành công",
                            "Thông báo",
                            JOptionPane.INFORMATION_MESSAGE);
                    loadStaff();
                } else {
                    JOptionPane.showMessageDialog(mainView,
                            "Xóa nhân viên thất bại",
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(mainView,
                        "Lỗi khi xóa nhân viên: " + e.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public void openAddStaffForm() {
        FormAddStaff addStaffForm = new FormAddStaff();
        addStaffForm.setVisible(true);
        
        if (addStaffForm.isSuccessful()) {
            try {
                loadStaff();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(mainView,
                        "Lỗi khi tải lại danh sách nhân viên: " + e.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public boolean isEmailExists(String email) throws SQLException {
        return nhanVienDAO.isEmailExists(email);
    }
    
    public boolean isPhoneExists(String phone) throws SQLException {
        return nhanVienDAO.isPhoneExists(phone);
    }

    public void editNhanVien() {
        int selectedRow = mainView.getStaffTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(mainView,
                    "Vui lòng chọn nhân viên cần chỉnh sửa!",
                    "Cảnh báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String staffId = mainView.getTableModel().getValueAt(selectedRow, 0).toString();
        FormEditStaff editForm = new FormEditStaff(staffId);
        editForm.setVisible(true);
        
        // Nếu chỉnh sửa thành công, cập nhật lại bảng
        if (editForm.isSuccessful()) {
            try {
                loadStaff();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(mainView,
                        "Lỗi khi tải lại danh sách nhân viên: " + e.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
