package com.TheProfessorsPlate.controller;

import com.TheProfessorsPlate.model.Menu;
import com.TheProfessorsPlate.service.MenuService;
import com.TheProfessorsPlate.util.RedirectionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/menu")
public class MenuController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private MenuService menuService;
    private RedirectionUtil redirectionUtil;

    @Override
    public void init() {
        menuService = new MenuService();
        redirectionUtil = new RedirectionUtil();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Get all menu items with their discounted prices
            List<Menu> menuList = menuService.getAllMenuItems();
            request.setAttribute("menuList", menuList);
            
            // Get success and error messages from session if they exist
            HttpSession session = request.getSession();
            if (session.getAttribute("success") != null) {
                request.setAttribute("success", session.getAttribute("success"));
                session.removeAttribute("success");
            }
            if (session.getAttribute("error") != null) {
                request.setAttribute("error", session.getAttribute("error"));
                session.removeAttribute("error");
            }
            
            // Forward to menu page
            request.getRequestDispatcher("/WEB-INF/pages/menu.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            redirectionUtil.setMsgAndRedirect(request, response, "error", 
                    "Error loading menu: " + e.getMessage(), "/home");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Menu page doesn't handle POST requests directly for product management
        // This is handled by AdminProductsController
        response.sendRedirect(request.getContextPath() + "/menu");
    }

    @Override
    public void destroy() {
        if (menuService != null) {
            menuService.close();
        }
    }
}