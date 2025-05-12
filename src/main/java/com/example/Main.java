package com.example;

import com.example.controller.AdminMainFormController;
import com.example.controller.LoginController;
import com.example.view.AdminMainForm;
import com.example.view.LoginForm;

import javax.swing.*;
import java.awt.*;
import javax.swing.plaf.FontUIResource;

public class Main {
    public static void main(String[] args) {
        // Đặt encoding cho JVM
        System.setProperty("file.encoding", "UTF-8");
        
        SwingUtilities.invokeLater(() -> {
            try {
                // Đặt look and feel của ứng dụng
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                
                // Cấu hình font mặc định hỗ trợ Unicode
                setUIFont(new FontUIResource(new Font("Segoe UI", Font.PLAIN, 12)));
                
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
    
    // Phương thức để đặt font mặc định cho tất cả các thành phần UI
    private static void setUIFont(FontUIResource font) {
        java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, font);
            }
        }
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
