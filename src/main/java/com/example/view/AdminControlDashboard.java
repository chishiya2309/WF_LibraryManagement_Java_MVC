package com.example.view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdminControlDashboard extends JPanel {
    // Colors
    private final Color PURPLE = new Color(76, 40, 130);
    private final Color BLUE = new Color(0, 120, 212);
    private final Color GREEN = new Color(0, 153, 93);
    private final Color RED = new Color(232, 17, 35);
    private final Color ORANGE = new Color(245, 135, 0);
    private final Color PINK = new Color(230, 0, 109);
    private final Color WHITE = Color.WHITE;
    private final Color LIGHT_GRAY = new Color(245, 245, 245);

    // Components
    private JPanel dashboardPanel;
    private JPanel statsPanel;
    private JPanel statusPanel;

    // Card panels
    private JPanel cardBook;
    private JPanel cardLib;
    private JPanel cardCus;
    private JPanel cardBookBorrow;
    private JPanel panel1;
    private JPanel panel3;

    // Color bars
    private JPanel colorBarBook;
    private JPanel colorBarLib;
    private JPanel colorBarCus;
    private JPanel colorBarBookBorrow;
    private JPanel panel2;
    private JPanel panel4;

    // Labels
    private JLabel titleLabel;
    private JLabel systemStatusLabel;

    // Card title labels
    private JLabel cardTitleBook;
    private JLabel cardTitleBookLib;
    private JLabel cardTitleCus;
    private JLabel cardTitleBookBorrow;
    private JLabel label2;
    private JLabel label4;

    // Value labels
    private JLabel lblSachKhaDung;
    private JLabel lblNhanVien;
    private JLabel lblThanhVien;
    private JLabel lblSachMuonHomNay;
    private JLabel lblSachTraHomNay;
    private JLabel lblSachQuaHan;

    // Status labels
    private JLabel databaseLabel;
    private JLabel backupLabel;
    private JLabel versionLabel;

    // Timer for auto refresh
    private javax.swing.Timer refreshTimer;

    public AdminControlDashboard() {
        setLayout(new BorderLayout());

        initComponents();
        layoutComponents();

        statsPanel.setPreferredSize(new Dimension(dashboardPanel.getWidth() - 40, 243));
        statusPanel.setPreferredSize(new Dimension(dashboardPanel.getWidth() - 40, 200));

        refreshTimer = new javax.swing.Timer(60000, e -> refreshDashboardData());
        refreshTimer.start();

        loadDashboardData();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                handleResize();
            }
        });
    }

    private void initComponents() {
        dashboardPanel = new JPanel(new BorderLayout());
        dashboardPanel.setBackground(WHITE);

        titleLabel = new JLabel("Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));

        statsPanel = new JPanel(new GridLayout(2, 3, 1, 1));
        statsPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        statsPanel.setBackground(Color.LIGHT_GRAY);

        systemStatusLabel = new JLabel("Trạng thái hệ thống");
        systemStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        statusPanel = new JPanel();
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
        statusPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        statusPanel.setBackground(WHITE);

        databaseLabel = new JLabel("Cơ sở dữ liệu: Đang hoạt động");
        databaseLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        databaseLabel.setForeground(Color.GREEN);

        backupLabel = new JLabel("Cập nhật lần cuối: ...");
        backupLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        backupLabel.setForeground(Color.GREEN);

        versionLabel = new JLabel("Phiên bản: Library Management System v1.0");
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        initializeCards();
    }

    private void initializeCards() {
        cardBook = createCardPanel();
        colorBarBook = createColorBar(PURPLE);
        cardTitleBook = createCardTitleLabel("Tổng số sách khả dụng");
        lblSachKhaDung = createValueLabel("12");

        cardLib = createCardPanel();
        colorBarLib = createColorBar(BLUE);
        cardTitleBookLib = createCardTitleLabel("Tổng số nhân viên");
        lblNhanVien = createValueLabel("12");

        cardCus = createCardPanel();
        colorBarCus = createColorBar(GREEN);
        cardTitleCus = createCardTitleLabel("Thành viên đăng ký");
        lblThanhVien = createValueLabel("852");

        cardBookBorrow = createCardPanel();
        colorBarBookBorrow = createColorBar(RED);
        cardTitleBookBorrow = createCardTitleLabel("Sách mượn hôm nay");
        lblSachMuonHomNay = createValueLabel("24");

        panel1 = createCardPanel();
        panel2 = createColorBar(ORANGE);
        label2 = createCardTitleLabel("Sách trả hôm nay");
        lblSachTraHomNay = createValueLabel("18");

        panel3 = createCardPanel();
        panel4 = createColorBar(PINK);
        label4 = createCardTitleLabel("Sách quá hạn");
        lblSachQuaHan = createValueLabel("28");
    }

    private JPanel createCardPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(WHITE);
        return panel;
    }

    private JPanel createColorBar(Color color) {
        JPanel bar = new JPanel();
        bar.setBackground(color);
        bar.setPreferredSize(new Dimension(100, 5));
        bar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 5));
        return bar;
    }

    private JLabel createCardTitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        label.setForeground(Color.GRAY);
        return label;
    }

    private JLabel createValueLabel(String value) {
        JLabel label = new JLabel(value);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        return label;
    }

    private void layoutComponents() {
        dashboardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(WHITE);
        titlePanel.add(titleLabel);
        dashboardPanel.add(titlePanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(WHITE);

        setupCardPanel(cardBook, colorBarBook, cardTitleBook, lblSachKhaDung);
        setupCardPanel(cardLib, colorBarLib, cardTitleBookLib, lblNhanVien);
        setupCardPanel(cardCus, colorBarCus, cardTitleCus, lblThanhVien);
        setupCardPanel(cardBookBorrow, colorBarBookBorrow, cardTitleBookBorrow, lblSachMuonHomNay);
        setupCardPanel(panel1, panel2, label2, lblSachTraHomNay);
        setupCardPanel(panel3, panel4, label4, lblSachQuaHan);

        statsPanel.add(cardBook);
        statsPanel.add(cardLib);
        statsPanel.add(cardCus);
        statsPanel.add(cardBookBorrow);
        statsPanel.add(panel1);
        statsPanel.add(panel3);

        JPanel statusContentPanel = new JPanel();
        statusContentPanel.setLayout(new BoxLayout(statusContentPanel, BoxLayout.Y_AXIS));
        statusContentPanel.setBackground(WHITE);
        statusContentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        statusContentPanel.add(databaseLabel);
        statusContentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        statusContentPanel.add(backupLabel);
        statusContentPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        statusContentPanel.add(versionLabel);

        statusPanel.add(statusContentPanel);

        centerPanel.add(statsPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel statusTitlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusTitlePanel.setBackground(WHITE);
        statusTitlePanel.add(systemStatusLabel);
        centerPanel.add(statusTitlePanel);

        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(statusPanel);

        dashboardPanel.add(centerPanel, BorderLayout.CENTER);
        add(dashboardPanel, BorderLayout.CENTER);
    }

    private void setupCardPanel(JPanel panel, JPanel colorBar, JLabel titleLabel, JLabel valueLabel) {
        panel.removeAll();
        panel.add(colorBar);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(valueLabel);
    }

    private void loadDashboardData() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String currentTime = dateFormat.format(new Date());
        backupLabel.setText("Cập nhật lần cuối: " + currentTime);
    }

    private void refreshDashboardData() {
        loadDashboardData();

        int availableBooks = 10 + (int)(Math.random() * 10);
        lblSachKhaDung.setText(String.valueOf(availableBooks));

        int borrowedToday = 20 + (int)(Math.random() * 10);
        lblSachMuonHomNay.setText(String.valueOf(borrowedToday));

        int returnedToday = 15 + (int)(Math.random() * 10);
        lblSachTraHomNay.setText(String.valueOf(returnedToday));

        revalidate();
        repaint();
    }

    private void handleResize() {
        statsPanel.setPreferredSize(new Dimension(dashboardPanel.getWidth() - 40, 243));
        statusPanel.setPreferredSize(new Dimension(dashboardPanel.getWidth() - 40, 200));
        revalidate();
    }

    public void handleVisibilityChanged(boolean visible) {
        if (visible) {
            refreshDashboardData();
        }
    }

    public void cleanup() {
        if (refreshTimer != null) {
            refreshTimer.stop();
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Dashboard");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(700, 650);

            AdminControlDashboard dashboard = new AdminControlDashboard();
            frame.add(dashboard);

            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    dashboard.cleanup();
                }
            });

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
