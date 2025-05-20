<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shopping Cart - The Professor's Plate</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/cart.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <jsp:include page="header.jsp" />
    
    <main class="cart-container">
        <h1><i class="fas fa-shopping-cart"></i> Your Cart</h1>
        
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
        
        <c:choose>
            <c:when test="${empty cartItems}">
                <div class="empty-cart">
                    <i class="fas fa-shopping-basket fa-4x"></i>
                    <h2>Your cart is empty</h2>
                    <p>Looks like you haven't added any items to your cart yet.</p>
                    <a href="${pageContext.request.contextPath}/menu" class="btn primary-btn">Explore Menu</a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="cart-content">
                    <div class="cart-items">
                        <c:forEach items="${cartItems}" var="item">
                            <div class="cart-item">
                                <div class="item-image">
                                    <img src="${pageContext.request.contextPath}/resources/productsImage/${item.foodImage}" alt="${item.foodName}">
                                </div>
                                <div class="item-details">
                                    <h3>${item.foodName}</h3>
                                    <p class="item-description">${item.foodDescription}</p>
                                </div>
                                <div class="item-price">
                                    Rs. ${item.foodPrice}
                                </div>
                                <div class="item-actions">
                                    <form action="${pageContext.request.contextPath}/cart" method="post">
                                        <input type="hidden" name="action" value="remove">
                                        <input type="hidden" name="foodId" value="${item.foodId}">
                                        <button type="submit" class="remove-btn"><i class="fas fa-trash-alt"></i></button>
                                    </form>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    
                    <div class="cart-summary">
                        <h3>Order Summary</h3>
                        <div class="summary-item">
                            <span>Subtotal</span>
                            <span>Rs. <span id="subtotal">0.00</span></span>
                        </div>
                        <div class="summary-item">
                            <span>Delivery Fee</span>
                            <span>Rs. <span id="delivery-fee">100.00</span></span>
                        </div>
                        <div class="summary-item total">
                            <span>Total</span>
                            <span>Rs. <span id="total">0.00</span></span>
                        </div>
                        <a href="${pageContext.request.contextPath}/checkout" class="btn checkout-btn">Proceed to Checkout</a>
                        <a href="${pageContext.request.contextPath}/menu" class="btn continue-shopping">Continue Shopping</a>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </main>
    
    <jsp:include page="footer.jsp" />
    
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Calculate totals
            const items = document.querySelectorAll('.cart-item');
            let subtotal = 0;
            
            items.forEach(item => {
                const priceText = item.querySelector('.item-price').textContent;
                const price = parseFloat(priceText.replace('Rs. ', ''));
                subtotal += price;
            });
            
            const deliveryFee = 100.00;
            const total = subtotal + deliveryFee;
            
            document.getElementById('subtotal').textContent = subtotal.toFixed(2);
            document.getElementById('total').textContent = total.toFixed(2);
            
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