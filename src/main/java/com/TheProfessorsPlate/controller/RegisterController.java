package com.TheProfessorsPlate.controller;

import java.io.IOException;
import com.TheProfessorsPlate.service.RegisterService;
import com.TheProfessorsPlate.util.ImageUtil;
import com.TheProfessorsPlate.util.PasswordUtil;
import com.TheProfessorsPlate.util.ValidationUtil;
import com.TheProfessorsPlate.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet(asyncSupported = true, urlPatterns = {"/register"})
@MultipartConfig(
	    fileSizeThreshold = 1024 * 1024, // 1 MB
	    maxFileSize = 1024 * 1024 * 5,   // 5 MB
	    maxRequestSize = 1024 * 1024 * 10 // 10 MB
	)
public class RegisterController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private final ImageUtil imageUtil = new ImageUtil();
	private final RegisterService registerService = new RegisterService();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("WEB-INF/pages/register.jsp").forward(req, resp);
	}
	
	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        try {
            // Validate form
            String validationMessage = validateRegistrationForm(req);
            if (validationMessage != null) {
                handleError(req, resp, validationMessage);
                return;
            }

            // Extract and create user
            User user = extractUser(req);
            
            // Handle image upload first
            Part imagePart = req.getPart("userImage");
            if (imagePart != null && imagePart.getSize() > 0) {
                if (!imageUtil.uploadImage(imagePart, req.getServletContext().getRealPath("/"), "user")) {
                    handleError(req, resp, "Could not upload the image. Please try again later!");
                    return;
                }
            }

            // Save user
            Boolean isAdded = registerService.addUser(user);
            if (isAdded == null) {
                handleError(req, resp, "Our server is under maintenance. Please try again later!");
            } else if (isAdded) {
                handleSuccess(req, resp, "Your account is successfully created!", "/WEB-INF/pages/login.jsp");
            } else {
                handleError(req, resp, "Could not register your account. Please try again later!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            handleError(req, resp, "An unexpected error occurred. Please try again later!");
        }
    }

    private User extractUser(HttpServletRequest req) throws Exception {
        User user = new User();
        
        // Basic information
        user.setUserName(req.getParameter("userName"));
        user.setUserRole(req.getParameter("userRole"));
        user.setUserEmail(req.getParameter("userEmail"));
        user.setPhoneNumber(req.getParameter("phoneNumber"));
        
        // Password handling
        String userPassword = req.getParameter("userPassword");
        user.setUserPassword(PasswordUtil.encrypt(user.getUserName(), userPassword));
        
        // Image handling
        Part imagePart = req.getPart("userImage");
        if (imagePart != null && imagePart.getSize() > 0) {
            String imageName = imageUtil.getImageNameFromPart(imagePart);
            user.setUserImage("resources/usersImage/user/" + imageName);
        }       
        
        return user;
    }


	private String validateRegistrationForm(HttpServletRequest req) {
		String userName = req.getParameter("userName");
		String userRole = req.getParameter("userRole");
		String userEmail = req.getParameter("userEmail");
		String phoneNumber = req.getParameter("phoneNumber");
		String userPassword = req.getParameter("userPassword");
		String retypePassword = req.getParameter("retypePassword");

		// Check for null or empty fields first
		if (ValidationUtil.isNullOrEmpty(userName))
		    return "Username is required.";
		if (ValidationUtil.isNullOrEmpty(userRole))
		    return "User role is required.";
		if (ValidationUtil.isNullOrEmpty(userEmail))
		    return "Email is required.";
		if (ValidationUtil.isNullOrEmpty(phoneNumber))
		    return "Phone number is required.";
		if (ValidationUtil.isNullOrEmpty(userPassword))
		    return "Password is required.";
		if (ValidationUtil.isNullOrEmpty(retypePassword))
		    return "Please retype the password.";

		// First validate format and content rules
		if (!ValidationUtil.isAlphanumericStartingWithLetter(userName))
		    return "Username must start with a letter and contain only letters and numbers.";
		if (!ValidationUtil.isValidUserRole(userRole))
		    return "User role must be 'admin' or 'customer'.";
		if (!ValidationUtil.isValidEmail(userEmail))
		    return "Invalid email format.";
		if (!ValidationUtil.isValidPhoneNumber(phoneNumber))
		    return "Phone number must be 10 digits and start with 98.";
		if (!ValidationUtil.isValidPassword(userPassword))
		    return "Password must be at least 8 characters long, with 1 uppercase letter, 1 number, and 1 symbol.";
		if (!ValidationUtil.doPasswordsMatch(userPassword, retypePassword))
		    return "Passwords do not match.";

		 try {
	            Part imagePart = req.getPart("userImage");
	            if (imagePart != null && imagePart.getSize() > 0 && 
	                !ValidationUtil.isValidImageExtension(imagePart)) {
	                return "Invalid image format. Only jpg, jpeg, png, and gif are allowed.";
	            }
	        } catch (IOException | ServletException e) {
	            return "Error handling image file. Please ensure the file is valid.";
	        }
	        return null;
	    }

	 private void handleSuccess(HttpServletRequest req, HttpServletResponse resp, 
	            String message, String redirectPage) throws ServletException, IOException {
	        req.setAttribute("success", message);
	        req.getRequestDispatcher(redirectPage).forward(req, resp);
	    }

	    private void handleError(HttpServletRequest req, HttpServletResponse resp, 
	            String message) throws ServletException, IOException {
	        req.setAttribute("error", message);
	        
	        // Retain form data
	        req.setAttribute("userName", req.getParameter("userName"));
	        req.setAttribute("userRole", req.getParameter("userRole"));
	        req.setAttribute("userEmail", req.getParameter("userEmail"));
	        req.setAttribute("phoneNumber", req.getParameter("phoneNumber"));
	        
	        // Forward back to registration page
	        req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
	    }
	}