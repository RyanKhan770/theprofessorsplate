package com.TheProfessorsPlate.controller;

import java.io.IOException;
import java.util.logging.Logger;

import com.TheProfessorsPlate.model.Order;
import com.TheProfessorsPlate.service.OrderService;
import com.TheProfessorsPlate.util.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(asyncSupported = true, urlPatterns = {"/order"})
public class OrderController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(OrderController.class.getName());
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
        
        if (userName == null) {
            request.setAttribute("error", "You must be logged in to view order details");
            request.getRequestDispatcher("/login").forward(request, response);
            return;
        }
        
        try {
            // Get order ID from request parameter
            String orderIdParam = request.getParameter("id");
            
            if (orderIdParam == null || orderIdParam.trim().isEmpty()) {
                // Check if we have a last order ID in session (for confirmation after checkout)
                Integer lastOrderId = (Integer) SessionUtil.getAttribute(request, "lastOrderId");
                
                if (lastOrderId != null) {
                    orderIdParam = lastOrderId.toString();
                    // Clear the last order ID from session
                    SessionUtil.removeAttribute(request, "lastOrderId");
                } else {
                    throw new Exception("Order ID is required");
                }
            }
            
            int orderId = Integer.parseInt(orderIdParam);
            
            // Get order details
            Order order = orderService.getOrderById(orderId);
            
            if (order == null) {
                throw new Exception("Order not found");
            }
            
            // Set order details in request
            request.setAttribute("order", order);
            
            // Forward to order detail page
            request.getRequestDispatcher("/WEB-INF/pages/order.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            logger.warning("Invalid order ID: " + e.getMessage());
            request.setAttribute("error", "Invalid order ID");
            response.sendRedirect(request.getContextPath() + "/orderHistory");
        } catch (Exception e) {
            logger.severe("Error retrieving order details: " + e.getMessage());
            request.setAttribute("error", "An error occurred while retrieving order details: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/orderHistory");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // This handles order status updates (could be used by admins)
        String userName = (String) SessionUtil.getAttribute(request, "userName");
        String userRole = (String) SessionUtil.getAttribute(request, "userRole");
        
        if (userName == null) {
            request.setAttribute("error", "You must be logged in to update order status");
            request.getRequestDispatcher("/login").forward(request, response);
            return;
        }
        
        // Only admin can update order status
        if (!"admin".equals(userRole)) {
            request.setAttribute("error", "You do not have permission to update order status");
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }
        
        try {
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            String status = request.getParameter("status");
            
            if (status == null || status.trim().isEmpty()) {
                throw new Exception("Status is required");
            }
            
            // Update order status
            boolean success = orderService.updateOrderStatus(orderId, status);
            
            if (success) {
                request.setAttribute("success", "Order status updated successfully");
                
                // Forward to order detail page
                response.sendRedirect(request.getContextPath() + "/order?id=" + orderId);
            } else {
                throw new Exception("Failed to update order status");
            }
            
        } catch (NumberFormatException e) {
            logger.warning("Invalid order ID: " + e.getMessage());
            request.setAttribute("error", "Invalid order ID");
            response.sendRedirect(request.getContextPath() + "/orderHistory");
        } catch (Exception e) {
            logger.severe("Error updating order status: " + e.getMessage());
            request.setAttribute("error", "An error occurred while updating order status: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/orderHistory");
        }
    }
    
    @Override
    public void destroy() {
        if (orderService != null) {
            orderService.close();
        }
    }
}