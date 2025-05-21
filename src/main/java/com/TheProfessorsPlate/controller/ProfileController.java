package com.TheProfessorsPlate.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.logging.Logger;

import com.TheProfessorsPlate.model.User;
import com.TheProfessorsPlate.service.UserService;
import com.TheProfessorsPlate.util.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet(urlPatterns = {"/profile", "/updateProfile", "/updateProfileImage", "/removeProfileImage"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024, // 1MB
    maxFileSize = 5 * 1024 * 1024,   // 5MB
    maxRequestSize = 20 * 1024 * 1024 // 20MB
)
public class ProfileController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(ProfileController.class.getName());
    private UserService userService;

    @Override
    public void init() {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if user is logged in
        String userName = (String) SessionUtil.getAttribute(request, "userName");
        Integer userId = (Integer) SessionUtil.getAttribute(request, "userId");
        
        if (userName == null || userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Get user details
        User user = userService.getUserById(userId);
        
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Set user attribute for use in JSP
        request.setAttribute("user", user);
        
        // Forward to profile page
        request.getRequestDispatcher("/WEB-INF/pages/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if user is logged in
        String userName = (String) SessionUtil.getAttribute(request, "userName");
        Integer userId = (Integer) SessionUtil.getAttribute(request, "userId");
        
        if (userName == null || userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String servletPath = request.getServletPath();
        
        if ("/updateProfile".equals(servletPath)) {
            handleProfileUpdate(request, response, userId);
        } else if ("/updateProfileImage".equals(servletPath)) {
            handleProfileImageUpdate(request, response, userId);
        } else if ("/removeProfileImage".equals(servletPath)) {
            handleProfileImageRemoval(request, response, userId);
        } else {
            response.sendRedirect(request.getContextPath() + "/profile");
        }
    }
    
    /**
     * Handle profile information update
     */
    private void handleProfileUpdate(HttpServletRequest request, HttpServletResponse response, int userId)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String phoneNumber = request.getParameter("phoneNumber");
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");
        
        // Get the current user
        User user = userService.getUserById(userId);
        
        if (user == null) {
            setErrorAndRedirect(request, response, "User not found.");
            return;
        }
        
        boolean profileUpdated = false;
        
        // Update username and phone number if changed
        if (!username.equals(user.getUserName()) || !phoneNumber.equals(user.getPhoneNumber())) {
            // Check if the username is available if it has changed
            if (!username.equals(user.getUserName()) && userService.getUserByUsername(username) != null) {
                setErrorAndRedirect(request, response, "Username already exists. Please choose a different one.");
                return;
            }
            
            profileUpdated = userService.updateUserProfile(userId, username, phoneNumber);
            
            if (profileUpdated) {
                // Update the session with the new username if it changed
                if (!username.equals(user.getUserName())) {
                    SessionUtil.setAttribute(request, "userName", username);
                }
                
                setSuccessAndRedirect(request, response, "Profile information updated successfully.");
            } else {
                setErrorAndRedirect(request, response, "Failed to update profile information.");
            }
        }
        
        // Update password if provided
        if (currentPassword != null && !currentPassword.isEmpty() && 
            newPassword != null && !newPassword.isEmpty() && 
            confirmPassword != null && !confirmPassword.isEmpty()) {
            
            // Validate password
            if (!newPassword.equals(confirmPassword)) {
                setErrorAndRedirect(request, response, "New password and confirmation do not match.");
                return;
            }
            
            // Check current password
            if (!userService.verifyPassword(userId, currentPassword)) {
                setErrorAndRedirect(request, response, "Current password is incorrect.");
                return;
            }
            
            // Update password
            boolean passwordUpdated = userService.updatePassword(userId, newPassword);
            
            if (passwordUpdated) {
                setSuccessAndRedirect(request, response, 
                    profileUpdated ? "Profile information and password updated successfully." 
                                  : "Password updated successfully.");
            } else {
                setErrorAndRedirect(request, response, "Failed to update password.");
            }
        } else if (!profileUpdated) {
            // If no updates were made
            response.sendRedirect(request.getContextPath() + "/profile");
        }
    }
    
    /**
     * Handle profile image update
     */
    private void handleProfileImageUpdate(HttpServletRequest request, HttpServletResponse response, int userId)
            throws ServletException, IOException {
        try {
            Part filePart = request.getPart("profileImage");
            
            if (filePart == null || filePart.getSize() <= 0) {
                setErrorAndRedirect(request, response, "No image file selected.");
                return;
            }
            
            // Validate file type
            String contentType = filePart.getContentType();
            if (!contentType.startsWith("image/")) {
                setErrorAndRedirect(request, response, "Only image files are allowed.");
                return;
            }
            
            // Create directories if they don't exist
            String uploadPath = "resources/usersImage/user";
            String absoluteUploadDir = getServletContext().getRealPath(uploadPath);
            File uploadDirFile = new File(absoluteUploadDir);
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
            }
            
            // Generate unique filename
            String fileName = UUID.randomUUID().toString();
            
            // Get the file extension
            String extension = getFileExtension(filePart);
            fileName = fileName + "." + extension;
            
            // Full file path in the server
            String serverFilePath = absoluteUploadDir + File.separator + fileName;
            
            // Delete the old image if exists
            User user = userService.getUserById(userId);
            if (user != null && user.getUserImage() != null && !user.getUserImage().isEmpty()) {
                String oldImagePath = getServletContext().getRealPath(user.getUserImage());
                try {
                    Files.deleteIfExists(Paths.get(oldImagePath));
                } catch (Exception e) {
                    logger.warning("Failed to delete old profile image: " + e.getMessage());
                }
            }
            
            // Save the file using streams for better handling
            try (InputStream input = filePart.getInputStream();
                 OutputStream output = new FileOutputStream(serverFilePath)) {
                
                byte[] buffer = new byte[1024];
                int length;
                while ((length = input.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }
            }
            
            // Update the database with the relative path
            String relativeFilePath = uploadPath + "/" + fileName;
            boolean updated = userService.updateProfileImage(userId, relativeFilePath);
            
            if (updated) {
                setSuccessAndRedirect(request, response, "Profile image updated successfully.");
            } else {
                // If database update failed, try to delete the uploaded file
                try {
                    Files.deleteIfExists(Paths.get(serverFilePath));
                } catch (Exception e) {
                    logger.warning("Failed to delete image after failed database update: " + e.getMessage());
                }
                setErrorAndRedirect(request, response, "Failed to update profile image in database.");
            }
        } catch (Exception e) {
            logger.severe("Error updating profile image: " + e.getMessage());
            setErrorAndRedirect(request, response, "An error occurred while uploading the image.");
        }
    }
    
    /**
     * Handle profile image removal
     */
    private void handleProfileImageRemoval(HttpServletRequest request, HttpServletResponse response, int userId)
            throws ServletException, IOException {
        try {
            // Get the user to check for existing image
            User user = userService.getUserById(userId);
            
            if (user == null) {
                setErrorAndRedirect(request, response, "User not found.");
                return;
            }
            
            // Delete the old image file if exists
            if (user.getUserImage() != null && !user.getUserImage().isEmpty()) {
                String oldImagePath = getServletContext().getRealPath(user.getUserImage());
                try {
                    Files.deleteIfExists(Paths.get(oldImagePath));
                } catch (Exception e) {
                    logger.warning("Failed to delete old profile image: " + e.getMessage());
                }
            }
            
            // Update database to set profile image to null
            boolean updated = userService.updateProfileImage(userId, null);
            
            if (updated) {
                setSuccessAndRedirect(request, response, "Profile image removed successfully.");
            } else {
                setErrorAndRedirect(request, response, "Failed to remove profile image.");
            }
        } catch (Exception e) {
            logger.severe("Error removing profile image: " + e.getMessage());
            setErrorAndRedirect(request, response, "An error occurred while removing the image.");
        }
    }
    
    /**
     * Get file extension from Part
     */
    private String getFileExtension(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] tokens = contentDisp.split(";");
        
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                String fileName = token.substring(token.indexOf("=") + 2, token.length() - 1);
                
                if (fileName.contains(".")) {
                    return fileName.substring(fileName.lastIndexOf(".") + 1);
                }
            }
        }
        
        return "jpg"; // Default extension
    }
    
    /**
     * Set error message and redirect
     */
    private void setErrorAndRedirect(HttpServletRequest request, HttpServletResponse response, String message)
            throws IOException {
        SessionUtil.setAttribute(request, "errorMessage", message);
        response.sendRedirect(request.getContextPath() + "/profile");
    }
    
    /**
     * Set success message and redirect
     */
    private void setSuccessAndRedirect(HttpServletRequest request, HttpServletResponse response, String message)
            throws IOException {
        SessionUtil.setAttribute(request, "successMessage", message);
        response.sendRedirect(request.getContextPath() + "/profile");
    }

    @Override
    public void destroy() {
        if (userService != null) {
            userService.close();
        }
    }
}