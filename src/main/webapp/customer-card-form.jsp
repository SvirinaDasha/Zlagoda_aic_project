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
    <title>${customerCard == null ? 'Add New Customer Card' : 'Edit Customer Card'} - ZLAGODA Supermarket</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<header>
    <h1>${customerCard == null ? 'Add New Customer Card' : 'Edit Customer Card'}</h1>
    <jsp:include page="navigation.jsp"/>
</header>
<main>
    <c:if test="${not empty errorMessage}">
        <div class="error">${errorMessage}</div>
    </c:if>
<form action="customers" method="post">
    <input type="hidden" name="action" value="${customerCard == null ? 'insert' : 'update'}"/>
    <input type="hidden" name="cardNumber" value="${customerCard.cardNumber}"/>
    <label for="custSurname">Surname:</label>
    <input type="text" id="custSurname" name="custSurname" value="${customerCard.custSurname}" required/><br/>
    <label for="custName">Name:</label>
    <input type="text" id="custName" name="custName" value="${customerCard.custName}" required/><br/>
    <label for="custPatronymic">Patronymic:</label>
    <input type="text" id="custPatronymic" name="custPatronymic" value="${customerCard.custPatronymic}"/><br/>
    <label for="phoneNumber">Phone Number:</label>
    <input type="text" id="phoneNumber" name="phoneNumber" value="${customerCard.phoneNumber}" required/><br/>
    <label for="city">City:</label>
    <input type="text" id="city" name="city" value="${customerCard.city}" required/><br/>
    <label for="street">Street:</label>
    <input type="text" id="street" name="street" value="${customerCard.street}" required/><br/>
    <label for="zipCode">Zip Code:</label>
    <input type="text" id="zipCode" name="zipCode" value="${customerCard.zipCode}" required/><br/>
    <label for="percent">Percent:</label>
    <input type="number" step="0.01" id="percent" name="percent" value="${customerCard.percent}" required/><br/>
    <input type="submit" value="Submit"/>
</form>
</main>
<footer>
    <p>&copy; 2024 ZLAGODA Supermarket Management System</p>
</footer>
</body>
</html>
