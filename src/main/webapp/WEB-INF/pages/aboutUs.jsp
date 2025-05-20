<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>About Us - The Professor's Plate</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/aboutUs.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <jsp:include page="header.jsp" />
    
    <main class="about-container">
        <section class="hero-section">
            <div class="hero-content">
                <h1>Our Story</h1>
                <p class="subtitle">A Journey of Culinary Excellence Since 2010</p>
            </div>
        </section>

        <section class="story-section">
            <div class="story-content">
                <div class="story-text">
                    <h2>How It All Began</h2>
                    <p>Founded by Professor James Wilson, The Professor's Plate started as a small café near the university campus. Our passion for combining academic precision with culinary creativity has made us what we are today.</p>
                </div>
                <div class="story-image">
                    <div class="image-placeholder"></div>
                </div>
            </div>
        </section>

        <section class="values-section">
            <h2>Our Core Values</h2>
            <div class="values-grid">
                <div class="value-card">
                    <i class="fas fa-leaf"></i>
                    <h3>Fresh Ingredients</h3>
                    <p>We source only the finest local ingredients for our dishes.</p>
                </div>
                <div class="value-card">
                    <i class="fas fa-heart"></i>
                    <h3>Passion for Food</h3>
                    <p>Every dish is crafted with love and attention to detail.</p>
                </div>
                <div class="value-card">
                    <i class="fas fa-users"></i>
                    <h3>Community First</h3>
                    <p>We're proud to be part of and serve our local community.</p>
                </div>
            </div>
        </section>

        <section class="stats-section">
            <div class="stats-grid">
                <div class="stat-card">
                    <h3>15+</h3>
                    <p>Years of Experience</p>
                </div>
                <div class="stat-card">
                    <h3>50+</h3>
                    <p>Team Members</p>
                </div>
                <div class="stat-card">
                    <h3>1000+</h3>
                    <p>Happy Customers</p>
                </div>
                <div class="stat-card">
                    <h3>100+</h3>
                    <p>Unique Dishes</p>
                </div>
            </div>
        </section>
    </main>

    <jsp:include page="footer.jsp" />
</body>
</html>