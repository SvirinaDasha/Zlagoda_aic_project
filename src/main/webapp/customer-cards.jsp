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
    <title>Customer Cards - ZLAGODA Supermarket</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<header>
    <h1>Manage Customer Cards</h1>
    <jsp:include page="navigation.jsp"/>
</header>
<main>
    <section>
        <h2>Customer Cards</h2>
        <form action="customers" method="get">
            <div>
                <label for="search">Search by surname:</label>
                <input type="text" id="search" name="search" value="${param.search}">
            </div>
            <div>
                <label for="percent">Filter by percent:</label>
                <input type="text" id="percent" name="percent" value="${param.percent}">
            </div>
            <button type="submit">Apply</button>
        </form>
        <a href="customers?action=new">New Customer Card</a>

        <table border="1">
            <tr>
                <th>Card Number</th>
                <th>Surname</th>
                <th>Name</th>
                <th>Patronymic</th>
                <th>Phone Number</th>
                <th>City</th>
                <th>Street</th>
                <th>Zip Code</th>
                <th>Percent</th>
                <th>Actions</th>
            </tr>
            <c:forEach var="customerCard" items="${listCustomerCards}">
                <tr>
                    <td>${customerCard.cardNumber}</td>
                    <td>${customerCard.custSurname}</td>
                    <td>${customerCard.custName}</td>
                    <td>${customerCard.custPatronymic}</td>
                    <td>${customerCard.phoneNumber}</td>
                    <td>${customerCard.city}</td>
                    <td>${customerCard.street}</td>
                    <td>${customerCard.zipCode}</td>
                    <td>${customerCard.percent}</td>
                    <td>
                        <a href="customers?action=edit&cardNumber=${customerCard.cardNumber}">Edit</a>
                        <c:if test="${userRole eq 'Manager'}">
                            <a href="customers?action=delete&cardNumber=${customerCard.cardNumber}">Delete</a>
                        </c:if>

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
