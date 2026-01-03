<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
    <title>User Form</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<header>
    <h1>User Form</h1>
    <jsp:include page="navigation.jsp"/>
</header>
<main>
    <form action="manage-users" method="post">
        <input type="hidden" name="id" value="${someuser.id}">
        <label for="username">Username:</label>
        <input type="text" id="username" name="username" value="${someuser.username}" required>

        <label for="password">Password:</label>
        <input type="password" id="password" name="password" required>

        <label for="idEmployee">Employee:</label>
        <select id="idEmployee" name="idEmployee" required>
            <c:forEach var="employee" items="${listEmployees}">
                <option value="${employee.idEmployee}" <c:if test="${employee.idEmployee == someuser.idEmployee}">selected</c:if>>${employee.surname} ${employee.name}</option>
            </c:forEach>
        </select>

        <button type="submit" name="action" value="${someuser.id != null ? 'update' : 'insert'}">
            ${someuser.id != null ? 'Update User' : 'Add User'}
        </button>
    </form>
</main>
<footer>
    <p>&copy; 2024 ZLAGODA Supermarket Management System</p>
</footer>
</body>
</html>
