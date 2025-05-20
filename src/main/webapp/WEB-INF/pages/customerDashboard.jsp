<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Dashboard - The Professor's Plate</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/customerDashboard.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <jsp:include page="header.jsp" />
    
    <div class="dashboard-container">
        <aside class="dashboard-sidebar">
            <div class="user-profile">
                <div class="profile-image">
                    <i class="fas fa-user-circle"></i>
                </div>
                <div class="user-info">
                    <h3>${user.name}</h3>
                    <p>${user.email}</p>
                </div>
            </div>

            <nav class="dashboard-nav">
                <ul>
                    <li class="active">
                        <a href="${pageContext.request.contextPath}/customerDashboard"><i class="fas fa-home"></i> Overview</a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/orderHistory"><i class="fas fa-history"></i> Order History</a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/favorites"><i class="fas fa-heart"></i> Favorites</a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/profile"><i class="fas fa-user"></i> Profile Settings</a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/addresses"><i class="fas fa-map-marker-alt"></i> Addresses</a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/review"><i class="fas fa-star"></i> My Reviews</a>
                    </li>
                </ul>
            </nav>
        </aside>

        <main class="dashboard-content">
            <section class="welcome-section">
                <h1>Welcome back, ${user.firstName}!</h1>
                <p>Last login: ${user.lastLogin}</p>
            </section>

            <section class="quick-stats">
                <div class="stat-card">
                    <i class="fas fa-shopping-bag"></i>
                    <div class="stat-info">
                        <h3>Total Orders</h3>
                        <p>${totalOrders}</p>
                    </div>
                </div>
                <div class="stat-card">
                    <i class="fas fa-heart"></i>
                    <div class="stat-info">
                        <h3>Favorites</h3>
                        <p>${totalFavorites}</p>
                    </div>
                </div>
                <div class="stat-card">
                    <i class="fas fa-star"></i>
                    <div class="stat-info">
                        <h3>Reviews</h3>
                        <p>${totalReviews}</p>
                    </div>
                </div>
            </section>

            <section class="recent-orders">
                <div class="section-header">
                    <h2>Recent Orders</h2>
                    <a href="orderHistory" class="view-all">View All</a>
                </div>
                <div class="orders-grid">
                    <c:forEach items="${recentOrders}" var="order">
                        <div class="order-card">
                            <div class="order-header">
                                <span class="order-id">Order #${order.id}</span>
                                <span class="order-date">${order.date}</span>
                            </div>
                            <div class="order-items">
                                <p>${order.itemCount} items</p>
                                <p class="order-total">$${order.total}</p>
                            </div>
                            <div class="order-status ${order.status.toLowerCase()}">
                                ${order.status}
                            </div>
                            <a href="order/${order.id}" class="order-details-btn">View Details</a>
                        </div>
                    </c:forEach>
                </div>
            </section>

            <section class="favorite-items">
                <div class="section-header">
                    <h2>Your Favorites</h2>
                    <a href="favorites" class="view-all">View All</a>
                </div>
                <div class="favorites-grid">
                    <c:forEach items="${favoriteItems}" var="item">
                        <div class="favorite-card">
                            <div class="item-image">
                                <img src="${item.imageUrl}" alt="${item.name}">
                            </div>
                            <div class="item-info">
                                <h3>${item.name}</h3>
                                <p class="item-price">$${item.price}</p>
                                <button class="add-to-cart-btn">Add to Cart</button>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </section>
        </main>
    </div>

    <jsp:include page="footer.jsp" />
</body>
</html>