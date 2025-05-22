<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Favorites - The Professor's Plate</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/favorites.css">
</head>
<body>
    <jsp:include page="header.jsp" />
    
    <main class="favorites-container">
        <div class="favorites-header">
            <h1><i class="fas fa-heart"></i> My Favorite Items</h1>
            <p>Quick access to your favorite dishes at The Professor's Plate</p>
        </div>

        <div class="favorites-grid">
            <!-- Sample favorite item 1 -->
            <div class="favorite-item">
                <div class="favorite-image">
                    <img src="${pageContext.request.contextPath}/resources/productsImage/Pasta.jpg" alt="Pasta Carbonara">
                </div>
                <div class="favorite-details">
                    <h3>Pasta Carbonara</h3>
                    <p class="category">Italian</p>
                    <div class="price-container">
                        <span class="regular-price">$12.99</span>
                    </div>
                </div>
                <div class="favorite-actions">
                    <button class="remove-favorite">
                        <i class="fas fa-heart-broken"></i> Remove
                    </button>
                    <button class="add-to-cart">
                        <i class="fas fa-shopping-cart"></i> Add to Cart
                    </button>
                </div>
            </div>
            
            <!-- Sample favorite item 2 -->
            <div class="favorite-item">
                <div class="favorite-image">
                    <img src="${pageContext.request.contextPath}/resources/productsImage/Piz.jpg" alt="Margherita Pizza">
                </div>
                <div class="favorite-details">
                    <h3>Margherita Pizza</h3>
                    <p class="category">Pizza</p>
                    <div class="price-container">
                        <span class="original-price">$15.99</span>
                        <span class="discounted-price">$13.99</span>
                    </div>
                </div>
                <div class="favorite-actions">
                    <button class="remove-favorite">
                        <i class="fas fa-heart-broken"></i> Remove
                    </button>
                    <button class="add-to-cart">
                        <i class="fas fa-shopping-cart"></i> Add to Cart
                    </button>
                </div>
            </div>
            
            <!-- Sample favorite item 3 -->
            <div class="favorite-item">
                <div class="favorite-image">
                    <img src="${pageContext.request.contextPath}/resources/productsImage/fruitesCombo.jpg" alt="Caesar Salad">
                </div>
                <div class="favorite-details">
                    <h3>Caesar Salad</h3>
                    <p class="category">Salads</p>
                    <div class="price-container">
                        <span class="regular-price">$8.99</span>
                    </div>
                </div>
                <div class="favorite-actions">
                    <button class="remove-favorite">
                        <i class="fas fa-heart-broken"></i> Remove
                    </button>
                    <button class="add-to-cart">
                        <i class="fas fa-shopping-cart"></i> Add to Cart
                    </button>
                </div>
            </div>
            
            <!-- Sample favorite item 4 -->
            <div class="favorite-item">
                <div class="favorite-image">
                    <img src="${pageContext.request.contextPath}/resources/productsImage/Burger.jpg" alt="Classic Burger">
                </div>
                <div class="favorite-details">
                    <h3>Classic Burger</h3>
                    <p class="category">Burgers</p>
                    <div class="price-container">
                        <span class="original-price">$11.99</span>
                        <span class="discounted-price">$9.99</span>
                    </div>
                </div>
                <div class="favorite-actions">
                    <button class="remove-favorite">
                        <i class="fas fa-heart-broken"></i> Remove
                    </button>
                    <button class="add-to-cart">
                        <i class="fas fa-shopping-cart"></i> Add to Cart
                    </button>
                </div>
            </div>
        </div>
    </main>
    
    <jsp:include page="footer.jsp" />
</body>
</html>