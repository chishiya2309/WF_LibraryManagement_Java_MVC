package com.example.controller;

import com.example.view.AdminMainForm;
import com.example.view.LoginForm;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginController {
    private LoginForm view;

    public LoginController(LoginForm view) {
        this.view = view;

        // Thêm sự kiện cho nút login
        view.getBtnLogin().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        
        // Thêm sự kiện phím Enter cho ô mật khẩu
        view.getTxtPassword().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    login();
                }
            }
        });
        
        // Thêm sự kiện cho liên kết "Quên mật khẩu"
        view.getForgotPasswordLink().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleForgotPassword();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                view.getForgotPasswordLink().setText("<html><u>Forgot Password?</u></html>");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                view.getForgotPasswordLink().setText("Forgot Password?");
            }
        });
        
        // Thêm các sự kiện khác
        view.addEventListeners();
    }

    private void login() {
        String username = view.getTxtUsername().getText().trim();
        String password = new String(view.getTxtPassword().getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            view.getErrorLabel().setText("Vui lòng nhập đầy đủ mã nhân viên và mật khẩu!");
            return;
        }

        if (authenticateLibraryStaff(username, password)) {
            if (view.getRememberMeCheckBox().isSelected()) {
                view.saveStaffCredentials(username);
            } else {
                view.clearSavedCredentials();
            }

            JOptionPane.showMessageDialog(view,
                    "Đăng nhập thành công! Chào mừng đến với hệ thống quản lý thư viện.",
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);

            // Ẩn form đăng nhập
            view.dispose();
            
            // Mở form Admin sử dụng helper method
            com.example.Main.openAdminForm();
        } else {
            view.getErrorLabel().setText("<html>ID nhân viên hoặc mật khẩu không chính xác.<br/> Vui lòng thử lại!</html>");
        }
    }
    
    private void handleForgotPassword() {
        JOptionPane.showMessageDialog(view,
                "Vui lòng liên hệ với quản trị viên hệ thống để đặt lại mật khẩu của bạn.",
                "Forgot Password",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private boolean authenticateLibraryStaff(String staffId, String password) {
        // This is a simple authentication for demonstration
        // In a real application, you would connect to a database
        return (staffId.equals("admin") && password.equals("admin"));
    }
}
