<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Our Team - The Professor's Plate</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/team.css" />
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
</head>
<body>
<jsp:include page="header.jsp" />
	
  <!-- Team Banner -->
  <div class="team-banner">
    <h1>OUR TEAM</h1>
  </div>

  <!-- Team Section -->
  <section class="team-container">
    <div class="team-grid">
      <!-- Team Member -->
      <div class="team-member">
        <div class="member-photo">
          <img src="${pageContext.request.contextPath}/resources/usersImage/avatar.jpg" alt="Bivek Dahal" />
        </div>
        <h2 class="member-name">Bivek Dahal</h2>
        <h3 class="member-task-title">Task:</h3>
        <p class="member-task">Database</p>
        <div class="member-contact">
          <p><i class="fas fa-map-marker-alt"></i> Tinkune, Kathmandu</p>
          <p><i class="fas fa-envelope"></i> <a href="mailto:bivek123dahal@gmail.com">bivek123dahal@gmail.com</a></p>
          <div class="social-links">
            <a href="#"><i class="fab fa-facebook-f"></i></a>
            <a href="#"><i class="fab fa-linkedin-in"></i></a>
          </div>
        </div>
      </div>

      <!-- Team Member -->
      <div class="team-member">
        <div class="member-photo">
          <img src="${pageContext.request.contextPath}/resources/usersImage/avatar.jpg" alt="Amin Tamang" />
        </div>
        <h2 class="member-name">Amin Tamang</h2>
        <h3 class="member-task-title">Task:</h3>
        <p class="member-task">Documentation</p>
        <div class="member-contact">
          <p><i class="fas fa-map-marker-alt"></i> Chandragiri Hills, Nepal</p>
          <p><i class="fas fa-envelope"></i> <a href="mailto:amin1000369@gmail.com">amin1000369@gmail.com</a></p>
          <div class="social-links">
            <a href="#"><i class="fab fa-facebook-f"></i></a>
            <a href="#"><i class="fab fa-linkedin-in"></i></a>
          </div>
        </div>
      </div>

      <!-- Team Member -->
      <div class="team-member">
        <div class="member-photo">
          <img src="${pageContext.request.contextPath}/resources/usersImage/avatar.jpg" alt="Nigel Awale" />
        </div>
        <h2 class="member-name">Nigel Awale</h2>
        <h3 class="member-task-title">Task:</h3>
        <p class="member-task">FrontEnd</p>
        <div class="member-contact">
          <p><i class="fas fa-map-marker-alt"></i> Satdobato, Lalitpur</p>
          <p><i class="fas fa-envelope"></i> <a href="mailto:nigelawale123@gmail.com">nigelawale123@gmail.com</a></p>
          <div class="social-links">
            <a href="#"><i class="fab fa-facebook-f"></i></a>
            <a href="#"><i class="fab fa-linkedin-in"></i></a>
          </div>
        </div>
      </div>

      <!-- Team Member -->
      <div class="team-member">
        <div class="member-photo">
          <img src="${pageContext.request.contextPath}/resources/usersImage/avatar.jpg" alt="Ryan Khan" />
        </div>
        <h2 class="member-name">Ryan Khan</h2>
        <h3 class="member-task-title">Task:</h3>
        <p class="member-task">BackEnd</p>
        <div class="member-contact">
          <p><i class="fas fa-map-marker-alt"></i> Patan, Lalitpur</p>
          <p><i class="fas fa-envelope"></i> <a href="mailto:ryankhan770@gmail.com">ryankhan770@gmail.com</a></p>
          <div class="social-links">
            <a href="#"><i class="fab fa-facebook-f"></i></a>
            <a href="#"><i class="fab fa-linkedin-in"></i></a>
          </div>
        </div>
      </div>

      <!-- Team Member -->
      <div class="team-member">
        <div class="member-photo">
          <img src="${pageContext.request.contextPath}/resources/usersImage/avatar.jpg" alt="Sanjog Shrestha" />
        </div>
        <h2 class="member-name">Sanjog Shrestha</h2>
        <h3 class="member-task-title">Task:</h3>
        <p class="member-task">FrontEnd</p>
        <div class="member-contact">
          <p><i class="fas fa-map-marker-alt"></i> Budhanilkantha, Nepal</p>
          <p><i class="fas fa-envelope"></i> <a href="mailto:sanjoksth99@gmail.com">sanjoksth99@gmail.com</a></p>
          <div class="social-links">
            <a href="#"><i class="fab fa-facebook-f"></i></a>
            <a href="#"><i class="fab fa-linkedin-in"></i></a>
          </div>
        </div>
      </div>
    </div>
  </section>
  <jsp:include page="footer.jsp" />
</body>
</html>