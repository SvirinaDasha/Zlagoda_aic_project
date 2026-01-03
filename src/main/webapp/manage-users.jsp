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
    <title>Manage Users</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<header>
    <h1>Manage Users</h1>
    <jsp:include page="navigation.jsp"/>
</header>
<main>
    <section>
        <a href="manage-users?action=new">Add new user</a>
        <h2>User List</h2>
        <table border="1">
            <tr>
                <th>ID</th>
                <th>Username</th>
                <th>Employee</th>
                <th>Actions</th>
            </tr>
            <c:forEach var="user" items="${listUsers}">
                <tr>
                    <td>${user.id}</td>
                    <td>${user.username}</td>
                    <td>${user.idEmployee}</td>
                    <td>
                        <a href="manage-users?action=edit&id=${user.id}">Edit</a>
                        <a href="manage-users?action=delete&id=${user.id}">Delete</a>
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
