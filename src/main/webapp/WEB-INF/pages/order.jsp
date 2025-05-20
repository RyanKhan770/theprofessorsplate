<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order Details - The Professor's Plate</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/order.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <jsp:include page="header.jsp" />
    
    <main class="order-container">
        <div class="order-header">
            <div class="order-info">
                <h1>Order #${order.id}</h1>
                <div class="order-meta">
                    <span class="order-date">
                        <i class="fas fa-calendar"></i>
                        ${order.date}
                    </span>
                    <span class="order-status ${order.status.toLowerCase()}">
                        <i class="fas fa-clock"></i>
                        ${order.status}
                    </span>
                </div>
            </div>
            <div class="order-actions">
                <button class="action-btn" onclick="window.print()">
                    <i class="fas fa-print"></i>
                    Print Order
                </button>
                <button class="action-btn support">
                    <i class="fas fa-headset"></i>
                    Need Help?
                </button>
            </div>
        </div>

        <div class="order-content">
            <div class="order-details">
                <section class="tracking-section">
                    <h2>Order Tracking</h2>
                    <div class="tracking-timeline">
                        <div class="timeline-item complete">
                            <div class="timeline-icon">
                                <i class="fas fa-check"></i>
                            </div>
                            <div class="timeline-content">
                                <h3>Order Confirmed</h3>
                                <p>Your order has been received</p>
                                <span class="timeline-time">10:21 AM</span>
                            </div>
                        </div>
                        <div class="timeline-item active">
                            <div class="timeline-icon">
                                <i class="fas fa-utensils"></i>
                            </div>
                            <div class="timeline-content">
                                <h3>Preparing</h3>
                                <p>Your food is being prepared</p>
                                <span class="timeline-time">10:25 AM</span>
                            </div>
                        </div>
                        <div class="timeline-item">
                            <div class="timeline-icon">
                                <i class="fas fa-motorcycle"></i>
                            </div>
                            <div class="timeline-content">
                                <h3>On the Way</h3>
                                <p>Order picked up for delivery</p>
                            </div>
                        </div>
                        <div class="timeline-item">
                            <div class="timeline-icon">
                                <i class="fas fa-home"></i>
                            </div>
                            <div class="timeline-content">
                                <h3>Delivered</h3>
                                <p>Enjoy your meal!</p>
                            </div>
                        </div>
                    </div>
                </section>

                <section class="items-section">
                    <h2>Order Items</h2>
                    <div class="order-items">
                        <c:forEach items="${order.items}" var="item">
                            <div class="order-item">
                                <div class="item-image">
                                    <img src="${item.imageUrl}" alt="${item.name}">
                                </div>
                                <div class="item-details">
                                    <h3>${item.name}</h3>
                                    <p class="item-options">${item.options}</p>
                                    <div class="item-quantity">
                                        Quantity: ${item.quantity}
                                    </div>
                                </div>
                                <div class="item-price">
                                    $${item.price}
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </section>
            </div>

            <div class="order-summary">
                <section class="delivery-info">
                    <h2>Delivery Information</h2>
                    <div class="info-card">
                        <div class="info-row">
                            <i class="fas fa-map-marker-alt"></i>
                            <div>
                                <h3>Delivery Address</h3>
                                <p>${order.address.street}</p>
                                <p>${order.address.city}, ${order.address.state} ${order.address.zip}</p>
                            </div>
                        </div>
                        <div class="info-row">
                            <i class="fas fa-user"></i>
                            <div>
                                <h3>Contact</h3>
                                <p>${order.customer.name}</p>
                                <p>${order.customer.phone}</p>
                            </div>
                        </div>
                    </div>
                </section>

                <section class="payment-summary">
                    <h2>Payment Summary</h2>
                    <div class="summary-card">
                        <div class="summary-row">
                            <span>Subtotal</span>
                            <span>$${order.subtotal}</span>
                        </div>
                        <div class="summary-row">
                            <span>Delivery Fee</span>
                            <span>$${order.deliveryFee}</span>
                        </div>
                        <div class="summary-row">
                            <span>Tax</span>
                            <span>$${order.tax}</span>
                        </div>
                        <div class="summary-row total">
                            <span>Total</span>
                            <span>$${order.total}</span>
                        </div>
                        <div class="payment-method">
                            <i class="fas fa-credit-card"></i>
                            <span>Paid with ${order.paymentMethod}</span>
                        </div>
                    </div>
                </section>
            </div>
        </div>
    </main>

    <div class="support-modal" style="display: none;">
        <div class="modal-content">
            <button class="close-modal">
                <i class="fas fa-times"></i>
            </button>
            <h2>Need Help?</h2>
            <div class="support-options">
                <button class="support-option">
                    <i class="fas fa-question-circle"></i>
                    <span>Order Issue</span>
                </button>
                <button class="support-option">
                    <i class="fas fa-exclamation-circle"></i>
                    <span>Wrong Order</span>
                </button>
                <button class="support-option">
                    <i class="fas fa-clock"></i>
                    <span>Delivery Delay</span>
                </button>
                <button class="support-option">
                    <i class="fas fa-phone"></i>
                    <span>Contact Support</span>
                </button>
            </div>
        </div>
    </div>

    <jsp:include page="footer.jsp" />
</body>
</html>