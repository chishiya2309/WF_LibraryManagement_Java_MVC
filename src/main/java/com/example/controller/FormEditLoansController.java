package com.example.controller;

import com.example.dao.PhieuMuonDAO;
import com.example.dao.SachDAO;
import com.example.view.FormEditLoansAndReturns;

import javax.swing.*;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Map;

public class FormEditLoansController {
    private FormEditLoansAndReturns view;
    private PhieuMuonDAO phieuMuonDAO;
    private SachDAO sachDAO;
    private Map<String, Object> originalLoanData;

    public FormEditLoansController(FormEditLoansAndReturns view, Map<String, Object> loanData) {
        this.view = view;
        this.phieuMuonDAO = new PhieuMuonDAO();
        this.sachDAO = new SachDAO();
        this.originalLoanData = loanData;
    }

    public boolean updateLoan(int maPhieu, java.util.Date ngayMuon, java.util.Date hanTra, 
                           java.util.Date ngayTraThucTe, String trangThai, int soLuong) {
        try {
            // Kiểm tra ngày mượn
            if (ngayMuon.after(new java.util.Date())) {
                JOptionPane.showMessageDialog(view,
                        "Ngày mượn không được lớn hơn ngày hiện tại!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Kiểm tra hạn trả
            if (hanTra.before(ngayMuon)) {
                JOptionPane.showMessageDialog(view,
                        "Hạn trả phải lớn hơn hoặc bằng ngày mượn!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Kiểm tra ngày trả thực tế nếu có
            if (ngayTraThucTe != null && ngayTraThucTe.before(ngayMuon)) {
                JOptionPane.showMessageDialog(view,
                        "Ngày trả thực tế phải lớn hơn hoặc bằng ngày mượn!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Kiểm tra trạng thái
            if (!trangThai.equals("Đang mượn") && !trangThai.equals("Đã trả") && !trangThai.equals("Quá hạn")) {
                JOptionPane.showMessageDialog(view,
                        "Trạng thái không hợp lệ! Chỉ chấp nhận: Đang mượn, Đã trả, Quá hạn.",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            // Kiểm tra ngày trả thực tế tương ứng với trạng thái
            if ("Đã trả".equals(trangThai) && ngayTraThucTe == null) {
                JOptionPane.showMessageDialog(view,
                        "Phiếu mượn có trạng thái 'Đã trả' phải có ngày trả thực tế!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (!"Đã trả".equals(trangThai) && ngayTraThucTe != null) {
                JOptionPane.showMessageDialog(view,
                        "Phiếu mượn chưa trả không nên có ngày trả thực tế!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            int oldSoLuong = ((Number)originalLoanData.get("SoLuong")).intValue();
            String maSach = (String)originalLoanData.get("MaSach");
            String maThanhVien = (String)originalLoanData.get("MaThanhVien");
            String oldTrangThai = (String)originalLoanData.get("TrangThai");
            
            System.out.println("DEBUG: oldTrangThai=" + oldTrangThai + ", trangThai=" + trangThai);
            System.out.println("DEBUG: oldSoLuong=" + oldSoLuong + ", soLuong=" + soLuong);
            
            // Kiểm tra số lượng hợp lệ
            if (soLuong <= 0) {
                JOptionPane.showMessageDialog(view,
                        "Số lượng mượn phải lớn hơn 0!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Kiểm tra giới hạn số lượng sách mượn
            if (trangThai.equals("Đang mượn") || trangThai.equals("Quá hạn")) {
                int tongSachDangMuon = 0;
                
                // Chỉ cộng số sách từ các phiếu mượn khác
                if (oldTrangThai.equals("Đang mượn") || oldTrangThai.equals("Quá hạn")) {
                    tongSachDangMuon = phieuMuonDAO.getSoSachDangMuonCuaThanhVien(maThanhVien, maPhieu);
                } else {
                    tongSachDangMuon = phieuMuonDAO.getSoSachDangMuon(maThanhVien);
                }

                if ((tongSachDangMuon + soLuong) > 5) {
                    JOptionPane.showMessageDialog(view,
                            "Thành viên đã mượn " + tongSachDangMuon + 
                            " cuốn, không thể mượn thêm " + soLuong + 
                            " cuốn nữa! Tối đa là 5 cuốn.",
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }

            // Kiểm tra số lượng sách khả dụng nếu tăng số lượng
            if ((trangThai.equals("Đang mượn") || trangThai.equals("Quá hạn")) && 
                soLuong > oldSoLuong) {
                
                int khaDung = sachDAO.getSachKhaDung(maSach);
                if (khaDung < (soLuong - oldSoLuong)) {
                    JOptionPane.showMessageDialog(view,
                            "Số lượng sách không đủ! Hiện có " + khaDung + " cuốn khả dụng.",
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                
                System.out.println("DEBUG: Tăng số lượng khi đang mượn, khả dụng đủ");
            }

            // Chuyển đổi java.util.Date thành java.sql.Date
            Date sqlNgayMuon = new Date(ngayMuon.getTime());
            Date sqlHanTra = new Date(hanTra.getTime());
            Date sqlNgayTraThucTe = ngayTraThucTe != null ? new Date(ngayTraThucTe.getTime()) : null;

            // Cập nhật phiếu mượn
            boolean success = phieuMuonDAO.updatePhieuMuon(maPhieu, sqlNgayMuon, sqlHanTra, 
                                                       sqlNgayTraThucTe, trangThai, soLuong);

            if (success) {
                JOptionPane.showMessageDialog(view,
                        "Cập nhật thông tin phiếu mượn thành công!",
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(view,
                        "Lỗi khi cập nhật thông tin phiếu mượn!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view,
                    "Lỗi SQL: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            return false;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view,
                    "Lỗi: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            return false;
        }
    }
} 