<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order History - The Professor's Plate</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/orderHistory.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <jsp:include page="header.jsp" />
    
    <main class="history-container">
        <div class="history-header">
            <div class="header-content">
                <h1>Order History</h1>
                <p>View and track all your orders</p>
            </div>
            <div class="header-actions">
                <div class="search-orders">
                    <i class="fas fa-search"></i>
                    <input type="text" placeholder="Search orders...">
                </div>
                <div class="filter-dropdown">
                    <button class="filter-btn">
                        <i class="fas fa-filter"></i>
                        Filter
                    </button>
                    <div class="filter-menu">
                        <label class="filter-option">
                            <input type="checkbox" checked> Recent Orders
                        </label>
                        <label class="filter-option">
                            <input type="checkbox"> Past Month
                        </label>
                        <label class="filter-option">
                            <input type="checkbox"> Past 3 Months
                        </label>
                    </div>
                </div>
            </div>
        </div>

        <div class="history-stats">
            <div class="stat-card">
                <div class="stat-icon">
                    <i class="fas fa-shopping-bag"></i>
                </div>
                <div class="stat-info">
                    <h3>Total Orders</h3>
                    <p>152</p>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon">
                    <i class="fas fa-dollar-sign"></i>
                </div>
                <div class="stat-info">
                    <h3>Total Spent</h3>
                    <p>$3,248</p>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon">
                    <i class="fas fa-star"></i>
                </div>
                <div class="stat-info">
                    <h3>Reviews Given</h3>
                    <p>48</p>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon">
                    <i class="fas fa-heart"></i>
                </div>
                <div class="stat-info">
                    <h3>Favorite Items</h3>
                    <p>24</p>
                </div>
            </div>
        </div>

        <div class="orders-timeline">
            <div class="timeline-header">
                <h2>Recent Orders</h2>
                <div class="timeline-nav">
                    <button class="nav-btn active">All</button>
                    <button class="nav-btn">Active</button>
                    <button class="nav-btn">Completed</button>
                    <button class="nav-btn">Cancelled</button>
                </div>
            </div>

            <div class="timeline-content">
                <c:forEach items="${orders}" var="order">
                    <div class="order-card">
                        <div class="order-header">
                            <div class="order-info">
                                <span class="order-id">Order #${order.id}</span>
                                <span class="order-date">${order.date}</span>
                            </div>
                            <span class="order-status ${order.status.toLowerCase()}">
                                ${order.status}
                            </span>
                        </div>
                        
                        <div class="order-items">
                            <div class="items-preview">
                                <c:forEach items="${order.items}" var="item" varStatus="status">
                                    <div class="item-image">
                                        <img src="${item.imageUrl}" alt="${item.name}">
                                    </div>
                                </c:forEach>
                            </div>
                            <div class="items-count">
                                ${order.itemCount} items
                            </div>
                        </div>
                        
                        <div class="order-summary">
                            <div class="price-info">
                                <span class="total-label">Total Amount:</span>
                                <span class="total-price">$${order.total}</span>
                            </div>
                            <div class="order-actions">
                                <a href="order/${order.id}" class="view-details">View Details</a>
                                <c:if test="${order.canReOrder}">
                                    <button class="reorder-btn">
                                        <i class="fas fa-redo"></i> Reorder
                                    </button>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>

        <div class="pagination">
            <button class="page-btn prev" disabled>
                <i class="fas fa-chevron-left"></i>
            </button>
            <div class="page-numbers">
                <button class="page-number active">1</button>
                <button class="page-number">2</button>
                <button class="page-number">3</button>
                <span class="page-ellipsis">...</span>
                <button class="page-number">12</button>
            </div>
            <button class="page-btn next">
                <i class="fas fa-chevron-right"></i>
            </button>
        </div>
    </main>

    <jsp:include page="footer.jsp" />
</body>
</html>