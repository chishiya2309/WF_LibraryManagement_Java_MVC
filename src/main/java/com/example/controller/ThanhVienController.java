package com.example.controller;

import com.example.dao.ThanhVienDAO;
import com.example.model.ThanhVien;
import com.example.view.AdminControlMember;
import com.example.view.FormAddMember;
import com.example.view.FormEditMember;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.SQLException;
import java.util.List;

public class ThanhVienController {
    private AdminControlMember mainView;
    private ThanhVienDAO thanhVienDAO;

    public ThanhVienController() {}

    public ThanhVienController(AdminControlMember mainView, ThanhVienDAO thanhVienDAO) {
        this.mainView = mainView;
        this.thanhVienDAO = thanhVienDAO;
        
        // Đăng ký các listener cho các sự kiện tìm kiếm
        initSearchListeners();
        
        // Đăng ký listener cho nút thêm thành viên
        initAddMemberListener();
        
        // Đăng ký listener cho nút chỉnh sửa thành viên
        initEditMemberListener();
        
        // Đăng ký listener cho nút xóa thành viên
        initDeleteMemberListener();
        
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
    
    private void initAddMemberListener() {
        // Thêm listener cho nút thêm thành viên
        mainView.getAddMemberButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddMemberForm();
            }
        });
    }
    
    private void initEditMemberListener() {
        // Thêm listener cho nút chỉnh sửa thành viên
        mainView.getEditMemberButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editSelectedMember();
            }
        });
    }
    
    private void initDeleteMemberListener() {
        // Thêm listener cho nút xóa thành viên
        mainView.getDeleteMemberButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedMember();
            }
        });
    }
    
    private void initReloadListener() {
        // Thêm listener cho nút reload
        mainView.getReloadButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    loadMembers();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(mainView,
                            "Lỗi khi tải dữ liệu thành viên: " + ex.getMessage(),
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    
    private void initContextMenuListeners() {
        // Thêm listener cho menu item chỉnh sửa trong context menu
        mainView.getEditMenuItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editSelectedMember();
            }
        });
        
        // Thêm listener cho menu item xóa trong context menu
        mainView.getDeleteMenuItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedMember();
            }
        });
        
        // Thêm listener cho menu item xem chi tiết trong context menu
        mainView.getViewDetailsMenuItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewMemberDetails();
            }
        });
        
        // Thêm listener cho menu item gia hạn thẻ trong context menu
        mainView.getRenewMembershipMenuItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                renewMembership();
            }
        });
        
        // Thêm listener cho menu item xem sách đang mượn trong context menu
        mainView.getViewLoansMenuItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewMemberLoans();
            }
        });
        
        // Thêm listener cho menu item in thẻ thành viên trong context menu
        mainView.getPrintCardMenuItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printMemberCard();
            }
        });
    }
    
    private void initTableListeners() {
        // Thêm mouse listener cho bảng để xử lý double-click và context menu
        mainView.getMembersTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    viewMemberDetails();
                }
            }

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
                JTable table = mainView.getMembersTable();
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
                loadMembers();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(mainView,
                        "Lỗi khi tải dữ liệu thành viên: " + ex.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
            return;
        }
        
        try {
            // Sử dụng phương thức searchThanhViens từ DAO để tìm kiếm trong database
            List<ThanhVien> searchResults = thanhVienDAO.searchThanhViens(searchTerm);
            
            // Hiển thị kết quả tìm kiếm
            displayMembers(searchResults);
            
            // Show/hide no data label
            mainView.getNoDataLabel().setVisible(searchResults.isEmpty());
            mainView.getMembersTable().setVisible(!searchResults.isEmpty());
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(mainView,
                    "Lỗi khi tìm kiếm thành viên: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void loadMembers() throws SQLException {
        List<ThanhVien> members = thanhVienDAO.getAllThanhViens();
        displayMembers(members);
        
        // Show/hide no data label
        mainView.getNoDataLabel().setVisible(members.isEmpty());
        mainView.getMembersTable().setVisible(!members.isEmpty());
    }
    
    private void displayMembers(List<ThanhVien> members) {
        // Xóa dữ liệu cũ
        mainView.getTableModel().setRowCount(0);
        
        // Định dạng ngày tháng DD/MM/YYYY
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd/MM/yyyy");
        
        // Thêm dữ liệu mới vào bảng
        for (ThanhVien member : members) {
            Object[] row = new Object[9];
            row[0] = member.getMaThanhVien();
            row[1] = member.getHoTen();
            row[2] = member.getGioiTinh();
            row[3] = member.getSoDienThoai();
            row[4] = member.getEmail();
            row[5] = member.getLoaiThanhVien();
            row[6] = member.getNgayDangKy() != null ? dateFormat.format(member.getNgayDangKy()) : "";
            row[7] = member.getNgayHetHan() != null ? dateFormat.format(member.getNgayHetHan()) : "";
            row[8] = member.getTrangThai();
            
            mainView.getTableModel().addRow(row);
        }
    }
    
    private void showAddMemberForm() {
        FormAddMember formAddMember = new FormAddMember();
        formAddMember.setVisible(true);
        formAddMember.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (formAddMember.isSuccessful()) {
                    try {
                        loadMembers();
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
    
    public void editSelectedMember() {
        int selectedRow = mainView.getMembersTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(mainView,
                    "Vui lòng chọn một thành viên để chỉnh sửa!",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String memberId = mainView.getTableModel().getValueAt(selectedRow, 0).toString();

        FormEditMember formEditMember = new FormEditMember(memberId);
        formEditMember.setVisible(true);
        formEditMember.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (formEditMember.isSuccessful()) {
                    try {
                        loadMembers();
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
    
    private void deleteSelectedMember() {
        int selectedRow = mainView.getMembersTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(mainView,
                    "Vui lòng chọn thành viên cần xóa!",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            String memberId = mainView.getTableModel().getValueAt(selectedRow, 0).toString();
            String memberName = mainView.getTableModel().getValueAt(selectedRow, 1).toString();

            int result = JOptionPane.showConfirmDialog(mainView,
                    "Bạn có chắc muốn xóa thành viên '" + memberName + "'?",
                    "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (result == JOptionPane.YES_OPTION) {
                boolean success = thanhVienDAO.deleteThanhVien(memberId);

                if (success) {
                    loadMembers();
                    JOptionPane.showMessageDialog(mainView,
                            "Đã xóa thành viên '" + memberName + "' thành công!",
                            "Thông báo",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(mainView,
                            "Không thể xóa thành viên này. Thành viên này có thể đang mượn sách hoặc có ràng buộc khác!",
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(mainView,
                    "Lỗi khi xóa thành viên: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewMemberDetails() {
        JOptionPane.showMessageDialog(mainView,
                "Chức năng xem chi tiết thành viên sẽ được triển khai sau.",
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void renewMembership() {
        JOptionPane.showMessageDialog(mainView,
                "Chức năng gia hạn thẻ thành viên sẽ được triển khai sau.",
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void viewMemberLoans() {
        JOptionPane.showMessageDialog(mainView,
                "Chức năng xem sách đang mượn sẽ được triển khai sau.",
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void printMemberCard() {
        JOptionPane.showMessageDialog(mainView,
                "Chức năng in thẻ thành viên sẽ được triển khai sau.",
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
    }
} 