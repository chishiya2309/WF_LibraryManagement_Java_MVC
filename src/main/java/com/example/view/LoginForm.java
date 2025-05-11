package com.example.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.prefs.Preferences;

public class LoginForm extends JFrame {
    // Components
    private JPanel mainPanel;
    private JPanel headerPanel;
    private JPanel loginPanel;
    private JPanel titlePanel;
    private JPanel formPanel;
    private JPanel optionsPanel;

    private JLabel titleLabel;
    private JLabel loginLabel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JLabel errorLabel;
    private JLabel versionLabel;

    private JTextField txtUsername;
    private JPasswordField txtPassword;

    private JCheckBox rememberMeCheckBox;
    private JButton btnLogin;
    private JLabel forgotPasswordLink;

    // Colors
    private final Color PURPLE = new Color(76, 40, 130);
    private final Color LIGHT_GRAY = new Color(245, 245, 245);
    private final Color WHITE = Color.WHITE;
    
    // Preferences for storing user credentials
    private Preferences prefs;

    public LoginForm() {
        // Set up the frame
        setTitle("Library Management System");
        setSize(900, 661);
        setMinimumSize(new Dimension(800, 700));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize preferences
        prefs = Preferences.userNodeForPackage(LoginForm.class);

        // Initialize components
        initComponents();

        // Add components to the frame
        layoutComponents();

        // Add event listeners
        addEventListeners();
        
        // Load saved credentials if any
        loadSavedCredentials();
    }

    private void initComponents() {
        // Initialize panels
        mainPanel = new JPanel(new BorderLayout());
        headerPanel = new JPanel(new BorderLayout());
        loginPanel = new JPanel();
        titlePanel = new JPanel();
        formPanel = new JPanel();
        optionsPanel = new JPanel(new BorderLayout());

        // Set panel backgrounds
        mainPanel.setBackground(LIGHT_GRAY);
        headerPanel.setBackground(PURPLE);
        loginPanel.setBackground(WHITE);
        titlePanel.setBackground(WHITE);
        formPanel.setBackground(WHITE);
        optionsPanel.setBackground(WHITE);

        // Initialize labels
        titleLabel = new JLabel("LIBRARY MANAGEMENT SYSTEM", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLabel.setForeground(WHITE);
        titleLabel.setBackground(PURPLE);
        titleLabel.setOpaque(true);

        loginLabel = new JLabel("Staff Login");
        loginLabel.setFont(new Font("Georgia", Font.BOLD, 18));
        loginLabel.setForeground(PURPLE);

        usernameLabel = new JLabel("Staff ID:");
        usernameLabel.setFont(new Font("Georgia", Font.PLAIN, 12));

        passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Georgia", Font.PLAIN, 12));

        errorLabel = new JLabel("");
        errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        errorLabel.setForeground(Color.RED);
        errorLabel.setHorizontalAlignment(JLabel.CENTER);

        versionLabel = new JLabel("Library Management System v1.0", JLabel.CENTER);
        versionLabel.setFont(new Font("Georgia", Font.PLAIN, 9));
        versionLabel.setForeground(Color.GRAY);

        // Initialize text fields
        txtUsername = new JTextField(20);
        txtUsername.setFont(new Font("Georgia", Font.PLAIN, 12));

        txtPassword = new JPasswordField(20);
        txtPassword.setFont(new Font("Georgia", Font.PLAIN, 12));

        // Initialize checkbox
        rememberMeCheckBox = new JCheckBox("Remember staff ID");
        rememberMeCheckBox.setFont(new Font("Georgia", Font.PLAIN, 10));
        rememberMeCheckBox.setBackground(WHITE);

        // Initialize "forgot password" link (using JLabel with mouse listener)
        forgotPasswordLink = new JLabel("Forgot Password?");
        forgotPasswordLink.setFont(new Font("Georgia", Font.PLAIN, 10));
        forgotPasswordLink.setForeground(PURPLE);
        forgotPasswordLink.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Initialize login button
        btnLogin = new JButton("LOGIN");
        btnLogin.setFont(new Font("Georgia", Font.BOLD, 14));
        btnLogin.setBackground(PURPLE);
        btnLogin.setForeground(WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void layoutComponents() {
        // Set the main layout
        setLayout(new BorderLayout());
        
        // Add header panel
        headerPanel.setLayout(new BorderLayout());
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.setPreferredSize(new Dimension(getWidth(), 100));
        add(headerPanel, BorderLayout.NORTH);

        // Set up the main content panel
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(LIGHT_GRAY);
        
        // Set up the login panel
        loginPanel = new JPanel();
        loginPanel.setLayout(new BorderLayout());
        loginPanel.setBackground(WHITE);
        loginPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        loginPanel.setPreferredSize(new Dimension(400, 400));
        
        // Set up the title panel
        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        titlePanel.add(loginLabel);
        
        // Add title panel to login panel
        loginPanel.add(titlePanel, BorderLayout.NORTH);

        // Set up the form panel
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        // Add username field
        JPanel usernamePanel = new JPanel(new BorderLayout());
        usernamePanel.setBackground(WHITE);
        usernamePanel.add(usernameLabel, BorderLayout.NORTH);
        usernamePanel.add(txtUsername, BorderLayout.CENTER);
        usernamePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        formPanel.add(usernamePanel);

        // Add password field
        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.setBackground(WHITE);
        passwordPanel.add(passwordLabel, BorderLayout.NORTH);
        passwordPanel.add(txtPassword, BorderLayout.CENTER);
        passwordPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        formPanel.add(passwordPanel);

        // Add options panel (remember me and forgot password)
        optionsPanel.setBackground(WHITE);
        optionsPanel.add(rememberMeCheckBox, BorderLayout.WEST);
        optionsPanel.add(forgotPasswordLink, BorderLayout.EAST);
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        formPanel.add(optionsPanel);

        // Add login button
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBackground(WHITE);
        buttonPanel.add(btnLogin, BorderLayout.CENTER);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        formPanel.add(buttonPanel);

        // Add error label
        JPanel errorPanel = new JPanel(new BorderLayout());
        errorPanel.setBackground(WHITE);
        errorPanel.add(errorLabel, BorderLayout.CENTER);
        formPanel.add(errorPanel);

        // Add form panel to login panel
        loginPanel.add(formPanel, BorderLayout.CENTER);
        
        // Add the login panel to the main panel with constraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(loginPanel, gbc);
        
        // Add version label at the bottom
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(LIGHT_GRAY);
        footerPanel.add(versionLabel, BorderLayout.CENTER);
        footerPanel.setPreferredSize(new Dimension(getWidth(), 35));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        // Add main panel and footer to the frame
        add(mainPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private void addEventListeners() {
        // Add action listener to login button
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        // Add key listener to password field
        txtPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    login();
                }
            }
        });

        // Add mouse listener to forgot password link
        forgotPasswordLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(LoginForm.this,
                        "Vui lòng liên hệ với quản trị viên hệ thống để đặt lại mật khẩu của bạn.",
                        "Forgot Password",
                        JOptionPane.INFORMATION_MESSAGE);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                forgotPasswordLink.setText("<html><u>Forgot Password?</u></html>");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                forgotPasswordLink.setText("Forgot Password?");
            }
        });

        // Add window listener to handle form closing
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        
        // Add component listener for resizing
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                handleResize();
            }
        });
    }

    private void login() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Vui lòng nhập đầy đủ mã nhân viên và mật khẩu!");
            return;
        }

        if (authenticateLibraryStaff(username, password)) {
            if (rememberMeCheckBox.isSelected()) {
                saveStaffCredentials(username);
            } else {
                clearSavedCredentials();
            }

            JOptionPane.showMessageDialog(this,
                    "Đăng nhập thành công! Chào mừng đến với hệ thống quản lý thư viện.",
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);

            // Mở form chính
            AdminMainForm adminForm = new AdminMainForm();
            adminForm.setVisible(true);

            // Ẩn form đăng nhập
            this.dispose(); // hoặc this.setVisible(false);
        } else {
            errorLabel.setText("<html>ID nhân viên hoặc mật khẩu không chính xác.<br/> Vui lòng thử lại!</html>");
        }
    }

    
    private boolean authenticateLibraryStaff(String staffId, String password) {
        // This is a simple authentication for demonstration
        // In a real application, you would connect to a database
        return (staffId.equals("admin") && password.equals("admin"));
    }
    
    private void saveStaffCredentials(String staffId) {
        prefs.put("lastUsername", staffId);
        prefs.putBoolean("rememberMe", true);
    }
    
    private void clearSavedCredentials() {
        prefs.put("lastUsername", "");
        prefs.putBoolean("rememberMe", false);
    }
    
    private void loadSavedCredentials() {
        if (prefs.getBoolean("rememberMe", false)) {
            txtUsername.setText(prefs.get("lastUsername", ""));
            rememberMeCheckBox.setSelected(true);
        }
    }
    
    private void handleResize() {
        // Adjust header based on window size
        headerPanel.setPreferredSize(new Dimension(getWidth(), Math.max(80, (int)(getHeight() * 0.1))));
        
        // Adjust font size based on window width
        if (getWidth() > 1920) {
            titleLabel.setFont(new Font("Georgia", Font.BOLD, 32));
        } else if (getWidth() > 1366) {
            titleLabel.setFont(new Font("Georgia", Font.BOLD, 28));
        } else if (getWidth() > 1024) {
            titleLabel.setFont(new Font("Georgia", Font.BOLD, 24));
        } else {
            titleLabel.setFont(new Font("Georgia", Font.BOLD, 20));
        }
        
        // Repaint and revalidate the components
        revalidate();
        repaint();
    }
} 