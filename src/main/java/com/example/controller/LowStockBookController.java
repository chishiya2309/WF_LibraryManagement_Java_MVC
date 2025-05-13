package com.example.controller;

import com.example.dao.LowStockBookDAO;
import com.example.model.LowStockBook;
import com.example.view.AdminControlLowStockBooks;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller cho báo cáo sách có số lượng khả dụng thấp
 */
public class LowStockBookController {
    private AdminControlLowStockBooks view;
    private LowStockBookDAO lowStockBookDAO;
    private int threshold = 3; // Ngưỡng mặc định
    
    /**
     * Constructor với tham số view
     * @param view View hiển thị báo cáo sách khả dụng thấp
     */
    public LowStockBookController(AdminControlLowStockBooks view) {
        this.view = view;
        this.lowStockBookDAO = new LowStockBookDAO();
    }
    
    /**
     * Constructor với tham số view và ngưỡng
     * @param view View hiển thị báo cáo sách khả dụng thấp
     * @param threshold Ngưỡng số lượng khả dụng
     */
    public LowStockBookController(AdminControlLowStockBooks view, int threshold) {
        this.view = view;
        this.lowStockBookDAO = new LowStockBookDAO();
        this.threshold = threshold;
    }
    
    /**
     * Tải dữ liệu sách có số lượng khả dụng thấp
     */
    public void loadData() {
        System.out.println("LowStockBookController.loadData() được gọi với ngưỡng: " + threshold);
        try {
            List<LowStockBook> lowStockBooks = lowStockBookDAO.getLowStockBooks(threshold);
            System.out.println("Đã tìm thấy " + lowStockBooks.size() + " sách có số lượng khả dụng thấp");
            
            if (lowStockBooks.isEmpty()) {
                System.out.println("Không có sách nào có số lượng khả dụng thấp hơn " + threshold);
            } else {
                System.out.println("Danh sách sách có số lượng khả dụng thấp:");
                for (LowStockBook book : lowStockBooks) {
                    System.out.println("  - " + book.getTenSach() + " (Mã: " + book.getMaSach() + ", Khả dụng: " + book.getKhaDung() + ")");
                }
            }
            
            displayLowStockBooks(lowStockBooks);
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
     * Đặt lại ngưỡng số lượng khả dụng và tải lại dữ liệu
     * @param threshold Ngưỡng mới
     */
    public void setThreshold(int threshold) {
        this.threshold = threshold;
        loadData();
    }
    
    /**
     * Hiển thị danh sách sách có số lượng khả dụng thấp lên bảng
     * @param lowStockBooks Danh sách sách có số lượng khả dụng thấp
     */
    private void displayLowStockBooks(List<LowStockBook> lowStockBooks) {
        DefaultTableModel tableModel = view.getTableModel();
        tableModel.setRowCount(0);
        
        boolean isEmpty = lowStockBooks.isEmpty();
        System.out.println("Hiển thị danh sách sách: " + (isEmpty ? "Trống" : "Có dữ liệu"));
        
        // Đặt trạng thái hiển thị cho thông báo và bảng
        view.setNoDataVisible(isEmpty);
        view.setTableVisible(!isEmpty);
        
        if (!isEmpty) {
            for (LowStockBook book : lowStockBooks) {
                Object[] row = {
                        book.getMaSach(),
                        book.getIsbn(),
                        book.getTenSach(),
                        book.getTacGia(),
                        book.getTenDanhMuc(),
                        book.getNamXuatBan(),
                        book.getNxb(),
                        book.getSoBan(),
                        book.getKhaDung(),
                        book.getViTri()
                };
                
                tableModel.addRow(row);
            }
            System.out.println("Đã thêm " + lowStockBooks.size() + " dòng vào bảng");
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
     * Lấy tổng số đầu sách có số lượng khả dụng thấp
     * @return Tổng số đầu sách có số lượng khả dụng thấp
     */
    public int getTotalLowStockBooks() {
        try {
            return lowStockBookDAO.countLowStockBooks(threshold);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return 0;
        }
    }
} 