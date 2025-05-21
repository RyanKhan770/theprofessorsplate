package com.TheProfessorsPlate.controller;

import java.io.IOException;
import java.util.List;

import com.TheProfessorsPlate.model.Menu;
import com.TheProfessorsPlate.model.Order;
import com.TheProfessorsPlate.model.Review;
import com.TheProfessorsPlate.model.User;
import com.TheProfessorsPlate.service.CustomerDashboardService;
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
    private User userService;
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
        
        // Get dashboard data
        int userId = user.getUserId();
        List<Order> recentOrders = customerDashboardService.getRecentOrdersByUserId(userId, 5);
        List<Menu> favoriteItems = customerDashboardService.getFavoriteMenuItems(userId, 4);
        List<Menu> recommendedItems = customerDashboardService.getRecommendedMenuItems(userId, 4);
        List<Review> recentReviews = customerDashboardService.getRecentReviewsByUserId(userId, 3);
        double totalSpent = customerDashboardService.getTotalSpentByUser(userId);
        
        // Set attributes
        request.setAttribute("user", user);
        request.setAttribute("recentOrders", recentOrders);
        request.setAttribute("favoriteItems", favoriteItems);
        request.setAttribute("recommendedItems", recommendedItems);
        request.setAttribute("recentReviews", recentReviews);
        request.setAttribute("totalSpent", totalSpent);
        
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
        customerDashboardService.close();
        userService.close();
    }
}