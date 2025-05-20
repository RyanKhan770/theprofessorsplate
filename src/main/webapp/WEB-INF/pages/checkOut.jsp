<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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

        <div class="checkout-content">
            <div class="delivery-details">
                <h2>Delivery Details</h2>
                
                <div class="saved-addresses">
                    <h3>Saved Addresses</h3>
                    <div class="address-cards">
                        <div class="address-card active">
                            <div class="address-type">Home</div>
                            <p>123 Main Street</p>
                            <p>Apt 4B</p>
                            <p>New York, NY 10001</p>
                            <button class="edit-address">Edit</button>
                        </div>
                        <div class="address-card">
                            <div class="address-type">Work</div>
                            <p>456 Office Plaza</p>
                            <p>Suite 789</p>
                            <p>New York, NY 10002</p>
                            <button class="edit-address">Edit</button>
                        </div>
                        <button class="add-address-card">
                            <i class="fas fa-plus"></i>
                            <span>Add New Address</span>
                        </button>
                    </div>
                </div>

                <div class="delivery-time">
                    <h3>Delivery Time</h3>
                    <div class="time-slots">
                        <button class="time-slot active">
                            <span class="slot-time">ASAP</span>
                            <span class="slot-eta">30-45 min</span>
                        </button>
                        <button class="time-slot">
                            <span class="slot-time">Later Today</span>
                            <span class="slot-eta">Schedule</span>
                        </button>
                    </div>
                </div>
            </div>

            <div class="order-summary">
                <h2>Order Summary</h2>
                <div class="cart-items">
                    <c:forEach items="${cartItems}" var="item">
                        <div class="cart-item">
                            <div class="item-image">
                                <img src="${item.imageUrl}" alt="${item.name}">
                            </div>
                            <div class="item-details">
                                <h4>${item.name}</h4>
                                <p class="item-description">${item.description}</p>
                                <div class="item-quantity">
                                    <button class="quantity-btn minus">-</button>
                                    <span>${item.quantity}</span>
                                    <button class="quantity-btn plus">+</button>
                                </div>
                            </div>
                            <div class="item-price">
                                $${item.price}
                            </div>
                        </div>
                    </c:forEach>
                </div>

                <div class="price-details">
                    <div class="price-row">
                        <span>Subtotal</span>
                        <span>$${subtotal}</span>
                    </div>
                    <div class="price-row">
                        <span>Delivery Fee</span>
                        <span>$${deliveryFee}</span>
                    </div>
                    <div class="price-row">
                        <span>Tax</span>
                        <span>$${tax}</span>
                    </div>
                    <div class="price-row total">
                        <span>Total</span>
                        <span>$${total}</span>
                    </div>
                </div>

                <button class="proceed-btn">
                    Proceed to Payment
                    <i class="fas fa-arrow-right"></i>
                </button>
            </div>
        </div>
    </main>

    <jsp:include page="footer.jsp" />
</body>
</html>