<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<%
  if (session == null || session.getAttribute("user") == null) {
    response.sendRedirect("login.jsp");
    return;
  }
%>
<!DOCTYPE html>
<html>
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
          <td>${product.categoryNumber}</td>
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
