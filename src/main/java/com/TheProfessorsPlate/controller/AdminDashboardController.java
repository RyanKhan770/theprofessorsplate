package com.TheProfessorsPlate.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.TheProfessorsPlate.model.Order;
import com.TheProfessorsPlate.model.User;
import com.TheProfessorsPlate.service.AdminDashboardService;
import com.TheProfessorsPlate.util.RedirectionUtil;
import com.TheProfessorsPlate.util.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(asyncSupported = true, urlPatterns = {"/adminDashboard"})
public class AdminDashboardController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private AdminDashboardService adminDashboardService;
    private RedirectionUtil redirectionUtil;
    
    @Override
    public void init() {
        adminDashboardService = new AdminDashboardService();
        redirectionUtil = new RedirectionUtil();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Check if user is admin
        String userRole = (String) SessionUtil.getAttribute(request, "userRole");
        
        if (userRole == null || !userRole.equals("admin")) {
            // Not an admin, redirect to login
            redirectionUtil.setMsgAndRedirect(request, response, "error", 
                    "You do not have permission to access this page", "/login");
            return;
        }
        
        // Get dashboard data
        int totalCustomers = adminDashboardService.getTotalCustomers();
        int totalOrders = adminDashboardService.getTotalOrders();
        double totalRevenue = adminDashboardService.getTotalRevenue();
        List<Order> recentOrders = adminDashboardService.getRecentOrders(5);
        List<User> recentUsers = adminDashboardService.getRecentUsers(5);
        Map<String, Integer> orderStats = adminDashboardService.getOrderStatsByStatus();
        
        // Set attributes
        request.setAttribute("totalCustomers", totalCustomers);
        request.setAttribute("totalOrders", totalOrders);
        request.setAttribute("totalRevenue", totalRevenue);
        request.setAttribute("recentOrders", recentOrders);
        request.setAttribute("recentUsers", recentUsers);
        request.setAttribute("orderStats", orderStats);
        
        // Forward to dashboard page
        redirectionUtil.redirectToPage(request, response, "/WEB-INF/pages/adminDashboard.jsp");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Check if user is admin
        String userRole = (String) SessionUtil.getAttribute(request, "userRole");
        
        if (userRole == null || !userRole.equals("admin")) {
            // Not an admin, redirect to login
            redirectionUtil.setMsgAndRedirect(request, response, "error", 
                    "You do not have permission to access this page", "/login");
            return;
        }
        
        // Get action parameter
        String action = request.getParameter("action");
        
        if ("updateRole".equals(action)) {
            // Update user role
            int userId = Integer.parseInt(request.getParameter("userId"));
            String newRole = request.getParameter("newRole");
            
            boolean success = adminDashboardService.updateUserRole(userId, newRole);
            
            if (success) {
                redirectionUtil.setMsgAndRedirect(request, response, "success", 
                        "User role updated successfully", "/adminDashboard");
            } else {
                redirectionUtil.setMsgAndRedirect(request, response, "error", 
                        "Failed to update user role", "/adminDashboard");
            }
        } else if ("deleteUser".equals(action)) {
            // Delete user
            int userId = Integer.parseInt(request.getParameter("userId"));
            
            boolean success = adminDashboardService.deleteUser(userId);
            
            if (success) {
                redirectionUtil.setMsgAndRedirect(request, response, "success", 
                        "User deleted successfully", "/adminDashboard");
            } else {
                redirectionUtil.setMsgAndRedirect(request, response, "error", 
                        "Failed to delete user, they may have associated records", "/adminDashboard");
            }
        } else {
            // Invalid action
            redirectionUtil.setMsgAndRedirect(request, response, "error", 
                    "Invalid action", "/adminDashboard");
        }
    }
    
    @Override
    public void destroy() {
        adminDashboardService.close();
    }
}