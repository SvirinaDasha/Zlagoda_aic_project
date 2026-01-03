<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<%
    if (session == null || session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    else{
        String userRole = ((m_vasyliev.ukma.zlagoda_ais.model.User) session.getAttribute("user")).getRole();
        request.setAttribute("userRole", userRole);
        if (!userRole.equals("Cashier")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied.");
            return;
        }
    }

%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Add Check</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
    <style>

    </style>
</head>
<body>
<header>
    <h1>Add check</h1>
    <jsp:include page="navigation.jsp"/>
</header>
<main>
    <div class="container">

        <c:if test="${not empty successMessage}">
            <div class="success-message">${successMessage}</div>
        </c:if>

        <c:if test="${not empty errorMessage}">
            <div class="error">${errorMessage}</div>
        </c:if>

        <form action="add-check" method="post">
            <div class="form-group">
                <label for="employeeName">Employee Name:</label>
                <input type="text" id="employeeName" name="employeeName" value="${employeeFullName}" readonly />
            </div>
            <div class="form-group">
                <label for="cardNumber">Customer Card Number:</label>
                <select id="cardNumber" name="cardNumber" onchange="submitFormWithCard()">
                    <option value="">-- Select --</option>
                    <c:forEach var="card" items="${customerCards}">
                        <option value="${card.cardNumber}" <c:if test="${card.cardNumber == selectedCardNumber}">selected</c:if>>${card.cardNumber} - ${card.custName}</option>
                    </c:forEach>
                </select>
            </div>
            <input type="hidden" name="action" value="selectCard" />
        </form>

        <h2>Products</h2>
        <form action="add-check" method="post">
            <input type="hidden" name="cardNumber" value="${selectedCardNumber}" />
            <div class="form-group">
                <label for="productSelect">Select Product:</label>
                <select id="productSelect" name="upc" onchange="updateProductDetails()" required>
                    <option value="">-- Select --</option>
                    <c:forEach var="product" items="${products}">
                        <option value="${product.upc}" data-name="${product.productName}" data-price="${product.sellingPrice}" data-promotional="${product.promotionalProduct}">
                                ${product.upc}, ${product.productName}, ${product.promotionalProduct ? 'Promotional' : 'Regular'}
                        </option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label for="productPrice">Price per Unit:</label>
                <input type="text" id="productPrice" name="productPrice" readonly />
            </div>
            <div class="form-group">
                <label for="quantity">Quantity:</label>
                <input type="number" id="quantity" name="quantity" min="1" required/>
            </div>
            <input type="hidden" name="action" value="addProduct" />
            <button type="submit">Add Product</button>
        </form>

        <h2>Check</h2>
        <table id="checkTable">
            <tr>
                <th>UPC</th>
                <th>Name</th>
                <th>Quantity</th>
                <th>Price</th>
                <th>Total</th>
                <th>Action</th>
            </tr>
            <c:forEach var="sale" items="${sales}">
                <tr>
                    <td>${sale.upc}</td>
                    <td>
                        <c:forEach var="product" items="${products}">
                            <c:if test="${product.upc == sale.upc}">
                                ${product.productName}
                            </c:if>
                        </c:forEach>
                    </td>
                    <td>${sale.productNumber}</td>
                    <td><fmt:formatNumber value="${sale.sellingPrice}" type="number" minFractionDigits="2" maxFractionDigits="2"/></td>
                    <td><fmt:formatNumber value="${sale.sellingPrice * sale.productNumber}" type="number" minFractionDigits="2" maxFractionDigits="2"/></td>
                    <td>
                        <form action="add-check" method="post" style="display:inline;">
                            <input type="hidden" name="upc" value="${sale.upc}" />
                            <input type="hidden" name="action" value="removeProduct" />
                            <input type="hidden" name="cardNumber" value="${selectedCardNumber}" />
                            <button type="submit">Remove</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </table>

        <c:set var="totalAmount" value="0" />
        <c:forEach var="sale" items="${sales}">
            <c:set var="totalAmount" value="${totalAmount + (sale.sellingPrice * sale.productNumber)}" />
        </c:forEach>
        <c:set var="vat" value="${totalAmount * 0.2}" />

        <div class="form-group">
            <label>Total Amount:</label>
            <fmt:formatNumber value="${totalAmount}" type="number" minFractionDigits="2" maxFractionDigits="2"/>
        </div>
        <div class="form-group">
            <label>VAT (20%):</label>
            <fmt:formatNumber value="${vat}" type="number" minFractionDigits="2" maxFractionDigits="2"/>
        </div>
        <div class="form-group">
            <label>Date:</label>
            <c:out value="${today}"/>
        </div>

        <form action="add-check" method="post">
            <input type="hidden" name="action" value="submitCheck" />
            <input type="hidden" name="cardNumber" value="${selectedCardNumber}" />
            <input type="submit" value="Submit Check" />
        </form>
    </div>
</main>
<footer>
    <p>&copy; 2024 ZLAGODA Supermarket Management System</p>
</footer>

<script>
    function submitFormWithCard() {
        document.forms[0].submit();
    }

    function updateProductDetails() {
        const productSelect = document.getElementById('productSelect');
        const selectedOption = productSelect.options[productSelect.selectedIndex];
        const productPrice = selectedOption.getAttribute('data-price');
        document.getElementById('productPrice').value = productPrice;
    }
</script>
</body>
</html>
