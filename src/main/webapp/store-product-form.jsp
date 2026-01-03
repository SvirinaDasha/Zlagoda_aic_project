<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<%
    String userRole = ((m_vasyliev.ukma.zlagoda_ais.model.User) session.getAttribute("user")).getRole();
    if (session == null || session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    if (!userRole.equals("Manager")) {
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied.");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${storeProduct == null ? 'Add New Store Product' : 'Edit Store Product'} - ZLAGODA Supermarket</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
    <style>
        .error {
            color: #D8000C;
            background-color: #FFBABA;
            border: 1px solid #D8000C;
            padding: 10px;
            margin: 10px 0;
            border-radius: 5px;
            font-weight: bold;
        }
    </style>
    <script>
        function updateSellingPrice() {
            const promotionalCheckbox = document.getElementById('promotionalProduct');
            const sellingPriceInput = document.getElementById('sellingPrice');
            const originalPrice = parseFloat(document.getElementById('originalPrice').value);

            if (promotionalCheckbox.checked) {
                sellingPriceInput.value = 0;
                sellingPriceInput.parentElement.style.display = 'none';
            } else {
                sellingPriceInput.parentElement.style.display = 'block';
                sellingPriceInput.value = originalPrice.toFixed(2);
            }
        }

        window.onload = function() {
            updateSellingPrice();
        }
    </script>
</head>
<body>
<header>
    <h1>${storeProduct == null ? 'Add New Store Product' : 'Edit Store Product'}</h1>
    <jsp:include page="navigation.jsp"/>
</header>
<main>
    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>
    <form action="store-products?action=${storeProduct == null ? 'insert' : 'update'}" method="post">
        <label for="upc">UPC:</label>
        <input type="text" id="upc" name="upc" value="${storeProduct != null ? storeProduct.upc : ''}" required>
        <br>
        <label for="productId">Product:</label>
        <select id="productId" name="productId" required>
            <c:forEach var="product" items="${listProduct}">
                <option value="${product.id}" ${storeProduct != null && product.id == storeProduct.productId ? 'selected' : ''}>${product.productName}</option>
            </c:forEach>
        </select>
        <br>
        <div id="price-container">
            <label for="sellingPrice">Selling Price:</label>
            <input type="number" id="sellingPrice" name="sellingPrice" step="0.01" value="${storeProduct != null ? storeProduct.sellingPrice : ''}">
            <input type="hidden" id="originalPrice" name="originalPrice" value="${storeProduct != null ? storeProduct.sellingPrice / (storeProduct.promotionalProduct ? 0.8 : 1) : ''}">
        </div>
        <br>
        <label for="productsNumber">Products Number:</label>
        <input type="number" id="productsNumber" name="productsNumber" value="${storeProduct != null ? storeProduct.productsNumber : ''}" required>
        <br>
        <label for="promotionalProduct">Promotional Product:</label>
        <input type="checkbox" id="promotionalProduct" name="promotionalProduct" ${storeProduct != null && storeProduct.promotionalProduct ? 'checked' : ''} onchange="updateSellingPrice()">
        <br>
        <input type="submit" value="${storeProduct == null ? 'Add Product' : 'Update Product'}">
    </form>
</main>
<footer>
    <p>&copy; 2024 ZLAGODA Supermarket Management System</p>
</footer>
</body>
</html>
