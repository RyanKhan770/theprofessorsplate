<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>The Professor's Plate - Registration</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/register.css" />
</head>
<body>
    <div class="container">
        <h1>Create Your Account</h1>
        
        <!-- Display error message if available -->
		<c:if test="${not empty error}">
			<p class="error-message">${error}</p>
		</c:if>

		<!-- Display success message if available -->
		<c:if test="${not empty success}">
			<p class="success-message">${success}</p>
		</c:if>
        
        <form action="${pageContext.request.contextPath}/register" method="post" enctype="multipart/form-data">
            <div class="row">
                <div class="col">
                    <label for="userName">Username:</label>
                    <input type="text" 
                           id="userName" 
                           name="userName" 
                           value="${userName}"
                           required>
                </div>
                <div class="col">
                    <label for="userEmail">Email:</label>
                    <input type="email" 
                           id="userEmail" 
                           name="userEmail" 
                           value="${userEmail}"
                           required>
                </div>
            </div>
            <div class="row">
                <div class="col">
                    <label for="password">Password:</label>
                    <input type="password" 
                           id="password" 
                           name="userPassword" 
                           required>
                </div>
                <div class="col">
                    <label for="retypePassword">Confirm Password:</label>
                    <input type="password" 
                           id="retypePassword" 
                           name="retypePassword" 
                           required>
                </div>
            </div>
            <div class="row">
                <div class="col">
                    <label for="phoneNumber">Phone Number:</label>
                    <input type="tel" 
                           id="phoneNumber" 
                           name="phoneNumber" 
                           pattern="98[0-9]{8}"
                           value="${phoneNumber}"
                           maxlength="10"
                           required>
                </div>
                <div class="col">
                    <label for="userRole">Role:</label>
                    <select id="userRole" name="userRole" required>
                        <option value="customer" ${userRole == 'customer' ? 'selected' : ''}>Customer</option>
                        <option value="admin" ${userRole == 'admin' ? 'selected' : ''}>Admin</option>
                    </select>
                </div>
            </div>
            <div class="row">
                <div class="col">
                    <label for="userImage">Profile Picture:</label>
                    <input type="file" 
                           id="userImage" 
                           name="userImage" 
                           accept="image/*">
                </div>
            </div>
            
            
            <!-- Buttons Row -->
            <div class="row buttons-row">
                <button type="submit" class="submit-button">Register</button>
                <a href="${pageContext.request.contextPath}/login" class="login-button">Already have an account? Login</a>
            </div>
        </form>
    </div>
</body>
</html>