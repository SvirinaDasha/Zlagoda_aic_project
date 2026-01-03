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
    <title>Search Employees - ZLAGODA Supermarket</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<header>
    <h1>Search Employees</h1>
    <jsp:include page="navigation.jsp"/>
</header>
<main>
    <section>
        <h2>Search Employees by Surname</h2>
        <form action="employees" method="get">
            <input type="hidden" name="action" value="search">
            <label for="surname">Surname:</label>
            <input type="text" id="surname" name="surname" required>
            <button type="submit">Search</button>
        </form>
        <h2>Search Results</h2>
        <table border="1">
            <tr>
                <th>Surname</th>
                <th>Name</th>
                <th>Patronymic</th>
                <th>Phone Number</th>
                <th>City</th>
                <th>Street</th>
                <th>Zip Code</th>
            </tr>
            <c:forEach var="employee" items="${searchResults}">
                <tr>
                    <td>${employee.surname}</td>
                    <td>${employee.name}</td>
                    <td>${employee.patronymic}</td>
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
