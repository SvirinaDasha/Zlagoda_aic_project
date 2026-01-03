<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
        if (userRole.equals("Cashier")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied.");
            return;
        }
    }

%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${employee == null ? 'Add New Employee' : 'Edit Employee'} - ZLAGODA Supermarket</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<header>
    <h1>${employee == null ? 'Add New Employee' : 'Edit Employee'}</h1>
    <jsp:include page="navigation.jsp"/>
</header>
<main>
    <c:if test="${not empty errorMessage}">
        <div class="error">${errorMessage}</div>
    </c:if>
    <form action="employees?action=${employee == null ? 'insert' : 'update'}" method="post">
        <input type="hidden" name="id" value="${employee != null ? employee.idEmployee : ''}">
        <label for="surname">Surname:</label>
        <input type="text" id="surname" name="surname" value="${employee != null ? employee.surname : ''}" required>
        <br>

        <label for="name">Name:</label>
        <input type="text" id="name" name="name" value="${employee != null ? employee.name : ''}" required>
        <br>

        <label for="patronymic">Patronymic:</label>
        <input type="text" id="patronymic" name="patronymic" value="${employee != null ? employee.patronymic : ''}" required>
        <br>

        <label for="role">Role:</label>
        <select id="role" name="role" required>
            <option value="Cashier" ${employee != null && employee.role == 'Cashier' ? 'selected' : ''}>Cashier</option>
            <option value="Manager" ${employee != null && employee.role == 'Manager' ? 'selected' : ''}>Manager</option>
        </select>
        <br>

        <label for="salary">Salary:</label>
        <input type="text" id="salary" name="salary" value="${employee != null ? employee.salary : ''}" required>
        <br>

        <label for="dateOfBirth">Date of Birth:</label>
        <input type="date" id="dateOfBirth" name="dateOfBirth" value="${employee != null ? employee.dateOfBirth : ''}" required>
        <br>

        <label for="dateOfStart">Date of Start:</label>
        <input type="date" id="dateOfStart" name="dateOfStart" value="${employee != null ? employee.dateOfStart : ''}" required>
        <br>

        <label for="phoneNumber">Phone Number:</label>
        <input type="text" id="phoneNumber" name="phoneNumber" value="${employee != null ? employee.phoneNumber : ''}" required>
        <br>

        <label for="city">City:</label>
        <input type="text" id="city" name="city" value="${employee != null ? employee.city : ''}" required>
        <br>

        <label for="street">Street:</label>
        <input type="text" id="street" name="street" value="${employee != null ? employee.street : ''}" required>
        <br>

        <label for="zipCode">Zip Code:</label>
        <input type="text" id="zipCode" name="zipCode" value="${employee != null ? employee.zipCode : ''}" required>
        <br>

        <input type="submit" value="${employee == null ? 'Add Employee' : 'Update Employee'}">
    </form>
</main>
<footer>
    <p>&copy; 2024 ZLAGODA Supermarket Management System</p>
</footer>
</body>
</html>