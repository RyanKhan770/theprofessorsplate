package com.TheProfessorsPlate.controller;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import com.TheProfessorsPlate.model.Order;
import com.TheProfessorsPlate.service.OrderService;
import com.TheProfessorsPlate.util.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(asyncSupported = true, urlPatterns = {"/orderHistory"})
public class OrderHistoryController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(OrderHistoryController.class.getName());
    private OrderService orderService;
    
    @Override
    public void init() {
        orderService = new OrderService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Check if user is logged in
        String userName = (String) SessionUtil.getAttribute(request, "userName");
        Integer userId = (Integer) SessionUtil.getAttribute(request, "userId");
        
        if (userName == null || userId == null) {
            request.setAttribute("error", "You must be logged in to view your order history");
            request.getRequestDispatcher("/login").forward(request, response);
            return;
        }
        
        try {
            // Get orders for the user
            List<Order> orders = orderService.getOrdersByUserId(userId);
            
            request.setAttribute("orders", orders);
            
            // Forward to order history page
            request.getRequestDispatcher("/WEB-INF/pages/orderHistory.jsp").forward(request, response);
            
        } catch (Exception e) {
            logger.severe("Error retrieving order history: " + e.getMessage());
            request.setAttribute("error", "An error occurred while retrieving your order history");
            request.getRequestDispatcher("/WEB-INF/pages/orderHistory.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // We'll just call doGet since we don't need any POST-specific handling
        doGet(request, response);
    }
    
    @Override
    public void destroy() {
        if (orderService != null) {
            orderService.close();
        }
    }
}