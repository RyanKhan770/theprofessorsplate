<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Customer Reviews - The Professor's Plate</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/review.css">
    <link href="https://fonts.googleapis.com/css2?family=Playfair+Display:wght@400;700&family=Poppins:wght@300;400;500;600&display=swap" rel="stylesheet">
</head>
<body>
	<jsp:include page="header.jsp" />

    <!-- Page Banner -->
    <section class="review-banner">
        <div class="banner-wrapper">
            <div class="banner-image">
                <img src="${pageContext.request.contextPath}/resources/productsImage/logo.jpg" alt="Restaurant Logo">
            </div>
            <div class="banner-text">
                <h1>Customer Testimonials</h1>
                <p>What our valued customers say about us</p>
            </div>
        </div>
    </section>

    <!-- Main Content -->
    <main class="review-content">
        <!-- Customer Reviews -->
        <section class="review-grid">
            <!-- Single Review -->
            <article class="review-box">
                <div class="user-photo">
                    <div class="user-avatar"></div>
                </div>
                <div class="review-details">
                    <h3 class="reviewer-name">Sophia Williams</h3>
                    <p class="review-text">What sets Professor's Plate apart is their delivery service. The food arrived hot and well-packaged, and the delivery staff is incredibly professional and punctual...</p>
                    <div class="review-verified">✓ Verified Order</div>
                    <button class="expand-review">Read More</button>
                    <div class="review-rating">
                        <span class="rating-stars">★★★★★</span>
                        <span class="rating-number">5/5</span>
                    </div>
                </div>
            </article>

       
        </section>

        <aside class="restaurant-showcase">
            <div class="showcase-header">
                <h2>The Professor's Plate</h2>
                <p class="contact-info">Call us: 9840754489 | Order Online</p>
                <p class="last-update">Updated: 2025-05-07 15:30:08</p>
            </div>
            <div class="showcase-gallery">
                <img src="${pageContext.request.contextPath}/resources/productsImage/bonaf.jpg" alt="Featured Dish" class="showcase-image">
                <img src="${pageContext.request.contextPath}/resources/productsImage/chilliChiken.jpg" alt="Featured Dish" class="showcase-image">
                <img src="${pageContext.request.contextPath}/resources/productsImage/fruitesCombo.jpg" alt="Featured Dish" class="showcase-image">
            </div>
        </aside>
    </main>

    <!-- Photo Gallery -->
    <section class="photo-gallery">
        <div class="gallery-grid">
            <img src="${pageContext.request.contextPath}/resources/productsImage/cookingImage.jpg" alt="Dining Experience" class="gallery-image large">
            <img src="${pageContext.request.contextPath}/resources/productsImage/indianCurd.jpg" class="gallery-image small">
            <img src="${pageContext.request.contextPath}/resources/productsImage/chef1.jpg" class="gallery-image tall">
            <img src="${pageContext.request.contextPath}/resources/productsImage/newDish.jpg" class="gallery-image wide">
        </div>
        <img src="${pageContext.request.contextPath}/resources/productsImage/midStew.jpg" alt="Restaurant Banner" class="gallery-banner">
    </section>

	<jsp:include page="footer.jsp" />
    <script src="${pageContext.request.contextPath}/javaScript/review.js"></script>
</body>
</html>