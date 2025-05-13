package com.example.view;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.SimpleDateFormat;

import com.example.controller.PhieuMuonQuaHanController;

/**
 * Panel hiển thị báo cáo phiếu mượn quá hạn
 */
public class AdminControlPhieuMuonQuaHan extends JPanel {
    // Colors
    private final Color WHITE = Color.WHITE;
    private final Color LIGHT_GRAY = new Color(240, 240, 240);
    private final Color RED = Color.RED;
    private final Color GRID_COLOR = new Color(230, 230, 230);
    private final Color PURPLE = new Color(76, 40, 130);

    // Components
    private JPanel mainPanel;
    private JTable loansTable;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;
    private JLabel noDataLabel;
    private JPanel contentPanel;
    private JLabel titleLabel;
    private JButton refreshButton;
    
    // Controller
    private PhieuMuonQuaHanController controller;

    /**
     * Constructor for the overdue loans report panel
     */
    public AdminControlPhieuMuonQuaHan() {
        // Set up the panel
        setLayout(new BorderLayout());
        setBackground(WHITE);

        // Initialize components
        initComponents();

        // Add components to the panel
        layoutComponents();
        
        // Initialize controller
        controller = new PhieuMuonQuaHanController(this);
        
        // Setup event listeners
        setupEventListeners();

        // Load data
        controller.loadData();
    }

    private void initComponents() {
        // Initialize title label
        titleLabel = new JLabel("BÁO CÁO PHIẾU MƯỢN QUÁ HẠN");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(PURPLE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
        
        // Initialize refresh button
        refreshButton = new JButton("Làm mới");
        refreshButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        refreshButton.setFocusPainted(false);
        
        // Initialize main panel
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(WHITE);
        
        // Initialize table model with columns
        String[] columnNames = {
                "Mã phiếu", "Thành viên", "Ngày mượn", "Hạn trả",
                "Ngày trả thực tế", "Trạng thái", "Sách", "Số lượng"
        };

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 7) { // SoLuong column
                    return Integer.class;
                } else if (columnIndex == 2 || columnIndex == 3 || columnIndex == 4) { // Date columns
                    return Date.class;
                }
                return String.class;
            }
        };

        // Initialize table
        loansTable = new JTable(tableModel);
        loansTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        loansTable.setRowHeight(30);
        loansTable.setShowGrid(true);
        loansTable.setGridColor(GRID_COLOR);
        loansTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        loansTable.setAutoCreateRowSorter(true);
        loansTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        loansTable.setIntercellSpacing(new Dimension(0, 0));
        
        // Set column widths for better display
        TableColumnModel columnModel = loansTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(70);   // Mã phiếu
        columnModel.getColumn(1).setPreferredWidth(150);  // Thành viên
        columnModel.getColumn(2).setPreferredWidth(100);  // Ngày mượn
        columnModel.getColumn(3).setPreferredWidth(100);  // Hạn trả
        columnModel.getColumn(4).setPreferredWidth(120);  // Ngày trả thực tế
        columnModel.getColumn(5).setPreferredWidth(80);   // Trạng thái
        columnModel.getColumn(6).setPreferredWidth(200);  // Sách
        columnModel.getColumn(7).setPreferredWidth(70);   // Số lượng

        // Set up alternating row colors and custom cell formatting
        loansTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            // Đặt định dạng ngày tháng theo yêu cầu (DD/MM/YYYY)
            private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Set alternating row colors
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? WHITE : LIGHT_GRAY);
                }

                // Format date columns
                if (value instanceof Date) {
                    setText(dateFormat.format((Date) value));
                }

                // Highlight overdue status
                if (column == 5 && "Quá hạn".equals(value)) { // TrangThai column
                    setForeground(RED);
                    if (c instanceof JLabel) {
                        ((JLabel) c).setFont(new Font("Segoe UI", Font.BOLD, 12));
                    }
                } else {
                    setForeground(Color.BLACK);
                }

                return c;
            }
        });

        // Initialize scroll pane
        scrollPane = new JScrollPane(loansTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(WHITE);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        
        // Initialize no data label
        noDataLabel = new JLabel("Không có phiếu mượn quá hạn nào");
        noDataLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        noDataLabel.setForeground(Color.GREEN);
        noDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
        noDataLabel.setVisible(false); // Ẩn mặc định
        
        // Tạo panel chứa nội dung (bảng hoặc thông báo)
        contentPanel = new JPanel(new CardLayout());
        contentPanel.setBackground(WHITE);
    }
    
    /**
     * Setup event listeners
     */
    private void setupEventListeners() {
        refreshButton.addActionListener(e -> controller.refreshData());
    }

    private void layoutComponents() {
        // Tạo panel cho tiêu đề và button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(WHITE);
        buttonPanel.add(refreshButton);
        
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        // Đặt kích thước cố định cho scrollPane và thêm border nhẹ để dễ thấy
        scrollPane.setPreferredSize(new Dimension(800, 400));
        scrollPane.setBorder(BorderFactory.createLineBorder(GRID_COLOR));
        
        // Canh giữa noDataLabel
        JPanel messagePanel = new JPanel(new GridBagLayout());
        messagePanel.setBackground(WHITE);
        messagePanel.add(noDataLabel);
        
        // Thêm scrollPane và messagePanel vào contentPanel dưới các tên có ý nghĩa
        contentPanel.add(scrollPane, "TABLE_VIEW");
        contentPanel.add(messagePanel, "NO_DATA_VIEW");
        
        // Mặc định hiển thị bảng
        ((CardLayout)contentPanel.getLayout()).show(contentPanel, "TABLE_VIEW");
        
        // Add components to main panel
        mainPanel.setLayout(new BorderLayout(0, 10)); // Tạo khoảng cách dọc giữa các thành phần
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Add main panel to this panel with correct constraints
        add(mainPanel, BorderLayout.CENTER);
    }
    
    /**
     * Lấy model của bảng
     * @return Model của bảng
     */
    public DefaultTableModel getTableModel() {
        return tableModel;
    }
    
    /**
     * Đặt trạng thái hiển thị của thông báo không có dữ liệu
     * @param visible true để hiển thị, false để ẩn
     */
    public void setNoDataVisible(boolean visible) {
        noDataLabel.setVisible(true); // Luôn hiển thị trong panel của nó
        
        if (visible) {
            // Hiển thị view không có dữ liệu
            ((CardLayout)contentPanel.getLayout()).show(contentPanel, "NO_DATA_VIEW");
        }
    }
    
    /**
     * Đặt trạng thái hiển thị của bảng
     * @param visible true để hiển thị, false để ẩn
     */
    public void setTableVisible(boolean visible) {
        if (visible) {
            // Nếu có dữ liệu, hiển thị bảng
            ((CardLayout)contentPanel.getLayout()).show(contentPanel, "TABLE_VIEW");
        }
    }
}