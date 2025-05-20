<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>The Professor's Plate - Fine Dining</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css">
    <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;700&family=Playfair+Display:wght@400;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>
    <jsp:include page="header.jsp" />
    
    <section class="hero" id="hero">
        <div class="hero-content">
            <div class="hero-text fade-in">
                <h1>Culinary Excellence<br>Meets Academic Charm</h1>
                <p>Experience fine dining in a sophisticated atmosphere</p>
                <a href="#menu" class="cta-button">Explore Menu</a>
            </div>
            <div class="hero-image fade-in">
                <img src="${pageContext.request.contextPath}/resources/productsImage/Steak.jpg" alt="Signature Dish">
            </div>
        </div>
    </section>

    <section class="categories" id="menu">
        <h2 class="section-title">Our Specialties</h2>
        <div class="category-grid">
            <div class="category-card">
                <img src="${pageContext.request.contextPath}/resources/productsImage/Steak.jpg" alt="Main Course">
                <h3>Main Course</h3>
                <p align="center">15 Dishes</p>
            </div>
            <div class="category-card">
                <img src="${pageContext.request.contextPath}/resources/productsImage/Drinks.jpg" alt="Beverages">
                <h3>Beverages</h3>
                <p align="center">12 Options</p>
            </div>
            <div class="category-card">
                <img src="${pageContext.request.contextPath}/resources/productsImage/Pasta.jpg" alt="Pasta">
                <h3>Pasta</h3>
                <p align="center">8 Varieties</p>
            </div>
        </div>
    </section>

    <section class="special-items">
        <h2 class="section-title">Today's Specials</h2>
        <div class="items-grid">
            <div class="item-card">
                <img src="${pageContext.request.contextPath}/resources/productsImage/Steak.jpg" alt="Ribeye Steak">
                <div class="item-info">
                    <h3>Ribeye Steak</h3>
                    <p>Premium cut, perfectly seasoned</p>
                    <span class="price">$34.99</span>
                    <button class="order-btn">Order Now</button>
                </div>
            </div>
            <div class="item-card">
                <img src="${pageContext.request.contextPath}/resources/productsImage/Salmon.jpg" alt="Grilled Salmon">
                <div class="item-info">
                    <h3>Grilled Salmon</h3>
                    <p>Fresh catch with herbs</p>
                    <span class="price">$29.99</span>
                    <button class="order-btn">Order Now</button>
                </div>
            </div>
            <div class="item-card">
                <img src="${pageContext.request.contextPath}/resources/productsImage/Pasta.jpg" alt="Truffle Pasta">
                <div class="item-info">
                    <h3>Truffle Pasta</h3>
                    <p>House-made specialty</p>
                    <span class="price">$24.99</span>
                    <button class="order-btn">Order Now</button>
                </div>
            </div>
        </div>
    </section>

    <section class="contact" id="contact">
        <div class="contact-content">
            <h2>Visit Us Today</h2>
            <p>Make a reservation or order online</p>
            <div class="contact-info">
                <p><i class="fas fa-phone"></i> +977 980-1234567</p>
                <p><i class="fas fa-envelope"></i> info@professorsplate.com</p>
                <p><i class="fas fa-map-marker-alt"></i> Kathmandu, Nepal</p>
            </div>
        </div>
    </section>
    
    <jsp:include page="footer.jsp" />
    <script src="${pageContext.request.contextPath}/javaScript/home.js"></script>
</body>
</html>