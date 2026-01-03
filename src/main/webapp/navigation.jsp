<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<nav>
    <ul>
        <li><a href="index.jsp">Home</a></li>
        <li><a href="user-profile">User Profile</a></li>
        <li><a href="logout">Logout</a></li>
        <c:if test="${userRole eq 'Manager'}">
            <li><a href="employees">Employees</a></li>
            <li><a href="categories">Categories</a></li>
            <li><a href="reports">Reports</a></li>
            <li><a href="manage-users">Users</a></li>
        </c:if>
        <li><a href="checks">Checks</a></li>
        <c:if test="${userRole eq 'Cashier'}">
            <li><a href="add-check">Add Check</a></li>
        </c:if>
        <li><a href="products">Products</a></li>
        <li><a href="store-products">Store Products</a></li>
        <li><a href="customers?action=list">Customer Cards</a></li>
    </ul>
</nav>
