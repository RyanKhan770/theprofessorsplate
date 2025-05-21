<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
    
    <!-- Display messages if any -->
    <c:if test="${not empty success}">
        <div class="alert success-alert">
            <p>${success}</p>
        </div>
    </c:if>
    
    <c:if test="${not empty error}">
        <div class="alert error-alert">
            <p>${error}</p>
        </div>
    </c:if>
    
    <main class="history-container">
        <div class="history-header">
            <div class="header-content">
                <h1>Order History</h1>
                <p>View and track all your orders</p>
            </div>
            <div class="header-actions">
                <div class="search-orders">
                    <i class="fas fa-search"></i>
                    <input type="text" id="orderSearch" placeholder="Search orders...">
                </div>
                <div class="filter-dropdown">
                    <button class="filter-btn">
                        <i class="fas fa-filter"></i>
                        Filter
                    </button>
                    <div class="filter-menu">
                        <label class="filter-option">
                            <input type="checkbox" checked data-filter="all"> All Orders
                        </label>
                        <label class="filter-option">
                            <input type="checkbox" data-filter="pending"> Pending
                        </label>
                        <label class="filter-option">
                            <input type="checkbox" data-filter="confirmed"> Confirmed
                        </label>
                        <label class="filter-option">
                            <input type="checkbox" data-filter="preparing"> Preparing
                        </label>
                        <label class="filter-option">
                            <input type="checkbox" data-filter="delivered"> Delivered
                        </label>
                        <label class="filter-option">
                            <input type="checkbox" data-filter="cancelled"> Cancelled
                        </label>
                    </div>
                </div>
            </div>
        </div>

        <c:choose>
            <c:when test="${empty orders}">
                <div class="empty-history">
                    <i class="fas fa-shopping-bag fa-4x"></i>
                    <h2>No Orders Yet</h2>
                    <p>You haven't placed any orders yet. Start exploring our menu!</p>
                    <a href="${pageContext.request.contextPath}/menu" class="btn primary-btn">Explore Menu</a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="orders-timeline">
                    <div class="timeline-header">
                        <h2>Your Orders</h2>
                    </div>

                    <div class="timeline-content">
                        <c:forEach items="${orders}" var="order">
                            <div class="timeline-card" data-status="${order.orderStatus}">
                                <div class="card-header">
                                    <div class="order-number">
                                        <h3>Order #${order.orderId}</h3>
                                        <span class="order-date"><fmt:formatDate value="${order.orderDate}" pattern="MMM dd, yyyy h:mm a" /></span>
                                    </div>
                                    <div class="order-status">
                                        <span class="status-badge ${order.orderStatus}">${order.orderStatus}</span>
                                    </div>
                                </div>
                                
                                <div class="card-content">
                                    <div class="order-preview">
                                        <c:forEach items="${order.items}" var="item" end="2">
                                            <div class="preview-item">
                                                <img src="${pageContext.request.contextPath}/${item.foodImage}" alt="${item.foodName}">
                                            </div>
                                        </c:forEach>
                                        <c:if test="${order.items.size() > 3}">
                                            <div class="preview-more">
                                                +${order.items.size() - 3}
                                            </div>
                                        </c:if>
                                    </div>
                                    <div class="order-summary">
                                        <p><i class="fas fa-shopping-basket"></i> ${order.orderQuantity} items</p>
                                        <p><i class="fas fa-money-bill-wave"></i> Rs. ${order.payment.paymentAmount}</p>
                                    </div>
                                </div>
                                
                                <div class="card-footer">
                                    <a href="${pageContext.request.contextPath}/order?id=${order.orderId}" class="view-details-btn">
                                        <i class="fas fa-eye"></i> View Details
                                    </a>
                                    <c:if test="${order.orderStatus eq 'delivered'}">
                                        <a href="#" class="reorder-btn">
                                            <i class="fas fa-redo"></i> Reorder
                                        </a>
                                    </c:if>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </main>

    <jsp:include page="footer.jsp" />
    
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Search functionality
            const searchInput = document.getElementById('orderSearch');
            const timelineCards = document.querySelectorAll('.timeline-card');
            
            searchInput.addEventListener('input', function() {
                const searchTerm = this.value.toLowerCase();
                
                timelineCards.forEach(card => {
                    const orderNumber = card.querySelector('.order-number h3').textContent.toLowerCase();
                    const orderDate = card.querySelector('.order-date').textContent.toLowerCase();
                    const orderStatus = card.querySelector('.order-status').textContent.toLowerCase();
                    
                    if (orderNumber.includes(searchTerm) || orderDate.includes(searchTerm) || orderStatus.includes(searchTerm)) {
                        card.style.display = 'flex';
                    } else {
                        card.style.display = 'none';
                    }
                });
            });
            
            // Filter functionality
            const filterOptions = document.querySelectorAll('.filter-option input');
            
            filterOptions.forEach(option => {
                option.addEventListener('change', function() {
                    const filterValue = this.getAttribute('data-filter');
                    
                    if (filterValue === 'all') {
                        // If "All Orders" is checked, show all cards
                        const isChecked = this.checked;
                        
                        timelineCards.forEach(card => {
                            card.style.display = isChecked ? 'flex' : 'none';
                        });
                        
                        // Uncheck other filters if "All Orders" is checked
                        if (isChecked) {
                            filterOptions.forEach(opt => {
                                if (opt.getAttribute('data-filter') !== 'all') {
                                    opt.checked = false;
                                }
                            });
                        }
                    } else {
                        // Uncheck "All Orders" filter
                        document.querySelector('input[data-filter="all"]').checked = false;
                        
                        // Get all selected filters
                        const selectedFilters = Array.from(filterOptions)
                            .filter(opt => opt.checked && opt.getAttribute('data-filter') !== 'all')
                            .map(opt => opt.getAttribute('data-filter'));
                        
                        // If no filters selected, check "All Orders" and show all cards
                        if (selectedFilters.length === 0) {
                            document.querySelector('input[data-filter="all"]').checked = true;
                            timelineCards.forEach(card => {
                                card.style.display = 'flex';
                            });
                        } else {
                            // Apply selected filters
                            timelineCards.forEach(card => {
                                const cardStatus = card.getAttribute('data-status');
                                card.style.display = selectedFilters.includes(cardStatus) ? 'flex' : 'none';
                            });
                        }
                    }
                });
            });
        });
    </script>
</body>
</html>