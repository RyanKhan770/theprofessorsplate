<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Checkout - The Professor's Plate</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/checkOut.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <jsp:include page="header.jsp" />
    
    <main class="checkout-container">
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
        
        <div class="checkout-progress">
            <div class="progress-step active">
                <span class="step-number">1</span>
                <span class="step-text">Delivery</span>
            </div>
            <div class="progress-step">
                <span class="step-number">2</span>
                <span class="step-text">Payment</span>
            </div>
            <div class="progress-step">
                <span class="step-number">3</span>
                <span class="step-text">Review</span>
            </div>
        </div>

        <form action="${pageContext.request.contextPath}/checkout" method="post" class="checkout-form">
            <div class="checkout-content">
                <div class="delivery-details">
                    <h2>Delivery Details</h2>
                    
                    <div class="form-section">
                        <div class="form-group">
                            <label for="deliveryLocation">Delivery Address</label>
                            <textarea id="deliveryLocation" name="deliveryLocation" required></textarea>
                        </div>
                        
                        <div class="form-group">
                            <label for="deliveryPhone">Contact Number</label>
                            <input type="tel" id="deliveryPhone" name="deliveryPhone" pattern="[0-9]{10}" required>
                            <small>Format: 10-digit number (e.g., 9866214499)</small>
                        </div>
                    </div>

                    <div class="delivery-time">
                        <h3>Delivery Time</h3>
                        <div class="time-slots">
                            <label class="time-slot active">
                                <input type="radio" name="deliveryTime" value="ASAP" checked>
                                <div class="slot-content">
                                    <span class="slot-time">ASAP</span>
                                    <span class="slot-eta">30-45 min</span>
                                </div>
                            </label>
                            <label class="time-slot">
                                <input type="radio" name="deliveryTime" value="Later">
                                <div class="slot-content">
                                    <span class="slot-time">Later Today</span>
                                    <span class="slot-eta">Schedule</span>
                                </div>
                            </label>
                        </div>
                    </div>
                    
                    <div class="payment-method">
                        <h3>Payment Method</h3>
                        <div class="payment-options">
                            <label class="payment-option active">
                                <input type="radio" name="paymentMethod" value="cash" checked>
                                <div class="option-content">
                                    <i class="fas fa-money-bill-wave"></i>
                                    <span>Cash on Delivery</span>
                                </div>
                            </label>
                            <label class="payment-option">
                                <input type="radio" name="paymentMethod" value="esewa">
                                <div class="option-content">
                                    <i class="fas fa-wallet"></i>
                                    <span>eSewa</span>
                                </div>
                            </label>
                        </div>
                    </div>
                </div>

                <div class="order-summary">
                    <h2>Order Summary</h2>
                    <div class="cart-items">
                        <c:forEach items="${cartItems}" var="item">
                            <div class="cart-item">
                                <div class="item-image">
                                    <img src="${pageContext.request.contextPath}/${item.foodImage}" alt="${item.foodName}">
                                </div>
                                <div class="item-details">
                                    <h4>${item.foodName}</h4>
                                    <p class="item-description">${item.foodDescription}</p>
                                </div>
                                <div class="item-price">
                                    <c:choose>
                                        <c:when test="${item.discountedPrice lt item.foodPrice}">
                                            <span class="discounted-price">Rs. ${item.discountedPrice}</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span>Rs. ${item.foodPrice}</span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    
                    <div class="summary-totals">
                        <div class="summary-item">
                            <span>Subtotal</span>
                            <span>Rs. ${subtotal}</span>
                        </div>
                        <div class="summary-item">
                            <span>Delivery Fee</span>
                            <span>Rs. ${deliveryFee}</span>
                        </div>
                        <div class="summary-item total">
                            <span>Total</span>
                            <span>Rs. ${total}</span>
                        </div>
                    </div>
                    
                    <button type="submit" class="place-order-btn">Place Order</button>
                </div>
            </div>
        </form>
    </main>

    <jsp:include page="footer.jsp" />

    <script>
        // Toggle active class for time slots
        document.addEventListener('DOMContentLoaded', function() {
            const timeSlots = document.querySelectorAll('.time-slot');
            timeSlots.forEach(slot => {
                slot.addEventListener('click', function() {
                    timeSlots.forEach(s => s.classList.remove('active'));
                    this.classList.add('active');
                });
            });
            
            // Toggle active class for payment options
            const paymentOptions = document.querySelectorAll('.payment-option');
            paymentOptions.forEach(option => {
                option.addEventListener('click', function() {
                    paymentOptions.forEach(o => o.classList.remove('active'));
                    this.classList.add('active');
                });
            });
        });
    </script>
</body>
</html>