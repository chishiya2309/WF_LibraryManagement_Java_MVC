package com.example.controller;

import com.example.view.AdminMainForm;
import com.example.view.LoginForm;

import javax.swing.*;
import java.awt.event.*;
import java.util.Map;

public class AdminMainFormController {
    private AdminMainForm view;

    public AdminMainFormController(AdminMainForm view) {
        this.view = view;

        // Thêm sự kiện cho các nút menu
        setupMenuListeners();

        // Thêm sự kiện cửa sổ và thay đổi kích thước
        setupWindowListeners();
    }

    private void setupMenuListeners() {
        view.getBtnDashboard().addActionListener(e -> handleMenuClick(view.getBtnDashboard()));
        view.getBtnBooks().addActionListener(e -> handleMenuClick(view.getBtnBooks()));
        view.getBtnCategories().addActionListener(e -> handleMenuClick(view.getBtnCategories()));
        view.getBtnMembers().addActionListener(e -> handleMenuClick(view.getBtnMembers()));
        view.getBtnStaff().addActionListener(e -> handleMenuClick(view.getBtnStaff()));
        view.getBtnLoanAndReturn().addActionListener(e -> handleMenuClick(view.getBtnLoanAndReturn()));
        view.getBtnReports().addActionListener(e -> handleMenuClick(view.getBtnReports()));
        view.getBtnLogout().addActionListener(e -> logout());
    }

    private void setupWindowListeners() {
        view.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleFormClosing();
            }
        });

        view.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                handleResize();
            }
        });
    }

    private void handleMenuClick(JButton clickedButton) {
        // Set the active button
        setActiveButton(clickedButton);

        // Load the corresponding panel
        Map<JButton, JPanel> menuMapping = view.getMenuMapping();
        loadPanel(menuMapping.get(clickedButton));
    }

    private void setActiveButton(JButton button) {
        view.setActiveButtonStatus(button);
    }

    private void loadPanel(JPanel panel) {
        view.loadPanelContent(panel);
    }

    private void logout() {
        int result = JOptionPane.showConfirmDialog(
                view,
                "Bạn có chắc muốn đăng xuất?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (result == JOptionPane.YES_OPTION) {
            // Close this form
            view.dispose();

            // Khởi tạo và hiển thị form đăng nhập mới
            SwingUtilities.invokeLater(() -> {
                try {
                    // Khởi tạo view
                    LoginForm loginForm = new LoginForm();

                    // Khởi tạo controller
                    LoginController controller = new LoginController(loginForm);

                    // Hiển thị form đăng nhập
                    loginForm.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void handleFormClosing() {
        // Handle form closing logic
        System.exit(0);
    }

    private void handleResize() {
        // Adjust components if needed
        view.revalidate();
    }
}
