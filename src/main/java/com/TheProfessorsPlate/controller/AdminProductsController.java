package com.TheProfessorsPlate.controller;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import com.TheProfessorsPlate.model.Discount;
import com.TheProfessorsPlate.model.Menu;
import com.TheProfessorsPlate.model.User;
import com.TheProfessorsPlate.service.AdminProductService;
import com.TheProfessorsPlate.service.UserService;
import com.TheProfessorsPlate.util.RedirectionUtil;
import com.TheProfessorsPlate.util.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@WebServlet(asyncSupported = true, urlPatterns = {"/adminProducts"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,  // 1MB
    maxFileSize = 5 * 1024 * 1024,     // 5MB
    maxRequestSize = 10 * 1024 * 1024  // 10MB
)
public class AdminProductsController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private AdminProductService adminProductService;
    private UserService userService;
    private RedirectionUtil redirectionUtil;
    
    @Override
    public void init() {
        adminProductService = new AdminProductService();
        userService = new UserService();
        redirectionUtil = new RedirectionUtil();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Check if user is admin
        String userRole = (String) SessionUtil.getAttribute(request, "userRole");
        
        if (userRole == null || !userRole.equals("admin")) {
            redirectionUtil.setMsgAndRedirect(request, response, "error", 
                    "You do not have permission to access this page", "/login");
            return;
        }
        
        // Get user information
        String userName = (String) SessionUtil.getAttribute(request, "userName");
        User user = userService.getUserByUsername(userName);
        
        if (user == null) {
            redirectionUtil.setMsgAndRedirect(request, response, "error", 
                    "User not found", "/login");
            return;
        }
        
        // Check for session messages and transfer to request attributes
        HttpSession session = request.getSession();
        if (session.getAttribute("success") != null) {
            request.setAttribute("success", session.getAttribute("success"));
            session.removeAttribute("success");
        }
        if (session.getAttribute("error") != null) {
            request.setAttribute("error", session.getAttribute("error"));
            session.removeAttribute("error");
        }
        
        // Set current date and time for display
        String currentDateTime = "2025-05-21 21:51:33"; // Updated with current time
        request.setAttribute("currentDateTime", currentDateTime);
        request.setAttribute("user", user);
        
        String action = request.getParameter("action");
        
        if ("add".equals(action)) {
            // Add new product form
            List<String> categories = adminProductService.getAllCategories();
            List<Discount> discounts = adminProductService.getAllDiscounts();
            
            request.setAttribute("categories", categories);
            request.setAttribute("discounts", discounts);
        } else if ("edit".equals(action)) {
            // Edit product form
            String foodIdParam = request.getParameter("foodId");
            
            if (foodIdParam == null || foodIdParam.trim().isEmpty()) {
                redirectionUtil.setMsgAndRedirect(request, response, "error", 
                        "Food ID is required", "/adminProducts");
                return;
            }
            
            try {
                int foodId = Integer.parseInt(foodIdParam);
                Menu product = adminProductService.getMenuItemById(foodId);
                
                if (product == null) {
                    redirectionUtil.setMsgAndRedirect(request, response, "error", 
                            "Product not found", "/adminProducts");
                    return;
                }
                
                List<String> categories = adminProductService.getAllCategories();
                List<Discount> discounts = adminProductService.getAllDiscounts();
                
                request.setAttribute("product", product);
                request.setAttribute("categories", categories);
                request.setAttribute("discounts", discounts);
            } catch (NumberFormatException e) {
                redirectionUtil.setMsgAndRedirect(request, response, "error", 
                        "Invalid product ID", "/adminProducts");
                return;
            }
        } else if ("inventory".equals(action)) {
            // Inventory management view
            List<Menu> products = adminProductService.getAllMenuItems();
            List<String> categories = adminProductService.getAllCategories();
            
            request.setAttribute("products", products);
            request.setAttribute("categories", categories);
        } else {
            // Default: list all products
            List<Menu> products = adminProductService.getAllMenuItems();
            List<String> categories = adminProductService.getAllCategories();
            
            request.setAttribute("products", products);
            request.setAttribute("categories", categories);
        }
        
        // Forward to products management page
        redirectionUtil.redirectToPage(request, response, "/WEB-INF/pages/adminProducts.jsp");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Check if user is admin
        String userRole = (String) SessionUtil.getAttribute(request, "userRole");
        String userName = (String) SessionUtil.getAttribute(request, "userName");
        
        if (userRole == null || !userRole.equals("admin")) {
            redirectionUtil.setMsgAndRedirect(request, response, "error", 
                    "You do not have permission to perform this action", "/login");
            return;
        }
        
        String action = request.getParameter("action");
        
        if ("add".equals(action)) {
            // Add new product
            String foodName = request.getParameter("foodName");
            String foodDescription = request.getParameter("foodDescription");
            String foodCategory = request.getParameter("foodCategory");
            String foodPriceStr = request.getParameter("foodPrice");
            String discountIdStr = request.getParameter("discountId");
            String stockQuantityStr = request.getParameter("stockQuantity");
            
            // Validate required fields
            if (foodName == null || foodDescription == null || foodCategory == null || 
                foodPriceStr == null || foodName.trim().isEmpty() || 
                foodDescription.trim().isEmpty() || foodCategory.trim().isEmpty() || 
                foodPriceStr.trim().isEmpty()) {
                
                redirectionUtil.setMsgAndRedirect(request, response, "error", 
                        "All fields are required", "/adminProducts?action=add");
                return;
            }
            
            try {
                BigDecimal foodPrice = new BigDecimal(foodPriceStr);
                int discountId = 0;
                int stockQuantity = 0;
                
                if (discountIdStr != null && !discountIdStr.trim().isEmpty()) {
                    discountId = Integer.parseInt(discountIdStr);
                }
                
                if (stockQuantityStr != null && !stockQuantityStr.trim().isEmpty()) {
                    stockQuantity = Integer.parseInt(stockQuantityStr);
                }
                
                // Handle file upload
                String foodImage = "resources/productsImage/default-food.jpg"; // Default image
                Part filePart = request.getPart("foodImage");
                
                if (filePart != null && filePart.getSize() > 0) {
                    // Generate unique filename
                    String originalFilename = filePart.getSubmittedFileName();
                    String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
                    String newFileName = "menu_" + System.currentTimeMillis() + extension;
                    
                    // Set upload directory
                    String uploadDir = "resources/productsImage";
                    String relativePath = uploadDir + "/" + newFileName;
                    
                    // Save the file using NIO (safer than java.io.File)
                    try (InputStream inputStream = filePart.getInputStream()) {
                        // Get the absolute path for the web app
                        String appPath = request.getServletContext().getRealPath("");
                        Path uploadPath = Paths.get(appPath, uploadDir);
                        
                        // Create directories if they don't exist
                        if (!Files.exists(uploadPath)) {
                            Files.createDirectories(uploadPath);
                        }
                        
                        // Save the file
                        Path filePath = Paths.get(appPath, relativePath);
                        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                        
                        // Set the relative path for database storage
                        foodImage = relativePath;
                    }
                }
                
                Menu newMenu = new Menu();
                newMenu.setFoodName(foodName);
                newMenu.setFoodDescription(foodDescription);
                newMenu.setFoodCategory(foodCategory);
                newMenu.setFoodPrice(foodPrice);
                newMenu.setDiscountId(discountId);
                newMenu.setFoodImage(foodImage);
                newMenu.setStockQuantity(stockQuantity);
                
                boolean success = adminProductService.addMenuItem(newMenu);
                
                if (success) {
                    redirectionUtil.setMsgAndRedirect(request, response, "success", 
                            "Menu item added successfully", "/adminProducts");
                } else {
                    redirectionUtil.setMsgAndRedirect(request, response, "error", 
                            "Failed to add menu item", "/adminProducts?action=add");
                }
                
            } catch (NumberFormatException e) {
                redirectionUtil.setMsgAndRedirect(request, response, "error", 
                        "Invalid number format", "/adminProducts?action=add");
            }
        } else if ("edit".equals(action)) {
            // Edit existing product
            String foodIdStr = request.getParameter("foodId");
            String foodName = request.getParameter("foodName");
            String foodDescription = request.getParameter("foodDescription");
            String foodCategory = request.getParameter("foodCategory");
            String foodPriceStr = request.getParameter("foodPrice");
            String discountIdStr = request.getParameter("discountId");
            String stockQuantityStr = request.getParameter("stockQuantity");
            
            // Validate required fields
            if (foodIdStr == null || foodName == null || foodDescription == null || 
                foodCategory == null || foodPriceStr == null || foodIdStr.trim().isEmpty() || 
                foodName.trim().isEmpty() || foodDescription.trim().isEmpty() || 
                foodCategory.trim().isEmpty() || foodPriceStr.trim().isEmpty()) {
                
                redirectionUtil.setMsgAndRedirect(request, response, "error", 
                        "All fields are required", "/adminProducts");
                return;
            }
            
            try {
                int foodId = Integer.parseInt(foodIdStr);
                BigDecimal foodPrice = new BigDecimal(foodPriceStr);
                int discountId = 0;
                int stockQuantity = 0;
                
                if (discountIdStr != null && !discountIdStr.trim().isEmpty()) {
                    discountId = Integer.parseInt(discountIdStr);
                }
                
                if (stockQuantityStr != null && !stockQuantityStr.trim().isEmpty()) {
                    stockQuantity = Integer.parseInt(stockQuantityStr);
                }
                
                // Get existing menu item to preserve image if not changed
                Menu existingMenu = adminProductService.getMenuItemById(foodId);
                if (existingMenu == null) {
                    redirectionUtil.setMsgAndRedirect(request, response, "error", 
                            "Menu item not found", "/adminProducts");
                    return;
                }
                
                String foodImage = existingMenu.getFoodImage(); // Default to existing image
                
                // Check if new image was uploaded
                Part filePart = request.getPart("foodImage");
                if (filePart != null && filePart.getSize() > 0) {
                    // Generate unique filename
                    String originalFilename = filePart.getSubmittedFileName();
                    String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
                    String newFileName = "menu_" + System.currentTimeMillis() + extension;
                    
                    // Set upload directory
                    String uploadDir = "resources/productsImage";
                    String relativePath = uploadDir + "/" + newFileName;
                    
                    // Save the file using NIO (safer than java.io.File)
                    try (InputStream inputStream = filePart.getInputStream()) {
                        // Get the absolute path for the web app
                        String appPath = request.getServletContext().getRealPath("");
                        Path uploadPath = Paths.get(appPath, uploadDir);
                        
                        // Create directories if they don't exist
                        if (!Files.exists(uploadPath)) {
                            Files.createDirectories(uploadPath);
                        }
                        
                        // Save the file
                        Path filePath = Paths.get(appPath, relativePath);
                        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                        
                        // Try to delete old image if it's not the default
                        if (!existingMenu.getFoodImage().contains("default-food.jpg")) {
                            try {
                                Path oldImagePath = Paths.get(appPath, existingMenu.getFoodImage());
                                Files.deleteIfExists(oldImagePath);
                            } catch (IOException e) {
                                // Just log the exception, don't break the application
                                System.err.println("Could not delete old image: " + e.getMessage());
                            }
                        }
                        
                        // Set the relative path for database storage
                        foodImage = relativePath;
                    }
                }
                
                Menu updatedMenu = new Menu();
                updatedMenu.setFoodId(foodId);
                updatedMenu.setFoodName(foodName);
                updatedMenu.setFoodDescription(foodDescription);
                updatedMenu.setFoodCategory(foodCategory);
                updatedMenu.setFoodPrice(foodPrice);
                updatedMenu.setDiscountId(discountId);
                updatedMenu.setFoodImage(foodImage);
                updatedMenu.setStockQuantity(stockQuantity);
                
                boolean success = adminProductService.updateMenuItem(updatedMenu);
                
                if (success) {
                    redirectionUtil.setMsgAndRedirect(request, response, "success", 
                            "Menu item updated successfully", "/adminProducts");
                } else {
                    redirectionUtil.setMsgAndRedirect(request, response, "error", 
                            "Failed to update menu item", "/adminProducts");
                }
                
            } catch (NumberFormatException e) {
                redirectionUtil.setMsgAndRedirect(request, response, "error", 
                        "Invalid number format", "/adminProducts");
            }
        } else if ("delete".equals(action)) {
            // Delete product
            String foodIdStr = request.getParameter("foodId");
            
            if (foodIdStr == null || foodIdStr.trim().isEmpty()) {
                redirectionUtil.setMsgAndRedirect(request, response, "error", 
                        "Food ID is required", "/adminProducts");
                return;
            }
            
            try {
                int foodId = Integer.parseInt(foodIdStr);
                
                // Get existing menu item to delete image
                Menu existingMenu = adminProductService.getMenuItemById(foodId);
                if (existingMenu == null) {
                    redirectionUtil.setMsgAndRedirect(request, response, "error", 
                            "Menu item not found", "/adminProducts");
                    return;
                }
                
                boolean success = adminProductService.deleteMenuItem(foodId);
                
                if (success) {
                    // Try to delete the image if it's not the default
                    if (!existingMenu.getFoodImage().contains("default-food.jpg")) {
                        try {
                            String appPath = request.getServletContext().getRealPath("");
                            Path imagePath = Paths.get(appPath, existingMenu.getFoodImage());
                            Files.deleteIfExists(imagePath);
                        } catch (IOException e) {
                            // Just log the exception, don't break the application
                            System.err.println("Could not delete image: " + e.getMessage());
                        }
                    }
                    
                    redirectionUtil.setMsgAndRedirect(request, response, "success", 
                            "Menu item deleted successfully", "/adminProducts");
                } else {
                    redirectionUtil.setMsgAndRedirect(request, response, "error", 
                            "Failed to delete menu item", "/adminProducts");
                }
                
            } catch (NumberFormatException e) {
                redirectionUtil.setMsgAndRedirect(request, response, "error", 
                        "Invalid product ID", "/adminProducts");
            }
        } else {
            redirectionUtil.setMsgAndRedirect(request, response, "error", 
                    "Invalid action", "/adminProducts");
        }
    }
    
    @Override
    public void destroy() {
        if (adminProductService != null) {
            adminProductService.close();
        }
        if (userService != null) {
            userService.close();
        }
    }
}