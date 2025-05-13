package com.example.view;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.Date;
import java.util.List;

import com.example.controller.PopularBooksController;
import com.example.model.PopularBookItem;

/**
 * Panel hiển thị danh sách sách phổ biến nhất
 */
public class AdminControlPopularBooks extends JPanel {
    // Colors
    private final Color WHITE = Color.WHITE;
    private final Color LIGHT_GRAY = new Color(240, 240, 240);
    private final Color GRID_COLOR = new Color(230, 230, 230);
    private final Color PURPLE = new Color(76, 40, 130);
    
    // Components
    private JPanel mainPanel;
    private JTable popularBooksTable;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;
    private JLabel titleLabel;
    private JLabel noDataLabel;
    private JPanel contentPanel;
    private JPanel filterPanel;
    private JComboBox<String> timeRangeComboBox;
    private JLabel timeRangeLabel;
    private JButton refreshButton;
    
    // Controller
    private PopularBooksController controller;

    /**
     * Constructor
     */
    public AdminControlPopularBooks() {
        // Set up panel
        setLayout(new BorderLayout());
        setBackground(WHITE);

        // Initialize components
        initComponents();
        
        // Add components to the panel
        layoutComponents();

        // Initialize controller
        controller = new PopularBooksController(this);
        
        // Setup event listeners
        setupEventListeners();

        // Load data
        controller.loadTop10PopularBooks();
    }

    private void initComponents() {
        // Initialize title label
        titleLabel = new JLabel("TOP SÁCH PHỔ BIẾN NHẤT");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(PURPLE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
        
        // Initialize main panel
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(WHITE);
        
        // Initialize filter panel
        filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(WHITE);
        filterPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
        
        timeRangeLabel = new JLabel("Khoảng thời gian: ");
        timeRangeLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        String[] timeRanges = {"Tất cả thời gian", "7 ngày qua", "30 ngày qua", "Năm nay"};
        timeRangeComboBox = new JComboBox<>(timeRanges);
        timeRangeComboBox.setSelectedItem("Tất cả thời gian"); // Mặc định
        timeRangeComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        refreshButton = new JButton("Làm mới");
        refreshButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        refreshButton.setFocusPainted(false);

        // Initialize table model with columns
        String[] columnNames = {
                "Số thứ tự", "Mã sách", "Tên sách", "Số lần mượn"
        };

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0 || columnIndex == 3) { // STT and SoLanMuon columns
                    return Integer.class;
                }
                return String.class;
            }
        };

        // Initialize table
        popularBooksTable = new JTable(tableModel);
        popularBooksTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        popularBooksTable.setRowHeight(30);
        popularBooksTable.setShowGrid(true);
        popularBooksTable.setGridColor(GRID_COLOR);
        popularBooksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        popularBooksTable.setAutoCreateRowSorter(true);
        popularBooksTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        popularBooksTable.setIntercellSpacing(new Dimension(0, 0));

        // Set column widths
        TableColumnModel columnModel = popularBooksTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(70);   // STT
        columnModel.getColumn(1).setPreferredWidth(100);  // MaSach
        columnModel.getColumn(2).setPreferredWidth(300);  // TenSach
        columnModel.getColumn(3).setPreferredWidth(100);  // SoLanMuon

        // Center align the STT and SoLanMuon columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        columnModel.getColumn(0).setCellRenderer(centerRenderer);
        columnModel.getColumn(3).setCellRenderer(centerRenderer);

        // Set up alternating row colors
        popularBooksTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? WHITE : LIGHT_GRAY);
                }

                return c;
            }
        });

        // Initialize scroll pane
        scrollPane = new JScrollPane(popularBooksTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(WHITE);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        
        // Initialize no data label
        noDataLabel = new JLabel("Không có dữ liệu thống kê sách phổ biến");
        noDataLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        noDataLabel.setForeground(Color.GRAY);
        noDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Tạo panel chứa nội dung (bảng hoặc thông báo)
        contentPanel = new JPanel(new CardLayout());
        contentPanel.setBackground(WHITE);
    }
    
    /**
     * Setup event listeners
     */
    private void setupEventListeners() {
        timeRangeComboBox.addActionListener(e -> {
            String selectedRange = (String) timeRangeComboBox.getSelectedItem();
            loadDataByTimeRange(selectedRange);
        });
        
        refreshButton.addActionListener(e -> {
            String selectedRange = (String) timeRangeComboBox.getSelectedItem();
            loadDataByTimeRange(selectedRange);
        });
    }
    
    /**
     * Tải dữ liệu theo khoảng thời gian đã chọn
     * @param timeRange Khoảng thời gian đã chọn
     */
    private void loadDataByTimeRange(String timeRange) {
        if (timeRange == null) return;
        
        switch (timeRange) {
            case "Tất cả thời gian":
                controller.loadTop10PopularBooks();
                break;
            case "7 ngày qua":
                Date endDate = new Date();
                Date startDate = new Date(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000L);
                controller.loadPopularBooksByDateRange(startDate, endDate);
                break;
            case "30 ngày qua":
                endDate = new Date();
                startDate = new Date(System.currentTimeMillis() - 30 * 24 * 60 * 60 * 1000L);
                controller.loadPopularBooksByDateRange(startDate, endDate);
                break;
            case "Năm nay":
                endDate = new Date();
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.set(java.util.Calendar.DAY_OF_YEAR, 1);
                startDate = cal.getTime();
                controller.loadPopularBooksByDateRange(startDate, endDate);
                break;
        }
    }

    private void layoutComponents() {
        // Tạo panel cho tiêu đề
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        // Add components to filter panel
        filterPanel.add(timeRangeLabel);
        filterPanel.add(timeRangeComboBox);
        filterPanel.add(Box.createHorizontalStrut(20));
        filterPanel.add(refreshButton);
        
        // Tạo panel chứa nội dung (bảng hoặc thông báo)
        
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
        
        // Tạo panel chính chứa cả filter và content
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(WHITE);
        centerPanel.add(filterPanel, BorderLayout.NORTH);
        centerPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Add components to main panel
        mainPanel.setLayout(new BorderLayout(0, 10)); // Tạo khoảng cách dọc giữa các thành phần
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Add main panel to this panel with correct constraints
        setLayout(new BorderLayout());
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
        if (visible) {
            ((CardLayout)contentPanel.getLayout()).show(contentPanel, "NO_DATA_VIEW");
        } else {
            ((CardLayout)contentPanel.getLayout()).show(contentPanel, "TABLE_VIEW");
        }
    }
    
    /**
     * Update table with data
     * @param popularBooks List of popular book items
     */
    public void updateTable(List<PopularBookItem> popularBooks) {
        // Clear existing data
        tableModel.setRowCount(0);

        // Add data to table
        for (PopularBookItem book : popularBooks) {
            tableModel.addRow(new Object[] {
                    book.getOrderNumber(),
                    book.getBookId(),
                    book.getBookTitle(),
                    book.getBorrowCount()
            });
        }
    }
    
    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        centerNoDataLabel();
    }
    
    private void centerNoDataLabel() {
        // Center the no data label in the panel
        if (popularBooksTable != null) {
            int x = (getWidth() - noDataLabel.getPreferredSize().width) / 2;
            int y = (getHeight() - noDataLabel.getPreferredSize().height) / 2;
            noDataLabel.setBounds(x, y, noDataLabel.getPreferredSize().width, noDataLabel.getPreferredSize().height);
        }
    }
}
