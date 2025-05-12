package com.example.controller;

import com.example.dao.NhanVienDAO;
import com.example.model.NhanVien;
import com.example.view.AdminControlStaff;
import com.example.view.FormAddStaff;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.List;

import javax.swing.table.DefaultTableModel;

public class NhanVienController {
    private AdminControlStaff mainView;
    private NhanVienDAO nhanVienDAO;

    public NhanVienController() {
        this.nhanVienDAO = new NhanVienDAO();
    }

    public NhanVienController(AdminControlStaff mainView, NhanVienDAO nhanVienDAO) {
        this.mainView = mainView;
        this.nhanVienDAO = nhanVienDAO;
        
        // Đăng ký các listener cho các sự kiện tìm kiếm
        initSearchListeners();
        
        // Đăng ký listener cho nút thêm nhân viên
        initAddStaffListener();
        
        // Đăng ký listener cho nút chỉnh sửa nhân viên
        initEditStaffListener();
        
        // Đăng ký listener cho nút xóa nhân viên
        initDeleteStaffListener();
        
        // Đăng ký listener cho nút reload
        initReloadListener();
        
        // Đăng ký listener cho context menu
        initContextMenuListeners();
        
        // Đăng ký listener cho bảng (double-click)
        initTableListeners();
        
        // Đăng ký listener cho resize event
        initResizeListener();
    }
    
    private void initSearchListeners() {
        // Thêm listener cho nút tìm kiếm
        mainView.getSearchButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });
        
        // Thêm listener cho ô tìm kiếm (Enter key)
        mainView.getSearchField().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performSearch();
                }
            }
        });
    }
    
    private void initAddStaffListener() {
        // Thêm listener cho nút thêm nhân viên
        mainView.getAddStaffButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStaff();
            }
        });
    }
    
    private void initEditStaffListener() {
        // Thêm listener cho nút chỉnh sửa nhân viên
        mainView.getEditStaffButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editStaff();
            }
        });
    }
    
    private void initDeleteStaffListener() {
        // Thêm listener cho nút xóa nhân viên
        mainView.getDeleteStaffButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteStaff();
            }
        });
    }
    
    private void initReloadListener() {
        // Thêm listener cho nút reload
        mainView.getReloadButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    loadStaff();
                    mainView.getSearchField().setText("");
                    JOptionPane.showMessageDialog(mainView, "Dữ liệu đã được tải lại.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(mainView,
                            "Lỗi khi tải dữ liệu nhân viên: " + ex.getMessage(),
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    
    private void initContextMenuListeners() {
        // Thêm listener cho menu item thêm trong context menu
        mainView.getAddMenuItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStaff();
            }
        });
        
        // Thêm listener cho menu item chỉnh sửa trong context menu
        mainView.getEditMenuItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editStaff();
            }
        });
        
        // Thêm listener cho menu item xóa trong context menu
        mainView.getDeleteMenuItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteStaff();
            }
        });
    }
    
    private void initTableListeners() {
        // Thêm mouse listener cho bảng để xử lý double-click và context menu
        mainView.getStaffTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Show context menu on right click
                if (e.isPopupTrigger()) {
                    showContextMenu(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // Show context menu on right click
                if (e.isPopupTrigger()) {
                    showContextMenu(e);
                }
            }

            private void showContextMenu(MouseEvent e) {
                JTable table = mainView.getStaffTable();
                int row = table.rowAtPoint(e.getPoint());
                if (row >= 0 && row < table.getRowCount()) {
                    table.setRowSelectionInterval(row, row);
                    mainView.getContextMenu().show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }
    
    private void initResizeListener() {
        // Thêm component listener để điều chỉnh kích thước bảng khi panel được resize
        mainView.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Điều chỉnh kích thước bảng khi panel được resize
                mainView.getTableScrollPane().setPreferredSize(new Dimension(
                        mainView.getWidth() - 40,
                        mainView.getHeight() - 200
                ));
                mainView.revalidate();
            }
        });
    }
    
    private void performSearch() {
        String searchTerm = mainView.getSearchField().getText().trim();
        
        if (searchTerm.isEmpty()) {
            try {
                loadStaff();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(mainView,
                        "Lỗi khi tải dữ liệu nhân viên: " + ex.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
            return;
        }
        
        try {
            // Sử dụng phương thức searchNhanVien từ DAO để tìm kiếm trong database
            List<NhanVien> searchResults = nhanVienDAO.searchNhanVien(searchTerm);
            
            // Hiển thị kết quả tìm kiếm
            displayStaff(searchResults);
            
            // Show/hide no data label
            mainView.getNoDataLabel().setVisible(searchResults.isEmpty());
            mainView.getStaffTable().setVisible(!searchResults.isEmpty());
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(mainView,
                    "Lỗi khi tìm kiếm nhân viên: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void loadStaff() throws SQLException {
        List<NhanVien> staffList = nhanVienDAO.getAllNhanVien();
        displayStaff(staffList);
        
        // Show/hide no data label
        mainView.getNoDataLabel().setVisible(staffList.isEmpty());
        mainView.getStaffTable().setVisible(!staffList.isEmpty());
    }
    
    private void displayStaff(List<NhanVien> staffList) {
        // Xóa dữ liệu cũ
        mainView.getTableModel().setRowCount(0);
        
        // Định dạng ngày tháng DD/MM/YYYY
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd/MM/yyyy");
        
        // Thêm dữ liệu mới vào bảng
        for (NhanVien staff : staffList) {
            mainView.getTableModel().addRow(new Object[]{
                staff.getId(),
                staff.getHoTen(),
                staff.getGioiTinh(),
                staff.getChucVu(),
                staff.getEmail(),
                staff.getSoDienThoai(),
                dateFormat.format(staff.getNgayVaoLam()),
                staff.getTrangThai()
            });
        }
    }
    
    private void addStaff() {
        // Mở form thêm nhân viên mới
        FormAddStaff formAddStaff = new FormAddStaff();
        formAddStaff.setVisible(true);
        formAddStaff.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (formAddStaff.isSuccessful()) {
                    try {
                        loadStaff();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(mainView,
                                "Lỗi khi tải lại dữ liệu: " + ex.getMessage(),
                                "Lỗi",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }
    
    private void editStaff() {
        int selectedRow = mainView.getStaffTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(mainView,
                    "Vui lòng chọn nhân viên cần chỉnh sửa thông tin!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String staffId = mainView.getTableModel().getValueAt(selectedRow, 0).toString();
        String staffName = mainView.getTableModel().getValueAt(selectedRow, 1).toString();

        JOptionPane.showMessageDialog(mainView,
                "Chỉnh sửa thông tin nhân viên: " + staffName + " (ID: " + staffId + ")",
                "Chỉnh sửa nhân viên",
                JOptionPane.INFORMATION_MESSAGE);
        // Ở đây sẽ mở form chỉnh sửa nhân viên
    }
    
    public boolean deleteStaff() {
        int selectedRow = mainView.getStaffTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(mainView,
                    "Vui lòng chọn nhân viên cần xóa!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        String staffId = mainView.getTableModel().getValueAt(selectedRow, 0).toString();
        String staffName = mainView.getTableModel().getValueAt(selectedRow, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(mainView,
                "Bạn có chắc muốn xóa nhân viên " + staffName + " có mã là " + staffId + "?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean success = nhanVienDAO.deleteNhanVien(staffId);
                if (success) {
                    loadStaff();
                    JOptionPane.showMessageDialog(mainView,
                            "Đã xóa nhân viên '" + staffName + "' thành công!",
                            "Thông báo",
                            JOptionPane.INFORMATION_MESSAGE);
                    return true;
                } else {
                    JOptionPane.showMessageDialog(mainView,
                            "Không thể xóa nhân viên. Nhân viên này có thể đang có ràng buộc dữ liệu khác!",
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(mainView,
                        "Lỗi khi xóa nhân viên: " + ex.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        return false;
    }

    public void loadDataToTable(DefaultTableModel tableModel) throws SQLException {
        // Xóa dữ liệu cũ
        tableModel.setRowCount(0);
        
        // Lấy danh sách nhân viên từ DAO
        List<NhanVien> nhanViens = nhanVienDAO.getAllNhanVien();
        
        // Thêm dữ liệu vào bảng
        for (NhanVien nhanVien : nhanViens) {
            tableModel.addRow(new Object[]{
                nhanVien.getId(),
                nhanVien.getHoTen(),
                nhanVien.getGioiTinh(),
                nhanVien.getChucVu(),
                nhanVien.getEmail(),
                nhanVien.getSoDienThoai(),
                nhanVien.getNgayVaoLam(),
                nhanVien.getTrangThai()
            });
        }
    }
    
    public void searchNhanVien(DefaultTableModel tableModel, String keyword) throws SQLException {
        // Xóa dữ liệu cũ
        tableModel.setRowCount(0);
        
        // Tìm kiếm nhân viên theo từ khóa
        List<NhanVien> nhanViens = nhanVienDAO.searchNhanVien(keyword);
        
        // Thêm dữ liệu vào bảng
        for (NhanVien nhanVien : nhanViens) {
            tableModel.addRow(new Object[]{
                nhanVien.getId(),
                nhanVien.getHoTen(),
                nhanVien.getGioiTinh(),
                nhanVien.getChucVu(),
                nhanVien.getEmail(),
                nhanVien.getSoDienThoai(),
                nhanVien.getNgayVaoLam(),
                nhanVien.getTrangThai()
            });
        }
    }
    
    public NhanVien getNhanVienById(String id) throws SQLException {
        return nhanVienDAO.getNhanVienById(id);
    }
    
    public void addNhanVien(NhanVien nhanVien) throws SQLException {
        nhanVienDAO.addNhanVien(nhanVien);
    }
    
    public void updateNhanVien(NhanVien nhanVien) throws SQLException {
        nhanVienDAO.updateNhanVien(nhanVien);
    }
    
    public boolean deleteNhanVien(String id) throws SQLException {
        return nhanVienDAO.deleteNhanVien(id);
    }
    
    public boolean isEmailExists(String email) throws SQLException {
        return nhanVienDAO.isEmailExists(email);
    }
    
    public boolean isPhoneExists(String phone) throws SQLException {
        return nhanVienDAO.isPhoneExists(phone);
    }
}
