<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
  <title>Home - ZLAGODA Supermarket</title>
  <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<header>
  <h1>Welcome to ZLAGODA Supermarket Management System</h1>
  <nav>
    <ul>
      <li><a href="user-profile">User Profile</a></li>
      <li><a href="logout">Logout</a></li>
    </ul>
  </nav>
</header>
<main>
  <section>
    <h2>About the System</h2>
    <p>This system helps you manage all aspects of the supermarket, from employees and products to customer cards and sales checks. Use the navigation above to access different management sections.</p>
  </section>
  <section>
    <h2>Quick Links</h2>
    <div class="quick-links">
      <a href="employees.jsp">
        <div class="quick-link">
          <h3>Employees</h3>
          <p>Manage employee information.</p>
        </div>
      </a>
      <a href="products">
        <div class="quick-link">
          <h3>Products</h3>
          <p>Manage product information.</p>
        </div>
      </a>
      <a href="store-products">
        <div class="quick-link">
          <h3>Store Products</h3>
          <p>Manage store products and inventory.</p>
        </div>
      </a>
      <a href="categories">
        <div class="quick-link">
          <h3>Categories</h3>
          <p>Manage product categories.</p>
        </div>
      </a>
      <a href="add-check">
        <div class="quick-link">
          <h3>Checks</h3>
          <p>Manage sales checks.</p>
        </div>
      </a>
      <a href="customers">
        <div class="quick-link">
          <h3>Customer Cards</h3>
          <p>Manage customer cards.</p>
        </div>
      </a>
      <a href="reports">
        <div class="quick-link">
          <h3>Reports</h3>
          <p>Generate various reports.</p>
        </div>
      </a>
      <a href="user-profile.jsp">
        <div class="quick-link">
          <h3>User Profile</h3>
          <p>View your profile information.</p>
        </div>
      </a>
    </div>
  </section>
</main>
<footer>
  <p>&copy; 2024 ZLAGODA Supermarket Management System</p>
</footer>
</body>
</html>
