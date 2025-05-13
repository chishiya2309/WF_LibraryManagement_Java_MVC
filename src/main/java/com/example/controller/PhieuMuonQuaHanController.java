package com.example.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import com.example.dao.PhieuMuonDAO;
import com.example.dao.SachDAO;
import com.example.dao.ThanhVienDAO;
import com.example.model.PhieuMuon;
import com.example.model.Sach;
import com.example.model.ThanhVien;
import com.example.view.AdminControlPhieuMuonQuaHan;

/**
 * Controller cho quản lý phiếu mượn quá hạn
 */
public class PhieuMuonQuaHanController {
    private AdminControlPhieuMuonQuaHan view;
    private PhieuMuonDAO phieuMuonDAO;
    private ThanhVienDAO thanhVienDAO;
    private SachDAO sachDAO;
    
    // Dữ liệu
    private List<PhieuMuon> phieuMuonQuaHan;
    private List<ThanhVien> thanhViens;
    private List<Sach> sachs;
    
    // Maps để tra cứu nhanh
    private Map<String, String> thanhVienNameMap;
    private Map<String, String> sachTitleMap;
    
    /**
     * Constructor
     * @param view View hiển thị phiếu mượn quá hạn
     */
    public PhieuMuonQuaHanController(AdminControlPhieuMuonQuaHan view) {
        this.view = view;
        this.phieuMuonDAO = new PhieuMuonDAO();
        this.thanhVienDAO = new ThanhVienDAO();
        this.sachDAO = new SachDAO();
        
        // Khởi tạo maps tra cứu
        thanhVienNameMap = new HashMap<>();
        sachTitleMap = new HashMap<>();
    }
    
    /**
     * Tải dữ liệu phiếu mượn quá hạn
     */
    public void loadData() {
        try {
            // Tải danh sách thành viên
            thanhViens = thanhVienDAO.getAllThanhViens();
            for (ThanhVien tv : thanhViens) {
                thanhVienNameMap.put(tv.getMaThanhVien(), tv.getHoTen());
            }
            
            // Tải danh sách sách
            sachs = sachDAO.getAllSachs();
            for (Sach sach : sachs) {
                sachTitleMap.put(sach.getMaSach(), sach.getTenSach());
            }
            
            // Tải danh sách phiếu mượn quá hạn
            phieuMuonQuaHan = phieuMuonDAO.getPhieuMuonQuaHan();
            
            // Cập nhật dữ liệu vào bảng
            updateTableData();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view,
                "Lỗi khi tải dữ liệu: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    /**
     * Cập nhật dữ liệu vào bảng
     */
    private void updateTableData() {
        DefaultTableModel tableModel = view.getTableModel();
        
        // Xóa dữ liệu cũ
        tableModel.setRowCount(0);
        
        // Kiểm tra nếu không có dữ liệu
        if (phieuMuonQuaHan == null || phieuMuonQuaHan.isEmpty()) {
            view.setNoDataVisible(true);
            view.setTableVisible(false);
            return;
        }
        
        // Có dữ liệu, hiển thị bảng
        view.setNoDataVisible(false);
        view.setTableVisible(true);
        
        // Thêm dữ liệu phiếu mượn quá hạn vào bảng
        for (PhieuMuon pm : phieuMuonQuaHan) {
            // Lấy tên thành viên và tên sách từ maps tra cứu
            String tenThanhVien = "";
            if (pm.getThanhVien() != null) {
                tenThanhVien = thanhVienNameMap.getOrDefault(
                    pm.getThanhVien().getMaThanhVien(), 
                    pm.getThanhVien().getHoTen() // Lấy tên trực tiếp từ đối tượng đã có nếu không tìm thấy trong map
                );
            }
            
            String tenSach = "";
            if (pm.getSach() != null) {
                tenSach = sachTitleMap.getOrDefault(
                    pm.getSach().getMaSach(), 
                    pm.getSach().getTenSach() // Lấy tên trực tiếp từ đối tượng đã có nếu không tìm thấy trong map
                );
            }
            
            Object[] row = {
                pm.getMaPhieu(),
                tenThanhVien,
                pm.getNgayMuon(),
                pm.getHanTra(),
                pm.getNgayTraThucTe(),
                pm.getTrangThai(),
                tenSach,
                pm.getSoLuong()
            };
            
            tableModel.addRow(row);
        }
    }
    
    /**
     * Làm mới dữ liệu
     */
    public void refreshData() {
        loadData();
    }
} 