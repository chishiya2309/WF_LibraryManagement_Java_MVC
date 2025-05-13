package com.example.view;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.text.SimpleDateFormat;

import com.example.controller.TraSachController;
import com.example.model.PhieuMuon;

public class TraSach extends JDialog {
    // Colors
    private final Color PURPLE = new Color(76, 40, 130);
    private final Color LIGHT_GRAY = new Color(240, 240, 240);
    private final Color WHITE = Color.WHITE;
    private final Color RED = new Color(200, 0, 0);

    // Components
    private JPanel mainPanel;
    private JPanel headerPanel;
    private JPanel contentPanel;
    private JPanel buttonPanel;

    private JLabel titleLabel;
    private JLabel loanIdLabel;
    private JLabel returnDateLabel;
    private JLabel loanInfoLabel;
    private JLabel returnDateInfoLabel;

    private JTextField loanIdField;
    private JSpinner returnDateSpinner;

    private JButton saveButton;
    private JButton cancelButton;

    // Controller và data
    private TraSachController controller;
    private PhieuMuon currentLoan;
    private boolean successful;

    /**
     * Constructor for the book return form
     * @param owner The parent frame
     * @param loan The loan to be returned
     */
    public TraSach(Frame owner, PhieuMuon loan) {
        super(owner, "Trả sách", true);

        // Set up the dialog
        setSize(550, 350);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(owner);
        setResizable(false);

        // Initialize data
        currentLoan = loan;
        
        // Initialize components
        initComponents();

        // Add components to the frame
        layoutComponents();
        
        // Initialize controller
        controller = new TraSachController(this, loan);

        // Kiểm tra trạng thái phiếu mượn
        if (!controller.checkLoanStatus()) {
            setVisible(false);
            dispose();
            return;
        }

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
        titleLabel = new JLabel("Trả sách");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 0));

        // Initialize labels
        loanIdLabel = new JLabel("Mã phiếu:");
        returnDateLabel = new JLabel("Ngày trả:");
        loanInfoLabel = new JLabel();
        returnDateInfoLabel = new JLabel();

        // Set label fonts
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 12);
        loanIdLabel.setFont(labelFont);
        returnDateLabel.setFont(labelFont);

        Font infoFont = new Font("Segoe UI", Font.PLAIN, 11);
        loanInfoLabel.setFont(infoFont);

        Font italicFont = new Font("Segoe UI", Font.ITALIC, 11);
        returnDateInfoLabel.setFont(italicFont);
        returnDateInfoLabel.setForeground(new Color(68, 68, 68));

        // Initialize text field
        loanIdField = new JTextField(20);
        loanIdField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        loanIdField.setEditable(false);
        loanIdField.setBackground(new Color(240, 240, 240));

        // Initialize date spinner
        Date today = new Date();
        SpinnerDateModel returnDateModel = new SpinnerDateModel(today, null, null, Calendar.DAY_OF_MONTH);
        returnDateSpinner = new JSpinner(returnDateModel);
        JSpinner.DateEditor returnDateEditor = new JSpinner.DateEditor(returnDateSpinner, "dd/MM/yyyy");
        returnDateSpinner.setEditor(returnDateEditor);
        returnDateSpinner.setFont(new Font("Segoe UI", Font.PLAIN, 12));

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

        // Loan ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        contentPanel.add(loanIdLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.7;
        contentPanel.add(loanIdField, gbc);

        // Return Date
        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPanel.add(returnDateLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        contentPanel.add(returnDateSpinner, gbc);

        // Loan Info
        gbc.gridx = 1;
        gbc.gridy = 2;
        contentPanel.add(loanInfoLabel, gbc);

        // Return Date Info
        gbc.gridx = 1;
        gbc.gridy = 3;
        contentPanel.add(returnDateInfoLabel, gbc);

        // Add filler to push everything up
        gbc.gridx = 0;
        gbc.gridy = 4;
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
        // Display loan ID
        loanIdField.setText(String.valueOf(currentLoan.getMaPhieu()));

        // Display loan information
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dueDate = dateFormat.format(currentLoan.getHanTra());
        loanInfoLabel.setText("Hạn trả: " + dueDate + ", Trạng thái: " + currentLoan.getTrangThai());

        // Check if overdue
        if (new Date().after(currentLoan.getHanTra()) && "Quá hạn".equals(currentLoan.getTrangThai())) {
            loanInfoLabel.setForeground(RED);
        }

        // Set return date to today
        returnDateSpinner.setValue(new Date());

        // Display return date info
        returnDateInfoLabel.setText("Lưu ý: Ngày trả thực tế sẽ được ghi nhận khi bạn ấn nút 'Lưu'");
    }

    private void addEventListeners() {
        // Add action listener to save button
        saveButton.addActionListener(e -> returnBook());

        // Add action listener to cancel button
        cancelButton.addActionListener(e -> {
            setVisible(false);
            dispose();
        });
    }

    private void returnBook() {
        // Sử dụng controller để xử lý trả sách
        successful = controller.returnBook((Date) returnDateSpinner.getValue());
        
        if (successful) {
            // Đóng form nếu thành công
            setVisible(false);
            dispose();
        }
    }

    public boolean isSuccessful() {
        return successful;
    }

    // Main method for testing
    public static void main(String[] args) {
        // Set the look and feel to the system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create sample loan data for testing
        PhieuMuon sampleLoan = new PhieuMuon();
        sampleLoan.setMaPhieu(1001);
        sampleLoan.setNgayMuon(new java.sql.Date(System.currentTimeMillis()));

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 14);
        sampleLoan.setHanTra(new java.sql.Date(calendar.getTimeInMillis()));

        sampleLoan.setTrangThai("Đang mượn");
        
        // Create and display the form
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            TraSach form = new TraSach(frame, sampleLoan);
            form.setVisible(true);
        });
    }
}