package com.example.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class AdminMainForm extends JFrame {
    // Colors
    private final Color PURPLE = new Color(76, 40, 130);
    private final Color DARK_GRAY = new Color(30, 30, 33);
    private final Color DARKER_GRAY = new Color(25, 25, 28);
    private final Color HOVER_GRAY = new Color(45, 45, 48);
    private final Color WHITE = Color.WHITE;
    private final Color LIGHT_GRAY = new Color(245, 245, 245);
    private final Color BORDER_COLOR = new Color(50, 50, 53);

    // Components
    private JPanel mainPanel;
    private JPanel headerPanel;
    private JPanel sidebarPanel;
    private JPanel contentPanel;
    private JPanel profilePanel;

    private JLabel headerLabel;
    private JLabel profileNameLabel;
    private JLabel profileRoleLabel;
    private JLabel profileImageLabel;

    private JButton btnDashboard;
    private JButton btnBooks;
    private JButton btnCategories;
    private JButton btnMembers;
    private JButton btnStaff;
    private JButton btnLoanAndReturn;
    private JButton btnReports;
    private JButton btnLogout;

    // Active button tracking
    private JButton activeButton;

    // Map to store button-panel relationships
    private Map<JButton, JPanel> menuMapping;

    public AdminMainForm() {
        // Set up the frame
        setTitle("Library Management System - Admin");
        setSize(1100, 700);
        setMinimumSize(new Dimension(900, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize components
        initComponents();

        // Add components to the frame
        layoutComponents();

        // Initialize menu mapping
        initMenuMapping();

        // Set dashboard as active by default
        setActiveButtonStatus(btnDashboard);
        loadPanelContent(menuMapping.get(btnDashboard));
    }

    private void initComponents() {
        // Initialize panels
        mainPanel = new JPanel(new BorderLayout());
        headerPanel = new JPanel(new BorderLayout());
        sidebarPanel = new JPanel(new BorderLayout());
        contentPanel = new JPanel(new BorderLayout());
        profilePanel = new JPanel(new BorderLayout());

        // Set panel backgrounds
        mainPanel.setBackground(LIGHT_GRAY);
        headerPanel.setBackground(PURPLE);
        sidebarPanel.setBackground(DARK_GRAY);
        contentPanel.setBackground(WHITE);
        profilePanel.setBackground(DARKER_GRAY);

        // Initialize header label
        headerLabel = new JLabel("Hệ thống quản lý thư viện");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        // Initialize profile components
        profileImageLabel = new JLabel();
        profileImageLabel.setPreferredSize(new Dimension(60, 60));
        profileImageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Create a circular avatar placeholder
        ImageIcon defaultAvatar = createCircularAvatar(60, 60, PURPLE);
        profileImageLabel.setIcon(defaultAvatar);

        profileNameLabel = new JLabel("Admin User");
        profileNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        profileNameLabel.setForeground(Color.WHITE);
        profileNameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        profileRoleLabel = new JLabel("Administrator");
        profileRoleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        profileRoleLabel.setForeground(new Color(200, 200, 200));
        profileRoleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Initialize buttons
        btnDashboard = createSidebarButton("Trang chính", "dashboard");
        btnBooks = createSidebarButton("Quản lý sách", "book");
        btnCategories = createSidebarButton("Quản lý danh mục", "category");
        btnMembers = createSidebarButton("Quản lý thành viên", "member");
        btnStaff = createSidebarButton("Quản lý nhân viên", "staff");
        btnLoanAndReturn = createSidebarButton("Quản lý mượn / trả sách", "loan");
        btnReports = createSidebarButton("Báo cáo và thống kê", "report");
        btnLogout = createSidebarButton("Đăng xuất", "logout");

        // Style logout button differently
        btnLogout.setBackground(new Color(180, 0, 0));
        btnLogout.setForeground(WHITE);
    }

    private JButton createSidebarButton(String text, String iconName) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setForeground(WHITE);
        button.setBackground(DARK_GRAY);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
                BorderFactory.createEmptyBorder(0, 15, 0, 0)
        ));
        button.setPreferredSize(new Dimension(220, 45));

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button != activeButton) {
                    button.setBackground(HOVER_GRAY);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (button != activeButton) {
                    button.setBackground(DARK_GRAY);
                }
            }
        });

        return button;
    }

    private void layoutComponents() {
        // Set the main layout
        setLayout(new BorderLayout());

        // Set up the header panel
        headerPanel.setPreferredSize(new Dimension(900, 60));
        headerPanel.add(headerLabel, BorderLayout.CENTER);

        // Set up the profile panel
        profilePanel.setPreferredSize(new Dimension(220, 150));
        profilePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
                BorderFactory.createEmptyBorder(20, 10, 20, 10)
        ));

        JPanel profileInfoPanel = new JPanel();
        profileInfoPanel.setLayout(new BoxLayout(profileInfoPanel, BoxLayout.Y_AXIS));
        profileInfoPanel.setBackground(DARKER_GRAY);
        profileInfoPanel.add(profileImageLabel);
        profileInfoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        profileInfoPanel.add(profileNameLabel);
        profileInfoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        profileInfoPanel.add(profileRoleLabel);

        profilePanel.add(profileInfoPanel, BorderLayout.CENTER);

        // Set up the sidebar panel
        sidebarPanel.setPreferredSize(new Dimension(220, 600));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(DARK_GRAY);

        // Add buttons to the button panel
        buttonPanel.add(btnDashboard);
        buttonPanel.add(btnBooks);
        buttonPanel.add(btnCategories);
        buttonPanel.add(btnMembers);
        buttonPanel.add(btnStaff);
        buttonPanel.add(btnLoanAndReturn);
        buttonPanel.add(btnReports);

        // Add a glue to push logout to the bottom
        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(btnLogout);

        // Create a panel for the sidebar content
        JPanel sidebarContentPanel = new JPanel(new BorderLayout());
        sidebarContentPanel.setBackground(DARK_GRAY);
        sidebarContentPanel.add(profilePanel, BorderLayout.NORTH);
        sidebarContentPanel.add(buttonPanel, BorderLayout.CENTER);

        // Create a scroll pane for the sidebar content
        JScrollPane scrollPane = new JScrollPane(sidebarContentPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Add the scroll pane to the sidebar panel
        sidebarPanel.add(scrollPane, BorderLayout.CENTER);

        // Set up the content panel
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create the main layout
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(sidebarPanel, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Add the main panel to the frame
        add(mainPanel);
    }

    private void initMenuMapping() {
        menuMapping = new HashMap<>();
        menuMapping.put(btnDashboard, new AdminControlDashboard());
        menuMapping.put(btnBooks, new AdminControlBooks());
        menuMapping.put(btnCategories, new AdminControlCategories());
        menuMapping.put(btnMembers, new AdminControlMember());
        menuMapping.put(btnStaff, new AdminControlStaff());
        menuMapping.put(btnLoanAndReturn, new AdminControlLoanAndReturn());
        menuMapping.put(btnReports, new AdminControlReports());
    }
    
    // Getters for buttons
    public JButton getBtnDashboard() {
        return btnDashboard;
    }
    
    public JButton getBtnBooks() {
        return btnBooks;
    }
    
    public JButton getBtnCategories() {
        return btnCategories;
    }
    
    public JButton getBtnMembers() {
        return btnMembers;
    }
    
    public JButton getBtnStaff() {
        return btnStaff;
    }
    
    public JButton getBtnLoanAndReturn() {
        return btnLoanAndReturn;
    }
    
    public JButton getBtnReports() {
        return btnReports;
    }
    
    public JButton getBtnLogout() {
        return btnLogout;
    }
    
    public Map<JButton, JPanel> getMenuMapping() {
        return menuMapping;
    }
    
    // Methods that were previously private but now need to be called from Controller
    public void setActiveButtonStatus(JButton button) {
        // Reset previous active button
        if (activeButton != null) {
            activeButton.setBackground(DARK_GRAY);
        }

        // Set new active button
        activeButton = button;
        activeButton.setBackground(PURPLE);
    }

    public void loadPanelContent(JPanel panel) {
        // Clear the content panel
        contentPanel.removeAll();

        // Add the new panel
        if (panel != null) {
            panel.setVisible(true);
            contentPanel.add(panel, BorderLayout.CENTER);
        }

        // Refresh the content panel
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // Helper method to create a circular avatar
    private ImageIcon createCircularAvatar(int width, int height, Color color) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        // Enable anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw circle
        g2d.setColor(color);
        g2d.fillOval(0, 0, width, height);

        // Draw text (first letter of "Admin")
        g2d.setColor(WHITE);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 24));
        FontMetrics fm = g2d.getFontMetrics();
        String text = "A";
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        g2d.drawString(text, (width - textWidth) / 2, height / 2 + textHeight / 4);

        g2d.dispose();

        return new ImageIcon(image);
    }

    private class AdminControlMember extends JPanel {
        public AdminControlMember() {
            setLayout(new BorderLayout());
            setBackground(WHITE);

            JLabel titleLabel = new JLabel("Quản lý thành viên");
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
            titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

            add(titleLabel, BorderLayout.NORTH);
        }
    }

    private class AdminControlStaff extends JPanel {
        public AdminControlStaff() {
            setLayout(new BorderLayout());
            setBackground(WHITE);

            JLabel titleLabel = new JLabel("Quản lý nhân viên");
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
            titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

            add(titleLabel, BorderLayout.NORTH);
        }
    }

    private class AdminControlLoanAndReturn extends JPanel {
        public AdminControlLoanAndReturn() {
            setLayout(new BorderLayout());
            setBackground(WHITE);

            JLabel titleLabel = new JLabel("Quản lý mượn / trả sách");
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
            titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

            add(titleLabel, BorderLayout.NORTH);
        }
    }

    private class AdminControlReports extends JPanel {
        public AdminControlReports() {
            setLayout(new BorderLayout());
            setBackground(WHITE);

            JLabel titleLabel = new JLabel("Báo cáo và thống kê");
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
            titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

            add(titleLabel, BorderLayout.NORTH);
        }
    }

    // Need to import this for the circular avatar
    private static class BufferedImage extends java.awt.image.BufferedImage {
        public BufferedImage(int width, int height, int imageType) {
            super(width, height, imageType);
        }
    }
}