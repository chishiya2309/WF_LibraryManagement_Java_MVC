package com.example.controller;

import com.example.dao.DashboardDAO;
import com.example.model.DashboardStatistics;
import com.example.view.AdminControlDashboard;

public class DashboardController {
    private AdminControlDashboard view;
    private DashboardDAO dashboardDAO;
    
    public DashboardController(AdminControlDashboard view) {
        this.view = view;
        this.dashboardDAO = new DashboardDAO();
    }
    
    public void loadDashboardData() {
        try {
            // Lấy dữ liệu từ DAO
            DashboardStatistics stats = dashboardDAO.getOverviewStatistics();
            
            // Cập nhật giao diện
            view.updateStatistics(stats);
        } catch (Exception e) {
            System.err.println("Lỗi khi tải dữ liệu Dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public DashboardStatistics getDashboardStatistics() {
        return dashboardDAO.getOverviewStatistics();
    }
}
