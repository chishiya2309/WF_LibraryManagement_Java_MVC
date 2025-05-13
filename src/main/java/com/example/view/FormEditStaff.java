package com.example.view;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.regex.Pattern;
import com.example.controller.FormEditStaffController;
import com.example.dao.NhanVienDAO;
import com.example.model.NhanVien;

public class FormEditStaff extends JDialog {
    // Colors
    private final Color PURPLE = new Color(76, 40, 130);
    private final Color LIGHT_GRAY = new Color(240, 240, 240);
    private final Color WHITE = Color.WHITE;

    // Components
    private JPanel mainPanel;
    private JPanel headerPanel;
    private JPanel contentPanel;
    private JPanel buttonPanel;

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
    private FormEditStaffController controller;
    private NhanVienDAO nhanVienDAO;

    /**
     * Constructor for the staff edit form
     * @param staffId ID của nhân viên cần chỉnh sửa
     */
    public FormEditStaff(String staffId) {
        // Set up the dialog
        setTitle("Chỉnh sửa thông tin nhân viên");
        setSize(550, 500);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setModal(true);

        // Initialize data access objects
        nhanVienDAO = new NhanVienDAO();

        // Initialize components
        initComponents();

        // Add components to the frame
        layoutComponents();

        // Initialize controller
        controller = new FormEditStaffController(this, nhanVienDAO);

        // Load staff data
        controller.loadStaffData(staffId);

        // Add event listeners
        addEventListeners();
    }

    private void initComponents() {
        // Initialize panels
        mainPanel = new JPanel(new BorderLayout());
        headerPanel = new JPanel(new BorderLayout());
        contentPanel = new JPanel(new GridBagLayout());
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // Set panel backgrounds
        headerPanel.setBackground(PURPLE);
        contentPanel.setBackground(WHITE);
        buttonPanel.setBackground(LIGHT_GRAY);

        // Initialize header label
        titleLabel = new JLabel("Sửa thông tin nhân viên");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 0));

        // Initialize labels
        idLabel = new JLabel("ID:");
        nameLabel = new JLabel("Họ tên:");
        genderLabel = new JLabel("Giới tính:");
        positionLabel = new JLabel("Chức vụ:");
        emailLabel = new JLabel("Email:");
        phoneLabel = new JLabel("Số điện thoại:");
        startDateLabel = new JLabel("Ngày vào làm:");
        statusLabel = new JLabel("Trạng thái:");

        // Set label fonts
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 12);
        idLabel.setFont(labelFont);
        nameLabel.setFont(labelFont);
        genderLabel.setFont(labelFont);
        positionLabel.setFont(labelFont);
        emailLabel.setFont(labelFont);
        phoneLabel.setFont(labelFont);
        startDateLabel.setFont(labelFont);
        statusLabel.setFont(labelFont);

        // Initialize text fields
        idField = new JTextField(20);
        nameField = new JTextField(20);
        emailField = new JTextField(20);
        phoneField = new JTextField(20);

        // Initialize combo boxes
        genderComboBox = new JComboBox<>(new String[]{"Nam", "Nữ"});
        positionComboBox = new JComboBox<>(new String[]{"Nhân Viên", "Quản Lý", "Admin"});
        statusComboBox = new JComboBox<>(new String[]{"Đang làm", "Tạm nghỉ"});

        // Initialize date spinner
        Date today = new Date();
        SpinnerDateModel startDateModel = new SpinnerDateModel(today, null, null, Calendar.DAY_OF_MONTH);
        startDateSpinner = new JSpinner(startDateModel);
        JSpinner.DateEditor startDateEditor = new JSpinner.DateEditor(startDateSpinner, "dd/MM/yyyy");
        startDateSpinner.setEditor(startDateEditor);

        // Set component fonts
        Font componentFont = new Font("Segoe UI", Font.PLAIN, 12);
        idField.setFont(componentFont);
        nameField.setFont(componentFont);
        genderComboBox.setFont(componentFont);
        positionComboBox.setFont(componentFont);
        emailField.setFont(componentFont);
        phoneField.setFont(componentFont);
        startDateSpinner.setFont(componentFont);
        statusComboBox.setFont(componentFont);

        // Make ID field read-only
        idField.setEditable(false);
        idField.setBackground(new Color(240, 240, 240));

        // Initialize buttons
        saveButton = new JButton("Lưu");
        cancelButton = new JButton("Hủy");

        // Style buttons
        saveButton.setBackground(PURPLE);
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        saveButton.setFocusPainted(false);
        saveButton.setBorderPainted(false);
        saveButton.setPreferredSize(new Dimension(120, 40));
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        cancelButton.setBackground(new Color(200, 200, 200));
        cancelButton.setForeground(Color.BLACK);
        cancelButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cancelButton.setFocusPainted(false);
        cancelButton.setBorderPainted(false);
        cancelButton.setPreferredSize(new Dimension(100, 40));
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void layoutComponents() {
        // Set up the main layout
        setLayout(new BorderLayout());

        // Set up the header panel
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Set up the content panel with GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 20, 10, 20);

        // ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        contentPanel.add(idLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.7;
        contentPanel.add(idField, gbc);

        // Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        contentPanel.add(nameField, gbc);

        // Gender
        gbc.gridx = 0;
        gbc.gridy = 2;
        contentPanel.add(genderLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        contentPanel.add(genderComboBox, gbc);

        // Position
        gbc.gridx = 0;
        gbc.gridy = 3;
        contentPanel.add(positionLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        contentPanel.add(positionComboBox, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 4;
        contentPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        contentPanel.add(emailField, gbc);

        // Phone
        gbc.gridx = 0;
        gbc.gridy = 5;
        contentPanel.add(phoneLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        contentPanel.add(phoneField, gbc);

        // Start Date
        gbc.gridx = 0;
        gbc.gridy = 6;
        contentPanel.add(startDateLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        contentPanel.add(startDateSpinner, gbc);

        // Status
        gbc.gridx = 0;
        gbc.gridy = 7;
        contentPanel.add(statusLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 7;
        contentPanel.add(statusComboBox, gbc);

        // Add filler to push everything up
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.weighty = 1.0;
        contentPanel.add(Box.createVerticalGlue(), gbc);

        // Set up the button panel
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 20));
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        // Add panels to the frame
        add(headerPanel, BorderLayout.NORTH);
        add(new JScrollPane(contentPanel), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addEventListeners() {
        // Add action listener to save button
        saveButton.addActionListener(e -> controller.saveStaff());

        // Add action listener to cancel button
        cancelButton.addActionListener(e -> {
            setVisible(false);
            dispose();
        });
    }

    // Getter methods cho controller
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

    public boolean isSuccessful() {
        return controller.isSuccessful();
    }

    // Data access class (simplified)
    private class DBStaff {
        public boolean updateStaff(String staffId, String name, String gender, String position,
                                   String email, String phone, Date startDate, String status) {
            // This would typically connect to a database and update the staff
            // For demonstration purposes, we'll just return true
            System.out.println("Updating staff: " + staffId + ", " + name);
            return true;
        }
    }

    // Main method for testing
    public static void main(String[] args) {
        // Set the look and feel to the system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create sample staff data for testing
        Map<String, Object> sampleStaffData = new HashMap<>();
        sampleStaffData.put("ID", "NV001");
        sampleStaffData.put("HoTen", "Nguyễn Văn A");
        sampleStaffData.put("GioiTinh", "Nam");
        sampleStaffData.put("ChucVu", "Nhân Viên");
        sampleStaffData.put("Email", "nguyenvana@email.com");
        sampleStaffData.put("SoDienThoai", "0901234567");
        sampleStaffData.put("NgayVaoLam", new Date());
        sampleStaffData.put("TrangThai", "Đang làm");

        // Create and display the form
        SwingUtilities.invokeLater(() -> {
            FormEditStaff form = new FormEditStaff("NV001");
            form.setVisible(true);
        });
    }
}