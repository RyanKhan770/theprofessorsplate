package com.TheProfessorsPlate.controller;

import com.TheProfessorsPlate.model.Cart;
import com.TheProfessorsPlate.model.Delivery;
import com.TheProfessorsPlate.model.Menu;
import com.TheProfessorsPlate.model.Order;
import com.TheProfessorsPlate.model.Payment;
import com.TheProfessorsPlate.service.CartService;
import com.TheProfessorsPlate.service.DeliveryService;
import com.TheProfessorsPlate.service.OrderService;
import com.TheProfessorsPlate.service.PaymentService;
import com.TheProfessorsPlate.util.RedirectionUtil;
import com.TheProfessorsPlate.util.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@WebServlet("/checkout")
public class CheckOutController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CartService cartService;
    private OrderService orderService;
    private PaymentService paymentService;
    private DeliveryService deliveryService;
    private RedirectionUtil redirectionUtil;
    
    @Override
    public void init() {
        cartService = new CartService();
        orderService = new OrderService();
        paymentService = new PaymentService();
        deliveryService = new DeliveryService();
        redirectionUtil = new RedirectionUtil();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Verify user is logged in
        Integer userId = (Integer) SessionUtil.getAttribute(request, "userId");
        
        if (userId == null) {
            // User not logged in, redirect to login page
            redirectionUtil.setMsgAndRedirect(request, response, "error", "Please log in to checkout", 
                                              "/login");
            return;
        }
        
        // Get cart ID from session
        Integer cartId = (Integer) SessionUtil.getAttribute(request, "cartId");
        
        if (cartId == null) {
            // No active cart, redirect to menu
            redirectionUtil.setMsgAndRedirect(request, response, "error", "No items in cart", 
                                              "/menu");
            return;
        }
        
        // Get cart details
        Cart cart = cartService.getCartById(cartId);
        request.setAttribute("cart", cart);
        
        // Get cart items
        List<Menu> cartItems = cartService.getCartItems(cartId);
        request.setAttribute("cartItems", cartItems);
        
        // Calculate total price
        double total = 0;
        for (Menu item : cartItems) {
            total += item.getFoodPrice();
        }
        request.setAttribute("totalPrice", total);
        
        // Forward to checkout.jsp
        redirectionUtil.redirectToPage(request, response, "/WEB-INF/pages/checkout.jsp");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Verify user is logged in
        Integer userId = (Integer) SessionUtil.getAttribute(request, "userId");
        
        if (userId == null) {
            // User not logged in, redirect to login page
            redirectionUtil.setMsgAndRedirect(request, response, "error", "Please log in to checkout", 
                                              "/login");
            return;
        }
        
        // Get cart ID from session
        Integer cartId = (Integer) SessionUtil.getAttribute(request, "cartId");
        
        if (cartId == null) {
            // No active cart, redirect to menu
            redirectionUtil.setMsgAndRedirect(request, response, "error", "No items in cart", 
                                              "/menu");
            return;
        }
        
        // Process checkout
        try {
            // Create payment
            String paymentMethod = request.getParameter("paymentMethod");
            double paymentAmount = Double.parseDouble(request.getParameter("totalAmount"));
            
            Payment payment = new Payment(
                new Date(),
                paymentMethod,
                "pending", // Initial status
                paymentAmount
            );
            Payment createdPayment = paymentService.createPayment(payment);
            
            if (createdPayment == null) {
                throw new Exception("Failed to create payment");
            }
            
            // Create delivery
            String deliveryAddress = request.getParameter("deliveryAddress");
            String deliveryPhone = request.getParameter("deliveryPhone");
            
            Delivery delivery = new Delivery(
                "", // Will be assigned later
                "pending", // Initial status
                deliveryPhone,
                new Date(), // Current date, will be updated
                deliveryAddress,
                createdPayment.getPaymentId()
            );
            Delivery createdDelivery = deliveryService.createDelivery(delivery);
            
            if (createdDelivery == null) {
                throw new Exception("Failed to create delivery");
            }
            
            // Create order
            List<Menu> cartItems = cartService.getCartItems(cartId);
            Order order = new Order(
                new Date(),
                "pending", // Initial status
                cartItems.size(),
                createdDelivery.getDeliveryId(),
                createdPayment.getPaymentId()
            );
            Order createdOrder = orderService.createOrder(order);
            
            if (createdOrder == null) {
                throw new Exception("Failed to create order");
            }
            
            // Update cart with order, delivery, and payment IDs
            Cart cart = cartService.getCartById(cartId);
            cart.setOrderId(createdOrder.getOrderId());
            cart.setDeliveryId(createdDelivery.getDeliveryId());
            cart.setPaymentId(createdPayment.getPaymentId());
            cart.setTotalPrice(paymentAmount);
            
            if (!cartService.updateCart(cart)) {
                throw new Exception("Failed to update cart");
            }
            
            // Process payment (in a real application, this would integrate with a payment gateway)
            if (paymentService.processPayment(createdPayment)) {
                // Update order status
                orderService.updateOrderStatus(createdOrder.getOrderId(), "confirmed");
                
                // Clear cart ID from session
                SessionUtil.removeAttribute(request, "cartId");
                
                // Redirect to success page
                redirectionUtil.setMsgAndRedirect(request, response, "success", "Order placed successfully", 
                                                 "/order?id=" + createdOrder.getOrderId());
            } else {
                throw new Exception("Payment processing failed");
            }
            
        } catch (Exception e) {
            redirectionUtil.setMsgAndRedirect(request, response, "error", "Checkout failed: " + e.getMessage(), 
                                             "/checkout");
        }
    }
}