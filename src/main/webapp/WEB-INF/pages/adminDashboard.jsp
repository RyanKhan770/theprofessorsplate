<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - The Professor's Plate</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/adminDashboard.css">
    <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;700&family=Playfair+Display:wght@400;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <!-- Include Chart.js for analytics -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
    <jsp:include page="header.jsp" />
    
    <!-- Dashboard Hero -->
    <section class="dashboard-hero">
        <div class="dashboard-hero-content">
            <div class="welcome-text fade-in">
                <h1>Admin Dashboard</h1>
                <p>Manage products, orders, and restaurant operations</p>
            </div>
            <div class="admin-info fade-in">
                <div class="admin-avatar">
                    <c:choose>
                        <c:when test="${not empty user.userImage}">
                            <img src="${pageContext.request.contextPath}/${user.userImage}" alt="${user.userName}">
                        </c:when>
                        <c:otherwise>
                            <img src="${pageContext.request.contextPath}/resources/images/default-user.png" alt="${user.userName}">
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="admin-details">
                    <h3>${user.userName}</h3>
                    <p><i class="fas fa-crown"></i> Administrator</p>
                    <p><i class="fas fa-clock"></i> ${currentDateTime}</p>
                </div>
            </div>
        </div>
    </section>

    <div class="dashboard-container">
        <!-- Sidebar -->
        <aside class="dashboard-sidebar">
            <nav class="sidebar-menu">
                <ul>
                    <li class="active"><a href="${pageContext.request.contextPath}/adminDashboard"><i class="fas fa-tachometer-alt"></i> Dashboard</a></li>
                    <li><a href="${pageContext.request.contextPath}/adminProducts"><i class="fas fa-utensils"></i> Manage Products</a></li>
                    <li><a href="${pageContext.request.contextPath}/adminOrders"><i class="fas fa-shopping-bag"></i> Manage Orders</a></li>
                    <li><a href="${pageContext.request.contextPath}/adminUsers"><i class="fas fa-users"></i> Manage Users</a></li>
                    <li><a href="${pageContext.request.contextPath}/adminReviews"><i class="fas fa-star"></i> Manage Reviews</a></li>
                    <li><a href="${pageContext.request.contextPath}/adminReports"><i class="fas fa-chart-bar"></i> Reports</a></li>
                    <li><a href="${pageContext.request.contextPath}/adminSettings"><i class="fas fa-cog"></i> Settings</a></li>
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
                <h2 class="section-title">Restaurant Overview</h2>
                <div class="stats-grid">
                    <div class="stat-card">
                        <div class="stat-icon">
                            <i class="fas fa-users"></i>
                        </div>
                        <div class="stat-info">
                            <h3>Total Customers</h3>
                            <p>${totalCustomers}</p>
                        </div>
                    </div>
                    
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
                            <h3>Total Revenue</h3>
                            <p>Rs. <fmt:formatNumber value="${totalRevenue}" pattern="#,##0.00" /></p>
                        </div>
                    </div>
                    
                    <div class="stat-card">
                        <div class="stat-icon">
                            <i class="fas fa-utensils"></i>
                        </div>
                        <div class="stat-info">
                            <h3>Menu Items</h3>
                            <p>${totalMenuItems}</p>
                        </div>
                    </div>
                </div>
            </section>
            
            <!-- Order Stats and Sales Charts -->
            <div class="charts-section">
                <!-- Order Stats Chart -->
                <section class="chart-card">
                    <div class="section-header">
                        <h2 class="section-title">Order Status</h2>
                    </div>
                    <div class="chart-container">
                        <canvas id="orderStatsChart"></canvas>
                    </div>
                </section>
                
                <!-- Sales Chart -->
                <section class="chart-card">
                    <div class="section-header">
                        <h2 class="section-title">Weekly Revenue</h2>
                    </div>
                    <div class="chart-container">
                        <canvas id="salesChart"></canvas>
                    </div>
                </section>
            </div>
            
            <!-- Quick Actions -->
            <section class="quick-actions">
                <h2 class="section-title">Quick Actions</h2>
                <div class="actions-grid">
                    <a href="${pageContext.request.contextPath}/adminProducts?action=add" class="action-card">
                        <div class="action-icon">
                            <i class="fas fa-plus"></i>
                        </div>
                        <h3>Add New Menu Item</h3>
                    </a>
                    <a href="${pageContext.request.contextPath}/adminOrders?action=pending" class="action-card">
                        <div class="action-icon">
                            <i class="fas fa-tasks"></i>
                        </div>
                        <h3>Manage Pending Orders</h3>
                    </a>
                    <a href="${pageContext.request.contextPath}/adminProducts?action=inventory" class="action-card">
                        <div class="action-icon">
                            <i class="fas fa-boxes"></i>
                        </div>
                        <h3>Check Inventory</h3>
                    </a>
                    <a href="${pageContext.request.contextPath}/adminReports?report=daily" class="action-card">
                        <div class="action-icon">
                            <i class="fas fa-file-alt"></i>
                        </div>
                        <h3>View Today's Report</h3>
                    </a>
                </div>
            </section>
            
            <!-- Recent Orders -->
            <section class="orders-section">
                <div class="section-header">
                    <h2 class="section-title">Recent Orders</h2>
                    <a href="${pageContext.request.contextPath}/adminOrders" class="view-all">View All</a>
                </div>
                <div class="table-responsive">
                    <table>
                        <thead>
                            <tr>
                                <th>Order ID</th>
                                <th>Customer</th>
                                <th>Date</th>
                                <th>Amount</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${recentOrders}" var="order">
                                <tr>
                                    <td>#${order.orderId}</td>
                                    <td>${order.customerName}</td>
                                    <td><fmt:formatDate value="${order.orderDate}" pattern="MMM dd, yyyy HH:mm" /></td>
                                    <td>Rs. <fmt:formatNumber value="${order.orderAmount}" pattern="#,##0.00" /></td>
                                    <td>
                                        <select class="status-select" data-order-id="${order.orderId}" data-current-status="${order.orderStatus}">
                                            <option value="pending" ${order.orderStatus == 'pending' ? 'selected' : ''}>Pending</option>
                                            <option value="processing" ${order.orderStatus == 'processing' ? 'selected' : ''}>Processing</option>
                                            <option value="shipping" ${order.orderStatus == 'shipping' ? 'selected' : ''}>Shipping</option>
                                            <option value="delivered" ${order.orderStatus == 'delivered' ? 'selected' : ''}>Delivered</option>
                                            <option value="cancelled" ${order.orderStatus == 'cancelled' ? 'selected' : ''}>Cancelled</option>
                                        </select>
                                    </td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/adminOrders?action=view&orderId=${order.orderId}" class="action-btn">
                                            <i class="fas fa-eye"></i>
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty recentOrders}">
                                <tr>
                                    <td colspan="6" class="text-center">No recent orders found</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </section>
            
            <!-- Recently Added Products -->
            <section class="products-section">
                <div class="section-header">
                    <h2 class="section-title">Recently Added Menu Items</h2>
                    <a href="${pageContext.request.contextPath}/adminProducts" class="view-all">View All</a>
                </div>
                <div class="products-grid">
                    <c:forEach items="${recentProducts}" var="product">
                        <div class="product-card">
                            <div class="product-image">
                                <img src="${pageContext.request.contextPath}/${product.foodImage}" alt="${product.foodName}">
                                <span class="product-category">${product.foodCategory}</span>
                            </div>
                            <div class="product-info">
                                <h3>${product.foodName}</h3>
                                <div class="product-price">
                                    <c:choose>
                                        <c:when test="${product.discountedPrice != product.foodPrice}">
                                            <span class="original-price">Rs. <fmt:formatNumber value="${product.foodPrice}" pattern="#,##0.00" /></span>
                                            <span class="price">Rs. <fmt:formatNumber value="${product.discountedPrice}" pattern="#,##0.00" /></span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="price">Rs. <fmt:formatNumber value="${product.foodPrice}" pattern="#,##0.00" /></span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div class="product-actions">
                                    <a href="${pageContext.request.contextPath}/adminProducts?action=edit&foodId=${product.foodId}" class="action-btn edit">
                                        <i class="fas fa-edit"></i> Edit
                                    </a>
                                    <button class="action-btn delete" data-product-id="${product.foodId}" data-product-name="${product.foodName}">
                                        <i class="fas fa-trash"></i> Delete
                                    </button>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                    <div class="add-product-card">
                        <a href="${pageContext.request.contextPath}/adminProducts?action=add" class="add-product-btn">
                            <i class="fas fa-plus"></i>
                            <h3>Add New Menu Item</h3>
                        </a>
                    </div>
                </div>
            </section>
            
            <!-- Recent Users -->
            <section class="users-section">
                <div class="section-header">
                    <h2 class="section-title">Recent Users</h2>
                    <a href="${pageContext.request.contextPath}/adminUsers" class="view-all">View All</a>
                </div>
                <div class="table-responsive">
                    <table>
                        <thead>
                            <tr>
                                <th>User ID</th>
                                <th>Name</th>
                                <th>Email</th>
                                <th>Role</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${recentUsers}" var="user">
                                <tr>
                                    <td>#${user.userId}</td>
                                    <td>${user.userName}</td>
                                    <td>${user.userEmail}</td>
                                    <td>${user.userRole}</td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/adminUsers?action=view&userId=${user.userId}" class="action-btn">
                                            <i class="fas fa-eye"></i>
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty recentUsers}">
                                <tr>
                                    <td colspan="5" class="text-center">No recent users found</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </section>
        </main>
    </div>
    
    <!-- Delete Product Confirmation Modal -->
    <div id="deleteModal" class="modal">
        <div class="modal-content">
            <span class="close-modal">&times;</span>
            <h3>Confirm Deletion</h3>
            <p>Are you sure you want to delete <strong id="productName"></strong>?</p>
            <div class="modal-actions">
                <form id="deleteForm" method="POST" action="${pageContext.request.contextPath}/adminProducts">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" id="productIdInput" name="productId" value="">
                    <button type="submit" class="action-btn delete">Yes, Delete</button>
                    <button type="button" class="action-btn cancel" id="cancelDelete">Cancel</button>
                </form>
            </div>
        </div>
    </div>
    
    <jsp:include page="footer.jsp" />
    
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Order Status Chart
            const orderStatusLabels = [];
            const orderStatusData = [];
            <c:forEach items="${orderStats}" var="entry">
                orderStatusLabels.push("${entry.key}");
                orderStatusData.push(${entry.value});
            </c:forEach>
            
            const orderStatusCtx = document.getElementById('orderStatsChart').getContext('2d');
            const orderStatusChart = new Chart(orderStatusCtx, {
                type: 'doughnut',
                data: {
                    labels: orderStatusLabels,
                    datasets: [{
                        data: orderStatusData,
                        backgroundColor: [
                            'rgba(255, 152, 0, 0.7)', // pending
                            'rgba(33, 150, 243, 0.7)', // processing
                            'rgba(156, 39, 176, 0.7)', // shipping
                            'rgba(58, 90, 64, 0.7)', // delivered
                            'rgba(244, 67, 54, 0.7)', // cancelled
                        ],
                        borderColor: [
                            'rgba(255, 152, 0, 1)',
                            'rgba(33, 150, 243, 1)',
                            'rgba(156, 39, 176, 1)',
                            'rgba(58, 90, 64, 1)',
                            'rgba(244, 67, 54, 1)',
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
                        }
                    }
                }
            });
            
            // Weekly Sales Chart
            const salesLabels = [];
            const salesData = [];
            <c:forEach items="${weeklySales}" var="entry">
                salesLabels.push("${entry.key}");
                salesData.push(${entry.value});
            </c:forEach>
            
            const salesCtx = document.getElementById('salesChart').getContext('2d');
            const salesChart = new Chart(salesCtx, {
                type: 'bar',
                data: {
                    labels: salesLabels,
                    datasets: [{
                        label: 'Revenue (Rs.)',
                        data: salesData,
                        backgroundColor: 'rgba(197, 168, 128, 0.7)',
                        borderColor: 'rgba(197, 168, 128, 1)',
                        borderWidth: 1
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
                    }
                }
            });
            
            // Status Change Handler
            const statusSelects = document.querySelectorAll('.status-select');
            statusSelects.forEach(select => {
                select.addEventListener('change', function() {
                    const orderId = this.dataset.orderId;
                    const currentStatus = this.dataset.currentStatus;
                    const newStatus = this.value;
                    
                    if (currentStatus !== newStatus) {
                        if (confirm(`Are you sure you want to change order #${orderId} status to "${newStatus}"?`)) {
                            // Send AJAX request to update order status
                            const xhr = new XMLHttpRequest();
                            xhr.open('POST', '${pageContext.request.contextPath}/adminOrders', true);
                            xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                            xhr.onload = function() {
                                if (xhr.status === 200) {
                                    const response = JSON.parse(xhr.responseText);
                                    if (response.success) {
                                        // Update the data-current-status attribute
                                        select.dataset.currentStatus = newStatus;
                                        
                                        // Show success message
                                        const alert = document.createElement('div');
                                        alert.className = 'alert success';
                                        alert.innerHTML = `
                                            <p>Order #${orderId} status has been updated to "${newStatus}"</p>
                                            <span class="close-btn">&times;</span>
                                        `;
                                        document.querySelector('.dashboard-main').insertBefore(alert, document.querySelector('.stats-section'));
                                        
                                        // Add close functionality for the new alert
                                        const closeBtn = alert.querySelector('.close-btn');
                                        closeBtn.addEventListener('click', function() {
                                            alert.remove();
                                        });
                                        
                                        // Auto-hide alert after 5 seconds
                                        setTimeout(() => {
                                            alert.style.opacity = '0';
                                            setTimeout(() => {
                                                alert.remove();
                                            }, 500);
                                        }, 5000);
                                    } else {
                                        // Show error message
                                        alert('Error: ' + response.message);
                                        // Reset the select to previous value
                                        select.value = currentStatus;
                                    }
                                } else {
                                    // Show error message
                                    alert('Error: Could not update order status. Please try again.');
                                    // Reset the select to previous value
                                    select.value = currentStatus;
                                }
                            };
                            xhr.onerror = function() {
                                alert('Network Error: Could not connect to server.');
                                select.value = currentStatus;
                            };
                            xhr.send(`action=updateStatus&orderId=${orderId}&status=${newStatus}`);
                        } else {
                            // Reset to previous value if user cancels
                            select.value = currentStatus;
                        }
                    }
                });
            });
            
            // Delete Product Modal
            const deleteModal = document.getElementById('deleteModal');
            const productNameElem = document.getElementById('productName');
            const productIdInput = document.getElementById('productIdInput');
            const deleteButtons = document.querySelectorAll('.action-btn.delete');
            const closeModalBtn = document.querySelector('.close-modal');
            const cancelDeleteBtn = document.getElementById('cancelDelete');
            
            deleteButtons.forEach(button => {
                if (button.classList.contains('action-btn') && !button.type) {
                    button.addEventListener('click', function() {
                        const productId = this.dataset.productId;
                        const productName = this.dataset.productName;
                        
                        productNameElem.textContent = productName;
                        productIdInput.value = productId;
                        deleteModal.style.display = 'block';
                    });
                }
            });
            
            closeModalBtn.addEventListener('click', function() {
                deleteModal.style.display = 'none';
            });
            
            cancelDeleteBtn.addEventListener('click', function() {
                deleteModal.style.display = 'none';
            });
            
            window.addEventListener('click', function(event) {
                if (event.target == deleteModal) {
                    deleteModal.style.display = 'none';
                }
            });
            
            // Handle alert message dismissal
            const alerts = document.querySelectorAll('.alert');
            const closeButtons = document.querySelectorAll('.close-btn');
            
            closeButtons.forEach((btn, index) => {
                if (alerts[index]) {
                    btn.addEventListener('click', () => {
                        alerts[index].style.display = 'none';
                    });
                }
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