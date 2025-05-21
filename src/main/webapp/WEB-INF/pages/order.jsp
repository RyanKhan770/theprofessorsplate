<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
    
    <main class="order-container">
        <div class="order-header">
            <div class="order-info">
                <h1>Order #${order.orderId}</h1>
                <div class="order-meta">
                    <span class="order-date">
                        <i class="fas fa-calendar"></i>
                        <fmt:formatDate value="${order.orderDate}" pattern="MMMM dd, yyyy HH:mm" />
                    </span>
                    <span class="order-status ${order.orderStatus.toLowerCase()}">
                        <i class="fas fa-clock"></i>
                        ${order.orderStatus}
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
                        <div class="timeline-item ${order.orderStatus == 'pending' || order.orderStatus == 'confirmed' || order.orderStatus == 'preparing' || order.orderStatus == 'delivered' ? 'complete' : ''}">
                            <div class="timeline-icon">
                                <i class="fas fa-check"></i>
                            </div>
                            <div class="timeline-content">
                                <h3>Order Confirmed</h3>
                                <p>Your order has been received</p>
                                <span class="timeline-time"><fmt:formatDate value="${order.orderDate}" pattern="HH:mm" /></span>
                            </div>
                        </div>
                        <div class="timeline-item ${order.orderStatus == 'preparing' || order.orderStatus == 'delivered' ? 'complete' : order.orderStatus == 'confirmed' ? 'active' : ''}">
                            <div class="timeline-icon">
                                <i class="fas fa-utensils"></i>
                            </div>
                            <div class="timeline-content">
                                <h3>Preparing</h3>
                                <p>Your food is being prepared</p>
                                <c:if test="${order.orderStatus == 'preparing' || order.orderStatus == 'delivered'}">
                                    <span class="timeline-time"><fmt:formatDate value="${order.orderDate}" pattern="HH:mm" /></span>
                                </c:if>
                            </div>
                        </div>
                        <div class="timeline-item ${order.orderStatus == 'in-transit' || order.orderStatus == 'delivered' ? 'complete' : order.orderStatus == 'preparing' ? 'active' : ''}">
                            <div class="timeline-icon">
                                <i class="fas fa-motorcycle"></i>
                            </div>
                            <div class="timeline-content">
                                <h3>On the Way</h3>
                                <p>Order picked up for delivery</p>
                                <c:if test="${order.orderStatus == 'in-transit' || order.orderStatus == 'delivered'}">
                                    <span class="timeline-time"><fmt:formatDate value="${order.delivery.deliveryTime}" pattern="HH:mm" /></span>
                                </c:if>
                            </div>
                        </div>
                        <div class="timeline-item ${order.orderStatus == 'delivered' ? 'complete' : order.orderStatus == 'in-transit' ? 'active' : ''}">
                            <div class="timeline-icon">
                                <i class="fas fa-home"></i>
                            </div>
                            <div class="timeline-content">
                                <h3>Delivered</h3>
                                <p>Enjoy your meal!</p>
                                <c:if test="${order.orderStatus == 'delivered'}">
                                    <span class="timeline-time"><fmt:formatDate value="${order.delivery.deliveryTime}" pattern="HH:mm" /></span>
                                </c:if>
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
                                    <img src="${pageContext.request.contextPath}/${item.foodImage}" alt="${item.foodName}">
                                </div>
                                <div class="item-details">
                                    <h4>${item.foodName}</h4>
                                    <p class="item-description">${item.foodDescription}</p>
                                </div>
                                <div class="item-price">Rs. ${item.discountedPrice}</div>
                            </div>
                        </c:forEach>
                    </div>
                </section>
            </div>
            
            <aside class="order-sidebar">
                <section class="delivery-info">
                    <h3>Delivery Information</h3>
                    <p><i class="fas fa-map-marker-alt"></i> ${order.delivery.deliveryLocation}</p>
                    <p><i class="fas fa-phone"></i> ${order.delivery.deliveryPhone}</p>
                    <p><i class="fas fa-user"></i> Delivery Person: 
                        <c:choose>
                            <c:when test="${order.delivery.deliveryPerson eq 'To be assigned'}">
                                <span class="awaiting">To be assigned</span>
                            </c:when>
                            <c:otherwise>
                                ${order.delivery.deliveryPerson}
                            </c:otherwise>
                        </c:choose>
                    </p>
                    <p><i class="fas fa-info-circle"></i> Status: <span class="status-badge ${order.delivery.deliveryStatus}">${order.delivery.deliveryStatus}</span></p>
                </section>
                
                <section class="payment-info">
                    <h3>Payment Information</h3>
                    <p><i class="fas fa-money-bill-wave"></i> Method: ${order.payment.paymentMethod}</p>
                    <p><i class="fas fa-info-circle"></i> Status: <span class="status-badge ${order.payment.paymentStatus}">${order.payment.paymentStatus}</span></p>
                    <p><i class="fas fa-receipt"></i> Amount: Rs. ${order.payment.paymentAmount}</p>
                </section>
                
                <section class="order-summary">
                    <h3>Order Summary</h3>
                    <div class="summary-item">
                        <span>Subtotal</span>
                        <span>Rs. ${order.payment.paymentAmount - 100.00}</span>
                    </div>
                    <div class="summary-item">
                        <span>Delivery Fee</span>
                        <span>Rs. 100.00</span>
                    </div>
                    <div class="summary-item total">
                        <span>Total</span>
                        <span>Rs. ${order.payment.paymentAmount}</span>
                    </div>
                </section>
            </aside>
        </div>
    </main>

    <jsp:include page="footer.jsp" />
</body>
</html>