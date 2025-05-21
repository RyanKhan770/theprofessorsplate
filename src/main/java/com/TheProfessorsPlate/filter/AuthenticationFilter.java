package com.TheProfessorsPlate.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import com.TheProfessorsPlate.util.CookieUtil;
import com.TheProfessorsPlate.util.SessionUtil;

/**
 * Filter for authentication and authorization control across the application.
 * Controls access to different pages based on user role and authentication status.
 */
@WebFilter(asyncSupported = true, urlPatterns = "/*")
public class AuthenticationFilter implements Filter {
    private static final Logger logger = Logger.getLogger(AuthenticationFilter.class.getName());
    
    // Path constants
    private static final String LOGIN = "/login";
    private static final String REGISTER = "/register";
    private static final String HOME = "/home";
    private static final String ROOT = "/";
    private static final String ADMIN_DASHBOARD = "/adminDashboard";
    private static final String CUSTOMER_DASHBOARD = "/customerDashboard";
    private static final String MENU = "/menu";
    private static final String ABOUT = "/aboutUs";
    private static final String PROFILE = "/profile";
    private static final String CONTACT = "/contactUs";
    private static final String ORDER_HISTORY = "/orderHistory";
    private static final String CART = "/cart";
    private static final String CHECKOUT = "/checkout";
    
    // Public resources that don't require authentication
    private static final Set<String> PUBLIC_RESOURCES = new HashSet<>(Arrays.asList(
        LOGIN, REGISTER, HOME, ROOT, MENU, ABOUT, CONTACT
    ));
    
    // Resource file extensions that should always be accessible
    private static final Set<String> RESOURCE_EXTENSIONS = new HashSet<>(Arrays.asList(
        ".css", ".js", ".png", ".jpg", ".jpeg", ".gif", ".svg", ".ico", ".woff", ".woff2"
    ));

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("Authentication filter initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        
        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();
        
        // Remove context path to get clean URI
        if (uri.startsWith(contextPath)) {
            uri = uri.substring(contextPath.length());
        }
        
        // Allow access to all static resources
        if (isResourceFile(uri)) {
            chain.doFilter(request, response);
            return;
        }
        
        // Check if this is an API request that should use different auth
        if (uri.startsWith("/api/")) {
            // For API endpoints, you might want to implement token-based auth
            // But for now, we'll just allow them through
            chain.doFilter(request, response);
            return;
        }
        
        // Get authentication status from session (not cookie)
        String userName = (String) SessionUtil.getAttribute(req, "userName");
        String userRole = (String) SessionUtil.getAttribute(req, "userRole");
        
        boolean isAuthenticated = userName != null;
        
        logger.fine("Request URI: " + uri + ", Authenticated: " + isAuthenticated + 
                   ", Role: " + userRole);
        
        if (isAuthenticated) {
            // User is logged in
            if ("admin".equals(userRole)) {
                handleAdminAccess(req, res, chain, uri);
            } else if ("customer".equals(userRole)) {
                handleCustomerAccess(req, res, chain, uri);
            } else {
                // Unknown role - treat as customer
                handleCustomerAccess(req, res, chain, uri);
            }
        } else {
            // User is not logged in - only allow access to public pages
            if (PUBLIC_RESOURCES.contains(uri) || isDefaultServletResource(uri)) {
                chain.doFilter(request, response);
            } else {
                logger.info("Unauthenticated access attempt to: " + uri + " - redirecting to login");
                res.sendRedirect(req.getContextPath() + LOGIN);
            }
        }
    }
    
    /**
     * Handle access control for admin users
     */
    private void handleAdminAccess(HttpServletRequest req, HttpServletResponse res, 
                                  FilterChain chain, String uri) 
            throws IOException, ServletException {
        if (uri.equals(LOGIN) || uri.equals(REGISTER)) {
            // Admin trying to access login/register - redirect to admin dashboard
            res.sendRedirect(req.getContextPath() + ADMIN_DASHBOARD);
        } else if (uri.equals(CART) || uri.equals(CHECKOUT) || uri.equals(CUSTOMER_DASHBOARD)) {
            // Admin shouldn't access customer-specific pages
            res.sendRedirect(req.getContextPath() + ADMIN_DASHBOARD);
        } else {
            // Admin can access all other pages
            chain.doFilter(req, res);
        }
    }
    
    /**
     * Handle access control for customer users
     */
    private void handleCustomerAccess(HttpServletRequest req, HttpServletResponse res, 
                                     FilterChain chain, String uri) 
            throws IOException, ServletException {
        if (uri.equals(LOGIN) || uri.equals(REGISTER)) {
            // Customer trying to access login/register - redirect to home
            res.sendRedirect(req.getContextPath() + HOME);
        } else if (uri.equals(ADMIN_DASHBOARD)) {
            // Customer trying to access admin dashboard - redirect to home
            res.sendRedirect(req.getContextPath() + HOME);
        } else {
            // Customer can access all other pages
            chain.doFilter(req, res);
        }
    }
    
    /**
     * Checks if the URI corresponds to a static resource file
     */
    private boolean isResourceFile(String uri) {
        if (uri == null) return false;
        
        // Check for common resource file extensions
        for (String extension : RESOURCE_EXTENSIONS) {
            if (uri.endsWith(extension)) {
                return true;
            }
        }
        
        // Also check resource directories
        return uri.startsWith("/css/") || 
               uri.startsWith("/js/") || 
               uri.startsWith("/images/") || 
               uri.startsWith("/fonts/") || 
               uri.startsWith("/resources/");
    }
    
    /**
     * Checks if the URI might be a default servlet resource
     */
    private boolean isDefaultServletResource(String uri) {
        return uri.startsWith("/favicon.ico") || 
               uri.startsWith("/robots.txt");
    }

    @Override
    public void destroy() {
        logger.info("Authentication filter destroyed");
    }
}