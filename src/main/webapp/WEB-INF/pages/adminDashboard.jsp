<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - The Professor's Plate</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/adminDashboard.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <div class="admin-container">
        <aside class="sidebar">
            <div class="sidebar-header">
                <h2>Admin Panel</h2>
                <p>Welcome, ${adminName}</p>
            </div>
            
            <nav class="sidebar-nav">
                <ul>
                    <li class="active">
                        <a href="#dashboard"><i class="fas fa-home"></i> Dashboard</a>
                    </li>
                    <li>
                        <a href="#orders"><i class="fas fa-shopping-cart"></i> Orders</a>
                    </li>
                    <li>
                        <a href="#menu"><i class="fas fa-utensils"></i> Menu Items</a>
                    </li>
                    <li>
                        <a href="#users"><i class="fas fa-users"></i> Users</a>
                    </li>
                    <li>
                        <a href="/review"><i class="fas fa-star"></i> Reviews</a>
                    </li>
                    <li>
                        <a href="/profile"><i class="fas fa-cog"></i> Settings</a>
                    </li>
                    <li>
                        <a href="/logout" class="logout"><i class="fas fa-sign-out-alt"></i> Logout</a>
                    </li>
                </ul>
            </nav>
        </aside>

        <main class="main-content">
            <header class="content-header">
                <h1>Dashboard Overview</h1>
                <div class="header-actions">
                    <button class="notification-btn">
                        <i class="fas fa-bell"></i>
                        <span class="notification-badge">3</span>
                    </button>
                    <button class="profile-btn">
                        <i class="fas fa-user-circle"></i>
                    </button>
                </div>
            </header>

            <div class="dashboard-grid">
                <div class="stats-card">
                    <div class="stats-icon orders">
                        <i class="fas fa-shopping-bag"></i>
                    </div>
                    <div class="stats-info">
                        <h3>Total Orders</h3>
                        <p>145</p>
                        <span class="trend positive">+12.5%</span>
                    </div>
                </div>

                <div class="stats-card">
                    <div class="stats-icon revenue">
                        <i class="fas fa-dollar-sign"></i>
                    </div>
                    <div class="stats-info">
                        <h3>Revenue</h3>
                        <p>$15,480</p>
                        <span class="trend positive">+8.2%</span>
                    </div>
                </div>

                <div class="stats-card">
                    <div class="stats-icon customers">
                        <i class="fas fa-users"></i>
                    </div>
                    <div class="stats-info">
                        <h3>Customers</h3>
                        <p>850</p>
                        <span class="trend positive">+5.3%</span>
                    </div>
                </div>

                <div class="stats-card">
                    <div class="stats-icon reviews">
                        <i class="fas fa-star"></i>
                    </div>
                    <div class="stats-info">
                        <h3>Reviews</h3>
                        <p>4.8</p>
                        <span class="trend positive">+0.2</span>
                    </div>
                </div>
            </div>

            <div class="dashboard-sections">
                <section class="recent-orders">
                    <h2>Recent Orders</h2>
                    <div class="table-responsive">
                        <table>
                            <thead>
                                <tr>
                                    <th>Order ID</th>
                                    <th>Customer</th>
                                    <th>Items</th>
                                    <th>Total</th>
                                    <th>Status</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>#12345</td>
                                    <td>John Doe</td>
                                    <td>2 items</td>
                                    <td>$45.00</td>
                                    <td><span class="status pending">Pending</span></td>
                                    <td>
                                        <button class="action-btn">View</button>
                                    </td>
                                </tr>
                                <!-- Add more rows as needed -->
                            </tbody>
                        </table>
                    </div>
                </section>

                <section class="popular-items">
                    <h2>Popular Items</h2>
                    <div class="items-grid">
                        <div class="item-card">
                            <div class="item-image"></div>
                            <div class="item-info">
                                <h3>Classic Burger</h3>
                                <p>Orders: 89</p>
                                <span class="rating">4.8 ★</span>
                            </div>
                        </div>
                        <!-- Add more item cards as needed -->
                    </div>
                </section>
            </div>
        </main>
    </div>
</body>
</html>