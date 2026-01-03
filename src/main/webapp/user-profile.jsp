<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
  <title>User Profile - ZLAGODA Supermarket</title>
  <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<header>
  <h1>User Profile</h1>
  <jsp:include page="navigation.jsp"/>
</header>
<main>
  <section>
    <h2>Personal Information</h2>
    <table border="1">
      <tr>
        <th>ID</th>
        <td>${employee.idEmployee}</td>
      </tr>
      <tr>
        <th>Surname</th>
        <td>${employee.surname}</td>
      </tr>
      <tr>
        <th>Name</th>
        <td>${employee.name}</td>
      </tr>
      <tr>
        <th>Patronymic</th>
        <td>${employee.patronymic}</td>
      </tr>
      <tr>
        <th>Role</th>
        <td>${employee.role}</td>
      </tr>
      <tr>
        <th>Salary</th>
        <td>${employee.salary}</td>
      </tr>
      <tr>
        <th>Date of Birth</th>
        <td>${employee.dateOfBirth}</td>
      </tr>
      <tr>
        <th>Date of Start</th>
        <td>${employee.dateOfStart}</td>
      </tr>
      <tr>
        <th>Phone Number</th>
        <td>${employee.phoneNumber}</td>
      </tr>
      <tr>
        <th>City</th>
        <td>${employee.city}</td>
      </tr>
      <tr>
        <th>Street</th>
        <td>${employee.street}</td>
      </tr>
      <tr>
        <th>Zip Code</th>
        <td>${employee.zipCode}</td>
      </tr>
    </table>
  </section>
</main>
<footer>
  <p>&copy; 2024 ZLAGODA Supermarket Management System</p>
</footer>
</body>
</html>
