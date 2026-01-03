<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
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
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Check Details</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<header>
    <h1>Check Information</h1>
    <jsp:include page="navigation.jsp"/>
</header>
<main>
    <div>
        <p><strong>Check Number:</strong> ${check.checkNumber}</p>
        <p><strong>Employee:</strong> ${check.employeeFullName}</p>
        <p><strong>Customer:</strong> ${check.customerFullName}</p>
        <p><strong>Print Date:</strong> ${check.printDate}</p>
        <p><strong>Sum Total:</strong> ${check.sumTotal}</p>
        <p><strong>VAT:</strong> ${check.vat}</p>
    </div>
    <h2>Sales Information</h2>
    <table border="1">
        <thead>
        <tr>
            <th>UPC</th>
            <th>Product Name</th>
            <th>Product Number</th>
            <th>Selling Price</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="sale" items="${sales}">
            <tr>
                <td>${sale.productId}</td>
                <td>${sale.productName}</td>
                <td>${sale.totalUnitsSold}</td>
                <td>${sale.totalSales}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</main>
</body>
</html>

