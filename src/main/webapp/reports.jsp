<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Reports</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
    <style>
        @media print {
            header, form, .print-button, .non-printable {
                display: none;
            }
            table {
                width: 100%;
                border-collapse: collapse;
            }
            th, td {
                border: 1px solid black;
                padding: 8px;
                text-align: left;
            }
        }
    </style>
    <script>
        function toggleFields() {
            var reportType = document.getElementById('reportType').value;
            document.getElementById('dateFields').style.display = (reportType === 'TSBP' || reportType === 'TSBCAP' || reportType === 'TUSBPAP') ? 'block' : 'none';
            document.getElementById('cashierField').style.display = (reportType === 'TSBCAP') ? 'block' : 'none';
            document.getElementById('productField').style.display = (reportType === 'TUSBPAP') ? 'block' : 'none';
        }

        function setDefaultDates() {
            var today = new Date().toISOString().split('T')[0];
            document.getElementById('startDateInput').value = today;
            document.getElementById('endDateInput').value = today;
        }

        window.onload = function() {
            toggleFields();
            setDefaultDates();
        };
    </script>
</head>
<body>
<header class="non-printable">
    <h1>Reports</h1>
    <nav>
        <ul>
            <li><a href="index.jsp">Home</a></li>
            <li><a href="logout">Logout</a></li>
        </ul>
    </nav>
</header>
<form class="non-printable" action="reports" method="get" onchange="toggleFields()">
    <label for="reportType">Select Report:</label>
    <select id="reportType" name="reportType">
        <option value="">-- Select --</option>
        <option value="employees">Employees</option>
        <option value="customers">Customers</option>
        <option value="categories">Categories</option>
        <option value="products">Products</option>
        <option value="store-products">Store Products</option>
        <option value="checks">Checks</option>
        <option value="TSBCAP">Sales by Cashier and Period</option>
        <option value="TSBP">Sales by Period</option>
        <option value="TUSBPAP">Units Sold by Product and Period</option>
    </select>
    <div id="dateFields" style="display: none;">
        <label for="startDateInput">Start Date:</label>
        <input type="date" id="startDateInput" name="startDate" required>
        <label for="endDateInput">End Date:</label>
        <input type="date" id="endDateInput" name="endDate" required>
    </div>
    <div id="cashierField" style="display: none;">
        <label for="cashierSelect">Select Cashier:</label>
        <select id="cashierSelect" name="cashierId">
            <c:forEach var="cashier" items="${cashiers}">
                <option value="${cashier.idEmployee}">${cashier.surname} ${cashier.name}</option>
            </c:forEach>
        </select>
    </div>
    <div id="productField" style="display: none;">
        <label for="productSelect">Select Product:</label>
        <select id="productSelect" name="productId">
            <c:forEach var="product" items="${products}">
                <option value="${product.id}">${product.productName}</option>
            </c:forEach>
        </select>
    </div>
    <button type="submit">Generate Report</button>
</form>
<c:if test="${not empty data}">
    <div class="non-printable">
        <button onclick="window.print()">Print Report</button>
    </div>
    <div>
        <p><strong>User:</strong> ${sessionScope.userFullName}</p>
    </div>
    <table border="1">
        <thead>
        <tr>
            <c:forEach var="column" items="${columns}">
                <th>${column}</th>
            </c:forEach>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="row" items="${data}">
            <tr>
                <c:forEach var="column" items="${columns}">
                    <td>${row[column]}</td>
                </c:forEach>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</c:if>
</body>
</html>
