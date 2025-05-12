package com.example.view;

import com.example.controller.FormAddStaffController;
import com.example.dao.NhanVienDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class FormAddStaff extends JDialog {
    // Colors
    private final Color PURPLE = new Color(76, 40, 130);
    private final Color WHITE = Color.WHITE;
    private final Color LIGHT_GRAY = new Color(240, 240, 240);

    // Components
    private JPanel contentPane;
    private JPanel headerPanel;
    private JPanel footerPanel;
    private JPanel formPanel;

    private JLabel titleLabel;
    private JLabel idLabel;
    private JLabel nameLabel;
    private JLabel genderLabel;
    private JLabel positionLabel;
    private JLabel emailLabel;
    private JLabel phoneLabel;
    private JLabel startDateLabel;
    private JLabel statusLabel;

    private JTextField idField;
    private JTextField nameField;
    private JComboBox<String> genderComboBox;
    private JComboBox<String> positionComboBox;
    private JTextField emailField;
    private JTextField phoneField;
    private JSpinner startDateSpinner;
    private JComboBox<String> statusComboBox;

    private JButton saveButton;
    private JButton cancelButton;

    // Controller
    private FormAddStaffController controller;
    private boolean successful = false;

    public FormAddStaff(Frame owner) {
        super(owner, "Thêm nhân viên mới", true);

        // Set up the dialog
        setSize(550, 500);
        setResizable(false);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Initialize components
        initComponents();

        // Initialize form with default values
        initializeForm();

        // Initialize controller
        controller = new FormAddStaffController(this, new NhanVienDAO());
    }
    
    // Constructor không tham số cho NhanVienController
    public FormAddStaff() {
        super((Frame) null, "Thêm nhân viên mới", true);

        // Set up the dialog
        setSize(550, 500);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Initialize components
        initComponents();

        // Initialize form with default values
        initializeForm();

        // Initialize controller
        controller = new FormAddStaffController(this, new NhanVienDAO());
    }

    private void initComponents() {
        // Create main content pane
        contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);

        // Create header panel
        headerPanel = new JPanel();
        headerPanel.setBackground(PURPLE);
        headerPanel.setPreferredSize(new Dimension(550, 60));
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));

        titleLabel = new JLabel("Thêm nhân viên mới");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(WHITE);
        headerPanel.add(titleLabel);

        // Create form panel
        formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(WHITE);
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Create footer panel
        footerPanel = new JPanel();
        footerPanel.setBackground(LIGHT_GRAY);
        footerPanel.setPreferredSize(new Dimension(550, 60));
        footerPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        // Create form components
        idLabel = new JLabel("ID");
        nameLabel = new JLabel("Họ tên");
        genderLabel = new JLabel("Giới tính");
        positionLabel = new JLabel("Chức vụ");
        emailLabel = new JLabel("Email");
        phoneLabel = new JLabel("Số điện thoại");
        startDateLabel = new JLabel("Ngày vào làm");
        statusLabel = new JLabel("Trạng thái");

        idField = new JTextField(20);
        nameField = new JTextField(20);
        genderComboBox = new JComboBox<>(new String[]{"Nam", "Nữ"});
        positionComboBox = new JComboBox<>(new String[]{"Nhân viên", "Quản lý", "Admin"});
        emailField = new JTextField(20);
        phoneField = new JTextField(20);

        // Create date spinner
        SpinnerDateModel dateModel = new SpinnerDateModel();
        startDateSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(startDateSpinner, "dd/MM/yyyy");
        startDateSpinner.setEditor(dateEditor);

        statusComboBox = new JComboBox<>(new String[]{"Đang làm", "Tạm nghỉ"});

        // Create buttons
        cancelButton = new JButton("Hủy");
        cancelButton.setPreferredSize(new Dimension(100, 40));
        cancelButton.setBackground(new Color(200, 200, 200));
        cancelButton.setForeground(Color.BLACK);
        cancelButton.setFocusPainted(false);
        cancelButton.setBorderPainted(false);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        saveButton = new JButton("Lưu");
        saveButton.setPreferredSize(new Dimension(124, 40));
        saveButton.setBackground(PURPLE);
        saveButton.setForeground(WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setBorderPainted(false);
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.saveStaff();
                successful = controller.isSuccessful();
            }
        });

        // Add components to form panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // ID row
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        formPanel.add(idLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(idField, gbc);

        // Name row
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        formPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(nameField, gbc);

        // Gender row
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        formPanel.add(genderLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(genderComboBox, gbc);

        // Position row
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        formPanel.add(positionLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(positionComboBox, gbc);

        // Email row
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.3;
        formPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(emailField, gbc);

        // Phone row
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.3;
        formPanel.add(phoneLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(phoneField, gbc);

        // Start date row
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.3;
        formPanel.add(startDateLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(startDateSpinner, gbc);

        // Status row
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.weightx = 0.3;
        formPanel.add(statusLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(statusComboBox, gbc);

        // Add buttons to footer panel
        footerPanel.add(cancelButton);
        footerPanel.add(saveButton);

        // Add panels to content pane
        contentPane.add(headerPanel, BorderLayout.NORTH);
        contentPane.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        contentPane.add(footerPanel, BorderLayout.SOUTH);
    }

    private void initializeForm() {
        // Set default values
        genderComboBox.setSelectedIndex(0);
        positionComboBox.setSelectedIndex(0);
        startDateSpinner.setValue(new Date());
        statusComboBox.setSelectedIndex(0);
    }

    // Getters for form components
    public JTextField getIdField() {
        return idField;
    }

    public JTextField getNameField() {
        return nameField;
    }

    public JComboBox<String> getGenderComboBox() {
        return genderComboBox;
    }

    public JComboBox<String> getPositionComboBox() {
        return positionComboBox;
    }

    public JTextField getEmailField() {
        return emailField;
    }

    public JTextField getPhoneField() {
        return phoneField;
    }

    public JSpinner getStartDateSpinner() {
        return startDateSpinner;
    }

    public JComboBox<String> getStatusComboBox() {
        return statusComboBox;
    }

    public boolean getResult() {
        return successful;
    }
    
    public boolean isSuccessful() {
        return successful;
    }
}