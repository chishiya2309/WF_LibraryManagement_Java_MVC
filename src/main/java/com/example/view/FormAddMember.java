package com.example.view;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.regex.Pattern;


public class FormAddMember extends JDialog {
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
    private JLabel memberIdLabel;
    private JLabel nameLabel;
    private JLabel genderLabel;
    private JLabel phoneLabel;
    private JLabel emailLabel;
    private JLabel memberTypeLabel;
    private JLabel registerDateLabel;
    private JLabel expiryDateLabel;
    private JLabel statusLabel;

    private JTextField memberIdField;
    private JTextField nameField;
    private JComboBox<String> genderComboBox;
    private JTextField phoneField;
    private JTextField emailField;
    private JComboBox<String> memberTypeComboBox;
    private JSpinner registerDateSpinner;
    private JSpinner expiryDateSpinner;
    private JComboBox<String> statusComboBox;

    private JButton saveButton;
    private JButton cancelButton;

    // Data access object
    private DBMembers dbMembers;
    private boolean successful = false;

    public FormAddMember() {
        // Set up the dialog
        setTitle("Thêm thành viên mới");
        setSize(550, 550);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setModal(true);

        // Initialize data access objects
        dbMembers = new DBMembers();

        // Initialize components
        initComponents();

        // Add components to the frame
        layoutComponents();

        // Set default values
        initializeForm();

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
        titleLabel = new JLabel("Thêm thành viên mới");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 0));

        // Initialize labels
        memberIdLabel = new JLabel("Mã thành viên:");
        nameLabel = new JLabel("Họ tên:");
        genderLabel = new JLabel("Giới tính:");
        phoneLabel = new JLabel("Số điện thoại:");
        emailLabel = new JLabel("Email:");
        memberTypeLabel = new JLabel("Loại thành viên:");
        registerDateLabel = new JLabel("Ngày đăng ký:");
        expiryDateLabel = new JLabel("Ngày hết hạn:");
        statusLabel = new JLabel("Trạng thái:");

        // Set label fonts
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 12);
        memberIdLabel.setFont(labelFont);
        nameLabel.setFont(labelFont);
        genderLabel.setFont(labelFont);
        phoneLabel.setFont(labelFont);
        emailLabel.setFont(labelFont);
        memberTypeLabel.setFont(labelFont);
        registerDateLabel.setFont(labelFont);
        expiryDateLabel.setFont(labelFont);
        statusLabel.setFont(labelFont);

        // Initialize text fields
        memberIdField = new JTextField(20);
        nameField = new JTextField(20);
        phoneField = new JTextField(20);
        emailField = new JTextField(20);

        // Initialize combo boxes
        genderComboBox = new JComboBox<>(new String[]{"Nam", "Nữ"});
        memberTypeComboBox = new JComboBox<>(new String[]{"Sinh viên", "Giảng viên", "Thường"});
        statusComboBox = new JComboBox<>(new String[]{"Hoạt động", "Hết hạn", "Khóa"});

        // Initialize date spinners
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.YEAR, 2); // Default expiry date is 2 years from now
        Date twoYearsLater = calendar.getTime();

        SpinnerDateModel registerDateModel = new SpinnerDateModel(today, null, null, Calendar.DAY_OF_MONTH);
        SpinnerDateModel expiryDateModel = new SpinnerDateModel(twoYearsLater, null, null, Calendar.DAY_OF_MONTH);

        registerDateSpinner = new JSpinner(registerDateModel);
        expiryDateSpinner = new JSpinner(expiryDateModel);

        JSpinner.DateEditor registerDateEditor = new JSpinner.DateEditor(registerDateSpinner, "dd/MM/yyyy");
        JSpinner.DateEditor expiryDateEditor = new JSpinner.DateEditor(expiryDateSpinner, "dd/MM/yyyy");

        registerDateSpinner.setEditor(registerDateEditor);
        expiryDateSpinner.setEditor(expiryDateEditor);

        // Set component fonts
        Font componentFont = new Font("Segoe UI", Font.PLAIN, 12);
        memberIdField.setFont(componentFont);
        nameField.setFont(componentFont);
        genderComboBox.setFont(componentFont);
        phoneField.setFont(componentFont);
        emailField.setFont(componentFont);
        memberTypeComboBox.setFont(componentFont);
        registerDateSpinner.setFont(componentFont);
        expiryDateSpinner.setFont(componentFont);
        statusComboBox.setFont(componentFont);

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

        // Member ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        contentPanel.add(memberIdLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.7;
        contentPanel.add(memberIdField, gbc);

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

        // Phone
        gbc.gridx = 0;
        gbc.gridy = 3;
        contentPanel.add(phoneLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        contentPanel.add(phoneField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 4;
        contentPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        contentPanel.add(emailField, gbc);

        // Member Type
        gbc.gridx = 0;
        gbc.gridy = 5;
        contentPanel.add(memberTypeLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        contentPanel.add(memberTypeComboBox, gbc);

        // Register Date
        gbc.gridx = 0;
        gbc.gridy = 6;
        contentPanel.add(registerDateLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        contentPanel.add(registerDateSpinner, gbc);

        // Expiry Date
        gbc.gridx = 0;
        gbc.gridy = 7;
        contentPanel.add(expiryDateLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 7;
        contentPanel.add(expiryDateSpinner, gbc);

        // Status
        gbc.gridx = 0;
        gbc.gridy = 8;
        contentPanel.add(statusLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 8;
        contentPanel.add(statusComboBox, gbc);

        // Add filler to push everything up
        gbc.gridx = 0;
        gbc.gridy = 9;
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

    private void initializeForm() {
        // Set default values
        Date today = new Date();
        registerDateSpinner.setValue(today);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.YEAR, 2);
        expiryDateSpinner.setValue(calendar.getTime());

        genderComboBox.setSelectedIndex(0);
        memberTypeComboBox.setSelectedIndex(0);
        statusComboBox.setSelectedIndex(0);
    }

    private void addEventListeners() {
        // Add action listener to save button
        saveButton.addActionListener(e -> saveMember());

        // Add action listener to cancel button
        cancelButton.addActionListener(e -> {
            setVisible(false);
            dispose();
        });
    }

    private void saveMember() {
        if (!validateInputs()) {
            return;
        }

        try {
            String memberId = memberIdField.getText().trim();
            String name = nameField.getText().trim();
            String gender = genderComboBox.getSelectedItem().toString();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();
            String memberType = memberTypeComboBox.getSelectedItem().toString();
            Date registerDate = (Date) registerDateSpinner.getValue();
            Date expiryDate = (Date) expiryDateSpinner.getValue();
            String status = statusComboBox.getSelectedItem().toString();

            // Add member to database
            boolean success = dbMembers.addMember(memberId, name, gender, phone, email,
                    memberType, registerDate, expiryDate, status);

            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Thêm thành viên mới thành công!",
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
                successful = true;
                setVisible(false);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Lỗi khi thêm thành viên",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateInputs() {
        // Check member ID
        if (memberIdField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập mã thành viên!",
                    "Lỗi",
                    JOptionPane.WARNING_MESSAGE);
            memberIdField.requestFocus();
            return false;
        }

        // Check name
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập họ tên thành viên!",
                    "Lỗi",
                    JOptionPane.WARNING_MESSAGE);
            nameField.requestFocus();
            return false;
        }

        // Check phone
        if (phoneField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập số điện thoại!",
                    "Lỗi",
                    JOptionPane.WARNING_MESSAGE);
            phoneField.requestFocus();
            return false;
        }

        // Check phone format
        String phonePattern = "^0\\d{9,10}$";
        if (!Pattern.matches(phonePattern, phoneField.getText().trim())) {
            JOptionPane.showMessageDialog(this,
                    "Số điện thoại không hợp lệ! Số điện thoại phải bắt đầu bằng số 0 và có 10-11 chữ số.",
                    "Lỗi",
                    JOptionPane.WARNING_MESSAGE);
            phoneField.requestFocus();
            return false;
        }

        // Check email
        if (emailField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập địa chỉ email!",
                    "Lỗi",
                    JOptionPane.WARNING_MESSAGE);
            emailField.requestFocus();
            return false;
        }

        // Check email format
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (!Pattern.matches(emailPattern, emailField.getText().trim())) {
            JOptionPane.showMessageDialog(this,
                    "Địa chỉ email không hợp lệ!",
                    "Lỗi",
                    JOptionPane.WARNING_MESSAGE);
            emailField.requestFocus();
            return false;
        }

        // Check expiry date is after register date
        Date registerDate = (Date) registerDateSpinner.getValue();
        Date expiryDate = (Date) expiryDateSpinner.getValue();

        if (!expiryDate.after(registerDate)) {
            JOptionPane.showMessageDialog(this,
                    "Ngày hết hạn phải lớn hơn ngày đăng ký!",
                    "Lỗi",
                    JOptionPane.WARNING_MESSAGE);
            expiryDateSpinner.requestFocus();
            return false;
        }

        return true;
    }

    public boolean isSuccessful() {
        return successful;
    }

    // Data access class (simplified)
    private class DBMembers {
        public boolean addMember(String memberId, String name, String gender, String phone,
                                 String email, String memberType, Date registerDate,
                                 Date expiryDate, String status) {
            // This would typically connect to a database and add the member
            // For demonstration purposes, we'll just return true
            System.out.println("Adding member: " + memberId + ", " + name);
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

        // Create and display the form
        SwingUtilities.invokeLater(() -> {
            FormAddMember form = new FormAddMember();
            form.setVisible(true);
        });
    }
}