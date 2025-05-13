package com.example.view;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.sql.SQLException;
import java.util.List;

import com.example.controller.PhieuMuonController;
import com.example.dao.PhieuMuonDAO;
import com.example.model.PhieuMuon;

public class AdminControlLoanAndReturn extends JPanel {
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

    private JButton loanButton;
    private JButton returnButton;
    private JButton editLoanButton;
    private JButton deleteLoanButton;
    private JButton reloadButton;
    private JButton searchButton;

    private JTable loanReturnTable;
    private DefaultTableModel tableModel;
    private JScrollPane tableScrollPane;

    private JPopupMenu contextMenu;
    private JMenuItem addMenuItem;
    private JMenuItem editMenuItem;
    private JMenuItem deleteMenuItem;

    // Data access object
    private PhieuMuonDAO phieuMuonDAO;
    
    // Controller
    private PhieuMuonController controller;

    public AdminControlLoanAndReturn() {
        // Set up the panel
        setLayout(new BorderLayout());
        setBackground(WHITE);

        // Initialize data access object
        phieuMuonDAO = new PhieuMuonDAO();

        // Initialize components
        initComponents();

        // Add components to the panel
        layoutComponents();
        
        // Initialize controller
        controller = new PhieuMuonController(this, phieuMuonDAO);
        
        // Register event listeners
        registerEventListeners();

        // Load initial data
        try {
            controller.loadPhieuMuons();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tải dữ liệu phiếu mượn: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initComponents() {
        // Initialize panels
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(WHITE);

        buttonPanel = new JPanel(new GridLayout(1, 5, 10, 0));
        buttonPanel.setBackground(WHITE);

        searchPanel = new JPanel();
        searchPanel.setBackground(LIGHT_GRAY);
        searchPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        // Initialize labels
        titleLabel = new JLabel("Quản lý mượn / trả sách");
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
        loanButton = createButton("Mượn sách", PURPLE);
        returnButton = createButton("Trả sách", GREEN);
        editLoanButton = createButton("Sửa phiếu mượn", ORANGE);
        deleteLoanButton = createButton("Xóa phiếu mượn", RED);
        reloadButton = createButton("Reload", BLUE);
        searchButton = createButton("Tìm", GREEN);

        // Initialize table
        String[] columnNames = {
                "Mã phiếu", "Thành viên", "Ngày mượn", "Hạn trả",
                "Ngày trả thực tế", "Trạng thái", "Sách", "Số lượng"
        };

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };

        loanReturnTable = new JTable(tableModel);
        loanReturnTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        loanReturnTable.setRowHeight(25);
        loanReturnTable.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        loanReturnTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 10));
        loanReturnTable.setShowGrid(false);
        loanReturnTable.setIntercellSpacing(new Dimension(0, 0));

        // Set up alternating row colors
        loanReturnTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? WHITE : new Color(240, 240, 240));
                }

                // Format status column
                if (column == 5 && value != null) {
                    String status = value.toString();
                    if (status.equals("Đang mượn")) {
                        c.setForeground(ORANGE);
                    } else if (status.equals("Đã trả")) {
                        c.setForeground(GREEN);
                    } else if (status.equals("Quá hạn")) {
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

        tableScrollPane = new JScrollPane(loanReturnTable);
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
        buttonPanel.add(loanButton);
        buttonPanel.add(returnButton);
        buttonPanel.add(editLoanButton);
        buttonPanel.add(deleteLoanButton);
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

    private void registerEventListeners() {
        // Add action listeners to buttons
        loanButton.addActionListener(e -> controller.showLoanForm());
        returnButton.addActionListener(e -> controller.showReturnForm());
        editLoanButton.addActionListener(e -> controller.editSelectedLoan());
        deleteLoanButton.addActionListener(e -> controller.deletePhieuMuon());
        reloadButton.addActionListener(e -> {
            try {
                controller.loadPhieuMuons();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        "Lỗi khi tải lại dữ liệu: " + ex.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        
        searchButton.addActionListener(e -> searchPhieuMuons());

        // Add key listener to search field
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchPhieuMuons();
                }
            }
        });

        // Add mouse listener to table for context menu and double-click
        loanReturnTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    controller.editSelectedLoan();
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
                int row = loanReturnTable.rowAtPoint(e.getPoint());
                if (row >= 0 && row < loanReturnTable.getRowCount()) {
                    loanReturnTable.setRowSelectionInterval(row, row);
                    contextMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        // Add action listeners to context menu items
        addMenuItem.addActionListener(e -> controller.showLoanForm());
        editMenuItem.addActionListener(e -> controller.editSelectedLoan());
        deleteMenuItem.addActionListener(e -> controller.deletePhieuMuon());

        // Add component listener for resize events
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Adjust table size when panel is resized
                tableScrollPane.setPreferredSize(new Dimension(
                        getWidth() - 40,
                        getHeight() - 200
                ));

                // Adjust no data label position
                noDataLabel.setBounds(
                        loanReturnTable.getX() + (loanReturnTable.getWidth() - noDataLabel.getWidth()) / 2,
                        loanReturnTable.getY() + (loanReturnTable.getHeight() - noDataLabel.getHeight()) / 2,
                        noDataLabel.getWidth(),
                        noDataLabel.getHeight()
                );

                revalidate();
            }
        });
    }
    
    private void searchPhieuMuons() {
        String keyword = searchField.getText().trim();
        try {
            controller.searchPhieuMuons(keyword);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tìm kiếm: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Getter methods for controller
    public JTable getLoanReturnTable() {
        return loanReturnTable;
    }
    
    public DefaultTableModel getTableModel() {
        return tableModel;
    }
    
    public JLabel getNoDataLabel() {
        return noDataLabel;
    }
    
    public JTextField getSearchField() {
        return searchField;
    }
}