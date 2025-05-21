<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Menu - The Professor's Plate</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/menu.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <jsp:include page="header.jsp" />
    
    <main class="menu-container">
        <section class="menu-hero">
            <h1>Our Menu</h1>
            <p>Explore our exquisite selection of culinary delights</p>
        </section>

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

        <!-- Category Filter -->
        <div class="category-filter">
            <button class="filter-btn active" data-filter="all">All</button>
            <button class="filter-btn" data-filter="nepali">Nepali</button>
            <button class="filter-btn" data-filter="italian">Italian</button>
            <button class="filter-btn" data-filter="american">American</button>
            <button class="filter-btn" data-filter="indian">Indian</button>
            <button class="filter-btn" data-filter="drinks">Drinks</button>
        </div>

        <!-- Menu Items Grid -->
        <div class="menu-grid">
            <c:forEach items="${menuList}" var="item">
                <div class="menu-item" data-category="${item.foodCategory}">
                    <div class="item-image">
                        <img src="${pageContext.request.contextPath}/${item.foodImage}" alt="${item.foodName}">
                        
                        <c:if test="${sessionScope.userId != null}">
                            <form action="${pageContext.request.contextPath}/cart" method="post" class="add-to-cart-form">
                                <input type="hidden" name="action" value="add">
                                <input type="hidden" name="foodId" value="${item.foodId}">
                                <button type="submit" class="add-to-cart-btn">
                                    <i class="fas fa-shopping-cart"></i> Add to Cart
                                </button>
                            </form>
                        </c:if>
                    </div>
                    <div class="item-details">
                        <h3>${item.foodName}</h3>
                        <p class="item-description">${item.foodDescription}</p>
                        <div class="item-price-section">
                            <span class="item-price">Rs. ${item.foodPrice}</span>
                            <c:if test="${item.discountId > 0}">
                                <!-- If there's a discount, we'll show it here -->
                                <span class="item-discount">Special Offer!</span>
                            </c:if>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </main>

    <jsp:include page="footer.jsp" />
    
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Filter functionality
            const filterButtons = document.querySelectorAll('.filter-btn');
            const menuItems = document.querySelectorAll('.menu-item');
            
            filterButtons.forEach(button => {
                button.addEventListener('click', function() {
                    // Remove active class from all buttons
                    filterButtons.forEach(btn => btn.classList.remove('active'));
                    
                    // Add active class to clicked button
                    this.classList.add('active');
                    
                    // Get filter value
                    const filterValue = this.getAttribute('data-filter');
                    
                    // Show/hide menu items based on filter
                    menuItems.forEach(item => {
                        if (filterValue === 'all' || item.getAttribute('data-category') === filterValue) {
                            item.style.display = 'block';
                        } else {
                            item.style.display = 'none';
                        }
                    });
                });
            });
            
            // Hide alert messages after 5 seconds
            const alerts = document.querySelectorAll('.alert');
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