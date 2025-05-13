package com.example.controller;

import com.example.dao.PhieuMuonDAO;
import com.example.dao.SachDAO;
import com.example.dao.ThanhVienDAO;
import com.example.model.PhieuMuon;
import com.example.model.Sach;
import com.example.model.ThanhVien;
import com.example.view.MuonSach;

import javax.swing.*;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class MuonSachController {
    private MuonSach view;
    private PhieuMuonDAO phieuMuonDAO;
    private ThanhVienDAO thanhVienDAO;
    private SachDAO sachDAO;
    private boolean successful = false;

    public MuonSachController(MuonSach view) {
        this.view = view;
        this.phieuMuonDAO = new PhieuMuonDAO();
        this.thanhVienDAO = new ThanhVienDAO();
        this.sachDAO = new SachDAO();
    }

    public List<ThanhVien> loadThanhViens() throws SQLException {
        return thanhVienDAO.getAllThanhViens();
    }

    public List<Sach> loadSachs() throws SQLException {
        return sachDAO.getAllSachs();
    }

    public boolean saveLoan() {
        try {
            // Lấy thông tin từ form
            ThanhVien selectedThanhVien = view.getSelectedThanhVien();
            Sach selectedSach = view.getSelectedSach();
            int soLuong = view.getSoLuong();
            Date ngayMuon = new Date(view.getNgayMuon().getTime());
            Date hanTra = new Date(view.getHanTra().getTime());

            // Kiểm tra đầu vào cơ bản
            if (selectedThanhVien == null) {
                JOptionPane.showMessageDialog(view, "Vui lòng chọn thành viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (selectedSach == null) {
                JOptionPane.showMessageDialog(view, "Vui lòng chọn sách!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (soLuong <= 0) {
                JOptionPane.showMessageDialog(view, "Số lượng mượn phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Kiểm tra trạng thái thành viên
            if ("Khóa".equals(selectedThanhVien.getTrangThai()) || "Hết hạn".equals(selectedThanhVien.getTrangThai())) {
                JOptionPane.showMessageDialog(view, "Thành viên đã bị khóa hoặc tài khoản đã hết hạn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Kiểm tra số sách khả dụng
            if (selectedSach.getKhaDung() < soLuong) {
                JOptionPane.showMessageDialog(view, 
                    "Số lượng sách không đủ để mượn! Hiện có " + selectedSach.getKhaDung() + " cuốn khả dụng.", 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Kiểm tra số lượng sách đang mượn
            int soSachDangMuon = phieuMuonDAO.getSoSachDangMuon(selectedThanhVien.getMaThanhVien());
            if ((soSachDangMuon + soLuong) > 5) {
                JOptionPane.showMessageDialog(view, 
                    "Thành viên đã mượn " + soSachDangMuon + " cuốn, không thể mượn thêm " + soLuong + " cuốn nữa! Tối đa là 5 cuốn.", 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Kiểm tra có phiếu quá hạn không
            if (phieuMuonDAO.hasQuaHan(selectedThanhVien.getMaThanhVien())) {
                JOptionPane.showMessageDialog(view, 
                    "Thành viên có sách mượn quá hạn chưa trả, không thể mượn thêm!", 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Kiểm tra ngày mượn - hạn trả
            java.util.Date today = new java.util.Date();
            if (ngayMuon.after(today)) {
                JOptionPane.showMessageDialog(view, "Ngày mượn không được lớn hơn ngày hiện tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (hanTra.before(ngayMuon)) {
                JOptionPane.showMessageDialog(view, "Hạn trả phải lớn hơn hoặc bằng ngày mượn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            String trangThai = "Đang mượn";
            if (hanTra.before(new Date(today.getTime()))) {
                trangThai = "Quá hạn";
                JOptionPane.showMessageDialog(view, 
                    "Hạn trả trong quá khứ, phiếu mượn sẽ được đặt trạng thái Quá hạn!", 
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            }

            // Tạo đối tượng PhieuMuon
            PhieuMuon phieuMuon = new PhieuMuon();
            phieuMuon.setThanhVien(selectedThanhVien);
            phieuMuon.setSach(selectedSach);
            phieuMuon.setSoLuong(soLuong);
            phieuMuon.setNgayMuon(ngayMuon);
            phieuMuon.setHanTra(hanTra);
            phieuMuon.setTrangThai(trangThai);

            // Thêm phiếu mượn vào cơ sở dữ liệu
            phieuMuonDAO.addPhieuMuon(phieuMuon);

            // Cập nhật lại số lượng sách khả dụng
            selectedSach.setKhaDung(selectedSach.getKhaDung() - soLuong);
            sachDAO.updateBook(selectedSach);

            JOptionPane.showMessageDialog(view, "Tạo phiếu mượn thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            successful = true;
            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Lỗi khi tạo phiếu mượn: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean isSuccessful() {
        return successful;
    }
} 