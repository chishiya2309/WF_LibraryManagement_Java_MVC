package com.example.controller;

import com.example.dao.ExpiringMemberDAO;
import com.example.model.ExpiringMember;
import com.example.view.AdminControlExpiringMembers;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Controller cho báo cáo thành viên sắp hết hạn
 */
public class ExpiringMemberController {
    private AdminControlExpiringMembers view;
    private ExpiringMemberDAO expiringMemberDAO;
    private int daysThreshold = 30; // Ngưỡng mặc định
    
    /**
     * Constructor với tham số view
     * @param view View hiển thị báo cáo thành viên sắp hết hạn
     */
    public ExpiringMemberController(AdminControlExpiringMembers view) {
        this.view = view;
        this.expiringMemberDAO = new ExpiringMemberDAO();
    }
    
    /**
     * Constructor với tham số view và ngưỡng
     * @param view View hiển thị báo cáo thành viên sắp hết hạn
     * @param daysThreshold Ngưỡng số ngày tính từ hiện tại
     */
    public ExpiringMemberController(AdminControlExpiringMembers view, int daysThreshold) {
        this.view = view;
        this.expiringMemberDAO = new ExpiringMemberDAO();
        this.daysThreshold = daysThreshold;
    }
    
    /**
     * Tải dữ liệu thành viên sắp hết hạn
     */
    public void loadData() {
        System.out.println("ExpiringMemberController.loadData() được gọi với ngưỡng: " + daysThreshold + " ngày");
        try {
            List<ExpiringMember> expiringMembers = expiringMemberDAO.getExpiringMembers(daysThreshold);
            System.out.println("Đã tìm thấy " + expiringMembers.size() + " thành viên sắp hết hạn trong " + daysThreshold + " ngày tới");
            
            if (expiringMembers.isEmpty()) {
                System.out.println("Không có thành viên nào sắp hết hạn trong " + daysThreshold + " ngày tới");
            } else {
                System.out.println("Danh sách thành viên sắp hết hạn:");
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                for (ExpiringMember member : expiringMembers) {
                    System.out.println("  - " + member.getHoTen() + " (Mã: " + member.getMaThanhVien() + 
                                      ", Hết hạn: " + sdf.format(member.getNgayHetHan()) + ")");
                }
            }
            
            displayExpiringMembers(expiringMembers);
        } catch (SQLException ex) {
            System.err.println("Lỗi SQL khi tải dữ liệu: " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view,
                    "Lỗi khi tải dữ liệu: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            System.err.println("Lỗi không xác định: " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view,
                    "Lỗi không xác định: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Đặt lại ngưỡng số ngày và tải lại dữ liệu
     * @param daysThreshold Ngưỡng mới
     */
    public void setDaysThreshold(int daysThreshold) {
        this.daysThreshold = daysThreshold;
        loadData();
    }
    
    /**
     * Hiển thị danh sách thành viên sắp hết hạn lên bảng
     * @param expiringMembers Danh sách thành viên sắp hết hạn
     */
    private void displayExpiringMembers(List<ExpiringMember> expiringMembers) {
        DefaultTableModel tableModel = view.getTableModel();
        tableModel.setRowCount(0);
        
        boolean isEmpty = expiringMembers.isEmpty();
        System.out.println("Hiển thị danh sách thành viên: " + (isEmpty ? "Trống" : "Có dữ liệu"));
        
        // Đặt trạng thái hiển thị cho thông báo và bảng
        view.setNoDataVisible(isEmpty);
        view.setTableVisible(!isEmpty);
        
        if (!isEmpty) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            for (ExpiringMember member : expiringMembers) {
                Object[] row = {
                        member.getMaThanhVien(),
                        member.getHoTen(),
                        member.getGioiTinh(),
                        member.getSoDienThoai(),
                        member.getEmail(),
                        member.getLoaiThanhVien(),
                        sdf.format(member.getNgayDangKy()),
                        sdf.format(member.getNgayHetHan()),
                        member.getTrangThai()
                };
                
                tableModel.addRow(row);
            }
            System.out.println("Đã thêm " + expiringMembers.size() + " dòng vào bảng");
        }
    }
    
    /**
     * Xuất báo cáo ra file Excel hoặc PDF (chức năng mở rộng)
     * @param filePath Đường dẫn file xuất
     * @param fileType Loại file (Excel/PDF)
     * @return Trạng thái xuất file
     */
    public boolean exportReport(String filePath, String fileType) {
        // TODO: Implement export functionality
        return false;
    }
    
    /**
     * Lấy tổng số thành viên sắp hết hạn
     * @return Tổng số thành viên sắp hết hạn
     */
    public int getTotalExpiringMembers() {
        try {
            return expiringMemberDAO.countExpiringMembers(daysThreshold);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return 0;
        }
    }
} 