package com.example.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class AdminControlReports extends JPanel {
    // Colors
    private final Color PURPLE = new Color(76, 40, 130);
    private final Color LIGHT_GRAY = new Color(245, 245, 250);
    private final Color WHITE = Color.WHITE;
    private final Color GRAY = new Color(128, 128, 128);

    // Components
    private JPanel mainPanel;
    private JPanel reportTypePanel;
    private JPanel reportOptionsPanel;
    private JPanel reportPreviewPanel;

    private JLabel titleLabel;
    private JLabel reportTypeTitleLabel;
    private JLabel reportOptionsTitleLabel;
    private JLabel dateRangeLabel;
    private JLabel startDateLabel;
    private JLabel endDateLabel;
    private JLabel reportPreviewTitleLabel;
    private JLabel placeholderLabel;

    private JList<String> reportTypeList;
    private JComboBox<String> dateRangeComboBox;
    private JSpinner startDateSpinner;
    private JSpinner endDateSpinner;
    private JButton generateReportButton;

    /**
     * Constructor for the reports control panel
     */
    public AdminControlReports() {
        // Set up the panel
        setLayout(new BorderLayout());
        setBackground(WHITE);

        // Initialize components
        initComponents();

        // Add components to the panel
        layoutComponents();

        // Add event listeners
        addEventListeners();

        // Set default values
        setDefaultValues();
    }

    private void initComponents() {
        // Initialize panels
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(WHITE);

        reportTypePanel = new JPanel(new BorderLayout());
        reportTypePanel.setBackground(LIGHT_GRAY);
        reportTypePanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        reportOptionsPanel = new JPanel(null); // Using null layout for absolute positioning
        reportOptionsPanel.setBackground(LIGHT_GRAY);
        reportOptionsPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        reportPreviewPanel = new JPanel(new BorderLayout());
        reportPreviewPanel.setBackground(WHITE);
        reportPreviewPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        // Initialize labels
        titleLabel = new JLabel("Báo cáo - thống kê");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));

        reportTypeTitleLabel = new JLabel("Loại báo cáo");
        reportTypeTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        reportTypeTitleLabel.setForeground(PURPLE);

        reportOptionsTitleLabel = new JLabel("Tùy chọn báo cáo");
        reportOptionsTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        reportOptionsTitleLabel.setForeground(PURPLE);

        dateRangeLabel = new JLabel("Khoảng thời gian:");
        dateRangeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        startDateLabel = new JLabel("Từ ngày:");
        startDateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        endDateLabel = new JLabel("Đến ngày:");
        endDateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        reportPreviewTitleLabel = new JLabel("Xem trước báo cáo");
        reportPreviewTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        reportPreviewTitleLabel.setForeground(PURPLE);

        placeholderLabel = new JLabel("Chọn loại báo cáo và nhấn 'Tạo báo cáo' để xem kết quả");
        placeholderLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        placeholderLabel.setForeground(GRAY);
        placeholderLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Initialize list
        String[] reportTypes = {
                "Sách có số lượng khả dụng thấp",
                "Danh sách thành viên sắp hết hạn",
                "Danh sách phiếu mượn quá hạn",
                "Lịch sử mượn sách của thành viên cụ thể",
                "Top sách phổ biến nhất"
        };

        reportTypeList = new JList<>(reportTypes);
        reportTypeList.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        reportTypeList.setBackground(LIGHT_GRAY);
        reportTypeList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        reportTypeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Initialize combo box
        String[] dateRanges = {
                "Hôm nay",
                "7 ngày qua",
                "30 ngày qua",
                "Năm hiện tại",
                "Tùy chỉnh"
        };

        dateRangeComboBox = new JComboBox<>(dateRanges);
        dateRangeComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        // Initialize date spinners
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -30);
        Date thirtyDaysAgo = calendar.getTime();

        SpinnerDateModel startDateModel = new SpinnerDateModel(thirtyDaysAgo, null, null, Calendar.DAY_OF_MONTH);
        startDateSpinner = new JSpinner(startDateModel);
        JSpinner.DateEditor startDateEditor = new JSpinner.DateEditor(startDateSpinner, "dd/MM/yyyy");
        startDateSpinner.setEditor(startDateEditor);
        startDateSpinner.setEnabled(false);

        SpinnerDateModel endDateModel = new SpinnerDateModel(today, null, null, Calendar.DAY_OF_MONTH);
        endDateSpinner = new JSpinner(endDateModel);
        JSpinner.DateEditor endDateEditor = new JSpinner.DateEditor(endDateSpinner, "dd/MM/yyyy");
        endDateSpinner.setEditor(endDateEditor);
        endDateSpinner.setEnabled(false);

        // Initialize button
        generateReportButton = new JButton("Tạo báo cáo");
        generateReportButton.setBackground(PURPLE);
        generateReportButton.setForeground(Color.WHITE);
        generateReportButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        generateReportButton.setFocusPainted(false);
        generateReportButton.setBorderPainted(false);
        generateReportButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void layoutComponents() {
        // Set up the main panel with padding
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add title to the top
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(WHITE);
        titlePanel.add(titleLabel);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Set up the center panel with report type, options, and preview
        JPanel centerPanel = new JPanel(null); // Using null layout for absolute positioning
        centerPanel.setBackground(WHITE);

        // Set up report type panel
        reportTypePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JPanel reportTypeTitlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        reportTypeTitlePanel.setBackground(LIGHT_GRAY);
        reportTypeTitlePanel.add(reportTypeTitleLabel);
        reportTypePanel.add(reportTypeTitlePanel, BorderLayout.NORTH);

        JScrollPane reportTypeScrollPane = new JScrollPane(reportTypeList);
        reportTypeScrollPane.setBorder(BorderFactory.createEmptyBorder());
        reportTypeScrollPane.setBackground(LIGHT_GRAY);
        reportTypePanel.add(reportTypeScrollPane, BorderLayout.CENTER);

        // Set up report options panel
        reportOptionsPanel.setLayout(null); // Using null layout for absolute positioning

        reportOptionsTitleLabel.setBounds(15, 15, 150, 21);
        dateRangeLabel.setBounds(15, 50, 120, 17);
        dateRangeComboBox.setBounds(150, 48, 200, 21);
        startDateLabel.setBounds(15, 85, 120, 17);
        startDateSpinner.setBounds(150, 83, 200, 25);
        endDateLabel.setBounds(15, 120, 120, 17);
        endDateSpinner.setBounds(150, 118, 200, 25);
        generateReportButton.setBounds(367, 79, 120, 35);

        reportOptionsPanel.add(reportOptionsTitleLabel);
        reportOptionsPanel.add(dateRangeLabel);
        reportOptionsPanel.add(dateRangeComboBox);
        reportOptionsPanel.add(startDateLabel);
        reportOptionsPanel.add(startDateSpinner);
        reportOptionsPanel.add(endDateLabel);
        reportOptionsPanel.add(endDateSpinner);
        reportOptionsPanel.add(generateReportButton);

        // Set up report preview panel
        reportPreviewPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel previewTitlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        previewTitlePanel.setBackground(WHITE);
        previewTitlePanel.add(reportPreviewTitleLabel);
        reportPreviewPanel.add(previewTitlePanel, BorderLayout.NORTH);

        JPanel placeholderPanel = new JPanel(new GridBagLayout());
        placeholderPanel.setBackground(WHITE);
        placeholderPanel.add(placeholderLabel);
        reportPreviewPanel.add(placeholderPanel, BorderLayout.CENTER);

        // Add panels to center panel with absolute positioning
        centerPanel.setLayout(null); // Using null layout for absolute positioning

        // Initial positioning (will be adjusted by resize listener)
        reportTypePanel.setBounds(0, 0, 300, 300);
        reportOptionsPanel.setBounds(320, 0, 500, 180);
        reportPreviewPanel.setBounds(320, 200, 500, 300);

        centerPanel.add(reportTypePanel);
        centerPanel.add(reportOptionsPanel);
        centerPanel.add(reportPreviewPanel);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Add main panel to this panel
        add(mainPanel);

        // Adjust component sizes initially
        adjustComponentSizes();
    }

    private void addEventListeners() {
        // Add action listener to date range combo box
        dateRangeComboBox.addActionListener(e -> {
            String selectedItem = (String) dateRangeComboBox.getSelectedItem();
            if ("Tùy chỉnh".equals(selectedItem)) {
                startDateSpinner.setEnabled(true);
                endDateSpinner.setEnabled(true);
            } else {
                startDateSpinner.setEnabled(false);
                endDateSpinner.setEnabled(false);

                // Set date range based on selection
                Date today = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(today);

                switch (selectedItem) {
                    case "Hôm nay":
                        startDateSpinner.setValue(today);
                        endDateSpinner.setValue(today);
                        break;
                    case "7 ngày qua":
                        calendar.add(Calendar.DAY_OF_MONTH, -7);
                        startDateSpinner.setValue(calendar.getTime());
                        endDateSpinner.setValue(today);
                        break;
                    case "30 ngày qua":
                        calendar.add(Calendar.DAY_OF_MONTH, -30);
                        startDateSpinner.setValue(calendar.getTime());
                        endDateSpinner.setValue(today);
                        break;
                    case "Năm hiện tại":
                        calendar.set(Calendar.DAY_OF_YEAR, 1);
                        startDateSpinner.setValue(calendar.getTime());
                        endDateSpinner.setValue(today);
                        break;
                }
            }
        });

        // Add action listener to generate report button
        generateReportButton.addActionListener(e -> {
            String reportType = reportTypeList.getSelectedValue();
            generateReport(reportType, reportPreviewPanel);
        });

        // Add component listener for resize events
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustComponentSizes();
            }
        });
    }

    private void setDefaultValues() {
        // Set default date range
        dateRangeComboBox.setSelectedIndex(2); // "30 ngày qua"

        // Set default report type if available
        if (reportTypeList.getModel().getSize() > 0) {
            reportTypeList.setSelectedIndex(0);
        }
    }

    private void adjustComponentSizes() {
        int width = getWidth();
        int height = getHeight();

        if (width == 0 || height == 0) return; // Skip if not yet visible

        // Adjust panel sizes
        reportTypePanel.setSize(300, height - 100);
        reportTypeList.setSize(reportTypePanel.getWidth() - 30, reportTypePanel.getHeight() - 70);

        reportOptionsPanel.setSize(width - 360, 180);
        generateReportButton.setLocation(reportOptionsPanel.getWidth() - 150, reportOptionsPanel.getHeight() - 50);

        reportPreviewPanel.setSize(width - 360, height - 300);
        reportPreviewPanel.setLocation(320, 200);

        placeholderLabel.setSize(reportPreviewPanel.getWidth() - 200, 20);

        // Revalidate and repaint
        revalidate();
        repaint();
    }

    private void generateReport(String reportType, JPanel previewPanel) {
        System.out.println("Tạo báo cáo loại: " + reportType);
        if (reportType == null || reportType.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn loại báo cáo",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Clear the preview panel except for the title
        Component titleComponent = null;
        for (Component component : previewPanel.getComponents()) {
            if (component instanceof JPanel && ((JPanel) component).getComponentCount() > 0 &&
                    ((JPanel) component).getComponent(0) == reportPreviewTitleLabel) {
                titleComponent = component;
            } else {
                previewPanel.remove(component);
            }
        }
        System.out.println("Đã xóa các component cũ trong previewPanel");

        // Create the appropriate report panel based on the selected type
        JPanel reportPanel = null;

        switch (reportType) {
            case "Sách có số lượng khả dụng thấp":
                System.out.println("Tạo báo cáo AdminControlLowStockBooks");
                reportPanel = new AdminControlLowStockBooks();
                break;
            case "Danh sách thành viên sắp hết hạn":
                System.out.println("Tạo báo cáo AdminControlExpiringMembers");
                reportPanel = new AdminControlExpiringMembers();
                break;
            case "Danh sách phiếu mượn quá hạn":
                reportPanel = new AdminControlPhieuMuonQuaHan();
                break;
            case "Lịch sử mượn sách của thành viên cụ thể":
                // Create a new instance and make sure it's properly initialized
                reportPanel = new AdminControlMemberLoanHistory();
                System.out.println("Đã tạo AdminControlMemberLoanHistory");
                break;
            case "Top sách phổ biến nhất":
                reportPanel = new AdminControlPopularBooks();
                break;
            default:
                JOptionPane.showMessageDialog(this,
                        "Loại báo cáo không hợp lệ",
                        "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
        }

        // Add the report panel to the preview panel
        if (reportPanel != null) {
            System.out.println("Thêm báo cáo " + reportPanel.getClass().getSimpleName() + " vào previewPanel");

            // Set size constraints for the report panel
            reportPanel.setBackground(WHITE);
            reportPanel.setPreferredSize(new Dimension(previewPanel.getWidth() - 30, previewPanel.getHeight() - 50));

            // Use BorderLayout.CENTER to ensure proper sizing
            previewPanel.add(reportPanel, BorderLayout.CENTER);

            // Add back the title component if it exists
            if (titleComponent != null) {
                previewPanel.add(titleComponent, BorderLayout.NORTH);
            }

            // Make sure the panel is visible
            reportPanel.setVisible(true);

            // Validate and repaint
            previewPanel.revalidate();
            previewPanel.repaint();
            System.out.println("Đã revalidate và repaint previewPanel");
        }
    }
}