package com.example.view;

import com.example.controller.FormEditMemberController;
import com.example.dao.ThanhVienDAO;
import com.example.model.ThanhVien;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class FormEditMember extends JFrame {
    // UI Components
    private JPanel contentPane;
    private JPanel headerPanel;
    private JPanel footerPanel;
    private JLabel lblTitle;
    private JTextField txtMaThanhVien;
    private JTextField txtHoTen;
    private JComboBox<String> cmbGioiTinh;
    private JTextField txtSoDienThoai;
    private JTextField txtEmail;
    private JComboBox<String> cmbLoaiThanhVien;
    private JSpinner dtpNgayDangKy;
    private JSpinner dtpNgayHetHan;
    private JComboBox<String> cmbTrangThai;
    private JButton btnSave;
    private JButton btnCancel;

    // Data
    private String maThanhVien;
    private ThanhVienDAO thanhVienDAO;
    private boolean successful = false;
    
    // Controller
    private FormEditMemberController controller;

    /**
     * Create the frame.
     */
    public FormEditMember(String maThanhVien) {
        this.maThanhVien = maThanhVien;
        this.thanhVienDAO = new ThanhVienDAO();

        setTitle("Chỉnh sửa thông tin thành viên");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 550, 560);
        setResizable(false);
        setLocationRelativeTo(null);

        // Initialize components
        initComponents();
        
        // Initialize controller
        controller = new FormEditMemberController(this, thanhVienDAO);

        // Load data
        controller.loadMemberData(maThanhVien);
    }

    private void initComponents() {
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        // Header Panel
        headerPanel = new JPanel();
        headerPanel.setBackground(new Color(76, 40, 130));
        headerPanel.setPreferredSize(new Dimension(550, 60));
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));

        lblTitle = new JLabel("Sửa thành viên");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle);

        contentPane.add(headerPanel, BorderLayout.NORTH);

        // Main Panel with Form Fields
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Ma Thanh Vien
        addFormField(mainPanel, gbc, 0, "Mã thành viên:", txtMaThanhVien = new JTextField());
        txtMaThanhVien.setEditable(false);

        // Ho Ten
        addFormField(mainPanel, gbc, 1, "Họ tên:", txtHoTen = new JTextField());

        // Gioi Tinh
        cmbGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        addFormField(mainPanel, gbc, 2, "Giới tính:", cmbGioiTinh);

        // So Dien Thoai
        addFormField(mainPanel, gbc, 3, "Số điện thoại:", txtSoDienThoai = new JTextField());

        // Email
        addFormField(mainPanel, gbc, 4, "Email:", txtEmail = new JTextField());

        // Loai Thanh Vien
        cmbLoaiThanhVien = new JComboBox<>(new String[]{"Sinh viên", "Giảng viên", "Thường"});
        addFormField(mainPanel, gbc, 5, "Loại thành viên:", cmbLoaiThanhVien);

        // Ngay Dang Ky
        SpinnerDateModel dateModel1 = new SpinnerDateModel();
        dtpNgayDangKy = new JSpinner(dateModel1);
        JSpinner.DateEditor dateEditor1 = new JSpinner.DateEditor(dtpNgayDangKy, "dd/MM/yyyy");
        dtpNgayDangKy.setEditor(dateEditor1);
        addFormField(mainPanel, gbc, 6, "Ngày đăng ký:", dtpNgayDangKy);

        // Ngay Het Han
        SpinnerDateModel dateModel2 = new SpinnerDateModel();
        dtpNgayHetHan = new JSpinner(dateModel2);
        JSpinner.DateEditor dateEditor2 = new JSpinner.DateEditor(dtpNgayHetHan, "dd/MM/yyyy");
        dtpNgayHetHan.setEditor(dateEditor2);
        addFormField(mainPanel, gbc, 7, "Ngày hết hạn:", dtpNgayHetHan);

        // Trang Thai
        cmbTrangThai = new JComboBox<>(new String[]{"Hoạt động", "Hết hạn", "Khóa"});
        addFormField(mainPanel, gbc, 8, "Trạng thái:", cmbTrangThai);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        // Footer Panel
        footerPanel = new JPanel();
        footerPanel.setBackground(new Color(240, 240, 240));
        footerPanel.setPreferredSize(new Dimension(550, 60));
        footerPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        btnCancel = new JButton("Hủy");
        btnCancel.setPreferredSize(new Dimension(100, 40));
        btnCancel.setBackground(new Color(200, 200, 200));
        btnCancel.setForeground(Color.BLACK);
        btnCancel.setFocusPainted(false);
        btnCancel.setBorderPainted(false);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        btnSave = new JButton("Lưu");
        btnSave.setPreferredSize(new Dimension(124, 40));
        btnSave.setBackground(new Color(76, 40, 130));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        btnSave.setBorderPainted(false);
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.saveMember();
            }
        });

        footerPanel.add(btnCancel);
        footerPanel.add(btnSave);

        contentPane.add(footerPanel, BorderLayout.SOUTH);
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent component) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        component.setPreferredSize(new Dimension(300, 25));
        panel.add(component, gbc);
    }

    // Getter methods for controller to access form fields
    public JTextField getTxtMaThanhVien() {
        return txtMaThanhVien;
    }

    public JTextField getTxtHoTen() {
        return txtHoTen;
    }

    public JComboBox<String> getCmbGioiTinh() {
        return cmbGioiTinh;
    }

    public JTextField getTxtSoDienThoai() {
        return txtSoDienThoai;
    }

    public JTextField getTxtEmail() {
        return txtEmail;
    }

    public JComboBox<String> getCmbLoaiThanhVien() {
        return cmbLoaiThanhVien;
    }

    public JSpinner getDtpNgayDangKy() {
        return dtpNgayDangKy;
    }

    public JSpinner getDtpNgayHetHan() {
        return dtpNgayHetHan;
    }

    public JComboBox<String> getCmbTrangThai() {
        return cmbTrangThai;
    }

    public boolean isSuccessful() {
        return controller.isSuccessful();
    }
}
