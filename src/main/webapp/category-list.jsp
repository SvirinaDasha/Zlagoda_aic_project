<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
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
    <title>Manage Categories - ZLAGODA Supermarket</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<header>
    <h1>Manage Categories</h1>
    <jsp:include page="navigation.jsp"/>
</header>
<main>
    <section>
        <h2>Category List</h2>
        <form action="categories" method="post">
            <input type="hidden" name="action" value="insert">
            <label for="newCategoryName">New Category Name:</label>
            <input type="text" id="newCategoryName" name="categoryName" required>
            <button type="submit">Add Category</button>
        </form>
        <table border="1">
            <tr>
                <th>Category Number</th>
                <th>Category Name</th>
                <th>Actions</th>
            </tr>
            <c:forEach var="category" items="${listCategory}">
                <tr>
                    <td>${category.categoryNumber}</td>
                    <td>${category.categoryName}</td>
                    <td>
                        <a href="categories?action=edit&id=${category.categoryNumber}">Edit</a>
                        <a href="categories?action=delete&id=${category.categoryNumber}">Delete</a>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </section>
</main>
<footer>
    <p>&copy; 2024 ZLAGODA Supermarket Management
</footer>
</body>
</html>