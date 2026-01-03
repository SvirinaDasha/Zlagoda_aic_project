<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<%
    if (session == null || session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    else{
        String userRole = ((m_vasyliev.ukma.zlagoda_ais.model.User) session.getAttribute("user")).getRole();
        request.setAttribute("userRole", userRole);
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Manage Store Products - ZLAGODA Supermarket</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<header>
    <h1>Manage Store Products</h1>
    <jsp:include page="navigation.jsp"/>
</header>
<main>
    <c:if test="${userRole == 'Manager'}">
        <a href="store-products?action=new">Add New Store Product</a>
    </c:if>
    <form action="store-products" method="get">
        <input type="text" name="upcSearch" placeholder="Search by UPC">
        <input type="submit" value="Search">
    </form>
    <form action="store-products" method="get">
        <select name="filter">
            <option value="all">All Products</option>
            <option value="promotional">Promotional Products</option>
            <option value="non-promotional">Non-Promotional Products</option>
        </select>
        <select name="sort">
            <option value="products_number">Sort by Quantity</option>
            <option value="product_name">Sort by Name</option>
        </select>
        <input type="submit" value="Filter">
    </form>
    <c:if test="${not empty searchResults}">
        <h2>Search Results</h2>
        <table border="1">
            <tr>
                <th>Price</th>
                <th>Quantity</th>
                <th>Name</th>
                <th>Characteristics</th>
            </tr>
            <c:forEach var="result" items="${searchResults}">
                <tr>
                    <td>${result.sellingPrice}</td>
                    <td>${result.productsNumber}</td>
                    <td>${result.productName}</td>
                    <td>${result.characteristics}</td>
                </tr>
            </c:forEach>
        </table>
    </c:if>
    <h2>Store Products</h2>
    <table border="1">
        <tr>
            <th>UPC</th>
            <th>UPC Promotional</th>
            <th>Product Name</th>
            <th>Price</th>
            <th>Quantity</th>
            <th>Promotional</th>
            <th>Actions</th>
        </tr>
        <c:forEach var="storeProduct" items="${listStoreProduct}">
            <tr>
                <td>${storeProduct.upc}</td>
                <td>${storeProduct.upcProm}</td>
                <td>${storeProduct.productName}</td>
                <td>${storeProduct.sellingPrice}</td>
                <td>${storeProduct.productsNumber}</td>
                <td>${storeProduct.promotionalProduct ? 'Yes' : 'No'}</td>
                <td>
                    <c:if test="${userRole eq 'Manager'}">
                        <a href="store-products?action=edit&upc=${storeProduct.upc}">Edit</a>
                        <a href="store-products?action=delete&upc=${storeProduct.upc}" onclick="return confirm('Are you sure you want to delete this store product?')">Delete</a>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
    </table>
</main>
<footer>
    <p>&copy; 2024 ZLAGODA Supermarket Management System</p>
</footer>
</body>
</html>
