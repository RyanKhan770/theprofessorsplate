package com.TheProfessorsPlate.controller;

import com.TheProfessorsPlate.model.Cart;
import com.TheProfessorsPlate.model.Menu;
import com.TheProfessorsPlate.service.CartService;
import com.TheProfessorsPlate.util.RedirectionUtil;
import com.TheProfessorsPlate.util.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(asyncSupported = true, urlPatterns = {"/cart"})
public class CartController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CartService cartService;
    private RedirectionUtil redirectionUtil;
    
    @Override
    public void init() {
        cartService = new CartService();
        redirectionUtil = new RedirectionUtil();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get cart ID from session
        Integer cartId = (Integer) SessionUtil.getAttribute(request, "cartId");
        
        if (cartId != null) {
            // Get cart details
            Cart cart = cartService.getCartById(cartId);
            request.setAttribute("cart", cart);
            
            // Get cart items
            List<Menu> cartItems = cartService.getCartItems(cartId);
            request.setAttribute("cartItems", cartItems);
        }
        
        // Forward to cart.jsp
        redirectionUtil.redirectToPage(request, response, "/WEB-INF/pages/cart.jsp");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get user ID from session
        Integer userId = (Integer) SessionUtil.getAttribute(request, "userId");
        
        if (userId == null) {
            // User not logged in, redirect to login page
            redirectionUtil.setMsgAndRedirect(request, response, "error", "Please log in to add items to cart", 
                                              "/login");
            return;
        }
        
        String action = request.getParameter("action");
        
        if ("add".equals(action)) {
            // Add item to cart
            int foodId = Integer.parseInt(request.getParameter("foodId"));
            Integer cartId = (Integer) SessionUtil.getAttribute(request, "cartId");
            
            if (cartId == null) {
                // Create a new cart
                Cart newCart = new Cart();
                newCart.setCount(1);
                newCart.setTotalPrice(0); // Will be updated later
                
                // For now, set defaults for required foreign keys
                // These would be updated when placing the order
                newCart.setOrderId(0);
                newCart.setDeliveryId(0);
                newCart.setPaymentId(0);
                
                Cart createdCart = cartService.createCart(newCart);
                
                if (createdCart != null) {
                    cartId = createdCart.getCartId();
                    SessionUtil.setAttribute(request, "cartId", cartId);
                } else {
                    redirectionUtil.setMsgAndRedirect(request, response, "error", "Failed to create cart", 
                                                     "/menu");
                    return;
                }
            }
            
            // Add item to cart
            if (cartService.addToCart(userId, foodId, cartId)) {
                redirectionUtil.setMsgAndRedirect(request, response, "success", "Item added to cart", 
                                                 "/menu");
            } else {
                redirectionUtil.setMsgAndRedirect(request, response, "error", "Failed to add item to cart", 
                                                 "/menu");
            }
        } else if ("remove".equals(action)) {
            // Remove item from cart
            int foodId = Integer.parseInt(request.getParameter("foodId"));
            Integer cartId = (Integer) SessionUtil.getAttribute(request, "cartId");
            
            if (cartId != null) {
                if (cartService.removeFromCart(userId, foodId, cartId)) {
                    redirectionUtil.setMsgAndRedirect(request, response, "success", "Item removed from cart", 
                                                     "/cart");
                } else {
                    redirectionUtil.setMsgAndRedirect(request, response, "error", "Failed to remove item from cart", 
                                                     "/cart");
                }
            } else {
                redirectionUtil.setMsgAndRedirect(request, response, "error", "No active cart found", 
                                                 "/menu");
            }
        }
    }
}