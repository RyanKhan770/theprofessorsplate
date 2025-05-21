<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Customer Dashboard - The Professor's Plate</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/customerDashboard.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>
    <!-- Navigation Bar -->
    <nav class="navbar">
        <div class="navbar-brand">
            <img src="${pageContext.request.contextPath}/resources/images/logo.png" alt="The Professor's Plate">
            <h1>The Professor's Plate</h1>
        </div>
        <ul class="navbar-menu">
            <li><a href="${pageContext.request.contextPath}/home"><i class="fas fa-home"></i> Home</a></li>
            <li class="active"><a href="${pageContext.request.contextPath}/customerDashboard"><i class="fas fa-user"></i> Dashboard</a></li>
            <li><a href="${pageContext.request.contextPath}/menu"><i class="fas fa-utensils"></i> Menu</a></li>
            <li><a href="${pageContext.request.contextPath}/cart"><i class="fas fa-shopping-cart"></i> Cart</a></li>
            <li><a href="${pageContext.request.contextPath}/logout"><i class="fas fa-sign-out-alt"></i> Logout</a></li>
        </ul>
        <div class="navbar-toggle">
            <i class="fas fa-bars"></i>
        </div>
    </nav>
    
    <div class="dashboard-container">
        <!-- Sidebar -->
        <aside class="sidebar">
            <div class="user-profile">
                <c:choose>
                    <c:when test="${not empty user.userImage}">
                        <img src="${pageContext.request.contextPath}/${user.userImage}" alt="${user.userName}">
                    </c:when>
                    <c:otherwise>
                        <img src="${pageContext.request.contextPath}/resources/images/default-user.png" alt="${user.userName}">
                    </c:otherwise>
                </c:choose>
                <h2>${user.userName}</h2>
                <p>${user.userEmail}</p>
            </div>
            
            <nav class="sidebar-menu">
                <ul>
                    <li class="active"><a href="${pageContext.request.contextPath}/customerDashboard"><i class="fas fa-tachometer-alt"></i> Dashboard</a></li>
                    <li><a href="${pageContext.request.contextPath}/profile"><i class="fas fa-user-circle"></i> My Profile</a></li>
                    <li><a href="${pageContext.request.contextPath}/orderHistory"><i class="fas fa-history"></i> Order History</a></li>
                    <li><a href="${pageContext.request.contextPath}/favorites"><i class="fas fa-heart"></i> Favorites</a></li>
                    <li><a href="${pageContext.request.contextPath}/settings"><i class="fas fa-cog"></i> Settings</a></li>
                </ul>
            </nav>
        </aside>
        
        <!-- Main Content -->
        <main class="main-content">
            <header>
                <h1>Welcome back, ${user.userName}!</h1>
                <p class="date-time">Current Date: <%= new java.text.SimpleDateFormat("MMMM dd, yyyy").format(new java.util.Date()) %></p>
            </header>
            
            <!-- Alert Messages -->
            <c:if test="${not empty success}">
                <div class="alert success">
                    <p>${success}</p>
                </div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="alert error">
                    <p>${error}</p>
                </div>
            </c:if>
            
            <!-- Dashboard Stats -->
            <div class="stats-cards">
                <div class="card">
                    <div class="card-icon">
                        <i class="fas fa-shopping-bag"></i>
                    </div>
                    <div class="card-info">
                        <h3>Total Orders</h3>
                        <p>${recentOrders.size()}</p>
                    </div>
                </div>
                
                <div class="card">
                    <div class="card-icon">
                        <i class="fas fa-money-bill-wave"></i>
                    </div>
                    <div class="card-info">
                        <h3>Total Spent</h3>
                        <p>Rs. ${totalSpent}</p>
                    </div>
                </div>
                
                <div class="card">
                    <div class="card-icon">
                        <i class="fas fa-star"></i>
                    </div>
                    <div class="card-info">
                        <h3>Reviews</h3>
                        <p>${recentReviews.size()}</p>
                    </div>
                </div>
                
                <div class="card">
                    <div class="card-icon">
                        <i class="fas fa-heart"></i>
                    </div>
                    <div class="card-info">
                        <h3>Favorites</h3>
                        <p>${favoriteItems.size()}</p>
                    </div>
                </div>
            </div>
            
            <!-- Recent Orders -->
            <section class="recent-orders">
                <div class="section-header">
                    <h2>Recent Orders</h2>
                    <a href="${pageContext.request.contextPath}/orderHistory" class="view-all">View All</a>
                </div>
                
                <div class="table-responsive">
                    <table>
                        <thead>
                            <tr>
                                <th>Order ID</th>
                                <th>Date</th>
                                <th>Status</th>
                                <th>Quantity</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${recentOrders}" var="order">
                                <tr>
                                    <td>#${order.orderId}</td>
                                    <td>${order.orderDate}</td>
                                    <td><span class="status ${order.orderStatus}">${order.orderStatus}</span></td>
                                    <td>${order.orderQuantity}</td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/orderDetails?orderId=${order.orderId}" class="btn btn-primary btn-sm">
                                            <i class="fas fa-eye"></i> View
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty recentOrders}">
                                <tr>
                                    <td colspan="5" class="text-center">No orders yet</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </section>
            
            <!-- Food Recommendations -->
            <div class="food-sections">
                <!-- Favorite Items -->
                <section class="food-section favorites">
                    <div class="section-header">
                        <h2>Your Favorites</h2>
                        <a href="${pageContext.request.contextPath}/favorites" class="view-all">View All</a>
                    </div>
                    
                    <div class="food-cards">
                        <c:forEach items="${favoriteItems}" var="item">
                            <div class="food-card">
                                <div class="food-image">
                                    <img src="${pageContext.request.contextPath}/${item.foodImage}" alt="${item.foodName}">
                                    <span class="food-category">${item.foodCategory}</span>
                                </div>
                                <div class="food-details">
                                    <h3>${item.foodName}</h3>
                                    <div class="food-price">
                                        <c:choose>
                                            <c:when test="${item.discountedPrice != item.foodPrice}">
                                                <span class="original-price">Rs. ${item.foodPrice}</span>
                                                <span class="discounted-price">Rs. ${item.discountedPrice}</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="price">Rs. ${item.foodPrice}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                    <a href="${pageContext.request.contextPath}/menu?foodId=${item.foodId}" class="btn btn-outline">View Item</a>
                                </div>
                            </div>
                        </c:forEach>
                        <c:if test="${empty favoriteItems}">
                            <div class="no-items">
                                <p>No favorite items yet. Explore our menu and add some!</p>
                               	                                <a href="${pageContext.request.contextPath}/menu" class="btn btn-primary">Explore Menu</a>
                            </div>
                        </c:if>
                    </div>
                </section>
                
                <!-- Recommended Items -->
                <section class="food-section recommended">
                    <div class="section-header">
                        <h2>Recommended for You</h2>
                        <a href="${pageContext.request.contextPath}/menu" class="view-all">View Menu</a>
                    </div>
                    
                    <div class="food-cards">
                        <c:forEach items="${recommendedItems}" var="item">
                            <div class="food-card">
                                <div class="food-image">
                                    <img src="${pageContext.request.contextPath}/${item.foodImage}" alt="${item.foodName}">
                                    <span class="food-category">${item.foodCategory}</span>
                                </div>
                                <div class="food-details">
                                    <h3>${item.foodName}</h3>
                                    <div class="food-price">
                                        <c:choose>
                                            <c:when test="${item.discountedPrice != item.foodPrice}">
                                                <span class="original-price">Rs. ${item.foodPrice}</span>
                                                <span class="discounted-price">Rs. ${item.discountedPrice}</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="price">Rs. ${item.foodPrice}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                    <a href="${pageContext.request.contextPath}/menu?foodId=${item.foodId}" class="btn btn-outline">View Item</a>
                                </div>
                            </div>
                        </c:forEach>
                        <c:if test="${empty recommendedItems}">
                            <div class="no-items">
                                <p>No recommendations available yet.</p>
                                <a href="${pageContext.request.contextPath}/menu" class="btn btn-primary">Explore Menu</a>
                            </div>
                        </c:if>
                    </div>
                </section>
            </div>
            
            <!-- Recent Reviews -->
            <section class="recent-reviews">
                <div class="section-header">
                    <h2>Your Recent Reviews</h2>
                </div>
                
                <div class="reviews-container">
                    <c:forEach items="${recentReviews}" var="review">
                        <div class="review-card">
                            <div class="review-header">
                                <div class="rating">
                                    <c:forEach begin="1" end="5" var="i">
                                        <c:choose>
                                            <c:when test="${i <= review.rating}">
                                                <i class="fas fa-star"></i>
                                            </c:when>
                                            <c:when test="${i <= review.rating + 0.5}">
                                                <i class="fas fa-star-half-alt"></i>
                                            </c:when>
                                            <c:otherwise>
                                                <i class="far fa-star"></i>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                    <span class="rating-value">${review.rating}/5</span>
                                </div>
                                <div class="review-date">${review.reviewDate}</div>
                            </div>
                            <div class="review-body">
                                <p>${review.reviewDescription}</p>
                            </div>
                        </div>
                    </c:forEach>
                    <c:if test="${empty recentReviews}">
                        <div class="no-items">
                            <p>You haven't written any reviews yet.</p>
                            <a href="${pageContext.request.contextPath}/menu" class="btn btn-primary">Browse Menu to Review</a>
                        </div>
                    </c:if>
                </div>
            </section>
        </main>
    </div>
    
    <!-- Footer -->
    <footer class="footer">
        <div class="footer-content">
            <div class="footer-section">
                <h3>The Professor's Plate</h3>
                <p>Delicious food served with academic excellence</p>
            </div>
            <div class="footer-section">
                <h3>Quick Links</h3>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/home">Home</a></li>
                    <li><a href="${pageContext.request.contextPath}/menu">Menu</a></li>
                    <li><a href="${pageContext.request.contextPath}/aboutUs">About Us</a></li>
                    <li><a href="${pageContext.request.contextPath}/contact">Contact</a></li>
                </ul>
            </div>
            <div class="footer-section">
                <h3>Contact Us</h3>
                <p><i class="fas fa-map-marker-alt"></i> 123 University Rd, Kathmandu</p>
                <p><i class="fas fa-phone"></i> +977 1 4123456</p>
                <p><i class="fas fa-envelope"></i> info@professorsplate.com</p>
            </div>
            <div class="footer-section">
                <h3>Follow Us</h3>
                <div class="social-links">
                    <a href="#"><i class="fab fa-facebook"></i></a>
                    <a href="#"><i class="fab fa-twitter"></i></a>
                    <a href="#"><i class="fab fa-instagram"></i></a>
                </div>
            </div>
        </div>
        <div class="footer-bottom">
            <p>&copy; 2025 The Professor's Plate. All rights reserved.</p>
        </div>
    </footer>
    
    <script src="${pageContext.request.contextPath}/javaScript/customerDashboard.js"></script>
</body>
</html>