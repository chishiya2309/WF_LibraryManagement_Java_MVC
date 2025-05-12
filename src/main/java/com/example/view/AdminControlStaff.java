package com.example.view;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.sql.SQLException;
import java.util.List;

import com.example.controller.NhanVienController;
import com.example.dao.NhanVienDAO;
import com.example.model.NhanVien;

public class AdminControlStaff extends JPanel {
    // Colors
    private final Color PURPLE = new Color(76, 40, 130);
    private final Color BLUE = new Color(0, 150, 200);
    private final Color ORANGE = new Color(255, 140, 0);
    private final Color RED = new Color(200, 0, 0);
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

    private JButton addStaffButton;
    private JButton editStaffButton;
    private JButton deleteStaffButton;
    private JButton reloadButton;
    private JButton searchButton;

    private JTable staffTable;
    private DefaultTableModel tableModel;
    private JScrollPane tableScrollPane;

    private JPopupMenu contextMenu;
    private JMenuItem addMenuItem;
    private JMenuItem editMenuItem;
    private JMenuItem deleteMenuItem;

    // Data access object
    private NhanVienDAO nhanVienDAO;
    
    // Controller
    private NhanVienController nhanVienController;

    public AdminControlStaff() {
        // Set up the panel
        setLayout(new BorderLayout());
        
        // Initialize data access object
        nhanVienDAO = new NhanVienDAO();

        // Initialize components
        initComponents();

        // Add components to the panel
        layoutComponents();
        
        // Initialize controller
        nhanVienController = new NhanVienController(this, nhanVienDAO);

        // Load initial data
        try {
            nhanVienController.loadStaff();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tải dữ liệu nhân viên: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
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
        titleLabel = new JLabel("Quản lý nhân viên");
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
        addStaffButton = createButton("Thêm nhân viên mới", PURPLE);
        editStaffButton = createButton("Sửa nhân viên", ORANGE);
        deleteStaffButton = createButton("Xóa nhân viên", RED);
        reloadButton = createButton("Reload", BLUE);
        searchButton = createButton("Tìm", GREEN);

        // Initialize table
        String[] columnNames = {
                "ID", "Họ và tên", "Giới tính", "Chức vụ",
                "Email", "Số điện thoại", "Ngày vào làm", "Trạng thái"
        };

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };

        staffTable = new JTable(tableModel);
        staffTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        staffTable.setRowHeight(25);
        staffTable.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        staffTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 10));
        staffTable.setShowGrid(false);
        staffTable.setIntercellSpacing(new Dimension(0, 0));

        // Set up alternating row colors
        staffTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? WHITE : new Color(240, 240, 240));
                }

                // Apply special formatting for status column
                if (column == 7 && value != null) {
                    String status = value.toString();
                    if ("Đang làm".equals(status)) {
                        c.setForeground(GREEN);
                    } else if ("Tạm nghỉ".equals(status)) {
                        c.setForeground(ORANGE);
                    } else {
                        c.setForeground(Color.BLACK);
                    }
                } else {
                    c.setForeground(Color.BLACK);
                }

                return c;
            }
        });

        tableScrollPane = new JScrollPane(staffTable);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Initialize context menu
        contextMenu = new JPopupMenu();
        addMenuItem = new JMenuItem("Thêm");
        editMenuItem = new JMenuItem("Chỉnh sửa");
        deleteMenuItem = new JMenuItem("Xóa");

        contextMenu.add(addMenuItem);
        contextMenu.add(editMenuItem);
        contextMenu.add(deleteMenuItem);
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
        buttonPanel.add(addStaffButton);
        buttonPanel.add(editStaffButton);
        buttonPanel.add(deleteStaffButton);
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

    // Getters for UI components
    public JButton getAddStaffButton() {
        return addStaffButton;
    }
    
    public JButton getEditStaffButton() {
        return editStaffButton;
    }
    
    public JButton getDeleteStaffButton() {
        return deleteStaffButton;
    }
    
    public JButton getReloadButton() {
        return reloadButton;
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

    public JTable getStaffTable() {
        return staffTable;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }
    
    public JMenuItem getAddMenuItem() {
        return addMenuItem;
    }
    
    public JMenuItem getEditMenuItem() {
        return editMenuItem;
    }
    
    public JMenuItem getDeleteMenuItem() {
        return deleteMenuItem;
    }
    
    public JPopupMenu getContextMenu() {
        return contextMenu;
    }
    
    public JScrollPane getTableScrollPane() {
        return tableScrollPane;
    }
}