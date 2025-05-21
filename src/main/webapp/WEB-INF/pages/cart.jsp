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
                                    <img src="${pageContext.request.contextPath}/${item.foodImage}" alt="${item.foodName}">
                                </div>
                                <div class="item-details">
                                    <h3>${item.foodName}</h3>
                                    <p class="item-description">${item.foodDescription}</p>
                                </div>
                                <div class="item-price">
                                    <c:choose>
                                        <c:when test="${item.discountedPrice lt item.foodPrice}">
                                            <span class="original-price">Rs. ${item.foodPrice}</span>
                                            <span class="discounted-price">Rs. ${item.discountedPrice}</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span>Rs. ${item.foodPrice}</span>
                                        </c:otherwise>
                                    </c:choose>
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
                            <span>Rs. <span id="subtotal">${cart.totalPrice}</span></span>
                        </div>
                        <div class="summary-item">
                            <span>Delivery Fee</span>
                            <span>Rs. <span id="delivery-fee">100.00</span></span>
                        </div>
                        <div class="summary-item total">
                            <span>Total</span>
                            <span>Rs. <span id="total">${cart.totalPrice + 100.00}</span></span>
                        </div>
                        <a href="${pageContext.request.contextPath}/checkout" class="checkout-btn">
                            Proceed to Checkout
                        </a>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </main>

    <jsp:include page="footer.jsp" />
    
    <script>
        // Calculate total based on subtotal and delivery fee
        document.addEventListener('DOMContentLoaded', function() {
            const subtotal = parseFloat(document.getElementById('subtotal').textContent) || 0;
            const deliveryFee = parseFloat(document.getElementById('delivery-fee').textContent) || 0;
            document.getElementById('total').textContent = (subtotal + deliveryFee).toFixed(2);
        });
    </script>
</body>
</html>