package com.TheProfessorsPlate.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

import com.TheProfessorsPlate.util.CookieUtil;
import com.TheProfessorsPlate.util.SessionUtil;

/**
 * Servlet implementation class LogoutController
 * Handles user logout by clearing session data and cookies
 */
@WebServlet(asyncSupported = true, urlPatterns = {"/logout"})
public class LogoutController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(LogoutController.class.getName());

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
        // Validating CSRF token would be ideal here
        // String formToken = request.getParameter("csrfToken");
        // String sessionToken = (String) SessionUtil.getAttribute(request, "csrfToken");
        // if (formToken == null || !formToken.equals(sessionToken)) {
        //     response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid logout request");
        //     return;
        // }
        
        handleLogout(request, response);
    }

    /**
     * Common method to handle logout logic
     * Clears all user-specific cookies and session data
     */
    private void handleLogout(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        String userName = (String) SessionUtil.getAttribute(request, "userName");
        String userRole = (String) SessionUtil.getAttribute(request, "userRole");
        
        logger.info("Logging out user: " + userName + " with role: " + userRole);
        
        // Clear all authentication cookies
        CookieUtil.deleteCookie(response, "userRole");
        CookieUtil.deleteCookie(response, "userName");
        CookieUtil.deleteCookie(response, "userId");
        
        // Clear all session attributes one by one for thoroughness
        SessionUtil.removeAttribute(request, "userName");
        SessionUtil.removeAttribute(request, "userRole");
        SessionUtil.removeAttribute(request, "userId");
        SessionUtil.removeAttribute(request, "csrfToken");
        
        // Finally invalidate the entire session
        SessionUtil.invalidateSession(request);
        
        logger.info("User successfully logged out");
        
        // Redirect to login page with a logout success parameter
        response.sendRedirect(request.getContextPath() + "/login?logout=success");
    }
}