<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%
    if (session == null || session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    else {
        String userRole = ((m_vasyliev.ukma.zlagoda_ais.model.User) session.getAttribute("user")).getRole();
        request.setAttribute("userRole", userRole);
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>View Checks</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<header class="non-printable">
    <h1>View Checks</h1>
    <jsp:include page="navigation.jsp"/>
</header>
<main>
    <form action="checks" method="get">
        <input type="hidden" name="action" value="">
        <c:if test="${userRole == 'Manager'}">
            <label for="cashierId">Select Cashier:</label>
            <select id="cashierId" name="cashierId">
                <option value="">-- All Cashiers --</option>
                <c:forEach var="cashier" items="${cashiers}">
                    <option value="${cashier.idEmployee}">${cashier.surname} ${cashier.name}</option>
                </c:forEach>
            </select>
        </c:if>
        <label for="startDate">Start Date:</label>
        <input type="date" id="startDate" name="startDate">
        <label for="endDate">End Date:</label>
        <input type="date" id="endDate" name="endDate">
        <button type="submit">Filter</button>
    </form>
    <form action="checks" method="get">
        <input type="hidden" name="action" value="search">
        <label for="checkNumber">Search by Check Number:</label>
        <input type="text" id="checkNumber" name="checkNumber" required>
        <button type="submit">Search</button>
    </form>
    <h2>Check List</h2>
    <table border="1">
        <thead>
        <tr>
            <th>Check Number</th>
            <th>Employee</th>
            <th>Customer</th>
            <th>Print Date</th>
            <th>Sum Total</th>
            <th>VAT</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="check" items="${checks}">
            <tr>
                <td>${check.checkNumber}</td>
                <td>${check.employeeFullName}</td>
                <td>${check.customerFullName}</td>
                <td>${check.printDate}</td>
                <td>${check.sumTotal}</td>
                <td>${check.vat}</td>
                <td>
                    <a href="checks?action=view&checkNumber=${check.checkNumber}">View Details</a> |
                    <c:if test="${userRole eq 'Manager'}">
                        <a href="checks?action=delete&checkNumber=${check.checkNumber}" onclick="return confirm('Are you sure you want to delete this check?')">Delete</a>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</main>
</body>
</html>
