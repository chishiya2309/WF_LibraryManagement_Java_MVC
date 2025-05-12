package com.example.view;

import com.example.dao.DanhMucSachDAO;
import com.example.controller.FormAddCategoryController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormAddCategory extends JDialog {
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
    private JLabel categoryIdLabel;
    private JLabel categoryNameLabel;
    private JLabel descriptionLabel;
    private JLabel parentCategoryLabel;
    private JLabel bookCountLabel;
    private JLabel statusLabel;

    private JTextField categoryIdField;
    private JTextField categoryNameField;
    private JTextField descriptionField;
    private JComboBox<String> parentCategoryComboBox;
    private JTextField bookCountField;
    private JComboBox<String> statusComboBox;

    private JButton saveButton;
    private JButton cancelButton;

    // Data
    private boolean successful = false;
    
    // DAO và Controller
    private DanhMucSachDAO danhMucSachDAO;
    private FormAddCategoryController controller;

    /**
     * Constructor for the category add form
     */
    public FormAddCategory() {
        // Set up the dialog
        setTitle("Thêm Danh Mục Mới");
        setSize(500, 450);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setModal(true);

        // Initialize data access objects
        danhMucSachDAO = new DanhMucSachDAO();
        
        // Initialize components
        initComponents();

        // Add components to the frame
        layoutComponents();
        
        // Initialize controller
        controller = new FormAddCategoryController(this, danhMucSachDAO);

        // Set default values
        setDefaultValues();

        // Add event listeners
        addEventListeners();
        
        // Load parent categories
        controller.loadCategories();
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
        titleLabel = new JLabel("Thêm danh mục mới");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 0));

        // Initialize labels
        categoryIdLabel = new JLabel("Mã danh mục:");
        categoryNameLabel = new JLabel("Tên danh mục:");
        descriptionLabel = new JLabel("Mô tả:");
        parentCategoryLabel = new JLabel("Danh mục cha:");
        bookCountLabel = new JLabel("Số lượng sách:");
        statusLabel = new JLabel("Trạng thái:");

        // Set label fonts
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 12);
        categoryIdLabel.setFont(labelFont);
        categoryNameLabel.setFont(labelFont);
        descriptionLabel.setFont(labelFont);
        parentCategoryLabel.setFont(labelFont);
        bookCountLabel.setFont(labelFont);
        statusLabel.setFont(labelFont);

        // Initialize text fields
        categoryIdField = new JTextField(20);
        categoryNameField = new JTextField(20);
        descriptionField = new JTextField(20);
        parentCategoryComboBox = new JComboBox<>();
        bookCountField = new JTextField(20);
        statusComboBox = new JComboBox<>(new String[]{"Hoạt động", "Ngừng hoạt động"});

        // Set text field fonts
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 12);
        categoryIdField.setFont(fieldFont);
        categoryNameField.setFont(fieldFont);
        descriptionField.setFont(fieldFont);
        parentCategoryComboBox.setFont(fieldFont);
        bookCountField.setFont(fieldFont);
        statusComboBox.setFont(fieldFont);

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

        // Category ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        contentPanel.add(categoryIdLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.7;
        contentPanel.add(categoryIdField, gbc);

        // Category Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPanel.add(categoryNameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        contentPanel.add(categoryNameField, gbc);

        // Description
        gbc.gridx = 0;
        gbc.gridy = 2;
        contentPanel.add(descriptionLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        contentPanel.add(descriptionField, gbc);

        // Parent Category
        gbc.gridx = 0;
        gbc.gridy = 3;
        contentPanel.add(parentCategoryLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        contentPanel.add(parentCategoryComboBox, gbc);

        // Book Count
        gbc.gridx = 0;
        gbc.gridy = 4;
        contentPanel.add(bookCountLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        contentPanel.add(bookCountField, gbc);

        // Status
        gbc.gridx = 0;
        gbc.gridy = 5;
        contentPanel.add(statusLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        contentPanel.add(statusComboBox, gbc);

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
        add(new JScrollPane(contentPanel), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void setDefaultValues() {
        // Set default values
        bookCountField.setText("0");
        statusComboBox.setSelectedIndex(0); // Default to "Hoạt động"
    }

    private void addEventListeners() {
        // Add action listener to save button
        saveButton.addActionListener(e -> controller.saveCategory());

        // Add action listener to cancel button
        cancelButton.addActionListener(e -> dispose());
    }

    // Getter methods for controller
    public JTextField getCategoryIdField() {
        return categoryIdField;
    }

    public JTextField getCategoryNameField() {
        return categoryNameField;
    }

    public JTextField getDescriptionField() {
        return descriptionField;
    }

    public JComboBox<String> getParentCategoryComboBox() {
        return parentCategoryComboBox;
    }

    public JTextField getBookCountField() {
        return bookCountField;
    }

    public JComboBox<String> getStatusComboBox() {
        return statusComboBox;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }
}