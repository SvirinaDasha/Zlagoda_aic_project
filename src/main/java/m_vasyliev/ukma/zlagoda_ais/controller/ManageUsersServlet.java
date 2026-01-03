package m_vasyliev.ukma.zlagoda_ais.controller;

import m_vasyliev.ukma.zlagoda_ais.dao.EmployeeDAO;
import m_vasyliev.ukma.zlagoda_ais.dao.UserDAO;
import m_vasyliev.ukma.zlagoda_ais.model.User;
import m_vasyliev.ukma.zlagoda_ais.model.Employee;
import m_vasyliev.ukma.zlagoda_ais.utils.PasswordUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/manage-users")
public class ManageUsersServlet extends HttpServlet {
    private UserDAO userDAO;
    private EmployeeDAO employeeDAO;

    @Override
    public void init() {
        userDAO = new UserDAO();
        employeeDAO = new EmployeeDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (!"Manager".equals(user.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied.");
            return;
        }

        if (action == null) {
            action = "";
        }
        try {
            switch (action) {
                case "new":
                    showNewForm(request, response);
                    break;
                case "insert":
                    insertUser(request, response);
                    break;
                case "delete":
                    deleteUser(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "update":
                    updateUser(request, response);
                    break;
                default:
                    listUsers(request, response);
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        doGet(request, response);
    }

    private void listUsers(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        List<User> listUser = userDAO.getAllUsers();
        List<Employee> listEmployee = employeeDAO.getAllEmployees();
        request.setAttribute("listUsers", listUser);
        request.setAttribute("listEmployees", listEmployee);
        request.getRequestDispatcher("/manage-users.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        List<Employee> listEmployee = employeeDAO.getAllEmployees();
        request.setAttribute("id", null);
        request.setAttribute("listEmployees", listEmployee);
        request.getRequestDispatcher("/user-form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        User existingUser = userDAO.getUserById(id);
        List<Employee> listEmployee = employeeDAO.getAllEmployees();
        request.setAttribute("someuser", existingUser);
        request.setAttribute("listEmployees", listEmployee);
        request.getRequestDispatcher("/user-form.jsp").forward(request, response);
    }

    private void insertUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String idEmployee = request.getParameter("idEmployee");

        String hashedPassword = PasswordUtils.hashPassword(password);

        User newUser = new User(username, hashedPassword, idEmployee);
        userDAO.addUser(newUser);
        response.sendRedirect("manage-users");
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String idEmployee = request.getParameter("idEmployee");

        String hashedPassword = PasswordUtils.hashPassword(password);

        User user = new User(username, hashedPassword, idEmployee);
        user.setId(id);
        userDAO.updateUser(user);
        response.sendRedirect("manage-users");
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        userDAO.deleteUser(id);
        response.sendRedirect("manage-users");
    }
}
