package com.TheProfessorsPlate.controller;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import com.TheProfessorsPlate.model.Cart;
import com.TheProfessorsPlate.model.CartItem;
import com.TheProfessorsPlate.service.CartService;
import com.TheProfessorsPlate.util.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(asyncSupported = true, urlPatterns = {"/cart"})
public class CartController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(CartController.class.getName());
    private CartService cartService;
    
    @Override
    public void init() {
        cartService = new CartService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Check if user is logged in
        String userName = (String) SessionUtil.getAttribute(request, "userName");
        Integer userId = (Integer) SessionUtil.getAttribute(request, "userId");
        
        if (userName == null || userId == null) {
            request.setAttribute("error", "You must be logged in to view your cart");
            request.getRequestDispatcher("/login").forward(request, response);
            return;
        }
        
        // Get cart ID from session or database
        Integer cartId = (Integer) SessionUtil.getAttribute(request, "cartId");
        
        if (cartId == null) {
            // Try to find an active cart for the user
            cartId = cartService.getActiveCartId(userId);
            
            if (cartId != null) {
                // Found an active cart, store it in session
                SessionUtil.setAttribute(request, "cartId", cartId);
            }
        }
        
        // If we have a cart, get the cart items
        if (cartId != null) {
            try {
                Cart cart = cartService.getCartById(cartId);
                List<CartItem> cartItems = cartService.getCartItems(cartId);
                
                request.setAttribute("cart", cart);
                request.setAttribute("cartItems", cartItems);
            } catch (Exception e) {
                logger.severe("Error retrieving cart: " + e.getMessage());
                request.setAttribute("error", "An error occurred while retrieving your cart");
            }
        }
        
        // Forward to cart page
        request.getRequestDispatcher("/WEB-INF/pages/cart.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Check if user is logged in
        String userName = (String) SessionUtil.getAttribute(request, "userName");
        Integer userId = (Integer) SessionUtil.getAttribute(request, "userId");
        
        if (userName == null || userId == null) {
            request.setAttribute("error", "You must be logged in to update your cart");
            request.getRequestDispatcher("/login").forward(request, response);
            return;
        }
        
        String action = request.getParameter("action");
        
        try {
            if ("add".equals(action)) {
                addToCart(request, response, userId);
            } else if ("remove".equals(action)) {
                removeFromCart(request, response, userId);
            } else {
                logger.warning("Invalid cart action: " + action);
                request.setAttribute("error", "Invalid cart action");
                request.getRequestDispatcher("/WEB-INF/pages/cart.jsp").forward(request, response);
            }
        } catch (Exception e) {
            logger.severe("Error processing cart action: " + e.getMessage());
            request.setAttribute("error", "An error occurred while processing your request");
            request.getRequestDispatcher("/WEB-INF/pages/cart.jsp").forward(request, response);
        }
    }
    
    private void addToCart(HttpServletRequest request, HttpServletResponse response, int userId) 
            throws ServletException, IOException {
        try {
            int foodId = Integer.parseInt(request.getParameter("foodId"));
            Integer cartId = (Integer) SessionUtil.getAttribute(request, "cartId");
            
            logger.info("Adding to cart - userId: " + userId + ", foodId: " + foodId + ", cartId: " + cartId);
            
            // If no cart exists, create a new one
            if (cartId == null) {
                logger.info("No cart found, creating new cart for user: " + userId);
                Cart newCart = cartService.createCart(userId);
                
                if (newCart != null) {
                    cartId = newCart.getCartId();
                    logger.info("New cart created with ID: " + cartId);
                    SessionUtil.setAttribute(request, "cartId", cartId);
                } else {
                    logger.severe("Failed to create new cart for user: " + userId);
                    throw new Exception("Failed to create cart");
                }
            }
            
            // Add the item to the cart - this will establish the connection in cart_details
            boolean success = cartService.addToCart(userId, foodId, cartId);
            
            if (success) {
                logger.info("Item " + foodId + " added successfully to cart ID: " + cartId);
                request.setAttribute("success", "Item added to cart successfully");
                
                // Redirect back to menu
                String referer = request.getHeader("Referer");
                if (referer != null && referer.contains("menu")) {
                    response.sendRedirect(referer);
                    return;
                }
                
                response.sendRedirect(request.getContextPath() + "/menu");
            } else {
                logger.severe("Failed to add item " + foodId + " to cart " + cartId);
                throw new Exception("Failed to add item to cart");
            }
        } catch (NumberFormatException e) {
            logger.warning("Invalid food ID: " + e.getMessage());
            request.setAttribute("error", "Invalid food ID");
            request.getRequestDispatcher("/WEB-INF/pages/cart.jsp").forward(request, response);
        } catch (Exception e) {
            logger.severe("Error adding item to cart: " + e.getMessage());
            request.setAttribute("error", "An error occurred while adding item to cart: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/pages/cart.jsp").forward(request, response);
        }
    }
    
    private void removeFromCart(HttpServletRequest request, HttpServletResponse response, int userId) 
            throws ServletException, IOException {
        try {
            int foodId = Integer.parseInt(request.getParameter("foodId"));
            Integer cartId = (Integer) SessionUtil.getAttribute(request, "cartId");
            
            if (cartId == null) {
                throw new Exception("Cart not found");
            }
            
            // Remove the item from the cart
            boolean success = cartService.removeFromCart(userId, foodId, cartId);
            
            if (success) {
                request.setAttribute("success", "Item removed from cart");
                
                // Check if the cart is now empty
                Cart cart = cartService.getCartById(cartId);
                if (cart.getCount() == 0) {
                    // If cart is empty, redirect to menu
                    response.sendRedirect(request.getContextPath() + "/menu");
                    return;
                }
                
                // Refresh the cart page
                doGet(request, response);
            } else {
                throw new Exception("Failed to remove item from cart");
            }
        } catch (NumberFormatException e) {
            logger.warning("Invalid food ID: " + e.getMessage());
            request.setAttribute("error", "Invalid food ID");
            doGet(request, response);
        } catch (Exception e) {
            logger.severe("Error removing item from cart: " + e.getMessage());
            request.setAttribute("error", "An error occurred while removing item from cart: " + e.getMessage());
            doGet(request, response);
        }
    }
    
    @Override
    public void destroy() {
        if (cartService != null) {
            cartService.close();
        }
    }
}