package com.TheProfessorsPlate.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.TheProfessorsPlate.model.Menu;
import com.TheProfessorsPlate.model.Order;
import com.TheProfessorsPlate.model.Review;
import com.TheProfessorsPlate.model.User;
import com.TheProfessorsPlate.service.CustomerDashboardService;
import com.TheProfessorsPlate.service.UserService;
import com.TheProfessorsPlate.util.RedirectionUtil;
import com.TheProfessorsPlate.util.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(asyncSupported = true, urlPatterns = {"/customerDashboard"})
public class CustomerDashboardController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CustomerDashboardService customerDashboardService;
    private UserService userService;
    private RedirectionUtil redirectionUtil;
    
    @Override
    public void init() {
        customerDashboardService = new CustomerDashboardService();
        userService = new UserService();
        redirectionUtil = new RedirectionUtil();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Check if user is logged in
        String userName = (String) SessionUtil.getAttribute(request, "userName");
        
        if (userName == null) {
            // Not logged in, redirect to login
            redirectionUtil.setMsgAndRedirect(request, response, "error", 
                    "Please log in to view your dashboard", "/login");
            return;
        }
        
        // Get user information
        User user = userService.getUserByUsername(userName);
        
        if (user == null) {
            // User not found, redirect to login
            redirectionUtil.setMsgAndRedirect(request, response, "error", 
                    "User not found", "/login");
            return;
        }
        
        // Get basic dashboard data
        int userId = user.getUserId();
        List<Order> recentOrders = customerDashboardService.getRecentOrdersByUserId(userId, 5);
        List<Menu> favoriteItems = customerDashboardService.getFavoriteMenuItems(userId, 4);
        List<Menu> recommendedItems = customerDashboardService.getRecommendedMenuItems(userId, 4);
        List<Review> recentReviews = customerDashboardService.getRecentReviewsByUserId(userId, 3);
        double totalSpent = customerDashboardService.getTotalSpentByUser(userId);
        
        // Get enhanced analytics data
        Map<String, Integer> orderCountByStatus = customerDashboardService.getOrderCountByStatus(userId);
        Map<String, Double> monthlySpending = customerDashboardService.getMonthlySpending(userId);
        Map<String, Double> spendingByCategory = customerDashboardService.getSpendingByCategory(userId);
        List<Map<String, Object>> recentActivity = customerDashboardService.getRecentActivity(userId, 10);
        List<Map<String, Object>> mostOrderedItems = customerDashboardService.getMostOrderedItems(userId, 3);
        
        // Calculate average order value
        double avgOrderValue = 0;
        int totalOrders = recentOrders.size();
        if (totalOrders > 0) {
            avgOrderValue = totalSpent / totalOrders;
        }
        
        // Format data for charts without using Gson
        // Pass the raw data to the JSP and use JavaScript to format it
        
        // Set current date and time in the specified format
        String currentDateTime = "2025-05-21 20:27:06";
        
        // Set attributes for the JSP
        request.setAttribute("user", user);
        request.setAttribute("recentOrders", recentOrders);
        request.setAttribute("favoriteItems", favoriteItems);
        request.setAttribute("recommendedItems", recommendedItems);
        request.setAttribute("recentReviews", recentReviews);
        request.setAttribute("totalSpent", totalSpent);
        
        // Set enhanced data attributes
        request.setAttribute("orderCountByStatus", orderCountByStatus);
        request.setAttribute("monthlySpending", monthlySpending);
        request.setAttribute("spendingByCategory", spendingByCategory);
        request.setAttribute("recentActivity", recentActivity);
        request.setAttribute("mostOrderedItems", mostOrderedItems);
        request.setAttribute("avgOrderValue", avgOrderValue);
        request.setAttribute("totalOrders", totalOrders);
        request.setAttribute("currentDateTime", currentDateTime);
        
        // Forward to dashboard page
        redirectionUtil.redirectToPage(request, response, "/WEB-INF/pages/customerDashboard.jsp");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Forward to doGet by default
        doGet(request, response);
    }
    
    @Override
    public void destroy() {
        if (customerDashboardService != null) {
            customerDashboardService.close();
        }
        if (userService != null) {
            userService.close();
        }
    }
}