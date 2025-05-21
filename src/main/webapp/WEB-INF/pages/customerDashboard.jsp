<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Customer Dashboard - The Professor's Plate</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/customerDashboard.css">
    <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;700&family=Playfair+Display:wght@400;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <!-- Include Chart.js for analytics -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
    <jsp:include page="header.jsp" />
    
    <!-- Dashboard Header -->
    <section class="dashboard-hero">
        <div class="dashboard-hero-content">
            <div class="welcome-text fade-in">
                <h1>Welcome back, ${user.userName}!</h1>
                <p>Manage your orders, favorites, and explore your dining history</p>
            </div>
            <div class="user-summary fade-in">
                <div class="user-avatar">
                    <c:choose>
                        <c:when test="${not empty user.userImage}">
                            <img src="${pageContext.request.contextPath}/${user.userImage}" alt="${user.userName}">
                        </c:when>
                        <c:otherwise>
                            <img src="${pageContext.request.contextPath}/resources/images/default-user.png" alt="${user.userName}">
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="user-info">
                    <p><i class="fas fa-envelope"></i> ${user.userEmail}</p>
                    <p><i class="fas fa-clock"></i> Last login: ${currentDateTime}</p>
                </div>
            </div>
        </div>
    </section>

    <!-- Dashboard Container -->
    <div class="dashboard-container">
        <!-- Sidebar -->
        <aside class="dashboard-sidebar">
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
        <main class="dashboard-main">
            <!-- Alert Messages -->
            <c:if test="${not empty success}">
                <div class="alert success">
                    <p>${success}</p>
                    <span class="close-btn">&times;</span>
                </div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="alert error">
                    <p>${error}</p>
                    <span class="close-btn">&times;</span>
                </div>
            </c:if>
            
            <!-- Dashboard Stats -->
            <section class="stats-section">
                <h2 class="section-title">Your Summary</h2>
                <div class="stats-grid">
                    <div class="stat-card">
                        <div class="stat-icon">
                            <i class="fas fa-shopping-bag"></i>
                        </div>
                        <div class="stat-info">
                            <h3>Total Orders</h3>
                            <p>${totalOrders}</p>
                        </div>
                    </div>
                    
                    <div class="stat-card">
                        <div class="stat-icon">
                            <i class="fas fa-money-bill-wave"></i>
                        </div>
                        <div class="stat-info">
                            <h3>Total Spent</h3>
                            <p>Rs. <fmt:formatNumber value="${totalSpent}" pattern="#,##0.00" /></p>
                        </div>
                    </div>
                    
                    <div class="stat-card">
                        <div class="stat-icon">
                            <i class="fas fa-receipt"></i>
                        </div>
                        <div class="stat-info">
                            <h3>Avg. Order Value</h3>
                            <p>Rs. <fmt:formatNumber value="${avgOrderValue}" pattern="#,##0.00" /></p>
                        </div>
                    </div>
                    
                    <div class="stat-card">
                        <div class="stat-icon">
                            <i class="fas fa-heart"></i>
                        </div>
                        <div class="stat-info">
                            <h3>Favorites</h3>
                            <p>${favoriteItems.size()}</p>
                        </div>
                    </div>
                </div>
            </section>
            
            <!-- Order Status Summary -->
            <section class="order-status-section">
                <h2 class="section-title">Your Orders by Status</h2>
                <div class="status-badges-grid">
                    <div class="status-badge">
                        <div class="badge-icon pending">
                            <i class="fas fa-clock"></i>
                        </div>
                        <div class="badge-info">
                            <h4>Pending</h4>
                            <p>${orderCountByStatus['pending'] != null ? orderCountByStatus['pending'] : 0}</p>
                        </div>
                    </div>
                    <div class="status-badge">
                        <div class="badge-icon processing">
                            <i class="fas fa-cog"></i>
                        </div>
                        <div class="badge-info">
                            <h4>Processing</h4>
                            <p>${orderCountByStatus['processing'] != null ? orderCountByStatus['processing'] : 0}</p>
                        </div>
                    </div>
                    <div class="status-badge">
                        <div class="badge-icon shipping">
                            <i class="fas fa-truck"></i>
                        </div>
                        <div class="badge-info">
                            <h4>Shipping</h4>
                            <p>${orderCountByStatus['shipping'] != null ? orderCountByStatus['shipping'] : 0}</p>
                        </div>
                    </div>
                    <div class="status-badge">
                        <div class="badge-icon delivered">
                            <i class="fas fa-check-circle"></i>
                        </div>
                        <div class="badge-info">
                            <h4>Delivered</h4>
                            <p>${orderCountByStatus['delivered'] != null ? orderCountByStatus['delivered'] : 0}</p>
                        </div>
                    </div>
                    <div class="status-badge">
                        <div class="badge-icon cancelled">
                            <i class="fas fa-times-circle"></i>
                        </div>
                        <div class="badge-info">
                            <h4>Cancelled</h4>
                            <p>${orderCountByStatus['cancelled'] != null ? orderCountByStatus['cancelled'] : 0}</p>
                        </div>
                    </div>
                </div>
            </section>
            
            <!-- Analytics Section with Charts -->
            <section class="analytics-section">
                <h2 class="section-title">Your Spending Analytics</h2>
                
                <div class="charts-container">
                    <!-- Monthly Spending Chart -->
                    <div class="chart-card">
                        <h3>Monthly Spending Trend</h3>
                        <div class="chart-container">
                            <canvas id="monthlySpendingChart"></canvas>
                        </div>
                    </div>
                    
                    <!-- Category Spending Chart -->
                    <div class="chart-card">
                        <h3>Spending by Category</h3>
                        <div class="chart-container">
                            <canvas id="categorySpendingChart"></canvas>
                        </div>
                    </div>
                </div>
            </section>
            
            <!-- Recent Activity and Most Ordered Items -->
            <div class="activity-section">
                <div class="activity-timeline">
                    <h2 class="section-title">Recent Activity</h2>
                    
                    <div class="timeline">
                        <c:forEach items="${recentActivity}" var="activity">
                            <div class="timeline-item">
                                <div class="timeline-icon ${activity.type}">
                                    <c:choose>
                                        <c:when test="${activity.type eq 'order'}">
                                            <i class="fas fa-shopping-bag"></i>
                                        </c:when>
                                        <c:when test="${activity.type eq 'review'}">
                                            <i class="fas fa-star"></i>
                                        </c:when>
                                        <c:otherwise>
                                            <i class="fas fa-circle"></i>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div class="timeline-content">
                                    <h4>${activity.description}</h4>
                                    <p class="time"><i class="far fa-clock"></i> <fmt:formatDate value="${activity.date}" pattern="MMM dd, yyyy HH:mm" /></p>
                                    <c:if test="${activity.type eq 'order'}">
                                        <a href="${pageContext.request.contextPath}/order?id=${activity.id}" class="action-btn">View Details</a>
                                    </c:if>
                                </div>
                            </div>
                        </c:forEach>
                        <c:if test="${empty recentActivity}">
                            <div class="empty-message">
                                <p>No recent activity found.</p>
                            </div>
                        </c:if>
                    </div>
                </div>
                
                <div class="most-ordered">
                    <h2 class="section-title">Your Most Ordered Items</h2>
                    
                    <div class="most-ordered-items">
                        <c:forEach items="${mostOrderedItems}" var="item">
                            <div class="most-ordered-card">
                                <div class="item-image">
                                    <img src="${pageContext.request.contextPath}/${item.foodImage}" alt="${item.foodName}">
                                </div>
                                <div class="item-details">
                                    <h3>${item.foodName}</h3>
                                    <p>Ordered ${item.orderCount} times</p>
                                    <a href="${pageContext.request.contextPath}/menu?foodId=${item.foodId}" class="action-btn">View Item</a>
                                </div>
                            </div>
                        </c:forEach>
                        <c:if test="${empty mostOrderedItems}">
                            <div class="empty-message">
                                <p>You haven't ordered anything yet.</p>
                                <a href="${pageContext.request.contextPath}/menu" class="cta-button">Explore Menu</a>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
            
            <!-- Recent Orders -->
            <section class="recent-orders-section">
                <div class="section-header">
                    <h2 class="section-title">Recent Orders</h2>
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
                                    <td><fmt:formatDate value="${order.orderDate}" pattern="MMM dd, yyyy HH:mm" /></td>
                                    <td><span class="status ${order.orderStatus}">${order.orderStatus}</span></td>
                                    <td>${order.orderQuantity}</td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/order?id=${order.orderId}" class="action-btn">
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
            <section class="food-recommendations">
                <h2 class="section-title">Recommended For You</h2>
                <div class="items-grid">
                    <c:forEach items="${recommendedItems}" var="item">
                        <div class="item-card">
                            <img src="${pageContext.request.contextPath}/${item.foodImage}" alt="${item.foodName}">
                            <div class="item-info">
                                <h3>${item.foodName}</h3>
                                <p>${item.foodCategory}</p>
                                <div class="food-price">
                                    <c:choose>
                                        <c:when test="${item.discountedPrice != item.foodPrice}">
                                            <span class="original-price">Rs. <fmt:formatNumber value="${item.foodPrice}" pattern="#,##0.00" /></span>
                                            <span class="price">Rs. <fmt:formatNumber value="${item.discountedPrice}" pattern="#,##0.00" /></span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="price">Rs. <fmt:formatNumber value="${item.foodPrice}" pattern="#,##0.00" /></span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <a href="${pageContext.request.contextPath}/menu?foodId=${item.foodId}" class="order-btn">View Item</a>
                            </div>
                        </div>
                    </c:forEach>
                    <c:if test="${empty recommendedItems}">
                        <div class="empty-message center-message">
                            <p>No recommendations available yet.</p>
                            <a href="${pageContext.request.contextPath}/menu" class="cta-button">Explore Menu</a>
                        </div>
                    </c:if>
                </div>
            </section>
            
            <!-- Your Favorites -->
            <section class="your-favorites">
                <div class="section-header">
                    <h2 class="section-title">Your Favorites</h2>
                    <a href="${pageContext.request.contextPath}/favorites" class="view-all">View All</a>
                </div>
                <div class="items-grid">
                    <c:forEach items="${favoriteItems}" var="item">
                        <div class="item-card">
                            <img src="${pageContext.request.contextPath}/${item.foodImage}" alt="${item.foodName}">
                            <div class="item-info">
                                <h3>${item.foodName}</h3>
                                <p>${item.foodCategory}</p>
                                <div class="food-price">
                                    <c:choose>
                                        <c:when test="${item.discountedPrice != item.foodPrice}">
                                            <span class="original-price">Rs. <fmt:formatNumber value="${item.foodPrice}" pattern="#,##0.00" /></span>
                                            <span class="price">Rs. <fmt:formatNumber value="${item.discountedPrice}" pattern="#,##0.00" /></span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="price">Rs. <fmt:formatNumber value="${item.foodPrice}" pattern="#,##0.00" /></span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <a href="${pageContext.request.contextPath}/menu?foodId=${item.foodId}" class="order-btn">View Item</a>
                            </div>
                        </div>
                    </c:forEach>
                    <c:if test="${empty favoriteItems}">
                        <div class="empty-message center-message">
                            <p>No favorite items yet. Explore our menu and add some!</p>
                            <a href="${pageContext.request.contextPath}/menu" class="cta-button">Explore Menu</a>
                        </div>
                    </c:if>
                </div>
            </section>
        </main>
    </div>
    
    <jsp:include page="footer.jsp" />
    
    <script>
        // Charts initialization
        document.addEventListener('DOMContentLoaded', function() {
            // Extract data from model attributes for monthly spending chart
            const monthlyLabels = [];
            const monthlyData = [];
            <c:forEach items="${monthlySpending}" var="entry">
                monthlyLabels.push("${entry.key}");
                monthlyData.push(${entry.value});
            </c:forEach>
            
            // Extract data for category chart
            const categoryLabels = [];
            const categoryData = [];
            <c:forEach items="${spendingByCategory}" var="entry">
                categoryLabels.push("${entry.key}");
                categoryData.push(${entry.value});
            </c:forEach>
            
            // Monthly spending chart
            const monthlyCtx = document.getElementById('monthlySpendingChart').getContext('2d');
            const monthlyChart = new Chart(monthlyCtx, {
                type: 'line',
                data: {
                    labels: monthlyLabels,
                    datasets: [{
                        label: 'Monthly Spending (Rs.)',
                        data: monthlyData,
                        backgroundColor: 'rgba(58, 90, 64, 0.2)',
                        borderColor: 'rgba(58, 90, 64, 1)',
                        borderWidth: 2,
                        tension: 0.3,
                        pointBackgroundColor: 'rgba(58, 90, 64, 1)',
                        pointRadius: 4
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    scales: {
                        y: {
                            beginAtZero: true,
                            ticks: {
                                callback: function(value) {
                                    return 'Rs. ' + value;
                                }
                            }
                        }
                    },
                    plugins: {
                        legend: {
                            position: 'top',
                        },
                        tooltip: {
                            callbacks: {
                                label: function(context) {
                                    return 'Rs. ' + context.raw;
                                }
                            }
                        }
                    }
                }
            });
            
            // Category spending chart
            const categoryCtx = document.getElementById('categorySpendingChart').getContext('2d');
            const categoryChart = new Chart(categoryCtx, {
                type: 'pie',
                data: {
                    labels: categoryLabels,
                    datasets: [{
                        data: categoryData,
                        backgroundColor: [
                            'rgba(58, 90, 64, 0.7)',
                            'rgba(197, 168, 128, 0.7)',
                            'rgba(224, 90, 71, 0.7)',
                            'rgba(83, 144, 217, 0.7)',
                            'rgba(156, 39, 176, 0.7)',
                            'rgba(255, 159, 64, 0.7)',
                            'rgba(99, 99, 99, 0.7)'
                        ],
                        borderColor: [
                            'rgba(58, 90, 64, 1)',
                            'rgba(197, 168, 128, 1)',
                            'rgba(224, 90, 71, 1)',
                            'rgba(83, 144, 217, 1)',
                            'rgba(156, 39, 176, 1)',
                            'rgba(255, 159, 64, 1)',
                            'rgba(99, 99, 99, 1)'
                        ],
                        borderWidth: 1
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: {
                            position: 'right',
                            labels: {
                                boxWidth: 15
                            }
                        },
                        tooltip: {
                            callbacks: {
                                label: function(context) {
                                    return context.label + ': Rs. ' + context.raw;
                                }
                            }
                        }
                    }
                }
            });
            
            // Handle alert message dismissal
            const alerts = document.querySelectorAll('.alert');
            const closeButtons = document.querySelectorAll('.close-btn');
            
            closeButtons.forEach((btn, index) => {
                btn.addEventListener('click', () => {
                    alerts[index].style.display = 'none';
                });
            });
            
            // Auto-hide alerts after 5 seconds
            alerts.forEach(alert => {
                setTimeout(() => {
                    alert.style.opacity = '0';
                    setTimeout(() => {
                        alert.style.display = 'none';
                    }, 500);
                }, 5000);
            });
        });
    </script>
</body>
</html>