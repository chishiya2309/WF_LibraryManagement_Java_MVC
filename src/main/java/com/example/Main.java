package com.example;

import com.example.controller.AdminMainFormController;
import com.example.controller.LoginController;
import com.example.view.AdminMainForm;
import com.example.view.LoginForm;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Đặt look and feel của ứng dụng
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                
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
    
    // Helper method để khởi tạo AdminForm và Controller của nó
    public static void openAdminForm() {
        SwingUtilities.invokeLater(() -> {
            try {
                // Khởi tạo form admin
                AdminMainForm adminForm = new AdminMainForm();
                
                // Khởi tạo controller
                AdminMainFormController adminController = new AdminMainFormController(adminForm);
                
                // Hiển thị form
                adminForm.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
