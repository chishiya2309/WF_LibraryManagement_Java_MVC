package com.example.controller;

import com.example.dao.PhieuMuonDAO;
import com.example.model.PhieuMuon;
import com.example.view.TraSach;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.sql.SQLException;

public class TraSachController {
    private TraSach view;
    private PhieuMuonDAO phieuMuonDAO;
    private PhieuMuon currentLoan;

    public TraSachController(TraSach view, PhieuMuon loan) {
        this.view = view;
        this.phieuMuonDAO = new PhieuMuonDAO();
        this.currentLoan = loan;
    }

    /**
     * Kiểm tra phiếu mượn có thể trả hay không
     * @return true nếu có thể trả sách, false nếu không
     */
    public boolean checkLoanStatus() {
        // Nếu phiếu mượn đã trả rồi thì không cho phép trả nữa
        if ("Đã trả".equals(currentLoan.getTrangThai())) {
            JOptionPane.showMessageDialog(view,
                    "Sách này đã được trả trước đó!",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * Xử lý việc trả sách
     * @param returnDate ngày trả sách
     * @return true nếu trả sách thành công, false nếu thất bại
     */
    public boolean returnBook(java.util.Date returnDate) {
        try {
            // Kiểm tra ngày trả
            if (returnDate == null) {
                JOptionPane.showMessageDialog(view,
                        "Vui lòng chọn ngày trả sách!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            // Kiểm tra ngày trả có phải sau ngày mượn không
            if (returnDate.before(currentLoan.getNgayMuon())) {
                JOptionPane.showMessageDialog(view,
                        "Ngày trả phải sau ngày mượn sách!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Chuyển đổi từ java.util.Date sang java.sql.Date
            Date sqlReturnDate = new Date(returnDate.getTime());

            // Gọi phương thức trả sách từ DAO
            phieuMuonDAO.traSach(currentLoan.getMaPhieu(), sqlReturnDate);

            JOptionPane.showMessageDialog(view,
                    "Ghi nhận trả sách thành công!",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            
            return true;
        } catch (SQLException ex) {
            String errorMessage = "Lỗi khi ghi nhận trả sách: ";
            
            // Thêm thông tin cụ thể về lỗi
            if (ex.getMessage().contains("No operations allowed after connection closed")) {
                errorMessage += "Kết nối đã đóng. Vui lòng thử lại.";
            } else if (ex.getMessage().contains("Phiếu mượn không tồn tại")) {
                errorMessage += "Phiếu mượn không tồn tại.";
            } else if (ex.getMessage().contains("Phiếu mượn này đã được trả trước đó")) {
                errorMessage += "Phiếu mượn này đã được trả trước đó.";
            } else if (ex.getMessage().contains("Ngày trả thực tế phải lớn hơn")) {
                errorMessage += "Ngày trả phải sau ngày mượn sách.";
            } else {
                errorMessage += ex.getMessage();
            }
            
            JOptionPane.showMessageDialog(view,
                    errorMessage,
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            return false;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view,
                    "Lỗi không xác định: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            return false;
        }
    }
} 