package com.TheProfessorsPlate.controller;

import java.io.IOException;
import java.util.logging.Logger;

import com.TheProfessorsPlate.service.PaymentService;
import com.TheProfessorsPlate.util.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(asyncSupported = true, urlPatterns = {"/payment"})
public class PaymentController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(PaymentController.class.getName());
    private PaymentService paymentService;
    
    @Override
    public void init() {
        paymentService = new PaymentService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Check if user is logged in
        String userName = (String) SessionUtil.getAttribute(request, "userName");
        
        if (userName == null) {
            request.setAttribute("error", "You must be logged in to process payments");
            request.getRequestDispatcher("/login").forward(request, response);
            return;
        }
        
        // This endpoint would normally display payment details or options
        // But since we're handling payment during checkout, we'll redirect to checkout
        response.sendRedirect(request.getContextPath() + "/checkout");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Check if user is logged in
        String userName = (String) SessionUtil.getAttribute(request, "userName");
        
        if (userName == null) {
            request.setAttribute("error", "You must be logged in to process payments");
            request.getRequestDispatcher("/login").forward(request, response);
            return;
        }
        
        try {
            // Process payment
            int paymentId = Integer.parseInt(request.getParameter("paymentId"));
            String method = request.getParameter("method");
            
            if (method == null || method.trim().isEmpty()) {
                throw new Exception("Payment method is required");
            }
            
            // Process the payment
            boolean success = paymentService.processPayment(paymentId, method);
            
            if (success) {
                // If payment was part of an order flow, redirect to order page
                String orderId = request.getParameter("orderId");
                if (orderId != null && !orderId.trim().isEmpty()) {
                    response.sendRedirect(request.getContextPath() + "/order?id=" + orderId);
                } else {
                    // Otherwise redirect to payment success page or order history
                    request.setAttribute("success", "Payment processed successfully");
                    response.sendRedirect(request.getContextPath() + "/orderHistory");
                }
            } else {
                throw new Exception("Failed to process payment");
            }
            
        } catch (NumberFormatException e) {
            logger.warning("Invalid payment ID: " + e.getMessage());
            request.setAttribute("error", "Invalid payment ID");
            response.sendRedirect(request.getContextPath() + "/checkout");
        } catch (Exception e) {
            logger.severe("Error processing payment: " + e.getMessage());
            request.setAttribute("error", "An error occurred while processing payment: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/checkout");
        }
    }
    
    @Override
    public void destroy() {
        if (paymentService != null) {
            paymentService.close();
        }
    }
}