package com.example.view;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

import com.example.controller.LowStockBookController;

/**
 * Panel hiển thị báo cáo sách có số lượng khả dụng thấp
 */
public class AdminControlLowStockBooks extends JPanel {
    // Colors
    private final Color WHITE = Color.WHITE;
    private final Color LIGHT_GRAY = new Color(240, 240, 240);
    private final Color GREEN = new Color(0, 128, 0);
    private final Color GRID_COLOR = new Color(230, 230, 230);
    private final Color PURPLE = new Color(76, 40, 130);
    
    // Components
    private JPanel mainPanel;
    private JTable booksTable;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;
    private JLabel noDataLabel;
    private JPanel filterPanel;
    private JLabel thresholdLabel;
    private JComboBox<Integer> thresholdComboBox;
    private JPanel contentPanel;
    private JLabel titleLabel;
    
    // Controller
    private LowStockBookController controller;

    /**
     * Constructor cho báo cáo sách có số lượng khả dụng thấp
     */
    public AdminControlLowStockBooks() {
        // Set up the panel
        setLayout(new BorderLayout());
        setBackground(WHITE);

        // Initialize components
        initComponents();

        // Add components to the panel
        layoutComponents();
        
        // Initialize controller
        controller = new LowStockBookController(this);

        // Load data
        controller.loadData();
        
        // Debug hiển thị
        System.out.println("AdminControlLowStockBooks đã được khởi tạo");
    }

    private void initComponents() {
        // Initialize title label
        titleLabel = new JLabel("BÁO CÁO SÁCH CÓ SỐ LƯỢNG KHẢ DỤNG THẤP");
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
        
        thresholdLabel = new JLabel("Ngưỡng số lượng khả dụng thấp: ");
        thresholdLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        Integer[] thresholdValues = {1, 2, 3, 5, 10};
        thresholdComboBox = new JComboBox<>(thresholdValues);
        thresholdComboBox.setSelectedItem(3); // Mặc định là 3
        thresholdComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        thresholdComboBox.addActionListener(e -> {
            int selectedThreshold = (Integer) thresholdComboBox.getSelectedItem();
            controller.setThreshold(selectedThreshold);
        });

        // Initialize table model with columns
        String[] columnNames = {
                "Mã sách", "ISBN", "Tên sách", "Tác giả", "Danh mục",
                "Năm xuất bản", "NXB", "Số bản", "Khả dụng", "Vị trí"
        };

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 8 || columnIndex == 7 || columnIndex == 5) {
                    return Integer.class; // For numeric columns
                }
                return String.class;
            }
        };

        // Initialize table
        booksTable = new JTable(tableModel);
        booksTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        booksTable.setRowHeight(30);
        booksTable.setShowGrid(true);
        booksTable.setGridColor(GRID_COLOR);
        booksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        booksTable.setAutoCreateRowSorter(true);
        booksTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        booksTable.setIntercellSpacing(new Dimension(0, 0));
        
        // Set column widths for better display
        TableColumnModel columnModel = booksTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(70);  // Mã sách
        columnModel.getColumn(1).setPreferredWidth(100); // ISBN
        columnModel.getColumn(2).setPreferredWidth(200); // Tên sách
        columnModel.getColumn(3).setPreferredWidth(150); // Tác giả
        columnModel.getColumn(4).setPreferredWidth(120); // Danh mục
        columnModel.getColumn(5).setPreferredWidth(90);  // Năm xuất bản
        columnModel.getColumn(6).setPreferredWidth(150); // NXB
        columnModel.getColumn(7).setPreferredWidth(70);  // Số bản
        columnModel.getColumn(8).setPreferredWidth(70);  // Khả dụng
        columnModel.getColumn(9).setPreferredWidth(100); // Vị trí

        // Set up alternating row colors
        booksTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? WHITE : LIGHT_GRAY);
                }
                // Highlight sách có số lượng khả dụng rất thấp (0 hoặc 1)
                if (column == 8 && value != null) {
                    int khaDung = (Integer) value;
                    if (khaDung <= 1) {
                        c.setForeground(Color.RED);
                        if (c instanceof JLabel) {
                            ((JLabel) c).setFont(new Font("Segoe UI", Font.BOLD, 12));
                        }
                    } else {
                        c.setForeground(table.getForeground());
                    }
                } else {
                    c.setForeground(table.getForeground());
                }
                return c;
            }
        });

        // Initialize scroll pane
        scrollPane = new JScrollPane(booksTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(WHITE);
        scrollPane.setPreferredSize(new Dimension(800, 400));

        // Initialize no data label
        noDataLabel = new JLabel("Tất cả đầu sách đều có số lượng khả dụng từ 3 trở lên");
        noDataLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        noDataLabel.setForeground(GREEN);
        noDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
        noDataLabel.setVisible(false); // Ẩn mặc định
    }

    private void layoutComponents() {
        // Tạo panel cho tiêu đề
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        // Add components to filter panel
        filterPanel.add(thresholdLabel);
        filterPanel.add(thresholdComboBox);
        
        // Tạo panel chứa nội dung (bảng hoặc thông báo)
        contentPanel = new JPanel(new CardLayout());
        contentPanel.setBackground(WHITE);
        
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
        System.out.println("setNoDataVisible: " + visible);
        noDataLabel.setVisible(true); // Luôn hiển thị trong panel của nó
        
        if (visible) {
            // Tạo font đậm và cỡ chữ lớn hơn cho thông báo
            noDataLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            noDataLabel.setText("Tất cả đầu sách đều có số lượng khả dụng từ " + 
                               ((Integer)thresholdComboBox.getSelectedItem()) + " trở lên");
            
            // Hiển thị view không có dữ liệu
            ((CardLayout)contentPanel.getLayout()).show(contentPanel, "NO_DATA_VIEW");
        }
    }
    
    /**
     * Đặt trạng thái hiển thị của bảng
     * @param visible true để hiển thị, false để ẩn
     */
    public void setTableVisible(boolean visible) {
        System.out.println("setTableVisible: " + visible);
        
        if (visible) {
            // Nếu có dữ liệu, cập nhật tiêu đề của bảng và hiển thị bảng
            booksTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
            ((CardLayout)contentPanel.getLayout()).show(contentPanel, "TABLE_VIEW");
        }
    }
    
    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        centerNoDataLabel();
    }
    
    private void centerNoDataLabel() {
        // Center the no data label in the panel
        if (booksTable != null) {
            int x = (getWidth() - noDataLabel.getPreferredSize().width) / 2;
            int y = (getHeight() - noDataLabel.getPreferredSize().height) / 2;
            noDataLabel.setBounds(x, y, noDataLabel.getPreferredSize().width, noDataLabel.getPreferredSize().height);
        }
    }
}