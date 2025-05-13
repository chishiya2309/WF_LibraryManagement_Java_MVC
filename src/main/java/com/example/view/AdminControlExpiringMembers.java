package com.example.view;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.controller.ExpiringMemberController;

/**
 * Panel hiển thị báo cáo thành viên sắp hết hạn
 */
public class AdminControlExpiringMembers extends JPanel {
    // Colors
    private final Color WHITE = Color.WHITE;
    private final Color LIGHT_GRAY = new Color(240, 240, 240);
    private final Color GREEN = new Color(0, 128, 0);
    private final Color GRID_COLOR = new Color(230, 230, 230);
    private final Color PURPLE = new Color(76, 40, 130);
    private final Color RED = new Color(220, 0, 0);

    // Components
    private JPanel mainPanel;
    private JTable membersTable;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;
    private JLabel noDataLabel;
    private JPanel filterPanel;
    private JLabel daysThresholdLabel;
    private JComboBox<Integer> daysThresholdComboBox;
    private JPanel contentPanel;
    private JLabel titleLabel;
    
    // Controller
    private ExpiringMemberController controller;

    /**
     * Constructor cho báo cáo thành viên sắp hết hạn
     */
    public AdminControlExpiringMembers() {
        // Set up the panel
        setLayout(new BorderLayout());
        setBackground(WHITE);

        // Initialize components
        initComponents();

        // Add components to the panel
        layoutComponents();
        
        // Initialize controller
        controller = new ExpiringMemberController(this);

        // Load data
        controller.loadData();
        
        // Debug hiển thị
        System.out.println("AdminControlExpiringMembers đã được khởi tạo");
    }

    private void initComponents() {
        // Initialize title label
        titleLabel = new JLabel("BÁO CÁO THÀNH VIÊN SẮP HẾT HẠN");
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
        
        daysThresholdLabel = new JLabel("Thành viên sẽ hết hạn trong: ");
        daysThresholdLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        Integer[] daysThresholdValues = {7, 15, 30, 60, 90};
        daysThresholdComboBox = new JComboBox<>(daysThresholdValues);
        daysThresholdComboBox.setSelectedItem(30); // Mặc định là 30 ngày
        daysThresholdComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        daysThresholdComboBox.addActionListener(e -> {
            int selectedDaysThreshold = (Integer) daysThresholdComboBox.getSelectedItem();
            controller.setDaysThreshold(selectedDaysThreshold);
        });

        // Initialize table model with columns
        String[] columnNames = {
                "Mã thành viên", "Họ tên", "Giới tính", "Số điện thoại", "Email",
                "Loại thành viên", "Ngày đăng ký", "Ngày hết hạn", "Trạng thái"
        };

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return String.class;
            }
        };

        // Initialize table
        membersTable = new JTable(tableModel);
        membersTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        membersTable.setRowHeight(30);
        membersTable.setShowGrid(true);
        membersTable.setGridColor(GRID_COLOR);
        membersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        membersTable.setAutoCreateRowSorter(true);
        membersTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        membersTable.setIntercellSpacing(new Dimension(0, 0));
        
        // Set column widths for better display
        TableColumnModel columnModel = membersTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(80);  // Mã thành viên
        columnModel.getColumn(1).setPreferredWidth(150); // Họ tên
        columnModel.getColumn(2).setPreferredWidth(70);  // Giới tính
        columnModel.getColumn(3).setPreferredWidth(100); // Số điện thoại
        columnModel.getColumn(4).setPreferredWidth(150); // Email
        columnModel.getColumn(5).setPreferredWidth(100); // Loại thành viên
        columnModel.getColumn(6).setPreferredWidth(90);  // Ngày đăng ký
        columnModel.getColumn(7).setPreferredWidth(90);  // Ngày hết hạn
        columnModel.getColumn(8).setPreferredWidth(80);  // Trạng thái

        // Set up custom renderer for date columns and highlighting
        membersTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? WHITE : LIGHT_GRAY);
                }

                // Highlight ngày hết hạn
                if (column == 7) {
                    String dateStr = value.toString();
                    try {
                        // Tính số ngày còn lại đến ngày hết hạn
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Date expiryDate = sdf.parse(dateStr);
                        Date today = new Date();
                        long diffTime = expiryDate.getTime() - today.getTime();
                        long diffDays = diffTime / (1000 * 60 * 60 * 24);
                        
                        // Highlight dựa vào số ngày còn lại
                        if (diffDays <= 7) {
                            c.setForeground(RED); // Sắp hết hạn (dưới 7 ngày)
                            if (c instanceof JLabel) {
                                ((JLabel) c).setFont(new Font("Segoe UI", Font.BOLD, 12));
                            }
                        } else if (diffDays <= 15) {
                            c.setForeground(new Color(255, 100, 0)); // Gần hết hạn (dưới 15 ngày)
                        } else {
                            c.setForeground(table.getForeground());
                        }
                    } catch (Exception e) {
                        c.setForeground(table.getForeground());
                    }
                } else {
                    c.setForeground(table.getForeground());
                }
                return c;
            }
        });

        // Initialize scroll pane
        scrollPane = new JScrollPane(membersTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(WHITE);
        scrollPane.setPreferredSize(new Dimension(800, 400));

        // Initialize no data label
        noDataLabel = new JLabel("Không có thành viên nào sắp hết hạn trong 30 ngày tới");
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
        filterPanel.add(daysThresholdLabel);
        filterPanel.add(daysThresholdComboBox);
        
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
            noDataLabel.setText("Không có thành viên nào sắp hết hạn trong " + 
                               ((Integer)daysThresholdComboBox.getSelectedItem()) + " ngày tới");
            
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
            membersTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
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
        if (membersTable != null) {
            int x = (getWidth() - noDataLabel.getPreferredSize().width) / 2;
            int y = (getHeight() - noDataLabel.getPreferredSize().height) / 2;
            noDataLabel.setBounds(x, y, noDataLabel.getPreferredSize().width, noDataLabel.getPreferredSize().height);
        }
    }
}