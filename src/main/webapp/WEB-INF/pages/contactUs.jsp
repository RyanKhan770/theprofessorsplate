<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Contact Us - The Professor's Plate</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/contactUs.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <jsp:include page="header.jsp" />
    
    <main class="contact-container">
        <section class="contact-hero">
            <h1>Get in Touch</h1>
            <p>We'd love to hear from you. Send us a message and we'll respond as soon as possible.</p>
        </section>

        <section class="contact-content">
            <div class="contact-info">
                <div class="info-card">
                    <i class="fas fa-map-marker-alt"></i>
                    <h3>Visit Us</h3>
                    <p>123 University Avenue</p>
                    <p>New York, NY 10003</p>
                </div>
                
                <div class="info-card">
                    <i class="fas fa-phone"></i>
                    <h3>Call Us</h3>
                    <p>+1 (555) 123-4567</p>
                    <p>Mon-Sun: 10:00 AM - 10:00 PM</p>
                </div>
                
                <div class="info-card">
                    <i class="fas fa-envelope"></i>
                    <h3>Email Us</h3>
                    <p>info@professorsplate.com</p>
                    <p>support@professorsplate.com</p>
                </div>

                <div class="social-links">
                    <h3>Follow Us</h3>
                    <div class="social-icons">
                        <a href="#" class="social-icon"><i class="fab fa-facebook-f"></i></a>
                        <a href="#" class="social-icon"><i class="fab fa-twitter"></i></a>
                        <a href="#" class="social-icon"><i class="fab fa-instagram"></i></a>
                        <a href="#" class="social-icon"><i class="fab fa-linkedin-in"></i></a>
                    </div>
                </div>
            </div>

            <div class="contact-form-container">
                <form class="contact-form" action="submitContact" method="POST">
                    <h2>Send us a Message</h2>
                    
                    <div class="form-group">
                        <label for="name">Full Name</label>
                        <input type="text" id="name" name="name" required>
                    </div>

                    <div class="form-group">
                        <label for="email">Email Address</label>
                        <input type="email" id="email" name="email" required>
                    </div>

                    <div class="form-group">
                        <label for="subject">Subject</label>
                        <input type="text" id="subject" name="subject" required>
                    </div>

                    <div class="form-group">
                        <label for="message">Message</label>
                        <textarea id="message" name="message" rows="5" required></textarea>
                    </div>

                    <button type="submit" class="submit-btn">
                        Send Message
                        <i class="fas fa-paper-plane"></i>
                    </button>
                </form>
            </div>
        </section>

        <section class="map-section">
            <h2>Find Us on the Map</h2>
            <div class="map-container">
                <!-- Replace with actual map implementation -->
                <div class="map-placeholder">
                    <i class="fas fa-map"></i>
                    <p>Map Loading...</p>
                </div>
            </div>
        </section>

        <section class="faq-section">
            <h2>Frequently Asked Questions</h2>
            <div class="faq-grid">
                <div class="faq-item">
                    <h3>What are your opening hours?</h3>
                    <p>We are open daily from 10:00 AM to 10:00 PM.</p>
                </div>
                <div class="faq-item">
                    <h3>Do you offer delivery?</h3>
                    <p>Yes, we deliver within a 5-mile radius of our restaurant.</p>
                </div>
                <div class="faq-item">
                    <h3>Can I make a reservation?</h3>
                    <p>Yes, you can make reservations through our website or by calling us.</p>
                </div>
                <div class="faq-item">
                    <h3>Do you cater for events?</h3>
                    <p>Yes, we offer catering services for both small and large events.</p>
                </div>
            </div>
        </section>
    </main>

    <jsp:include page="footer.jsp" />
</body>
</html>