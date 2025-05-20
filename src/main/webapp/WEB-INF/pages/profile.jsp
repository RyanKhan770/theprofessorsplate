<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Profile Settings - The Professor's Plate</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/profile.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <jsp:include page="header.jsp" />
    
    <div class="dashboard-container">
        <aside class="dashboard-sidebar">
            <div class="user-profile">
                <div class="profile-image">
                    <i class="fas fa-user-circle"></i>
                </div>
                <div class="user-info">
                    <h3>${user.name}</h3>
                    <p>${user.email}</p>
                </div>
            </div>

            <nav class="dashboard-nav">
                <ul>
                    <li>
                        <a href="dashboard"><i class="fas fa-home"></i> Overview</a>
                    </li>
                    <li>
                        <a href="orderHistory"><i class="fas fa-history"></i> Order History</a>
                    </li>
                    <li>
                        <a href="favorites"><i class="fas fa-heart"></i> Favorites</a>
                    </li>
                    <li class="active">
                        <a href="profile"><i class="fas fa-user"></i> Profile Settings</a>
                    </li>
                    <li>
                        <a href="addresses"><i class="fas fa-map-marker-alt"></i> Addresses</a>
                    </li>
                    <li>
                        <a href="reviews"><i class="fas fa-star"></i> My Reviews</a>
                    </li>
                </ul>
            </nav>
        </aside>

        <main class="dashboard-content">
            <section class="welcome-section">
                <h1>Profile Settings</h1>
                <p>Update your personal information and account settings</p>
            </section>

            <section class="profile-sections">
                <div class="profile-image-section">
                    <h2>Profile Picture</h2>
                    <div class="image-upload-container">
                        <div class="current-image">
                            <img src="${user.profileImage != null ? user.profileImage : '../resources/images/default-avatar.png'}" 
                                 alt="Profile Picture" id="currentProfileImage">
                        </div>
                        <form action="updateProfileImage" method="POST" enctype="multipart/form-data">
                            <div class="upload-controls">
                                <input type="file" name="profileImage" id="profileImageInput" accept="image/*">
                                <button type="submit" class="upload-btn">Update Picture</button>
                            </div>
                        </form>
                    </div>
                </div>

                <div class="profile-details-section">
                    <h2>Account Information</h2>
                    <form action="updateProfile" method="POST" class="profile-form">
                        <div class="form-group">
                            <label for="username">Username</label>
                            <input type="text" id="username" name="username" value="${user.username}" required>
                        </div>

                        <div class="form-group">
                            <label for="email">Email Address</label>
                            <input type="email" id="email" name="email" value="${user.email}" readonly>
                        </div>

                        <div class="form-divider"></div>

                        <div class="form-group">
                            <label for="currentPassword">Current Password</label>
                            <input type="password" id="currentPassword" name="currentPassword">
                        </div>

                        <div class="form-group">
                            <label for="newPassword">New Password</label>
                            <input type="password" id="newPassword" name="newPassword">
                        </div>

                        <div class="form-group">
                            <label for="confirmPassword">Confirm New Password</label>
                            <input type="password" id="confirmPassword" name="confirmPassword">
                        </div>

                        <div class="form-actions">
                            <button type="submit" class="save-btn">Save Changes</button>
                            <button type="reset" class="cancel-btn">Cancel</button>
                        </div>
                    </form>
                </div>

                <div class="account-settings-section">
                    <h2>Account Settings</h2>
                    <div class="settings-list">
                        <div class="setting-item">
                            <div class="setting-info">
                                <h3>Two-Factor Authentication</h3>
                                <p>Add an extra layer of security to your account</p>
                            </div>
                            <label class="switch">
                                <input type="checkbox" name="2fa" ${user.twoFactorEnabled ? 'checked' : ''}>
                                <span class="slider"></span>
                            </label>
                        </div>

                        <div class="setting-item">
                            <div class="setting-info">
                                <h3>Email Notifications</h3>
                                <p>Receive updates about your orders and account</p>
                            </div>
                            <label class="switch">
                                <input type="checkbox" name="emailNotifications" ${user.emailNotificationsEnabled ? 'checked' : ''}>
                                <span class="slider"></span>
                            </label>
                        </div>
                    </div>
                </div>
            </section>
        </main>
    </div>

    <jsp:include page="footer.jsp" />
</body>
</html>