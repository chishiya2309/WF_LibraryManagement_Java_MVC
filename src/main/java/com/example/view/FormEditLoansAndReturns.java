package com.example.view;

import javax.swing.*;
import java.awt.*;
import java.util.*;

import com.example.controller.FormEditLoansController;

public class FormEditLoansAndReturns extends JDialog {
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
    private JLabel loanIdLabel;
    private JLabel loanDateLabel;
    private JLabel dueDateLabel;
    private JLabel actualReturnDateLabel;
    private JLabel statusLabel;
    private JLabel quantityLabel;

    private JTextField loanIdField;
    private JSpinner loanDateSpinner;
    private JSpinner dueDateSpinner;
    private JSpinner actualReturnDateSpinner;
    private JCheckBox actualReturnDateCheckBox;
    private JComboBox<String> statusComboBox;
    private JSpinner quantitySpinner;

    private JButton saveButton;
    private JButton cancelButton;

    // Controller
    private FormEditLoansController controller;
    private Map<String, Object> loanData;
    private boolean successful = false;

    /**
     * Constructor for the loan edit form
     * @param owner The parent frame
     * @param loanData The loan data to edit
     */
    public FormEditLoansAndReturns(Frame owner, Map<String, Object> loanData) {
        super(owner, "Chỉnh sửa phiếu mượn", true);

        // Set up the dialog
        setSize(550, 450);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(owner);
        setResizable(false);

        // Save loan data
        this.loanData = loanData;

        // Initialize components
        initComponents();

        // Add components to the frame
        layoutComponents();

        // Initialize controller
        controller = new FormEditLoansController(this, loanData);

        // Load data
        loadLoanData(loanData);

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
        titleLabel = new JLabel("Chỉnh sửa phiếu mượn");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 0));

        // Initialize labels
        loanIdLabel = new JLabel("Mã phiếu:");
        loanDateLabel = new JLabel("Ngày mượn:");
        dueDateLabel = new JLabel("Hạn trả:");
        actualReturnDateLabel = new JLabel("Ngày trả thực tế:");
        statusLabel = new JLabel("Trạng thái:");
        quantityLabel = new JLabel("Số lượng:");

        // Set label fonts
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 12);
        loanIdLabel.setFont(labelFont);
        loanDateLabel.setFont(labelFont);
        dueDateLabel.setFont(labelFont);
        actualReturnDateLabel.setFont(labelFont);
        statusLabel.setFont(labelFont);
        quantityLabel.setFont(labelFont);

        // Initialize text field
        loanIdField = new JTextField(20);
        loanIdField.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        // Initialize date spinners
        Date today = new Date();

        SpinnerDateModel loanDateModel = new SpinnerDateModel(today, null, null, Calendar.DAY_OF_MONTH);
        loanDateSpinner = new JSpinner(loanDateModel);
        JSpinner.DateEditor loanDateEditor = new JSpinner.DateEditor(loanDateSpinner, "dd/MM/yyyy");
        loanDateSpinner.setEditor(loanDateEditor);

        SpinnerDateModel dueDateModel = new SpinnerDateModel(today, null, null, Calendar.DAY_OF_MONTH);
        dueDateSpinner = new JSpinner(dueDateModel);
        JSpinner.DateEditor dueDateEditor = new JSpinner.DateEditor(dueDateSpinner, "dd/MM/yyyy");
        dueDateSpinner.setEditor(dueDateEditor);

        SpinnerDateModel actualReturnDateModel = new SpinnerDateModel(today, null, null, Calendar.DAY_OF_MONTH);
        actualReturnDateSpinner = new JSpinner(actualReturnDateModel);
        JSpinner.DateEditor actualReturnDateEditor = new JSpinner.DateEditor(actualReturnDateSpinner, "dd/MM/yyyy");
        actualReturnDateSpinner.setEditor(actualReturnDateEditor);

        // Initialize checkbox for actual return date
        actualReturnDateCheckBox = new JCheckBox();
        actualReturnDateCheckBox.setSelected(false);

        // Initialize combo box
        statusComboBox = new JComboBox<>(new String[]{"Đang mượn", "Đã trả", "Quá hạn"});
        statusComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        // Initialize quantity spinner
        SpinnerNumberModel quantityModel = new SpinnerNumberModel(1, 1, 5, 1);
        quantitySpinner = new JSpinner(quantityModel);
        quantitySpinner.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        // Set component fonts
        loanDateSpinner.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dueDateSpinner.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        actualReturnDateSpinner.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        // Initialize buttons
        saveButton = new JButton("Lưu thay đổi");
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

        // Loan ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        contentPanel.add(loanIdLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.7;
        contentPanel.add(loanIdField, gbc);

        // Loan Date
        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPanel.add(loanDateLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        contentPanel.add(loanDateSpinner, gbc);

        // Due Date
        gbc.gridx = 0;
        gbc.gridy = 2;
        contentPanel.add(dueDateLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        contentPanel.add(dueDateSpinner, gbc);

        // Actual Return Date
        gbc.gridx = 0;
        gbc.gridy = 3;
        contentPanel.add(actualReturnDateLabel, gbc);

        // Create a panel for the date spinner and checkbox
        JPanel actualReturnDatePanel = new JPanel(new BorderLayout(5, 0));
        actualReturnDatePanel.setBackground(WHITE);
        actualReturnDatePanel.add(actualReturnDateSpinner, BorderLayout.CENTER);
        actualReturnDatePanel.add(actualReturnDateCheckBox, BorderLayout.EAST);

        gbc.gridx = 1;
        gbc.gridy = 3;
        contentPanel.add(actualReturnDatePanel, gbc);

        // Status
        gbc.gridx = 0;
        gbc.gridy = 4;
        contentPanel.add(statusLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        contentPanel.add(statusComboBox, gbc);

        // Quantity
        gbc.gridx = 0;
        gbc.gridy = 5;
        contentPanel.add(quantityLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        contentPanel.add(quantitySpinner, gbc);

        // Add filler to push everything up
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weighty = 1.0;
        contentPanel.add(Box.createVerticalGlue(), gbc);

        // Set up the button panel
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 20));
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        // Add panels to the frame
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadLoanData(Map<String, Object> loanData) {
        // Set loan ID
        loanIdField.setText(loanData.get("MaPhieu").toString());
        loanIdField.setEditable(false);

        // Set focus to loan date
        loanDateSpinner.requestFocusInWindow();

        // Set loan date
        if (loanData.get("NgayMuon") != null) {
            loanDateSpinner.setValue(loanData.get("NgayMuon"));
        }

        // Set due date
        if (loanData.get("HanTra") != null) {
            dueDateSpinner.setValue(loanData.get("HanTra"));
        }

        // Set actual return date
        if (loanData.get("NgayTraThucTe") != null) {
            actualReturnDateSpinner.setValue(loanData.get("NgayTraThucTe"));
            actualReturnDateCheckBox.setSelected(true);
        } else {
            actualReturnDateSpinner.setValue(new Date());
            actualReturnDateCheckBox.setSelected(false);
        }

        // Set status
        String status = (String) loanData.get("TrangThai");
        statusComboBox.setSelectedItem(status);

        // Set quantity
        int quantity = ((Number) loanData.get("SoLuong")).intValue();
        quantitySpinner.setValue(quantity);
    }

    private void addEventListeners() {
        // Add action listener to save button
        saveButton.addActionListener(e -> saveChanges());

        // Add action listener to cancel button
        cancelButton.addActionListener(e -> {
            setVisible(false);
            dispose();
        });

        // Add action listener to status combo box
        statusComboBox.addActionListener(e -> {
            String selectedStatus = (String) statusComboBox.getSelectedItem();
            if ("Đang mượn".equals(selectedStatus) || "Quá hạn".equals(selectedStatus)) {
                actualReturnDateCheckBox.setSelected(false);
            } else if ("Đã trả".equals(selectedStatus)) {
                actualReturnDateCheckBox.setSelected(true);
            }
            updateReturnDateState();
        });

        // Add action listener to actual return date checkbox
        actualReturnDateCheckBox.addActionListener(e -> {
            if (actualReturnDateCheckBox.isSelected()) {
                if ("Đang mượn".equals(statusComboBox.getSelectedItem()) ||
                        "Quá hạn".equals(statusComboBox.getSelectedItem())) {
                    statusComboBox.setSelectedItem("Đã trả");
                }
            }
            updateReturnDateState();
        });
    }
    
    private void updateReturnDateState() {
        // Cập nhật trạng thái của spinner ngày trả thực tế theo checkbox
        actualReturnDateSpinner.setEnabled(actualReturnDateCheckBox.isSelected());
    }

    private void saveChanges() {
        try {
            // Get values from form
            int loanId = Integer.parseInt(loanIdField.getText());
            Date loanDate = (Date) loanDateSpinner.getValue();
            Date dueDate = (Date) dueDateSpinner.getValue();
            int quantity = (Integer) quantitySpinner.getValue();
            String status = (String) statusComboBox.getSelectedItem();

            // Get actual return date
            Date actualReturnDate = null;
            if (actualReturnDateCheckBox.isSelected()) {
                actualReturnDate = (Date) actualReturnDateSpinner.getValue();
            }

            // Gọi controller để xử lý việc cập nhật
            successful = controller.updateLoan(loanId, loanDate, dueDate, actualReturnDate, status, quantity);
            
            if (successful) {
                setVisible(false);
                dispose();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    public boolean isSuccessful() {
        return successful;
    }
}