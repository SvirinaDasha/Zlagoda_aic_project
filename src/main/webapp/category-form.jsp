<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%
    if (session == null || session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    else {
        String userRole = ((m_vasyliev.ukma.zlagoda_ais.model.User) session.getAttribute("user")).getRole();
        request.setAttribute("userRole", userRole);
        if(userRole.equals("Cashier")){
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied.");
        }
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Add/Edit Category</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<header>
    <h1>Add/Edit Category</h1>
    <jsp:include page="navigation.jsp"/>
</header>
<main>
    <section>
        <h2>Add/Edit Category</h2>
        <form action="categories" method="post">
            <input type="hidden" name="action" value="${empty category ? 'insert' : 'update'}">
            <c:if test="${not empty category}">
                <input type="hidden" name="categoryId" value="${category.categoryNumber}">
            </c:if>
            <label for="categoryName">Category Name:</label>
            <input type="text" id="categoryName" name="categoryName" value="${not empty category ? category.categoryName : ''}" required>
            <br>
            <button type="submit">${empty category ? 'Add Category' : 'Save Changes'}</button>
        </form>
    </section>
</main>
<footer>
    <p>&copy; 2024 ZLAGODA Supermarket Management System</p>
</footer>
</body>
</html>