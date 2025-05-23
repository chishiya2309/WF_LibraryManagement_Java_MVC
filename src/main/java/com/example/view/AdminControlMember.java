package com.example.view;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.sql.SQLException;

import com.example.controller.ThanhVienController;
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
    
    // Controller
    private ThanhVienController controller;

    public AdminControlMember() {
        // Set up the panel
        setLayout(new BorderLayout());
        setBackground(WHITE);

        // Initialize data access object
        thanhVienDAO = new ThanhVienDAO();

        // Initialize components
        initComponents();

        // Add components to the panel
        layoutComponents();
        
        // Initialize controller
        controller = new ThanhVienController(this, thanhVienDAO);

        // Load initial data
        try {
            controller.loadMembers();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tải dữ liệu thành viên: " + ex.getMessage(),
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

    // Getter methods for UI components
    public JButton getAddMemberButton() {
        return addMemberButton;
    }

    public JButton getEditMemberButton() {
        return editMemberButton;
    }
    
    public JButton getDeleteMemberButton() {
        return deleteMemberButton;
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

    public JTable getMembersTable() {
        return membersTable;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }
    
    public JMenuItem getDeleteMenuItem() {
        return deleteMenuItem;
    }

    public JMenuItem getEditMenuItem() {
        return editMenuItem;
    }

    public JMenuItem getViewDetailsMenuItem() {
        return viewDetailsMenuItem;
    }

    public JMenuItem getRenewMembershipMenuItem() {
        return renewMembershipMenuItem;
    }

    public JMenuItem getViewLoansMenuItem() {
        return viewLoansMenuItem;
    }

    public JMenuItem getPrintCardMenuItem() {
        return printCardMenuItem;
    }

    public JPopupMenu getContextMenu() {
        return contextMenu;
    }

    public JScrollPane getTableScrollPane() {
        return tableScrollPane;
    }
}