<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - The Professor's Plate</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/adminDashboard.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>
    <div class="dashboard-container">
        <div class="sidebar">
            <div class="logo">
                <img src="${pageContext.request.contextPath}/resources/images/logo.png" alt="The Professor's Plate">
                <h2>Admin Panel</h2>
            </div>
            <ul class="nav-links">
                <li class="active"><a href="${pageContext.request.contextPath}/adminDashboard"><i class="fas fa-home"></i> Dashboard</a></li>
                <li><a href="${pageContext.request.contextPath}/adminUsers"><i class="fas fa-users"></i> Users</a></li>
                <li><a href="${pageContext.request.contextPath}/adminMenu"><i class="fas fa-utensils"></i> Menu</a></li>
                <li><a href="${pageContext.request.contextPath}/adminOrders"><i class="fas fa-shopping-cart"></i> Orders</a></li>
                <li><a href="${pageContext.request.contextPath}/adminPayments"><i class="fas fa-credit-card"></i> Payments</a></li>
                <li><a href="${pageContext.request.contextPath}/logout"><i class="fas fa-sign-out-alt"></i> Logout</a></li>
            </ul>
        </div>
        
        <div class="main-content">
            <header>
                <h1>Dashboard</h1>
                <div class="user-info">
                    <img src="${pageContext.request.contextPath}/resources/usersImage/user/admin.jpg" alt="Admin">
                    <span>Welcome, ${sessionScope.userName}</span>
                </div>
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
            
            <div class="stats-cards">
                <div class="card">
                    <i class="fas fa-users"></i>
                    <div class="card-content">
                        <h3>Total Customers</h3>
                        <p>${totalCustomers}</p>
                    </div>
                </div>
                <div class="card">
                    <i class="fas fa-shopping-cart"></i>
                    <div class="card-content">
                        <h3>Total Orders</h3>
                        <p>${totalOrders}</p>
                    </div>
                </div>
                <div class="card">
                    <i class="fas fa-money-bill-wave"></i>
                    <div class="card-content">
                        <h3>Total Revenue</h3>
                        <p>Rs. ${totalRevenue}</p>
                    </div>
                </div>
                <div class="card">
                    <i class="fas fa-utensils"></i>
                    <div class="card-content">
                        <h3>Active Menu Items</h3>
                        <p>10</p>
                    </div>
                </div>
            </div>
            
            <div class="data-tables">
                <div class="recent-orders">
                    <h2>Recent Orders</h2>
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
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
                                    <td>
                                        <span class="status ${order.orderStatus}">${order.orderStatus}</span>
                                    </td>
                                    <td>${order.orderQuantity}</td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/adminOrders?orderId=${order.orderId}" class="action-btn view">
                                            <i class="fas fa-eye"></i>
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <a href="${pageContext.request.contextPath}/adminOrders" class="view-all">View All Orders</a>
                </div>
                
                <div class="recent-users">
                    <h2>Recent Customers</h2>
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Username</th>
                                <th>Email</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${recentUsers}" var="user">
                                <tr>
                                    <td>#${user.userId}</td>
                                    <td>${user.userName}</td>
                                    <td>${user.userEmail}</td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/adminUsers?userId=${user.userId}" class="action-btn view">
                                            <i class="fas fa-eye"></i>
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <a href="${pageContext.request.contextPath}/adminUsers" class="view-all">View All Users</a>
                </div>
            </div>
            
            <div class="order-stats">
                <h2>Order Statistics</h2>
                <div class="stats-container">
                    <div class="stat-item">
                        <div class="stat-label">Pending</div>
                        <div class="stat-bar">
                            <div class="progress" style="width: ${(orderStats['pending'] / totalOrders) * 100}%"></div>
                        </div>
                        <div class="stat-value">${orderStats['pending']}</div>
                    </div>
                    <div class="stat-item">
                        <div class="stat-label">Confirmed</div>
                        <div class="stat-bar">
                            <div class="progress" style="width: ${(orderStats['confirmed'] / totalOrders) * 100}%"></div>
                        </div>
                        <div class="stat-value">${orderStats['confirmed']}</div>
                    </div>
                    <div class="stat-item">
                        <div class="stat-label">Preparing</div>
                        <div class="stat-bar">
                            <div class="progress" style="width: ${(orderStats['preparing'] / totalOrders) * 100}%"></div>
                        </div>
                        <div class="stat-value">${orderStats['preparing']}</div>
                    </div>
                    <div class="stat-item">
                        <div class="stat-label">Delivered</div>
                        <div class="stat-bar">
                            <div class="progress" style="width: ${(orderStats['delivered'] / totalOrders) * 100}%"></div>
                        </div>
                        <div class="stat-value">${orderStats['delivered']}</div>
                    </div>
                    <div class="stat-item">
                        <div class="stat-label">Cancelled</div>
                        <div class="stat-bar">
                            <div class="progress" style="width: ${(orderStats['cancelled'] / totalOrders) * 100}%"></div>
                        </div>
                        <div class="stat-value">${orderStats['cancelled']}</div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="${pageContext.request.contextPath}/javaScript/adminDashboard.js"></script>
</body>
</html>