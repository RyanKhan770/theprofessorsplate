package com.TheProfessorsPlate.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.TheProfessorsPlate.util.CookieUtil;
import com.TheProfessorsPlate.util.SessionUtil;

/**
 * Servlet implementation class LogoutController
 */
@WebServlet(asyncSupported = true, urlPatterns = {"/logout"})
public class LogoutController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Handles GET requests for logout
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        handleLogout(request, response);
    }

    /**
     * Handles POST requests for logout
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        handleLogout(request, response);
    }

    /**
     * Common method to handle logout logic
     */
    private void handleLogout(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        // Clear all cookies
        CookieUtil.deleteCookie(response, "userRole");
        CookieUtil.deleteCookie(response, "userName");
        
        // Clear session
        SessionUtil.removeAttribute(request, "userName");
        SessionUtil.invalidateSession(request);
        
        // Redirect to login page
        response.sendRedirect(request.getContextPath() + "/login");
    }
}