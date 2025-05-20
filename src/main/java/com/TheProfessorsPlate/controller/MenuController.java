package com.TheProfessorsPlate.controller;

import com.TheProfessorsPlate.model.Menu;
import com.TheProfessorsPlate.service.MenuService;
import com.TheProfessorsPlate.util.RedirectionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(asyncSupported = true, urlPatterns = {"/menu"})
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
        // Get all menu items
        List<Menu> menuList = menuService.getAllMenuItems();
        request.setAttribute("menuList", menuList);
        
        // Forward to menu.jsp
        redirectionUtil.redirectToPage(request, response, "/WEB-INF/pages/menu.jsp");
    }
}
