package com.TheProfessorsPlate.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;

import com.TheProfessorsPlate.model.Cart;
import com.TheProfessorsPlate.model.CartItem;
import com.TheProfessorsPlate.model.Order;
import com.TheProfessorsPlate.service.CartService;
import com.TheProfessorsPlate.service.OrderService;
import com.TheProfessorsPlate.util.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(asyncSupported = true, urlPatterns = {"/checkout"})
public class CheckOutController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(CheckOutController.class.getName());
    private CartService cartService;
    private OrderService orderService;
    
    @Override
    public void init() {
        cartService = new CartService();
        orderService = new OrderService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Check if user is logged in
        String userName = (String) SessionUtil.getAttribute(request, "userName");
        Integer userId = (Integer) SessionUtil.getAttribute(request, "userId");
        
        if (userName == null || userId == null) {
            request.setAttribute("error", "You must be logged in to checkout");
            request.getRequestDispatcher("/login").forward(request, response);
            return;
        }
        
        // Get cart ID from session
        Integer cartId = (Integer) SessionUtil.getAttribute(request, "cartId");
        
        if (cartId == null) {
            // Try to find an active cart for the user
            cartId = cartService.getActiveCartId(userId);
            
            if (cartId != null) {
                // Found an active cart, store it in session
                SessionUtil.setAttribute(request, "cartId", cartId);
            } else {
                // No active cart, redirect to cart page with message
                request.setAttribute("error", "Your cart is empty");
                request.getRequestDispatcher("/cart").forward(request, response);
                return;
            }
        }
        
        // Get cart details and items
        try {
            Cart cart = cartService.getCartById(cartId);
            List<CartItem> cartItems = cartService.getCartItems(cartId);
            
            if (cart == null || cartItems == null || cartItems.isEmpty()) {
                request.setAttribute("error", "Your cart is empty");
                request.getRequestDispatcher("/cart").forward(request, response);
                return;
            }
            
            request.setAttribute("cart", cart);
            request.setAttribute("cartItems", cartItems);
            
            // Calculate subtotal, delivery fee, and total
            BigDecimal subtotal = cart.getTotalPrice();
            BigDecimal deliveryFee = new BigDecimal("100.00"); // Fixed delivery fee
            BigDecimal total = subtotal.add(deliveryFee);
            
            request.setAttribute("subtotal", subtotal);
            request.setAttribute("deliveryFee", deliveryFee);
            request.setAttribute("total", total);
            
            // Forward to checkout page
            request.getRequestDispatcher("/WEB-INF/pages/checkOut.jsp").forward(request, response);
            
        } catch (Exception e) {
            logger.severe("Error retrieving cart for checkout: " + e.getMessage());
            request.setAttribute("error", "An error occurred while preparing your checkout");
            request.getRequestDispatcher("/cart").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Check if user is logged in
        String userName = (String) SessionUtil.getAttribute(request, "userName");
        Integer userId = (Integer) SessionUtil.getAttribute(request, "userId");
        
        if (userName == null || userId == null) {
            request.setAttribute("error", "You must be logged in to place an order");
            request.getRequestDispatcher("/login").forward(request, response);
            return;
        }
        
        // Get cart ID from session
        Integer cartId = (Integer) SessionUtil.getAttribute(request, "cartId");
        
        if (cartId == null) {
            request.setAttribute("error", "Your cart is empty");
            request.getRequestDispatcher("/cart").forward(request, response);
            return;
        }
        
        // Process checkout form
        try {
            // Get form data
            String deliveryLocation = request.getParameter("deliveryLocation");
            String deliveryPhone = request.getParameter("deliveryPhone");
            String paymentMethod = request.getParameter("paymentMethod");
            
            // Validate form data
            if (deliveryLocation == null || deliveryLocation.trim().isEmpty() ||
                deliveryPhone == null || deliveryPhone.trim().isEmpty() ||
                paymentMethod == null || paymentMethod.trim().isEmpty()) {
                
                request.setAttribute("error", "Please fill in all required fields");
                doGet(request, response);
                return;
            }
            
            // Get cart total
            Cart cart = cartService.getCartById(cartId);
            if (cart == null) {
                request.setAttribute("error", "Your cart is empty");
                request.getRequestDispatcher("/cart").forward(request, response);
                return;
            }
            
            BigDecimal subtotal = cart.getTotalPrice();
            BigDecimal deliveryFee = new BigDecimal("100.00"); // Fixed delivery fee
            BigDecimal total = subtotal.add(deliveryFee);
            
            // Create order
            Order order = orderService.createOrder(userId, cartId, deliveryLocation, 
                                                  deliveryPhone, paymentMethod, total);
            
            if (order != null) {
                // Clear the cart ID from session
                SessionUtil.removeAttribute(request, "cartId");
                
                // Store the order ID in session for the confirmation page
                SessionUtil.setAttribute(request, "lastOrderId", order.getOrderId());
                
                // Redirect to order confirmation page
                response.sendRedirect(request.getContextPath() + "/order?id=" + order.getOrderId());
            } else {
                throw new Exception("Failed to create order");
            }
            
        } catch (Exception e) {
            logger.severe("Error processing checkout: " + e.getMessage());
            request.setAttribute("error", "An error occurred while processing your order: " + e.getMessage());
            doGet(request, response);
        }
    }
    
    @Override
    public void destroy() {
        if (cartService != null) {
            cartService.close();
        }
        if (orderService != null) {
            orderService.close();
        }
    }
}