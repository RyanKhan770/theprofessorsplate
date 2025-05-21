<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Profile Settings - The Professor's Plate</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/profile.css">
    <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;700&family=Playfair+Display:wght@400;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/cropperjs/1.5.12/cropper.min.css">
</head>
<body>
    <jsp:include page="header.jsp" />
    
    <!-- Profile Hero Section -->
    <section class="profile-hero">
        <div class="profile-hero-content">
            <div class="welcome-text fade-in">
                <h1>Profile Settings</h1>
                <p>Update your personal information and account settings</p>
            </div>
            <div class="current-date fade-in">
                <p><i class="far fa-calendar-alt"></i> ${currentDateTime}</p>
            </div>
        </div>
    </section>

    <div class="profile-container">
        <!-- Sidebar -->
        <aside class="profile-sidebar">
            <div class="user-profile">
                <div class="user-avatar">
                    <c:choose>
                        <c:when test="${not empty user.userImage}">
                            <img src="${pageContext.request.contextPath}/${user.userImage}" alt="Profile Picture" id="sidebarProfileImage">
                        </c:when>
                        <c:otherwise>
                            <img src="${pageContext.request.contextPath}/resources/images/default-user.png" alt="Default Profile" id="sidebarProfileImage">
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="user-info">
                    <h3>${user.userName}</h3>
                    <p>${user.userEmail}</p>
                </div>
            </div>

            <nav class="sidebar-menu">
                <ul>
                    <li>
                        <a href="${pageContext.request.contextPath}/customerDashboard"><i class="fas fa-tachometer-alt"></i> Dashboard</a>
                    </li>
                    <li class="active">
                        <a href="${pageContext.request.contextPath}/profile"><i class="fas fa-user-circle"></i> My Profile</a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/orderHistory"><i class="fas fa-history"></i> Order History</a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/favorites"><i class="fas fa-heart"></i> Favorites</a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/settings"><i class="fas fa-cog"></i> Settings</a>
                    </li>
                </ul>
            </nav>
        </aside>

        <!-- Main Content -->
        <main class="profile-main">
            <!-- Display messages if any -->
            <c:if test="${not empty sessionScope.successMessage}">
                <div class="alert success">
                    <p>${sessionScope.successMessage}</p>
                    <span class="close-btn">&times;</span>
                </div>
                <c:remove var="successMessage" scope="session" />
            </c:if>
            
            <c:if test="${not empty sessionScope.errorMessage}">
                <div class="alert error">
                    <p>${sessionScope.errorMessage}</p>
                    <span class="close-btn">&times;</span>
                </div>
                <c:remove var="errorMessage" scope="session" />
            </c:if>

            <div class="profile-sections">
                <!-- Profile Picture Section -->
                <section class="section-card">
                    <div class="section-header">
                        <h2 class="section-title">Profile Picture</h2>
                    </div>
                    <div class="image-upload-container">
                        <div class="current-image">
                            <c:choose>
                                <c:when test="${not empty user.userImage}">
                                    <img src="${pageContext.request.contextPath}/${user.userImage}" 
                                         alt="Profile Picture" id="currentProfileImage">
                                </c:when>
                                <c:otherwise>
                                    <img src="${pageContext.request.contextPath}/resources/images/default-user.png" 
                                         alt="Default Profile Picture" id="currentProfileImage">
                                </c:otherwise>
                            </c:choose>
                        </div>
                        
                        <!-- Hidden form for image upload -->
                        <form action="${pageContext.request.contextPath}/updateProfileImage" method="POST" enctype="multipart/form-data" id="profileImageForm">
                            <div class="upload-controls">
                                <input type="file" name="profileImage" id="profileImageInput" accept="image/*" style="display: none;">
                                <div class="button-group">
                                    <button type="button" id="changePhotoBtn" class="cta-button">Change Photo</button>
                                    
                                    <c:if test="${not empty user.userImage}">
                                        <button type="button" id="removePhotoBtn" class="action-btn danger">
                                            <i class="fas fa-trash-alt"></i> Remove
                                        </button>
                                    </c:if>
                                </div>
                                <p class="helper-text">Maximum file size: 5MB. Recommended dimensions: 300x300px.</p>
                            </div>
                        </form>
                        
                        <!-- Separate form for removing image -->
                        <form action="${pageContext.request.contextPath}/removeProfileImage" method="POST" id="removePhotoForm" style="display:none;"></form>
                    </div>
                </section>

                <!-- Account Information Section -->
                <section class="section-card">
                    <div class="section-header">
                        <h2 class="section-title">Account Information</h2>
                    </div>
                    <form action="${pageContext.request.contextPath}/updateProfile" method="POST" class="profile-form" id="profileForm">
                        <div class="form-row">
                            <div class="form-group">
                                <label for="username">Username</label>
                                <input type="text" id="username" name="username" value="${user.userName}" required>
                            </div>

                            <div class="form-group">
                                <label for="email">Email Address</label>
                                <input type="email" id="email" name="email" value="${user.userEmail}" readonly>
                                <small>Email cannot be changed</small>
                            </div>
                        </div>
                        
                        <div class="form-group">
                            <label for="phoneNumber">Phone Number</label>
                            <input type="text" id="phoneNumber" name="phoneNumber" value="${user.phoneNumber}" 
                                   pattern="[0-9]{10}" title="Please enter a valid 10-digit phone number" required>
                        </div>

                        <div class="form-divider"></div>

                        <div class="section-header password-section">
                            <h3>Change Password</h3>
                            <p>Leave blank if you don't want to change your password</p>
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label for="currentPassword">Current Password</label>
                                <input type="password" id="currentPassword" name="currentPassword">
                            </div>

                            <div class="form-group">
                                <label for="newPassword">New Password</label>
                                <input type="password" id="newPassword" name="newPassword" minlength="8">
                                <small>Must be at least 8 characters</small>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="confirmPassword">Confirm New Password</label>
                            <input type="password" id="confirmPassword" name="confirmPassword">
                        </div>

                        <div class="form-actions">
                            <button type="submit" class="cta-button">Save Changes</button>
                            <button type="reset" class="action-btn">Cancel</button>
                        </div>
                    </form>
                </section>

                <!-- Notification Preferences Section -->
                <section class="section-card">
                    <div class="section-header">
                        <h2 class="section-title">Notification Preferences</h2>
                    </div>
                    <div class="notification-settings">
                        <div class="setting-item">
                            <div class="setting-info">
                                <h3>Email Notifications</h3>
                                <p>Receive updates about your orders and account</p>
                            </div>
                            <label class="switch">
                                <input type="checkbox" name="emailNotifications" id="emailNotifications" checked>
                                <span class="slider"></span>
                            </label>
                        </div>
                        
                        <div class="setting-item">
                            <div class="setting-info">
                                <h3>Promotional Emails</h3>
                                <p>Receive special offers, discounts, and news</p>
                            </div>
                            <label class="switch">
                                <input type="checkbox" name="promoNotifications" id="promoNotifications">
                                <span class="slider"></span>
                            </label>
                        </div>
                        
                        <div class="setting-item">
                            <div class="setting-info">
                                <h3>Order Status Updates</h3>
                                <p>Get notified when your order status changes</p>
                            </div>
                            <label class="switch">
                                <input type="checkbox" name="orderUpdates" id="orderUpdates" checked>
                                <span class="slider"></span>
                            </label>
                        </div>
                    </div>
                </section>
            </div>
        </main>
    </div>
    
    <!-- Image cropper modal -->
    <div id="cropperModal" class="modal">
        <div class="modal-content">
            <span class="close-modal">&times;</span>
            <h3>Crop Profile Picture</h3>
            <div class="cropper-container">
                <img id="cropperImage" src="" alt="Image to crop">
            </div>
            <div class="cropper-controls">
                <button id="rotateLeftBtn" type="button" class="action-btn">
                    <i class="fas fa-undo"></i> Rotate Left
                </button>
                <button id="rotateRightBtn" type="button" class="action-btn">
                    <i class="fas fa-redo"></i> Rotate Right
                </button>
                <button id="cropBtn" type="button" class="cta-button">
                    <i class="fas fa-check"></i> Save
                </button>
                <button id="cancelCropBtn" type="button" class="action-btn">
                    <i class="fas fa-times"></i> Cancel
                </button>
            </div>
        </div>
    </div>

    <jsp:include page="footer.jsp" />
    
    <script src="https://cdnjs.cloudflare.com/ajax/libs/cropperjs/1.5.12/cropper.min.js"></script>
    <script>
        // Profile image handling
        document.addEventListener('DOMContentLoaded', function() {
            const profileImageInput = document.getElementById('profileImageInput');
            const changePhotoBtn = document.getElementById('changePhotoBtn');
            const removePhotoBtn = document.getElementById('removePhotoBtn');
            const removePhotoForm = document.getElementById('removePhotoForm');
            const currentProfileImage = document.getElementById('currentProfileImage');
            const sidebarProfileImage = document.getElementById('sidebarProfileImage');
            
            // Modal elements
            const cropperModal = document.getElementById('cropperModal');
            const cropperImage = document.getElementById('cropperImage');
            const closeModal = document.querySelector('.close-modal');
            const cropBtn = document.getElementById('cropBtn');
            const cancelCropBtn = document.getElementById('cancelCropBtn');
            const rotateLeftBtn = document.getElementById('rotateLeftBtn');
            const rotateRightBtn = document.getElementById('rotateRightBtn');
            
            let cropper;
            
            // Open file dialog when change photo button is clicked
            changePhotoBtn.addEventListener('click', function() {
                profileImageInput.click();
            });
            
            // Confirm before removing photo
            if (removePhotoBtn) {
                removePhotoBtn.addEventListener('click', function() {
                    if (confirm('Are you sure you want to remove your profile picture?')) {
                        removePhotoForm.submit();
                    }
                });
            }
            
            // Handle file selection
            profileImageInput.addEventListener('change', function(event) {
                const file = event.target.files[0];
                
                if (!file) {
                    return;
                }
                
                if (!file.type.match('image.*')) {
                    alert('Please select an image file.');
                    return;
                }
                
                // Check file size (5MB max)
                if (file.size > 5 * 1024 * 1024) {
                    alert('Please select an image smaller than 5MB.');
                    return;
                }
                
                // Show the cropper modal
                const reader = new FileReader();
                reader.onload = function(e) {
                    cropperImage.src = e.target.result;
                    cropperModal.style.display = 'block';
                    
                    // Initialize cropper
                    if (cropper) {
                        cropper.destroy();
                    }
                    
                    setTimeout(() => {
                        cropper = new Cropper(cropperImage, {
                            aspectRatio: 1,
                            viewMode: 1,
                            autoCropArea: 1,
                            responsive: true
                        });
                    }, 100);
                };
                reader.readAsDataURL(file);
            });
            
            // Close modal
            closeModal.addEventListener('click', function() {
                cropperModal.style.display = 'none';
                if (cropper) {
                    cropper.destroy();
                    cropper = null;
                }
            });
            
            // Cancel cropping
            cancelCropBtn.addEventListener('click', function() {
                cropperModal.style.display = 'none';
                if (cropper) {
                    cropper.destroy();
                    cropper = null;
                }
                profileImageInput.value = '';
            });
            
            // Rotate left
            rotateLeftBtn.addEventListener('click', function() {
                if (cropper) {
                    cropper.rotate(-90);
                }
            });
            
            // Rotate right
            rotateRightBtn.addEventListener('click', function() {
                if (cropper) {
                    cropper.rotate(90);
                }
            });
            
            // Apply crop
            cropBtn.addEventListener('click', function() {
                if (!cropper) return;
                
                // Get cropped canvas
                const canvas = cropper.getCroppedCanvas({
                    width: 300,
                    height: 300,
                    imageSmoothingEnabled: true,
                    imageSmoothingQuality: 'high'
                });
                
                if (!canvas) return;
                
                // Convert canvas to blob
                canvas.toBlob(function(blob) {
                    // Create a new File object
                    const file = new File([blob], 'profile-image.png', {
                        type: 'image/png',
                        lastModified: new Date().getTime()
                    });
                    
                    // Create a new FileList-like object
                    const dataTransfer = new DataTransfer();
                    dataTransfer.items.add(file);
                    
                    // Set the new file to the input
                    profileImageInput.files = dataTransfer.files;
                    
                    // Preview the image
                    const imageUrl = canvas.toDataURL();
                    currentProfileImage.src = imageUrl;
                    sidebarProfileImage.src = imageUrl;
                    
                    // Close the modal
                    cropperModal.style.display = 'none';
                    cropper.destroy();
                    cropper = null;
                    
                    // Submit the form
                    document.getElementById('profileImageForm').submit();
                }, 'image/png');
            });
            
            // Handle clicks outside the modal
            window.addEventListener('click', function(event) {
                if (event.target == cropperModal) {
                    cropperModal.style.display = 'none';
                    if (cropper) {
                        cropper.destroy();
                        cropper = null;
                    }
                    profileImageInput.value = '';
                }
            });
            
            // Handle alert message dismissal
            const alerts = document.querySelectorAll('.alert');
            const closeButtons = document.querySelectorAll('.close-btn');
            
            closeButtons.forEach((btn, index) => {
                btn.addEventListener('click', () => {
                    alerts[index].style.display = 'none';
                });
            });
            
            // Auto-hide alerts after 5 seconds
            alerts.forEach(alert => {
                setTimeout(() => {
                    alert.style.opacity = '0';
                    setTimeout(() => {
                        alert.style.display = 'none';
                    }, 500);
                }, 5000);
            });
        });
        
        // Form validation
        document.getElementById('profileForm').addEventListener('submit', function(event) {
            const currentPassword = document.getElementById('currentPassword').value;
            const newPassword = document.getElementById('newPassword').value;
            const confirmPassword = document.getElementById('confirmPassword').value;
            
            // If any password field is filled, all must be filled
            if (currentPassword || newPassword || confirmPassword) {
                if (!currentPassword || !newPassword || !confirmPassword) {
                    event.preventDefault();
                    alert('If changing password, all password fields must be filled.');
                    return;
                }
                
                // Check if passwords match
                if (newPassword !== confirmPassword) {
                    event.preventDefault();
                    alert('New password and confirmation do not match.');
                    return;
                }
                
                // Check minimum password length
                if (newPassword.length < 8) {
                    event.preventDefault();
                    alert('Password must be at least 8 characters long.');
                    return;
                }
            }
        });
    </script>
</body>
</html>