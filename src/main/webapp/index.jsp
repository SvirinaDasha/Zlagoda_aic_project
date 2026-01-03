<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page import="m_vasyliev.ukma.zlagoda_ais.model.User" %>
<%
  if (session == null || session.getAttribute("user") == null) {
    response.sendRedirect("login.jsp");
    return;
  }
  User user = (User) session.getAttribute("user");
  String userRole = user.getRole();
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
      <% if ("Manager".equals(userRole)) { %>
      <a href="employees">
        <div class="quick-link">
          <h3>Employees</h3>
        </div>
      </a>
      <a href="categories">
        <div class="quick-link">
          <h3>Categories</h3>
        </div>
      </a>
      <a href="reports">
        <div class="quick-link">
          <h3>Reports</h3>
        </div>
      </a>
      <a href="manage-users">
        <div class="quick-link">
          <h3>Manage Users</h3>
        </div>
      </a>
      <% } %>
      <a href="products">
        <div class="quick-link">
          <h3>Products</h3>
        </div>
      </a>
      <a href="store-products">
        <div class="quick-link">
          <h3>Store Products</h3>
        </div>
      </a>
      <a href="customers">
        <div class="quick-link">
          <h3>Customer Cards</h3>
        </div>
      </a>
      <a href="checks">
        <div class="quick-link">
          <h3>Checks</h3>
        </div>
      </a>
      <% if ("Cashier".equals(userRole)) { %>
      <a href="add-check">
        <div class="quick-link">
          <h3>Add Check</h3>
        </div>
      </a>
      <% } %>
    </div>
  </section>
</main>
<footer>
  <p>&copy; 2024 ZLAGODA Supermarket Management System</p>
</footer>
</body>
</html>
