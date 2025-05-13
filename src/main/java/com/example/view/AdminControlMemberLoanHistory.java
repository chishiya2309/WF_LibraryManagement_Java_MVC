package com.example.view;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.List;

import com.example.controller.MemberLoanHistoryController;
import com.example.model.MemberItem;

/**
 * Panel hiển thị lịch sử mượn sách của thành viên
 */
public class AdminControlMemberLoanHistory extends JPanel {
    // Colors
    private final Color WHITE = Color.WHITE;
    private final Color LIGHT_GRAY = new Color(240, 240, 240);
    private final Color RED = Color.RED;
    private final Color GRID_COLOR = new Color(230, 230, 230);
    private final Color PURPLE = new Color(76, 40, 130);
    
    // Components
    private JPanel mainPanel;
    private JTable loanHistoryTable;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;
    private JComboBox<MemberItem> cmbMembers;
    private JLabel lblMember;
    private JLabel titleLabel;
    private JLabel noDataLabel;
    private JPanel contentPanel;
    private JButton refreshButton;
    
    // Controller
    private MemberLoanHistoryController controller;
    
    // Data
    private String currentMemberId;

    /**
     * Constructor
     */
    public AdminControlMemberLoanHistory() {
        // Set up panel
        setLayout(new BorderLayout());
        setBackground(WHITE);
        
        // Initialize components
        initComponents();
        
        // Add components to the panel
        layoutComponents();
        
        // Initialize controller
        controller = new MemberLoanHistoryController(this);
        
        // Setup event listeners
        setupEventListeners();
        
        // Load data
        controller.loadMembers();
    }

    private void initComponents() {
        // Initialize title label
        titleLabel = new JLabel("LỊCH SỬ MƯỢN SÁCH THEO THÀNH VIÊN");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(PURPLE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
        
        // Initialize main panel
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(WHITE);
        
        // Initialize member label
        lblMember = new JLabel("Thành viên:");
        lblMember.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        // Initialize combo box
        cmbMembers = new JComboBox<>();
        cmbMembers.setPreferredSize(new Dimension(300, 25));
        cmbMembers.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        // Initialize refresh button
        refreshButton = new JButton("Làm mới");
        refreshButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        refreshButton.setFocusPainted(false);
        
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
        loanHistoryTable = new JTable(tableModel);
        loanHistoryTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        loanHistoryTable.setRowHeight(30);
        loanHistoryTable.setShowGrid(true);
        loanHistoryTable.setGridColor(GRID_COLOR);
        loanHistoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        loanHistoryTable.setAutoCreateRowSorter(true);
        loanHistoryTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        loanHistoryTable.setIntercellSpacing(new Dimension(0, 0));
        
        // Set column widths
        TableColumnModel columnModel = loanHistoryTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(70);   // Mã phiếu
        columnModel.getColumn(1).setPreferredWidth(150);  // Thành viên
        columnModel.getColumn(2).setPreferredWidth(100);  // Ngày mượn
        columnModel.getColumn(3).setPreferredWidth(100);  // Hạn trả
        columnModel.getColumn(4).setPreferredWidth(120);  // Ngày trả thực tế
        columnModel.getColumn(5).setPreferredWidth(80);   // Trạng thái
        columnModel.getColumn(6).setPreferredWidth(200);  // Sách
        columnModel.getColumn(7).setPreferredWidth(70);   // Số lượng

        // Set up alternating row colors and date formatting
        loanHistoryTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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
        scrollPane = new JScrollPane(loanHistoryTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(WHITE);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        
        // Initialize no data label
        noDataLabel = new JLabel("Không có dữ liệu lịch sử mượn sách cho thành viên này");
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
        cmbMembers.addActionListener(e -> {
            if (cmbMembers.getSelectedItem() != null) {
                MemberItem selectedMember = (MemberItem) cmbMembers.getSelectedItem();
                currentMemberId = selectedMember.getId();
                controller.loadLoanHistory(currentMemberId);
            }
        });
        
        refreshButton.addActionListener(e -> {
            if (currentMemberId != null && !currentMemberId.isEmpty()) {
                controller.loadLoanHistory(currentMemberId);
            }
        });
    }

    private void layoutComponents() {
        // Tạo panel cho tiêu đề
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        // Tạo filter panel cho combobox và button
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(WHITE);
        filterPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
        filterPanel.add(lblMember);
        filterPanel.add(cmbMembers);
        filterPanel.add(Box.createHorizontalStrut(20));
        filterPanel.add(refreshButton);
        
        // Đặt kích thước cố định cho scrollPane và thêm border nhẹ để dễ thấy
        scrollPane.setPreferredSize(new Dimension(800, 400));
        scrollPane.setBorder(BorderFactory.createLineBorder(GRID_COLOR));
        
        // Canh giữa noDataLabel
        JPanel messagePanel = new JPanel(new GridBagLayout());
        messagePanel.setBackground(WHITE);
        messagePanel.add(noDataLabel);
        
        // Thêm scrollPane và messagePanel vào contentPanel
        contentPanel.add(scrollPane, "TABLE_VIEW");
        contentPanel.add(messagePanel, "NO_DATA_VIEW");
        
        // Mặc định hiển thị bảng
        ((CardLayout)contentPanel.getLayout()).show(contentPanel, "TABLE_VIEW");
        
        // Tạo panel chính chứa cả filter và content
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(WHITE);
        centerPanel.add(filterPanel, BorderLayout.NORTH);
        centerPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Add components to main panel
        mainPanel.setLayout(new BorderLayout(0, 10)); // Tạo khoảng cách dọc giữa các thành phần
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Add main panel to this panel
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
     * Cập nhật danh sách thành viên vào combo box
     * @param members Danh sách thành viên
     */
    public void populateMemberComboBox(List<MemberItem> members) {
        cmbMembers.removeAllItems();
        
        for (MemberItem member : members) {
            cmbMembers.addItem(member);
        }
        
        if (cmbMembers.getItemCount() > 0) {
            cmbMembers.setSelectedIndex(0);
        }
    }
    
    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        centerNoDataLabel();
    }
    
    private void centerNoDataLabel() {
        // Center the no data label in the panel
        if (loanHistoryTable != null) {
            int x = (getWidth() - noDataLabel.getPreferredSize().width) / 2;
            int y = (getHeight() - noDataLabel.getPreferredSize().height) / 2;
            noDataLabel.setBounds(x, y, noDataLabel.getPreferredSize().width, noDataLabel.getPreferredSize().height);
        }
    }
}

