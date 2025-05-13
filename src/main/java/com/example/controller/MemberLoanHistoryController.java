package com.example.controller;

import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import com.example.dao.MemberLoanHistoryDAO;
import com.example.model.LoanHistoryItem;
import com.example.model.MemberItem;
import com.example.view.AdminControlMemberLoanHistory;

/**
 * Controller cho lịch sử mượn sách của thành viên
 */
public class MemberLoanHistoryController {
    private AdminControlMemberLoanHistory view;
    private MemberLoanHistoryDAO dao;
    
    /**
     * Constructor
     * @param view View lịch sử mượn sách
     */
    public MemberLoanHistoryController(AdminControlMemberLoanHistory view) {
        this.view = view;
        this.dao = new MemberLoanHistoryDAO();
    }
    
    /**
     * Tải danh sách thành viên vào combo box
     * @return true nếu thành công, false nếu có lỗi
     */
    public boolean loadMembers() {
        try {
            List<MemberItem> members = dao.getAllMembers();
            
            if (members == null || members.isEmpty()) {
                JOptionPane.showMessageDialog(view,
                    "Không có dữ liệu thành viên!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
                return false;
            }
            
            view.populateMemberComboBox(members);
            return true;
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view,
                "Lỗi khi tải dữ liệu thành viên: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Tải lịch sử mượn sách của thành viên theo mã thành viên
     * @param memberId Mã thành viên
     */
    public void loadLoanHistory(String memberId) {
        if (memberId == null || memberId.isEmpty()) return;
        
        try {
            // Lấy lịch sử mượn sách từ DAO
            List<LoanHistoryItem> loanHistory = dao.getLoanHistoryByMember(memberId);
            
            // Cập nhật bảng
            updateLoanHistoryTable(loanHistory);
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view,
                "Lỗi khi tải dữ liệu lịch sử mượn sách: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    /**
     * Cập nhật bảng hiển thị lịch sử mượn sách
     * @param loanHistory Danh sách lịch sử mượn sách
     */
    private void updateLoanHistoryTable(List<LoanHistoryItem> loanHistory) {
        DefaultTableModel model = view.getTableModel();
        
        // Xóa dữ liệu cũ
        model.setRowCount(0);
        
        // Kiểm tra dữ liệu trống
        if (loanHistory == null || loanHistory.isEmpty()) {
            view.setNoDataVisible(true);
            return;
        }
        
        view.setNoDataVisible(false);
        
        // Thêm dữ liệu vào bảng
        for (LoanHistoryItem item : loanHistory) {
            model.addRow(new Object[] {
                item.getLoanId(),
                item.getMemberName(),
                item.getLoanDate(),
                item.getDueDate(),
                item.getReturnDate(),
                item.getStatus(),
                item.getBookTitle(),
                item.getQuantity()
            });
        }
    }
} 