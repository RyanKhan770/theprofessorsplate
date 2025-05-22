<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>The Professor's Plate</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
    <header>
        <div class="header-container">
            <div class="left-section">
                <div class="profile-section">
                    <% if (session.getAttribute("userRole") != null) { %>
                        <div class="profile-dropdown">
                            <button class="profile-btn">
                                <i class="fas fa-user-circle fa-xl"></i>
                                <i class="fas fa-chevron-down"></i>
                            </button>
                            <div class="profile-dropdown-content">
                            	<c:if test="${sessionScope.userRole eq 'admin'}">
                            	<a href="${pageContext.request.contextPath}/adminDashboard"><i class="fa-solid fa-gauge"></i> Dashboard</a>
                            	</c:if>
                            	<c:if test="${sessionScope.userRole eq 'customer'}">
                            	<a href="${pageContext.request.contextPath}/customerDashboard"><i class="fa-solid fa-gauge"></i> Dashboard</a>
                            	<a href="${pageContext.request.contextPath}/cart"><i class="fa-solid fa-cart-shopping"></i> cart</a>
                            	</c:if>
                                <a href="${pageContext.request.contextPath}/profile"><i class="fas fa-user"></i> My Profile</a>
                                <a href="${pageContext.request.contextPath}/order"><i class="fas fa-shopping-bag"></i> Orders</a>
                                <a href="${pageContext.request.contextPath}/orderHistory"><i class="fas fa-shopping-bag"></i> Orders History</a>
                                <%-- <a href="${pageContext.request.contextPath}/settings"><i class="fas fa-cog"></i> Settings</a> --%>
                                <form action="${pageContext.request.contextPath}/logout" method="post" style="margin: 0;">
                                    <button type="submit" class="logout-link" style="width: 100%; text-align: left; border: none; background: none; padding: 8px 15px;">
                                        <i class="fas fa-sign-out-alt"></i> Logout
                                    </button>
                                </form>
                            </div>
                        </div>
                    <% } else { %>
                        <div class="auth-buttons">
                            <a href="${pageContext.request.contextPath}/login" class="login-btn">Login</a>
                            <a href="${pageContext.request.contextPath}/register" class="register-btn">Register</a>
                        </div>
                    <% } %>
                </div>
            </div>

            <div class="logo">
                <h1>The Professor's Plate</h1>
            </div>
            <nav>
                <ul class="main-nav">
                    <li><a href="${pageContext.request.contextPath}/home">Home</a></li>
                    <li class="dropdown">
                        <a href="${pageContext.request.contextPath}/menu" class="dropdown-trigger">Menu <i class="fas fa-chevron-down"></i></a>
                        <ul class="dropdown-content">
                            <li><a href="${pageContext.request.contextPath}/gallery">Gallery</a></li>
                        </ul>
                    </li>
                    <li class="dropdown">
                        <a href="${pageContext.request.contextPath}/team" class="dropdown-trigger">Team <i class="fas fa-chevron-down"></i></a>
                        <ul class="dropdown-content">
                            <li><a href="${pageContext.request.contextPath}/aboutUs">About Us</a></li>
                            <li><a href="${pageContext.request.contextPath}/review">Review</a></li>
                        </ul>
                    </li>
                    <li><a href="${pageContext.request.contextPath}/contactUs">Contact</a></li>
                </ul>
            </nav>
            <div class="mobile-menu-btn">
                <i class="fas fa-bars"></i>
            </div>
        </div>
    </header>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const mobileMenuBtn = document.querySelector('.mobile-menu-btn');
            const nav = document.querySelector('nav');
            
            mobileMenuBtn.addEventListener('click', function() {
                nav.style.display = nav.style.display === 'block' ? 'none' : 'block';
            });

            window.addEventListener('resize', function() {
                if (window.innerWidth > 768) {
                    nav.style.display = '';
                }
            });
        });
    </script>
</body>
</html>