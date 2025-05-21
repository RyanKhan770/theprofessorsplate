<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Menu Items - The Professor's Plate</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/adminDashboard.css">
    <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;700&family=Playfair+Display:wght@400;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
    <style>
        /* Additional styles for product management */
        .product-form-container {
            background-color: var(--color-white);
            border-radius: var(--border-radius);
            padding: 2rem;
            box-shadow: var(--shadow-sm);
            margin-bottom: 2rem;
        }
        
        .form-row {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 1.5rem;
            margin-bottom: 1.5rem;
        }
        
        .form-group {
            display: flex;
            flex-direction: column;
            gap: 0.5rem;
        }
        
        .form-group label {
            font-weight: 500;
        }
        
        .form-group input,
        .form-group select,
        .form-group textarea {
            padding: 0.75rem;
            border: 1px solid rgba(0, 0, 0, 0.1);
            border-radius: 8px;
            font-family: 'DM Sans', sans-serif;
        }
        
        .form-group textarea {
            min-height: 120px;
            resize: vertical;
        }
        
        .form-actions {
            display: flex;
            justify-content: flex-end;
            gap: 1rem;
            margin-top: 1.5rem;
        }
        
        .submit-btn {
            background-color: var(--color-primary);
            color: white;
            border: none;
            padding: 0.75rem 1.5rem;
            border-radius: 30px;
            font-weight: 500;
            cursor: pointer;
            transition: background-color 0.3s, transform 0.3s;
        }
        
        .submit-btn:hover {
            background-color: #2a4230;
            transform: translateY(-3px);
        }
        
        .cancel-btn {
            background-color: #e0e0e0;
            color: var(--color-text);
            border: none;
            padding: 0.75rem 1.5rem;
            border-radius: 30px;
            font-weight: 500;
            cursor: pointer;
            transition: background-color 0.3s;
        }
        
        .cancel-btn:hover {
            background-color: #d0d0d0;
        }
        
        .product-preview {
            margin-top: 2rem;
            padding: 1rem;
            border: 1px dashed rgba(0, 0, 0, 0.1);
            border-radius: var(--border-radius);
        }
        
        .product-preview h3 {
            margin-bottom: 1rem;
            color: var(--color-primary);
        }
        
        .preview-image {
            max-width: 100%;
            height: 200px;
            object-fit: cover;
            border-radius: 8px;
            margin-bottom: 1rem;
        }
        
        .filter-container {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 2rem;
            flex-wrap: wrap;
            gap: 1rem;
        }
        
        .filter-group {
            display: flex;
            gap: 0.75rem;
            align-items: center;
        }
        
        .search-box {
            display: flex;
            align-items: center;
        }
        
        .search-input {
            padding: 0.6rem 1rem;
            border: 1px solid rgba(0, 0, 0, 0.1);
            border-radius: 30px 0 0 30px;
            font-family: 'DM Sans', sans-serif;
            min-width: 250px;
        }
        
        .search-btn {
            background-color: var(--color-primary);
            color: white;
            border: none;
            padding: 0.6rem 1rem;
            border-radius: 0 30px 30px 0;
            cursor: pointer;
        }
        
        .filter-select {
            padding: 0.6rem 1rem;
            border: 1px solid rgba(0, 0, 0, 0.1);
            border-radius: 30px;
            font-family: 'DM Sans', sans-serif;
            min-width: 150px;
        }
        
        .select2-container {
            min-width: 180px !important;
        }
        
        .select2-container--default .select2-selection--single {
            height: 41px !important;
            padding: 6px 12px !important;
            border-radius: 30px !important;
            border: 1px solid rgba(0, 0, 0, 0.1) !important;
        }
        
        .select2-container--default .select2-selection--single .select2-selection__arrow {
            height: 39px !important;
        }
        
        .image-preview-container {
            width: 100%;
            margin-bottom: 1rem;
        }
        
        #imagePreview {
            max-width: 100%;
            max-height: 200px;
            border-radius: 8px;
            display: none;
        }
        
        .inventory-badge {
            display: inline-block;
            padding: 0.25rem 0.75rem;
            border-radius: 20px;
            font-size: 0.85rem;
            font-weight: 500;
        }
        
        .in-stock {
            background-color: rgba(76, 175, 80, 0.1);
            color: #4caf50;
        }
        
        .low-stock {
            background-color: rgba(255, 152, 0, 0.1);
            color: #ff9800;
        }
        
        .out-of-stock {
            background-color: rgba(244, 67, 54, 0.1);
            color: #f44336;
        }
        
        @media (max-width: 768px) {
            .form-row {
                grid-template-columns: 1fr;
            }
            
            .filter-container {
                flex-direction: column;
                align-items: stretch;
            }
            
            .filter-group, .search-box {
                width: 100%;
            }
            
            .search-input {
                flex: 1;
            }
        }
    </style>
</head>
<body>
    <jsp:include page="header.jsp" />
    
    <!-- Dashboard Hero -->
    <section class="dashboard-hero">
        <div class="dashboard-hero-content">
            <div class="welcome-text fade-in">
                <h1>Manage Menu Items</h1>
                <p>Add, edit, and manage your restaurant's menu offerings</p>
            </div>
            <div class="admin-info fade-in">
                <div class="admin-avatar">
                    <c:choose>
                        <c:when test="${not empty user.userImage}">
                            <img src="${pageContext.request.contextPath}/${user.userImage}" alt="${user.userName}">
                        </c:when>
                        <c:otherwise>
                            <img src="${pageContext.request.contextPath}/resources/images/default-user.png" alt="${user.userName}">
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="admin-details">
                    <h3>${user.userName}</h3>
                    <p><i class="fas fa-crown"></i> Administrator</p>
                    <p><i class="fas fa-clock"></i> ${currentDateTime}</p>
                </div>
            </div>
        </div>
    </section>

    <div class="dashboard-container">
        <!-- Sidebar -->
        <aside class="dashboard-sidebar">
            <nav class="sidebar-menu">
                <ul>
                    <li><a href="${pageContext.request.contextPath}/adminDashboard"><i class="fas fa-tachometer-alt"></i> Dashboard</a></li>
                    <li class="active"><a href="${pageContext.request.contextPath}/adminProducts"><i class="fas fa-utensils"></i> Manage Products</a></li>
                    <li><a href="${pageContext.request.contextPath}/adminOrders"><i class="fas fa-shopping-bag"></i> Manage Orders</a></li>
                    <li><a href="${pageContext.request.contextPath}/adminUsers"><i class="fas fa-users"></i> Manage Users</a></li>
                    <li><a href="${pageContext.request.contextPath}/adminReviews"><i class="fas fa-star"></i> Manage Reviews</a></li>
                    <li><a href="${pageContext.request.contextPath}/adminReports"><i class="fas fa-chart-bar"></i> Reports</a></li>
                    <li><a href="${pageContext.request.contextPath}/adminSettings"><i class="fas fa-cog"></i> Settings</a></li>
                </ul>
            </nav>
        </aside>
        
        <!-- Main Content -->
        <main class="dashboard-main">
            <!-- Alert Messages -->
            <c:if test="${not empty success}">
                <div class="alert success">
                    <p>${success}</p>
                    <span class="close-btn">&times;</span>
                </div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="alert error">
                    <p>${error}</p>
                    <span class="close-btn">&times;</span>
                </div>
            </c:if>
            
            <!-- Add/Edit Product Form -->
            <c:if test="${param.action == 'add' || param.action == 'edit'}">
                <section class="product-form-container">
                    <h2 class="section-title">
                        <c:choose>
                            <c:when test="${param.action == 'add'}">Add New Menu Item</c:when>
                            <c:otherwise>Edit Menu Item</c:otherwise>
                        </c:choose>
                    </h2>
                    
                    <form action="${pageContext.request.contextPath}/adminProducts" method="POST" enctype="multipart/form-data" id="productForm">
                        <input type="hidden" name="action" value="${param.action}">
                        <c:if test="${param.action == 'edit'}">
                            <input type="hidden" name="foodId" value="${product.foodId}">
                        </c:if>
                        
                        <div class="form-row">
                            <div class="form-group">
                                <label for="foodName">Food Name <span style="color: red;">*</span></label>
                                <input type="text" id="foodName" name="foodName" value="${product.foodName}" required>
                            </div>
                            
                            <div class="form-group">
                                <label for="foodCategory">Category <span style="color: red;">*</span></label>
                                <select id="foodCategory" name="foodCategory" required>
                                    <option value="" disabled selected>Select a category</option>
                                    <c:forEach items="${categories}" var="category">
                                        <option value="${category}" ${product.foodCategory == category ? 'selected' : ''}>${category}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        
                        <div class="form-row">
                            <div class="form-group">
                                <label for="foodPrice">Price (Rs.) <span style="color: red;">*</span></label>
                                <input type="number" id="foodPrice" name="foodPrice" value="${product.foodPrice}" step="0.01" min="0" required>
                            </div>
                            
                            <div class="form-group">
                                <label for="discount">Discount</label>
                                <select id="discount" name="discountId">
                                    <option value="0">No Discount</option>
                                    <c:forEach items="${discounts}" var="discount">
                                        <option value="${discount.discountId}" ${product.discountId == discount.discountId ? 'selected' : ''}>
                                            ${discount.discountName} - 
                                            <c:choose>
                                                <c:when test="${discount.discountPercentage > 0}">
                                                    ${discount.discountPercentage}%
                                                </c:when>
                                                <c:otherwise>
                                                    Rs. ${discount.discountAmount}
                                                </c:otherwise>
                                            </c:choose>
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        
                        <div class="form-row">
                            <div class="form-group">
                                <label for="stockQuantity">Stock Quantity <span style="color: red;">*</span></label>
                                <input type="number" id="stockQuantity" name="stockQuantity" value="${product.stockQuantity}" min="0" required>
                            </div>
                            
                            <div class="form-group">
                                <label for="foodImage">Food Image</label>
                                <input type="file" id="foodImage" name="foodImage" accept="image/*">
                                <div class="image-preview-container">
                                    <img id="imagePreview" 
                                         src="${not empty product.foodImage ? pageContext.request.contextPath.concat('/').concat(product.foodImage) : ''}" 
                                         style="${not empty product.foodImage ? 'display:block;' : ''}">
                                </div>
                            </div>
                        </div>
                        
                        <div class="form-group">
                            <label for="foodDescription">Description <span style="color: red;">*</span></label>
                            <textarea id="foodDescription" name="foodDescription" required>${product.foodDescription}</textarea>
                        </div>
                        
                        <div class="form-actions">
                            <a href="${pageContext.request.contextPath}/adminProducts" class="cancel-btn">Cancel</a>
                            <button type="submit" class="submit-btn">
                                <i class="fas fa-save"></i> 
                                <c:choose>
                                    <c:when test="${param.action == 'add'}">Add Menu Item</c:when>
                                    <c:otherwise>Update Menu Item</c:otherwise>
                                </c:choose>
                            </button>
                        </div>
                        
                        <c:if test="${param.action == 'add'}">
                            <div class="product-preview" id="productPreview" style="display: none;">
                                <h3>Preview</h3>
                                <img id="previewImage" class="preview-image" src="" alt="Product Preview">
                                <h4 id="previewName"></h4>
                                <p id="previewDescription"></p>
                                <p>Price: Rs. <span id="previewPrice"></span></p>
                                <p>Category: <span id="previewCategory"></span></p>
                            </div>
                        </c:if>
                    </form>
                </section>
            </c:if>
            
            <!-- Products List -->
            <c:if test="${param.action != 'add' && param.action != 'edit'}">
                <section class="products-list-section">
                    <div class="section-header">
                        <h2 class="section-title">Menu Items</h2>
                        <a href="${pageContext.request.contextPath}/adminProducts?action=add" class="action-btn">
                            <i class="fas fa-plus"></i> Add New Item
                        </a>
                    </div>
                    
                   	                    <div class="filter-container">
                        <div class="search-box">
                            <input type="text" id="searchInput" class="search-input" placeholder="Search menu items...">
                            <button id="searchBtn" class="search-btn"><i class="fas fa-search"></i></button>
                        </div>
                        
                        <div class="filter-group">
                            <label for="categoryFilter">Category:</label>
                            <select id="categoryFilter" class="filter-select">
                                <option value="">All Categories</option>
                                <c:forEach items="${categories}" var="category">
                                    <option value="${category}">${category}</option>
                                </c:forEach>
                            </select>
                            
                            <label for="stockFilter">Stock:</label>
                            <select id="stockFilter" class="filter-select">
                                <option value="">All</option>
                                <option value="in-stock">In Stock</option>
                                <option value="low-stock">Low Stock</option>
                                <option value="out-of-stock">Out of Stock</option>
                            </select>
                        </div>
                    </div>
                    
                    <div class="table-responsive">
                        <table id="productsTable">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Image</th>
                                    <th>Name</th>
                                    <th>Category</th>
                                    <th>Price</th>
                                    <th>Discounted</th>
                                    <th>Stock</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${products}" var="product">
                                    <tr>
                                        <td>#${product.foodId}</td>
                                        <td>
                                            <img src="${pageContext.request.contextPath}/${product.foodImage}" 
                                                 alt="${product.foodName}" 
                                                 style="width: 50px; height: 50px; object-fit: cover; border-radius: 5px;">
                                        </td>
                                        <td>${product.foodName}</td>
                                        <td>${product.foodCategory}</td>
                                        <td>Rs. <fmt:formatNumber value="${product.foodPrice}" pattern="#,##0.00" /></td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${product.discountedPrice != product.foodPrice}">
                                                    Rs. <fmt:formatNumber value="${product.discountedPrice}" pattern="#,##0.00" />
                                                </c:when>
                                                <c:otherwise>
                                                    -
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <span class="inventory-badge 
                                                <c:choose>
                                                    <c:when test="${product.stockQuantity > 10}">in-stock</c:when>
                                                    <c:when test="${product.stockQuantity > 0}">low-stock</c:when>
                                                    <c:otherwise>out-of-stock</c:otherwise>
                                                </c:choose>">
                                                ${product.stockQuantity}
                                            </span>
                                        </td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/adminProducts?action=edit&foodId=${product.foodId}" class="action-btn edit">
                                                <i class="fas fa-edit"></i>
                                            </a>
                                            <button class="action-btn delete" data-product-id="${product.foodId}" data-product-name="${product.foodName}">
                                                <i class="fas fa-trash"></i>
                                            </button>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty products}">
                                    <tr>
                                        <td colspan="8" class="text-center">No menu items found.</td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </section>
            </c:if>
        </main>
    </div>
    
    <!-- Delete Product Confirmation Modal -->
    <div id="deleteModal" class="modal">
        <div class="modal-content">
            <span class="close-modal">&times;</span>
            <h3>Confirm Deletion</h3>
            <p>Are you sure you want to delete <strong id="productName"></strong>?</p>
            <p>This action cannot be undone.</p>
            <div class="modal-actions">
                <form id="deleteForm" method="POST" action="${pageContext.request.contextPath}/adminProducts">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" id="productIdInput" name="foodId" value="">
                    <button type="button" id="cancelDelete" class="cancel-btn">Cancel</button>
                    <button type="submit" class="action-btn delete">Delete</button>
                </form>
            </div>
        </div>
    </div>
    
    <jsp:include page="footer.jsp" />
    
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Initialize Select2
            $(document).ready(function() {
                $('#foodCategory, #categoryFilter').select2();
            });
            
            // Image preview functionality
            const imageInput = document.getElementById('foodImage');
            const imagePreview = document.getElementById('imagePreview');
            
            if (imageInput) {
                imageInput.addEventListener('change', function() {
                    const file = this.files[0];
                    if (file) {
                        const reader = new FileReader();
                        reader.onload = function(e) {
                            imagePreview.src = e.target.result;
                            imagePreview.style.display = 'block';
                            
                            // Update preview section if it exists
                            const previewImage = document.getElementById('previewImage');
                            if (previewImage) {
                                previewImage.src = e.target.result;
                                document.getElementById('productPreview').style.display = 'block';
                            }
                        };
                        reader.readAsDataURL(file);
                    }
                });
            }
            
            // Live preview for add new product
            const productForm = document.getElementById('productForm');
            if (productForm && document.getElementById('productPreview')) {
                const previewName = document.getElementById('previewName');
                const previewDesc = document.getElementById('previewDescription');
                const previewPrice = document.getElementById('previewPrice');
                const previewCategory = document.getElementById('previewCategory');
                
                document.getElementById('foodName').addEventListener('input', function() {
                    previewName.textContent = this.value;
                });
                
                document.getElementById('foodDescription').addEventListener('input', function() {
                    previewDesc.textContent = this.value;
                });
                
                document.getElementById('foodPrice').addEventListener('input', function() {
                    previewPrice.textContent = this.value;
                });
                
                document.getElementById('foodCategory').addEventListener('change', function() {
                    previewCategory.textContent = this.options[this.selectedIndex].text;
                });
            }
            
            // Delete product confirmation
            const deleteButtons = document.querySelectorAll('.action-btn.delete');
            const deleteModal = document.getElementById('deleteModal');
            const productNameElem = document.getElementById('productName');
            const productIdInput = document.getElementById('productIdInput');
            const closeModalBtn = document.querySelector('.close-modal');
            const cancelDeleteBtn = document.getElementById('cancelDelete');
            
            deleteButtons.forEach(button => {
                button.addEventListener('click', function() {
                    const productId = this.dataset.productId;
                    const productName = this.dataset.productName;
                    
                    productNameElem.textContent = productName;
                    productIdInput.value = productId;
                    deleteModal.style.display = 'block';
                });
            });
            
            if (closeModalBtn) {
                closeModalBtn.addEventListener('click', function() {
                    deleteModal.style.display = 'none';
                });
            }
            
            if (cancelDeleteBtn) {
                cancelDeleteBtn.addEventListener('click', function() {
                    deleteModal.style.display = 'none';
                });
            }
            
            window.addEventListener('click', function(event) {
                if (event.target == deleteModal) {
                    deleteModal.style.display = 'none';
                }
            });
            
            // Filtering and searching
            const searchInput = document.getElementById('searchInput');
            const searchBtn = document.getElementById('searchBtn');
            const categoryFilter = document.getElementById('categoryFilter');
            const stockFilter = document.getElementById('stockFilter');
            
            if (searchBtn) {
                searchBtn.addEventListener('click', filterProducts);
            }
            
            if (searchInput) {
                searchInput.addEventListener('keyup', function(e) {
                    if (e.key === 'Enter') {
                        filterProducts();
                    }
                });
            }
            
            if (categoryFilter) {
                categoryFilter.addEventListener('change', filterProducts);
            }
            
            if (stockFilter) {
                stockFilter.addEventListener('change', filterProducts);
            }
            
            function filterProducts() {
                const searchValue = searchInput.value.toLowerCase();
                const categoryValue = categoryFilter.value.toLowerCase();
                const stockValue = stockFilter.value;
                
                const rows = document.querySelectorAll('#productsTable tbody tr');
                
                rows.forEach(row => {
                    const name = row.cells[2].textContent.toLowerCase();
                    const category = row.cells[3].textContent.toLowerCase();
                    
                    let stockMatch = true;
                    if (stockValue) {
                        const stockBadge = row.cells[6].querySelector('.inventory-badge');
                        stockMatch = stockBadge.classList.contains(stockValue);
                    }
                    
                    const nameMatch = name.includes(searchValue);
                    const categoryMatch = !categoryValue || category === categoryValue;
                    
                    if (nameMatch && categoryMatch && stockMatch) {
                        row.style.display = '';
                    } else {
                        row.style.display = 'none';
                    }
                });
            }
            
            // Handle alert message dismissal
            const alerts = document.querySelectorAll('.alert');
            const closeButtons = document.querySelectorAll('.close-btn');
            
            closeButtons.forEach((btn, index) => {
                if (alerts[index]) {
                    btn.addEventListener('click', () => {
                        alerts[index].style.display = 'none';
                    });
                }
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
    </script>
</body>
</html>
                        