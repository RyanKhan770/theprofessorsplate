<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Food Gallery - The Professor's Plate</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/gallery.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Playfair+Display:wght@400;700&family=Poppins:wght@300;400;500;600&display=swap" rel="stylesheet">
</head>
<body>
   	<jsp:include page="header.jsp" />
	
    <!-- Hero Section -->
    <section class="hero">
        <div class="hero-sidebar">
            <h1 class="headline">Food</h1>
            <h1 class="headline-secondary">Gallery</h1>
            <p class="hero-text">Experience culinary artistry through our lens</p>
            <a href="/gallery" class="cta-button">Explore Gallery</a>
        </div>
        <div class="hero-image">
            <img src="${pageContext.request.contextPath}/resources/productsImage/Piz.jpg" alt="Pizza">
            <div class="image-overlay">
                <div class="overlay-content">
                    <span class="tag">Featured Dish</span>
                    <h2>Pizza</h2>
                    <p>Hand-crafted with quality ingredients</p>
                </div>
            </div>
        </div>
    </section>
    
    <!-- Filter Controls -->
    <div class="filter-controls">
        <div class="container">
            <h2 class="section-title">Our Culinary Collection</h2>
            <div class="filters">
                <button class="filter-btn active" data-filter="all">All</button>
                <button class="filter-btn" data-filter="mains">Main Courses</button>
                <button class="filter-btn" data-filter="drinks">Beverages</button>
                <button class="filter-btn" data-filter="experience">Experience</button>
            </div>
        </div>
    </div>
    
    <!-- Gallery Grid -->
    <section id="gallery" class="gallery-grid">
        <div class="gallery-item" data-category="mains">
            <img src="${pageContext.request.contextPath}/resources/productsImage/Steak.jpg" alt="Steak dish">
            <div class="item-overlay">
                <h3>Steak</h3>
                <p>Chef's specialty</p>
            </div>
        </div>
        <div class="gallery-item" data-category="mains">
            <img src="${pageContext.request.contextPath}/resources/productsImage/Vegsteak.jpg" alt="Steak with vegetables">
            <div class="item-overlay">
                <h3>Steak & Vegetables</h3>
                <p>Farm fresh</p>
            </div>
        </div>
        <div class="gallery-item" data-category="drinks">
            <img src="${pageContext.request.contextPath}/resources/productsImage/Drink.jpg" alt="Cocktail">
            <div class="item-overlay">
                <h3>Cocktail</h3>
                <p>House specialty</p>
            </div>
        </div>
        
        <div class="gallery-item" data-category="mains">
            <img src="${pageContext.request.contextPath}/resources/productsImage/Salmon.jpg" alt="Salmon dish">
            <div class="item-overlay">
                <h3>Salmon</h3>
                <p>Fresh catch</p>
            </div>
        </div>
        <div class="gallery-item" data-category="experience">
            <img src="${pageContext.request.contextPath}/resources/productsImage/Food.jpg" alt="Cooking class">
            <div class="item-overlay">
                <h3>Cooking Class</h3>
                <p>Learn with us</p>
            </div>
        </div>
        <div class="gallery-item" data-category="experience">
            <img src="${pageContext.request.contextPath}/resources/productsImage/Delivery.jpg" alt="Food delivery">
            <div class="item-overlay">
                <h3>Delivery</h3>
                <p>At your door</p>
            </div>
        </div>
        
        <div class="gallery-item" data-category="drinks">
            <img src="${pageContext.request.contextPath}/resources/productsImage/Drinks.jpg" alt="Beverages">
            <div class="item-overlay">
                <h3>Beverages</h3>
                <p>Refreshing options</p>
            </div>
        </div>
        <div class="gallery-item tall wide" data-category="drinks">
            <img src="${pageContext.request.contextPath}/resources/productsImage/Whiskey.jpg" alt="Whiskey">
            <div class="item-overlay">
                <h3>Spirits</h3>
                <p>Curated selection</p>
            </div>
        </div>
        
        <div class="gallery-item" data-category="mains">
            <img src="${pageContext.request.contextPath}/resources/productsImage/Pasta.jpg" alt="Pasta dish">
            <div class="item-overlay">
                <h3>Pasta</h3>
                <p>House-made</p>
            </div>
        </div>
    </section>
    
    <!-- Feature Image (Burger) -->
    <div class="feature-section">
        <div class="feature-text">
            <h2>Chef's Special</h2>
            <p>Our signature burger is made with quality ingredients and served with fries and our special sauce.</p>
            <a href="#" class="cta-button secondary">Order Now</a>
        </div>
        <div class="feature-image">
            <img src="${pageContext.request.contextPath}/resources/productsImage/Burger.jpg" alt="Burger with fries">
        </div>
    </div>
    
  
	<jsp:include page="footer.jsp" />
    <script src="${pageContext.request.contextPath}/javaScript/gallery.js"></script>
</body>
</html>