package com.TheProfessorsPlate.controller;

import java.io.IOException;
import java.util.logging.Logger;

import com.TheProfessorsPlate.model.User;
import com.TheProfessorsPlate.service.LoginService;
import com.TheProfessorsPlate.util.CookieUtil;
import com.TheProfessorsPlate.util.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(asyncSupported = true, urlPatterns = { "/login" })
public class LoginController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(LoginController.class.getName());
    private final LoginService loginService;

    public LoginController() {
        this.loginService = new LoginService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        logger.info("Processing login request");
        
        try {
            String userName = req.getParameter("userName");
            String userPassword = req.getParameter("userPassword");
            
            // Validate input
            if (userName == null || userPassword == null || 
                userName.trim().isEmpty() || userPassword.trim().isEmpty()) {
                handleLoginFailure(req, resp, false);
                return;
            }

            // Create user object with plain password for comparison
            User user = new User(userName, userPassword);
            
            // Attempt authentication
            User authenticatedUser = loginService.loginUser(user);
            
            if (authenticatedUser != null) {
                logger.info("User authenticated successfully: " + userName);
                
                // Store user information in session
                SessionUtil.setAttribute(req, "userName", authenticatedUser.getUserName());
                SessionUtil.setAttribute(req, "userRole", authenticatedUser.getUserRole());
                logger.info("Setting session - userId: " + authenticatedUser.getUserId() + 
                        ", userName: " + authenticatedUser.getUserName() + 
                        ", userRole: " + authenticatedUser.getUserRole());
                SessionUtil.setAttribute(req, "userId", authenticatedUser.getUserId());
                
                // Set role cookie
                String userRole = authenticatedUser.getUserRole();
                CookieUtil.addCookie(resp, "userRole", userRole, 86400);
                
                // Redirect based on role
                switch(userRole.toLowerCase()) {
                    case "admin":
                        logger.info("Redirecting admin to dashboard");
                        resp.sendRedirect(req.getContextPath() + "/adminDashboard");
                        break;
                    case "customer":
                        logger.info("Redirecting customer to home");
                        resp.sendRedirect(req.getContextPath() + "/home");
                        break;
                    default:
                        logger.info("Unknown role, redirecting to home");
                        resp.sendRedirect(req.getContextPath() + "/home");
                }
            } else {
                logger.warning("Authentication failed for user: " + userName);
                handleLoginFailure(req, resp, false);
            }
        } catch (Exception e) {
            logger.severe("Error during login process: " + e.getMessage());
            handleLoginFailure(req, resp, null);
        }
    }

    private void handleLoginFailure(HttpServletRequest req, HttpServletResponse resp, Boolean loginStatus) 
            throws ServletException, IOException {
        String errorMessage;
        if (loginStatus == null) {
            errorMessage = "Our server is under maintenance. Please try again later!";
            logger.severe("Server error during login");
        } else {
            errorMessage = "User credential mismatch. Please try again!";
            logger.warning("Invalid credentials provided");
        }
        req.setAttribute("error", errorMessage);
        req.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(req, resp);
    }
}