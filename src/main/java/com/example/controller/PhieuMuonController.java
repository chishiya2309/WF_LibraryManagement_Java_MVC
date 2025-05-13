package com.example.controller;

import com.example.dao.PhieuMuonDAO;
import com.example.dao.SachDAO;
import com.example.dao.ThanhVienDAO;
import com.example.model.PhieuMuon;
import com.example.model.Sach;
import com.example.model.ThanhVien;
import com.example.view.AdminControlLoanAndReturn;
import com.example.view.MuonSach;
import com.example.view.TraSach;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

public class PhieuMuonController {
    private AdminControlLoanAndReturn view;
    private PhieuMuonDAO phieuMuonDAO;
    private ThanhVienDAO thanhVienDAO;
    private SachDAO sachDAO;

    public PhieuMuonController(AdminControlLoanAndReturn view, PhieuMuonDAO phieuMuonDAO) {
        this.view = view;
        this.phieuMuonDAO = phieuMuonDAO;
        this.thanhVienDAO = new ThanhVienDAO();
        this.sachDAO = new SachDAO();
    }

    public void loadPhieuMuons() throws SQLException {
        List<PhieuMuon> phieuMuons = phieuMuonDAO.getAllPhieuMuons();
        displayPhieuMuons(phieuMuons);
    }

    private void displayPhieuMuons(List<PhieuMuon> phieuMuons) {
        // Xóa dữ liệu cũ trong bảng
        DefaultTableModel tableModel = view.getTableModel();
        tableModel.setRowCount(0);

        // Định dạng ngày tháng
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        // Thêm dữ liệu mới vào bảng
        for (PhieuMuon phieuMuon : phieuMuons) {
            tableModel.addRow(new Object[]{
                phieuMuon.getMaPhieu(),
                phieuMuon.getThanhVien().getHoTen(),
                phieuMuon.getNgayMuon() != null ? dateFormat.format(phieuMuon.getNgayMuon()) : "",
                phieuMuon.getHanTra() != null ? dateFormat.format(phieuMuon.getHanTra()) : "",
                phieuMuon.getNgayTraThucTe() != null ? dateFormat.format(phieuMuon.getNgayTraThucTe()) : "",
                phieuMuon.getTrangThai(),
                phieuMuon.getSach().getTenSach(),
                phieuMuon.getSoLuong()
            });
        }

        // Hiển thị "Không tìm thấy kết quả" nếu không có dữ liệu
        view.getNoDataLabel().setVisible(phieuMuons.isEmpty());
        view.getLoanReturnTable().setVisible(!phieuMuons.isEmpty());
    }

    public void searchPhieuMuons(String keyword) throws SQLException {
        List<PhieuMuon> phieuMuons = phieuMuonDAO.searchPhieuMuons(keyword);
        displayPhieuMuons(phieuMuons);
    }

    public void showLoanForm() {
        // Hiển thị form mượn sách
        MuonSach muonSachForm = new MuonSach((Frame) SwingUtilities.getWindowAncestor(view));
        muonSachForm.setVisible(true);
        
        // Nếu đã tạo phiếu mượn thành công, tải lại dữ liệu
        if (muonSachForm.isSuccessful()) {
            try {
                loadPhieuMuons();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(view,
                        "Lỗi khi tải lại dữ liệu phiếu mượn: " + e.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void showReturnForm() {
        int selectedRow = view.getLoanReturnTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view,
                    "Vui lòng chọn phiếu mượn cần trả sách!",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Lấy ID của phiếu mượn đã chọn
        int maPhieu = (int) view.getTableModel().getValueAt(selectedRow, 0);
        
        try {
            // Lấy thông tin phiếu mượn
            PhieuMuon phieuMuon = phieuMuonDAO.getPhieuMuonById(maPhieu);
            
            if (phieuMuon == null) {
                JOptionPane.showMessageDialog(view,
                        "Không tìm thấy thông tin phiếu mượn!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Kiểm tra nếu phiếu mượn đã trả trước đó
            if ("Đã trả".equals(phieuMuon.getTrangThai())) {
                JOptionPane.showMessageDialog(view,
                        "Phiếu mượn này đã được trả trước đó!",
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // Hiển thị form trả sách
            Frame owner = (Frame) SwingUtilities.getWindowAncestor(view);
            TraSach traSachForm = new TraSach(owner, phieuMuon);
            traSachForm.setVisible(true);
            
            // Nếu đã trả sách thành công, tải lại dữ liệu
            if (traSachForm.isSuccessful()) {
                try {
                    loadPhieuMuons();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(view,
                            "Lỗi khi tải lại dữ liệu phiếu mượn: " + e.getMessage(),
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view,
                    "Lỗi khi lấy thông tin phiếu mượn: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void editSelectedLoan() {
        int selectedRow = view.getLoanReturnTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view,
                    "Vui lòng chọn phiếu mượn cần chỉnh sửa!",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Lấy ID của phiếu mượn đã chọn
        int maPhieu = (int) view.getTableModel().getValueAt(selectedRow, 0);

        // Hiển thị form chỉnh sửa phiếu mượn
        JOptionPane.showMessageDialog(view,
                "Chức năng chỉnh sửa phiếu mượn sẽ được triển khai sau. Mã phiếu: " + maPhieu,
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
    }
} 