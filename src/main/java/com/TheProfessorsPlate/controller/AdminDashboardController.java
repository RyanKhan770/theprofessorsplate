package com.TheProfessorsPlate.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.TheProfessorsPlate.model.Order;
import com.TheProfessorsPlate.model.User;
import com.TheProfessorsPlate.model.Menu;
import com.TheProfessorsPlate.service.AdminDashboardService;
import com.TheProfessorsPlate.service.UserService;
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
    private UserService userService;
    private RedirectionUtil redirectionUtil;
    
    @Override
    public void init() {
        adminDashboardService = new AdminDashboardService();
        userService = new UserService();
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
        
        // Get user information
        String userName = (String) SessionUtil.getAttribute(request, "userName");
        User user = userService.getUserByUsername(userName);
        
        if (user == null) {
            // User not found, redirect to login
            redirectionUtil.setMsgAndRedirect(request, response, "error", 
                    "User not found", "/login");
            return;
        }
        
        // Get dashboard data
        int totalCustomers = adminDashboardService.getTotalCustomers();
        int totalOrders = adminDashboardService.getTotalOrders();
        double totalRevenue = adminDashboardService.getTotalRevenue();
        int totalMenuItems = adminDashboardService.getTotalMenuItems();
        
        // Get recent orders with customer info
        List<Order> recentOrders = adminDashboardService.getRecentOrders(5);
        
        // Get recent users
        List<User> recentUsers = adminDashboardService.getRecentUsers(5);
        
        // Get recent products
        List<Menu> recentProducts = adminDashboardService.getRecentMenuItems(4);
        
        // Get order stats by status
        Map<String, Integer> orderStats = adminDashboardService.getOrderStatsByStatus();
        
        // Get weekly sales data
        Map<String, Double> weeklySales = adminDashboardService.getWeeklySales();
        
        // Set current date and time for display
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateTime = "2025-05-21 20:58:42"; // Using provided fixed date
        
        // Set attributes
        request.setAttribute("user", user);
        request.setAttribute("totalCustomers", totalCustomers);
        request.setAttribute("totalOrders", totalOrders);
        request.setAttribute("totalRevenue", totalRevenue);
        request.setAttribute("totalMenuItems", totalMenuItems);
        request.setAttribute("recentOrders", recentOrders);
        request.setAttribute("recentUsers", recentUsers);
        request.setAttribute("recentProducts", recentProducts);
        request.setAttribute("orderStats", orderStats);
        request.setAttribute("weeklySales", weeklySales);
        request.setAttribute("currentDateTime", currentDateTime);
        
        // Forward to dashboard page
        redirectionUtil.redirectToPage(request, response, "/WEB-INF/pages/adminDashboard.jsp");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Forward to doGet by default
        doGet(request, response);
    }
    
    @Override
    public void destroy() {
        if (adminDashboardService != null) {
            adminDashboardService.close();
        }
        if (userService != null) {
            userService.close();
        }
    }
}