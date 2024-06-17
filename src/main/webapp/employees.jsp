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
    <title>Employees - ZLAGODA Supermarket</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<header>
    <h1>Manage Employees</h1>
    <nav>
        <ul>
            <li><a href="index.jsp">Home</a></li>
            <li><a href="employees.jsp">Manage Employees</a></li>
            <li><a href="products">Manage Products</a></li>
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
        <h2>Employee List</h2>
        <table border="1">
            <tr>
                <th>ID</th>
                <th>Surname</th>
                <th>Name</th>
                <th>Patronymic</th>
                <th>Role</th>
                <th>Salary</th>
                <th>Date of Birth</th>
                <th>Date of Start</th>
                <th>Phone Number</th>
                <th>City</th>
                <th>Street</th>
                <th>Zip Code</th>
            </tr>
            <c:forEach var="employee" items="${employees}">
                <tr>
                    <td>${employee.id}</td>
                    <td>${employee.surname}</td>
                    <td>${employee.name}</td>
                    <td>${employee.patronymic}</td>
                    <td>${employee.role}</td>
                    <td>${employee.salary}</td>
                    <td>${employee.dateOfBirth}</td>
                    <td>${employee.dateOfStart}</td>
                    <td>${employee.phoneNumber}</td>
                    <td>${employee.city}</td>
                    <td>${employee.street}</td>
                    <td>${employee.zipCode}</td>
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
