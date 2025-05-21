// Admin Dashboard JavaScript

// Function to handle alert message dismissal
document.addEventListener('DOMContentLoaded', function() {
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
    
    // Handle sidebar toggle for mobile
    const sidebarToggle = document.createElement('button');
    sidebarToggle.classList.add('sidebar-toggle');
    sidebarToggle.innerHTML = '<i class="fas fa-bars"></i>';
    sidebarToggle.style.position = 'fixed';
    sidebarToggle.style.top = '20px';
    sidebarToggle.style.left = '20px';
    sidebarToggle.style.zIndex = '1000';
    sidebarToggle.style.display = 'none';
    sidebarToggle.style.background = 'var(--primary-color)';
    sidebarToggle.style.color = 'white';
    sidebarToggle.style.border = 'none';
    sidebarToggle.style.borderRadius = '5px';
    sidebarToggle.style.padding = '10px';
    sidebarToggle.style.cursor = 'pointer';
    
    document.body.appendChild(sidebarToggle);
    
    sidebarToggle.addEventListener('click', function() {
        const sidebar = document.querySelector('.sidebar');
        sidebar.classList.toggle('show');
    });
    
    // Show toggle button on mobile
    function handleResize() {
        if (window.innerWidth <= 768) {
            sidebarToggle.style.display = 'block';
        } else {
            sidebarToggle.style.display = 'none';
            document.querySelector('.sidebar').classList.remove('show');
        }
    }
    
    window.addEventListener('resize', handleResize);
    handleResize();
    
    // Add CSS for mobile sidebar
    const style = document.createElement('style');
    style.textContent = `
        @media (max-width: 768px) {
            .sidebar {
                transform: translateX(-100%);
                transition: transform 0.3s ease;
                z-index: 999;
            }
            
            .sidebar.show {
                transform: translateX(0);
            }
            
            .main-content {
                margin-left: 0;
                padding-top: 60px;
            }
        }
    `;
    document.head.appendChild(style);
});

// Function to confirm deletion of a user
function confirmDelete(userId, userName) {
    if (confirm(`Are you sure you want to delete user ${userName}? This action cannot be undone.`)) {
        document.getElementById('deleteUserId').value = userId;
        document.getElementById('deleteUserForm').submit();
    }
}

// Function to update user role
function updateRole(userId) {
    const roleSelect = document.getElementById(`role-${userId}`);
    const newRole = roleSelect.value;
    
    if (confirm(`Are you sure you want to change the role to ${newRole}?`)) {
        document.getElementById('updateUserId').value = userId;
        document.getElementById('updateUserRole').value = newRole;
        document.getElementById('updateRoleForm').submit();
    }
}