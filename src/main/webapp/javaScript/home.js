document.addEventListener('DOMContentLoaded', function() {
    // Smooth scroll for navigation
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function(e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });

    // Simple order button interaction
    document.querySelectorAll('.order-btn').forEach(button => {
        button.addEventListener('click', function() {
            this.textContent = 'Added to Cart';
            // Get the CSS variable value using getComputedStyle
            const accentColor = getComputedStyle(document.documentElement)
                .getPropertyValue('--color-accent').trim();
            this.style.backgroundColor = accentColor;
            
            setTimeout(() => {
                this.textContent = 'Order Now';
                this.style.backgroundColor = '';
            }, 2000);
        });
    });
});