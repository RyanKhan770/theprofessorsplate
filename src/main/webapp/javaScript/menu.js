document.addEventListener('DOMContentLoaded', function() {
    // Get current time and user info from the page
    const currentTime = '2025-05-07 03:47:39';
    const currentUser = 'RyanKhan770';
    
    // Mobile Menu Toggle
    const hamburger = document.querySelector('.hamburger-menu');
    const nav = document.querySelector('.nav');

    if (hamburger && nav) {
        hamburger.addEventListener('click', function() {
            nav.classList.toggle('active');
            this.classList.toggle('active');
        });
    }

    // Menu Filtering
    const filterBtns = document.querySelectorAll('.filter-btn');
    const menuItems = document.querySelectorAll('.menu-item');

    if (filterBtns.length && menuItems.length) {
        // Initial animation for menu items
        menuItems.forEach((item, index) => {
            item.style.animationDelay = `${index * 0.1}s`;
        });

        filterBtns.forEach(btn => {
            btn.addEventListener('click', function() {
                // Update active button
                filterBtns.forEach(btn => btn.classList.remove('active'));
                this.classList.add('active');

                // Filter menu items with animation
                const filterValue = this.getAttribute('data-filter');

                menuItems.forEach(item => {
                    item.style.animation = 'none';
                    item.offsetHeight; // Trigger reflow
                    item.style.animation = null;
                    
                    if (filterValue === 'all' || item.getAttribute('data-category') === filterValue) {
                        item.style.display = 'block';
                        item.classList.add('fade-in');
                    } else {
                        item.style.display = 'none';
                        item.classList.remove('fade-in');
                    }
                });
            });
        });
    }

    // Price Sorting
    const sortPriceBtn = document.createElement('button');
    sortPriceBtn.className = 'filter-btn sort-btn';
    sortPriceBtn.innerHTML = '<i class="fas fa-sort"></i> Sort by Price';
    document.querySelector('.filters').appendChild(sortPriceBtn);

    let ascending = true;
    sortPriceBtn.addEventListener('click', function() {
        const menuGrid = document.querySelector('.menu-grid');
        const items = Array.from(menuItems);

        // Sort items by price
        items.sort((a, b) => {
            const priceA = parseFloat(a.querySelector('.price').textContent.replace('$', ''));
            const priceB = parseFloat(b.querySelector('.price').textContent.replace('$', ''));
            return ascending ? priceA - priceB : priceB - priceA;
        });

        // Update sort button text
        this.innerHTML = ascending ? 
            '<i class="fas fa-sort-up"></i> Price: High to Low' : 
            '<i class="fas fa-sort-down"></i> Price: Low to High';
        
        // Toggle sort direction
        ascending = !ascending;

        // Reorder items in the DOM
        items.forEach(item => menuGrid.appendChild(item));
    });

    // Add hover effects
    menuItems.forEach(item => {
        item.addEventListener('mouseenter', function() {
            const overlay = this.querySelector('.item-overlay');
            overlay.style.transform = 'translateY(0)';
        });

        item.addEventListener('mouseleave', function() {
            const overlay = this.querySelector('.item-overlay');
            overlay.style.transform = 'translateY(100%)';
        });
    });

    // Special Offers Timer
    function updateSpecialOfferTimer() {
        const now = new Date();
        const endOfDay = new Date();
        endOfDay.setHours(23, 59, 59);
        
        const timeLeft = endOfDay - now;
        const hours = Math.floor(timeLeft / (1000 * 60 * 60));
        const minutes = Math.floor((timeLeft % (1000 * 60 * 60)) / (1000 * 60));
        
        const timerElement = document.querySelector('.special-price .per-person');
        if (timerElement) {
            timerElement.textContent = `Offer ends in: ${hours}h ${minutes}m`;
        }
    }

    // Update timer every minute
    updateSpecialOfferTimer();
    setInterval(updateSpecialOfferTimer, 60000);

    // Add to Cart functionality
    function setupAddToCartButtons() {
        const menuItems = document.querySelectorAll('.menu-item');
        
        menuItems.forEach(item => {
            const overlay = item.querySelector('.item-overlay');
            const addButton = document.createElement('button');
            addButton.className = 'add-to-cart-btn';
            addButton.innerHTML = '<i class="fas fa-plus"></i> Add to Cart';
            
            addButton.addEventListener('click', function(e) {
                e.preventDefault();
                const itemName = item.querySelector('h3').textContent;
                const itemPrice = item.querySelector('.price').textContent;
                
                // Animation feedback
                this.innerHTML = '<i class="fas fa-check"></i> Added!';
                this.classList.add('added');
                
                setTimeout(() => {
                    this.innerHTML = '<i class="fas fa-plus"></i> Add to Cart';
                    this.classList.remove('added');
                }, 2000);

                // Log the action
                console.log(`Added to cart: ${itemName} - ${itemPrice}`);
                console.log(`Time: ${currentTime}`);
                console.log(`User: ${currentUser}`);
            });
            
            overlay.appendChild(addButton);
        });
    }

    setupAddToCartButtons();

    // Add CSS animation classes
    document.querySelectorAll('.menu-item').forEach((item, index) => {
        item.style.opacity = '0';
        item.style.animation = `fadeIn 0.5s ease-out ${index * 0.1}s forwards`;
    });
});

// Add these CSS animations to your stylesheet
const style = document.createElement('style');
style.textContent = `
    @keyframes fadeIn {
        from {
            opacity: 0;
            transform: translateY(20px);
        }
        to {
            opacity: 1;
            transform: translateY(0);
        }
    }

    .fade-in {
        animation: fadeIn 0.5s ease-out forwards;
    }

    .add-to-cart-btn {
        margin-top: 10px;
        padding: 8px 16px;
        background-color: var(--secondary-color);
        border: none;
        border-radius: 4px;
        color: var(--dark-color);
        cursor: pointer;
        transition: all 0.3s ease;
    }

    .add-to-cart-btn.added {
        background-color: var(--primary-color);
        color: white;
    }
`;
document.head.appendChild(style);