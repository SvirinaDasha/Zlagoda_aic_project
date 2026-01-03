<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
    <title>${product == null ? 'Add New Product' : 'Edit Product'} - ZLAGODA Supermarket</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<header>
    <h1>${product == null ? 'Add New Product' : 'Edit Product'}</h1>
    <jsp:include page="navigation.jsp"/>
</header>
<main class="container">
    <form action="products?action=${product == null ? 'insert' : 'update'}" method="post" >
        <input type="hidden" name="id" value="${product != null ? product.id : ''}">
        <label for="categoryNumber">Category:</label>
        <select id="categoryNumber" name="categoryNumber" required>
            <c:forEach var="category" items="${listCategory}">
                <option value="${category.categoryNumber}"
                    ${product != null && category.categoryNumber == product.categoryNumber ? 'selected' : ''}>
                        ${category.categoryName}
                </option>
            </c:forEach>
        </select>
        <br>
        <label for="productName">Product Name:</label>
        <input type="text" id="productName" name="productName" value="${product != null ? product.productName : ''}" required>
        <br>
        <label for="characteristics">Characteristics:</label>
        <input type="text" id="characteristics" name="characteristics" value="${product != null ? product.characteristics : ''}" required>
        <br>
        <input type="submit" value="${product == null ? 'Add Product' : 'Update Product'}">
    </form>
</main>
<footer>
    <p>&copy; 2024 ZLAGODA Supermarket Management System</p>
</footer>
</body>
</html>
