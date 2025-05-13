package com.example.view;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.sql.SQLException;

import com.example.controller.MuonSachController;
import com.example.model.Sach;
import com.example.model.ThanhVien;

public class MuonSach extends JDialog {
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
    private JLabel memberLabel;
    private JLabel bookLabel;
    private JLabel quantityLabel;
    private JLabel loanDateLabel;
    private JLabel dueDateLabel;

    private JComboBox<ThanhVien> memberComboBox;
    private JComboBox<Sach> bookComboBox;
    private JSpinner quantitySpinner;
    private JSpinner loanDateSpinner;
    private JSpinner dueDateSpinner;

    private JButton saveButton;
    private JButton cancelButton;

    // Controller
    private MuonSachController controller;
    
    // Dữ liệu
    private List<ThanhVien> thanhViens;
    private List<Sach> sachs;
    private boolean successful = false;

    public MuonSach(Frame owner) {
        super(owner, "Mượn sách", true);

        // Set up the dialog
        setSize(550, 400);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(owner);
        setResizable(false);

        // Initialize controller
        controller = new MuonSachController(this);

        // Initialize components
        initComponents();

        // Add components to the frame
        layoutComponents();

        // Load data
        loadData();

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
        titleLabel = new JLabel("Mượn sách");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 0));

        // Initialize labels
        memberLabel = new JLabel("Thành viên:");
        bookLabel = new JLabel("Sách:");
        quantityLabel = new JLabel("Số lượng:");
        loanDateLabel = new JLabel("Ngày mượn:");
        dueDateLabel = new JLabel("Hạn trả:");

        // Set label fonts
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 12);
        memberLabel.setFont(labelFont);
        bookLabel.setFont(labelFont);
        quantityLabel.setFont(labelFont);
        loanDateLabel.setFont(labelFont);
        dueDateLabel.setFont(labelFont);

        // Initialize combo boxes
        memberComboBox = new JComboBox<>();
        bookComboBox = new JComboBox<>();

        // Initialize spinners
        SpinnerNumberModel quantityModel = new SpinnerNumberModel(1, 1, 5, 1);
        quantitySpinner = new JSpinner(quantityModel);

        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.DAY_OF_MONTH, 14); // Default due date is 14 days from now
        Date dueDate = calendar.getTime();

        SpinnerDateModel loanDateModel = new SpinnerDateModel(today, null, null, Calendar.DAY_OF_MONTH);
        SpinnerDateModel dueDateModel = new SpinnerDateModel(dueDate, null, null, Calendar.DAY_OF_MONTH);

        loanDateSpinner = new JSpinner(loanDateModel);
        dueDateSpinner = new JSpinner(dueDateModel);

        JSpinner.DateEditor loanDateEditor = new JSpinner.DateEditor(loanDateSpinner, "dd/MM/yyyy");
        JSpinner.DateEditor dueDateEditor = new JSpinner.DateEditor(dueDateSpinner, "dd/MM/yyyy");

        loanDateSpinner.setEditor(loanDateEditor);
        dueDateSpinner.setEditor(dueDateEditor);

        // Set component fonts
        Font componentFont = new Font("Segoe UI", Font.PLAIN, 12);
        memberComboBox.setFont(componentFont);
        bookComboBox.setFont(componentFont);
        quantitySpinner.setFont(componentFont);
        loanDateSpinner.setFont(componentFont);
        dueDateSpinner.setFont(componentFont);

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

        // Member
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        contentPanel.add(memberLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.7;
        contentPanel.add(memberComboBox, gbc);

        // Book
        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPanel.add(bookLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        contentPanel.add(bookComboBox, gbc);

        // Quantity
        gbc.gridx = 0;
        gbc.gridy = 2;
        contentPanel.add(quantityLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        contentPanel.add(quantitySpinner, gbc);

        // Loan Date
        gbc.gridx = 0;
        gbc.gridy = 3;
        contentPanel.add(loanDateLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        contentPanel.add(loanDateSpinner, gbc);

        // Due Date
        gbc.gridx = 0;
        gbc.gridy = 4;
        contentPanel.add(dueDateLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        contentPanel.add(dueDateSpinner, gbc);

        // Add filler to push everything up
        gbc.gridx = 0;
        gbc.gridy = 5;
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

    private void loadData() {
        try {
            // Load members
            thanhViens = controller.loadThanhViens();
            memberComboBox.removeAllItems();
            for (ThanhVien thanhVien : thanhViens) {
                memberComboBox.addItem(thanhVien);
            }

            // Load books
            sachs = controller.loadSachs();
            bookComboBox.removeAllItems();
            for (Sach sach : sachs) {
                bookComboBox.addItem(sach);
            }

            // Set default dates
            Date today = new Date();
            loanDateSpinner.setValue(today);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(today);
            calendar.add(Calendar.DAY_OF_MONTH, 14);
            dueDateSpinner.setValue(calendar.getTime());

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tải dữ liệu: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void addEventListeners() {
        // Add action listener to save button
        saveButton.addActionListener(e -> {
            if (controller.saveLoan()) {
                successful = true;
                setVisible(false);
                dispose();
            }
        });

        // Add action listener to cancel button
        cancelButton.addActionListener(e -> {
            setVisible(false);
            dispose();
        });
    }
    
    // Getter methods for controller
    public ThanhVien getSelectedThanhVien() {
        return (ThanhVien) memberComboBox.getSelectedItem();
    }
    
    public Sach getSelectedSach() {
        return (Sach) bookComboBox.getSelectedItem();
    }
    
    public int getSoLuong() {
        return (Integer) quantitySpinner.getValue();
    }
    
    public Date getNgayMuon() {
        return (Date) loanDateSpinner.getValue();
    }
    
    public Date getHanTra() {
        return (Date) dueDateSpinner.getValue();
    }
    
    public boolean isSuccessful() {
        return successful;
    }
}