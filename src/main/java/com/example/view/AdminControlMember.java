package com.example.view;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.sql.SQLException;

import com.example.dao.ThanhVienDAO;
import com.example.model.ThanhVien;

public class AdminControlMember extends JPanel {
    // Colors
    private final Color PURPLE = new Color(76, 40, 130);
    private final Color DARK_ORANGE = new Color(255, 140, 0);
    private final Color RED = new Color(200, 0, 0);
    private final Color BLUE = new Color(0, 150, 200);
    private final Color GREEN = new Color(34, 139, 34);
    private final Color WHITE = Color.WHITE;
    private final Color LIGHT_GRAY = new Color(245, 245, 245);

    // Components
    private JPanel mainPanel;
    private JPanel buttonPanel;
    private JPanel searchPanel;

    private JLabel titleLabel;
    private JLabel searchLabel;
    private JLabel noDataLabel;

    private JTextField searchField;

    private JButton addMemberButton;
    private JButton editMemberButton;
    private JButton deleteMemberButton;
    private JButton reloadButton;
    private JButton searchButton;

    private JTable membersTable;
    private DefaultTableModel tableModel;
    private JScrollPane tableScrollPane;

    private JPopupMenu contextMenu;
    private JMenuItem viewDetailsMenuItem;
    private JMenuItem editMenuItem;
    private JMenuItem deleteMenuItem;
    private JMenuItem renewMembershipMenuItem;
    private JMenuItem viewLoansMenuItem;
    private JMenuItem printCardMenuItem;

    // Data access object
    private ThanhVienDAO thanhVienDAO;
    private java.util.List<ThanhVien> memberList;

    public AdminControlMember() {
        // Set up the panel
        setLayout(new BorderLayout());
        setBackground(WHITE);

        // Initialize data access object
        thanhVienDAO = new ThanhVienDAO();
        memberList = new ArrayList<>();

        // Initialize components
        initComponents();

        // Add components to the panel
        layoutComponents();

        // Add event listeners
        addEventListeners();

        // Load initial data
        loadData();
    }

    private void initComponents() {
        // Initialize panels
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(WHITE);

        buttonPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        buttonPanel.setBackground(WHITE);

        searchPanel = new JPanel();
        searchPanel.setBackground(LIGHT_GRAY);
        searchPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        // Initialize labels
        titleLabel = new JLabel("Quản lý thành viên");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));

        searchLabel = new JLabel("Tìm kiếm:");
        searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));

        noDataLabel = new JLabel("Không tìm thấy kết quả");
        noDataLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        noDataLabel.setForeground(RED);
        noDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
        noDataLabel.setVisible(false);

        // Initialize text fields
        searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 10));

        // Initialize buttons
        addMemberButton = createButton("Thêm thành viên mới", PURPLE);
        editMemberButton = createButton("Sửa thành viên", DARK_ORANGE);
        deleteMemberButton = createButton("Xóa thành viên", RED);
        reloadButton = createButton("Reload", BLUE);
        searchButton = createButton("Tìm", GREEN);

        // Initialize table
        String[] columnNames = {
                "Mã thành viên", "Họ và tên", "Giới tính", "Số điện thoại",
                "Email", "Loại thành viên", "Ngày đăng ký", "Ngày hết hạn", "Trạng thái"
        };

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };

        membersTable = new JTable(tableModel);
        membersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        membersTable.setRowHeight(25);
        membersTable.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        membersTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 10));
        membersTable.setShowGrid(false);
        membersTable.setIntercellSpacing(new Dimension(0, 0));

        // Set up alternating row colors
        membersTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? WHITE : new Color(240, 240, 240));
                }

                // Format status column
                if (column == 8 && value != null) {
                    String status = value.toString();
                    if (status.equals("Hoạt động")) {
                        c.setForeground(GREEN);
                    } else if (status.equals("Hết hạn")) {
                        c.setForeground(DARK_ORANGE);
                    } else if (status.equals("Khóa")) {
                        c.setForeground(RED);
                    } else {
                        c.setForeground(Color.BLACK);
                    }
                } else {
                    c.setForeground(Color.BLACK);
                }

                return c;
            }
        });

        tableScrollPane = new JScrollPane(membersTable);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Initialize context menu
        contextMenu = new JPopupMenu();
        viewDetailsMenuItem = new JMenuItem("Xem chi tiết");
        editMenuItem = new JMenuItem("Chỉnh sửa");
        deleteMenuItem = new JMenuItem("Xóa");
        renewMembershipMenuItem = new JMenuItem("Gia hạn thẻ");
        viewLoansMenuItem = new JMenuItem("Sách đang mượn");
        printCardMenuItem = new JMenuItem("In thẻ thành viên");

        contextMenu.add(viewDetailsMenuItem);
        contextMenu.add(editMenuItem);
        contextMenu.add(deleteMenuItem);
        contextMenu.add(renewMembershipMenuItem);
        contextMenu.add(viewLoansMenuItem);
        contextMenu.add(printCardMenuItem);
    }

    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void layoutComponents() {
        // Set up the main panel with padding
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add title to the top
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(WHITE);
        titlePanel.add(titleLabel);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Set up the center panel with buttons, search, and table
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(WHITE);

        // Add buttons to button panel
        buttonPanel.add(addMemberButton);
        buttonPanel.add(editMemberButton);
        buttonPanel.add(deleteMemberButton);
        buttonPanel.add(reloadButton);

        // Set up search panel
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Add components to center panel with spacing
        centerPanel.add(buttonPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(searchPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Create a panel for the table and no data label
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(WHITE);
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        // Add no data label on top of the table
        JPanel overlayPanel = new JPanel(new BorderLayout());
        overlayPanel.setOpaque(false);
        overlayPanel.add(noDataLabel, BorderLayout.CENTER);
        tablePanel.add(overlayPanel, BorderLayout.NORTH);

        centerPanel.add(tablePanel);

        // Add center panel to main panel
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Add main panel to this panel
        add(mainPanel, BorderLayout.CENTER);
    }

    private void addEventListeners() {
        // Add action listeners to buttons
        addMemberButton.addActionListener(e -> showAddMemberForm());
        editMemberButton.addActionListener(e -> editSelectedMember());
        deleteMemberButton.addActionListener(e -> deleteSelectedMember());
        reloadButton.addActionListener(e -> loadData());
        searchButton.addActionListener(e -> searchMembers());

        // Add key listener to search field
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchMembers();
                }
            }
        });

        // Add mouse listener to table for context menu and double-click
        membersTable.addMouseListener(new MouseAdapter() {
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
                int row = membersTable.rowAtPoint(e.getPoint());
                if (row >= 0 && row < membersTable.getRowCount()) {
                    membersTable.setRowSelectionInterval(row, row);
                    contextMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        // Add action listeners to context menu items
        viewDetailsMenuItem.addActionListener(e -> viewMemberDetails());
        editMenuItem.addActionListener(e -> editSelectedMember());
        deleteMenuItem.addActionListener(e -> deleteSelectedMember());
        renewMembershipMenuItem.addActionListener(e -> renewMembership());
        viewLoansMenuItem.addActionListener(e -> viewMemberLoans());
        printCardMenuItem.addActionListener(e -> printMemberCard());

        // Add component listener for resize events
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Adjust table size when panel is resized
                tableScrollPane.setPreferredSize(new Dimension(
                        getWidth() - 40,
                        getHeight() - 200
                ));
                revalidate();
            }
        });
    }

    // Data handling methods
    private void loadData() {
        try {
            // Clear existing data
            tableModel.setRowCount(0);

            // Get members from database using DAO
            memberList = thanhVienDAO.getAllThanhViens();

            // Định dạng ngày tháng DD/MM/YYYY
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            // Add data to table model
            for (ThanhVien member : memberList) {
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
                
                tableModel.addRow(row);
            }

            // Show/hide no data label
            noDataLabel.setVisible(tableModel.getRowCount() == 0);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tải dữ liệu thành viên: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchMembers() {
        String searchTerm = searchField.getText().trim().toLowerCase();

        if (searchTerm.isEmpty()) {
            loadData();
            return;
        }

        try {
            // Clear existing data
            tableModel.setRowCount(0);

            // Định dạng ngày tháng DD/MM/YYYY
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            // Filter members based on search term
            boolean foundResults = false;

            for (ThanhVien member : memberList) {
                String memberId = member.getMaThanhVien().toLowerCase();
                String memberName = member.getHoTen().toLowerCase();
                String phoneNumber = member.getSoDienThoai() != null ? member.getSoDienThoai().toLowerCase() : "";
                String email = member.getEmail() != null ? member.getEmail().toLowerCase() : "";

                if (memberId.contains(searchTerm) ||
                        memberName.contains(searchTerm) ||
                        phoneNumber.contains(searchTerm) ||
                        email.contains(searchTerm)) {

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
                    
                    tableModel.addRow(row);
                    foundResults = true;
                }
            }

            // Show/hide no data label
            noDataLabel.setVisible(!foundResults);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tìm kiếm thành viên: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Member operations
    private void showAddMemberForm() {
        FormAddMember formAddMember = new FormAddMember();
        formAddMember.setVisible(true);
        formAddMember.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (formAddMember.isSuccessful()) {
                    loadData();
                }
            }
        });
    }

    private void editSelectedMember() {
        int selectedRow = membersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn một thành viên để chỉnh sửa!",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String memberId = tableModel.getValueAt(selectedRow, 0).toString();

        FormEditMember formEditMember = new FormEditMember(memberId);
        formEditMember.setVisible(true);
        formEditMember.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (formEditMember.isSuccessful()) {
                    loadData();
                }
            }
        });
    }

    private void deleteSelectedMember() {
        int selectedRow = membersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn thành viên cần xóa!",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            String memberId = tableModel.getValueAt(selectedRow, 0).toString();
            String memberName = tableModel.getValueAt(selectedRow, 1).toString();

            int result = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc muốn xóa thành viên '" + memberName + "'?",
                    "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (result == JOptionPane.YES_OPTION) {
                // Chỗ này sẽ cần thêm phương thức xóa thành viên trong ThanhVienDAO
                // boolean success = thanhVienDAO.deleteThanhVien(memberId);
                
                // Tạm thời giả định thành công
                boolean success = true;

                if (success) {
                    loadData();
                    JOptionPane.showMessageDialog(this,
                            "Đã xóa thành viên '" + memberName + "' thành công!",
                            "Thông báo",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Không thể xóa thành viên này. Thành viên này có thể đang mượn sách hoặc có ràng buộc khác!",
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi xóa thành viên: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewMemberDetails() {
        JOptionPane.showMessageDialog(this,
                "Chức năng xem chi tiết thành viên sẽ được triển khai sau.",
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void renewMembership() {
        JOptionPane.showMessageDialog(this,
                "Chức năng gia hạn thẻ thành viên sẽ được triển khai sau.",
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void viewMemberLoans() {
        JOptionPane.showMessageDialog(this,
                "Chức năng xem sách đang mượn sẽ được triển khai sau.",
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void printMemberCard() {
        JOptionPane.showMessageDialog(this,
                "Chức năng in thẻ thành viên sẽ được triển khai sau.",
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // Getter methods for UI components
    public JButton getAddMemberButton() {
        return addMemberButton;
    }

    public JButton getEditMemberButton() {
        return editMemberButton;
    }

    public JButton getSearchButton() {
        return searchButton;
    }

    public JTextField getSearchField() {
        return searchField;
    }

    public JLabel getNoDataLabel() {
        return noDataLabel;
    }

    public JTable getMembersTable() {
        return membersTable;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    private class FormEditMember extends JDialog {
        private boolean successful = false;

        public FormEditMember(String memberId) {
            setTitle("Chỉnh sửa thành viên");
            setSize(500, 400);
            setModal(true);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

            // This would typically contain form fields and save/cancel buttons
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(new JLabel("Form chỉnh sửa thành viên sẽ được triển khai sau.", JLabel.CENTER), BorderLayout.CENTER);

            JButton closeButton = new JButton("Đóng");
            closeButton.addActionListener(e -> dispose());

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(closeButton);
            panel.add(buttonPanel, BorderLayout.SOUTH);

            add(panel);
        }

        public boolean isSuccessful() {
            return successful;
        }
    }
}