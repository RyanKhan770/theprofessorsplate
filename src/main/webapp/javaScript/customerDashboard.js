// Customer Dashboard JavaScript

document.addEventListener('DOMContentLoaded', function() {
    // Mobile navigation toggle
    const navbarToggle = document.querySelector('.navbar-toggle');
    const navbarMenu = document.querySelector('.navbar-menu');
    
    if (navbarToggle) {
        navbarToggle.addEventListener('click', function() {
            navbarMenu.classList.toggle('show');
        });
    }
    
    // Handle alert message dismissal
    const alerts = document.querySelectorAll('.alert');
    
    alerts.forEach(alert => {
        // Add close button to alerts
        const closeBtn = document.createElement('span');
        closeBtn.innerHTML = '&times;';
        closeBtn.classList.add('close-btn');
        closeBtn.style.float = 'right';
        closeBtn.style.cursor = 'pointer';
        closeBtn.style.fontSize = '20px';
        
        closeBtn.addEventListener('click', function() {
            alert.style.display = 'none';
        });
        
        alert.prepend(closeBtn);
        
        // Auto-hide alerts after 5 seconds
        setTimeout(() => {
            alert.style.opacity = '0';
            setTimeout(() => {
                alert.style.display = 'none';
            }, 300);
        }, 5000);
    });
    
    // Add CSS for auto-hiding alerts
    const style = document.createElement('style');
    style.textContent = `
        .alert {
            transition: opacity 0.3s ease;
        }
        
        .close-btn {
            margin-left: 10px;
        }
    `;
    document.head.appendChild(style);
    
    // Make tables responsive
    const tables = document.querySelectorAll('table');
    tables.forEach(table => {
        const wrapper = document.createElement('div');
        wrapper.classList.add('table-responsive');
        table.parentNode.insertBefore(wrapper, table);
        wrapper.appendChild(table);
    });
});