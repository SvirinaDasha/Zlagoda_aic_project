<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<%
  if (session == null || session.getAttribute("user") == null) {
    response.sendRedirect("login.jsp");
    return;
  }
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Products - ZLAGODA Supermarket</title>
  <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<header>
  <h1>Manage Products</h1>
  <nav>
    <ul>
      <li><a href="index.jsp">Home</a></li>
      <li><a href="employees">Manage Employees</a></li>
      <li><a href="store-products">Manage Store Products</a></li>
      <li><a href="categories">Manage Categories</a></li>
      <li><a href="checks">Manage Checks</a></li>
      <li><a href="customers">Manage Customer Cards</a></li>
      <li><a href="reports">Generate Reports</a></li>
      <li><a href="user-profile">User Profile</a></li>
      <li><a href="logout">Logout</a></li>
    </ul>
  </nav>
</header>
<main>
  <section>
    <h2>Product List</h2>
    <form action="products" method="get">
      <div>
        <label for="search">Search by name:</label>
        <input type="text" id="search" name="search" value="${param.search}">
      </div>
      <div>
        <label for="category">Filter by category:</label>
        <select id="category" name="category">
          <option value="-1">All</option>
          <c:forEach var="category" items="${listCategory}">
            <option value="${category.categoryNumber}" <c:if test="${selectedCategory == category.categoryNumber}">selected</c:if>>${category.categoryName}</option>
          </c:forEach>
        </select>
      </div>
      <button type="submit">Apply</button>
    </form>
    <a href="products?action=new">Add New Product</a>
    <table border="1">
      <tr>
        <th>ID</th>
        <th>Category</th>
        <th>Name</th>
        <th>Characteristics</th>
        <th>Actions</th>
      </tr>
      <c:forEach var="product" items="${listProduct}">
        <tr>
          <td>${product.id}</td>
          <td>${product.categoryName}</td>
          <td>${product.productName}</td>
          <td>${product.characteristics}</td>
          <td>
            <a href="products?action=edit&id=${product.id}">Edit</a>
            <a href="products?action=delete&id=${product.id}">Delete</a>
          </td>
        </tr>
      </c:forEach>
    </table>
  </section>
</main>
<footer>
  <p>&copy; 2024 ZLAGODA Supermarket Management System</p>
</footer>
</body>
</html>
