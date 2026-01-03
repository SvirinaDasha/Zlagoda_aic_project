package m_vasyliev.ukma.zlagoda_ais.controller;

import m_vasyliev.ukma.zlagoda_ais.utils.Validator;
import m_vasyliev.ukma.zlagoda_ais.dao.EmployeeDAO;
import m_vasyliev.ukma.zlagoda_ais.model.Employee;
import m_vasyliev.ukma.zlagoda_ais.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@WebServlet("/employees")
public class EmployeeServlet extends HttpServlet {
    private EmployeeDAO employeeDAO = new EmployeeDAO();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        if (!"Manager".equals(user.getRole())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied.");
            return;
        }

        String action = req.getParameter("action");
        if (action == null) {
            action = "";
        }
        try {
            System.out.println("action: " + action);
            if (!Validator.isManager(req)) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied.");
                return;
            }
            switch (action) {
                case "new":
                    showNewForm(req, resp);
                    break;
                case "insert":
                    insertEmployee(req, resp);
                    break;
                case "delete":
                    deleteEmployee(req, resp);
                    break;
                case "edit":
                    showEditForm(req, resp);
                    break;
                case "update":
                    updateEmployee(req, resp);
                    break;
                case "search":
                    searchEmployee(req, resp);
                    break;
                default:
                    listEmployees(req, resp);
                    break;
            }
        } catch (SQLException | ParseException ex) {
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    private void listEmployees(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException, ServletException {
        String role = req.getParameter("selectedRole");
        List<Employee> listEmployee;
        if (role == null || role.isEmpty()) {
            listEmployee = employeeDAO.getAllEmployees();
        } else {
            listEmployee = employeeDAO.getEmployeesByRole(role);
        }
        List<String> listRoles = employeeDAO.getallroles();
        req.setAttribute("listEmployees", listEmployee);
        req.setAttribute("listRoles", listRoles);
        req.setAttribute("selectedRole", role);
        req.getRequestDispatcher("/employees.jsp").forward(req, resp);
    }

    private void searchEmployee(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException, ServletException {
        String surname = req.getParameter("surname");
        List<Employee> searchResults = employeeDAO.searchEmployeesBySurname(surname);
        req.setAttribute("searchResults", searchResults);
        req.getRequestDispatcher("/employee-search.jsp").forward(req, resp);
    }

    private void showNewForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/employee-form.jsp").forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {
        String id = req.getParameter("id");
        Employee existingEmployee = employeeDAO.getEmployeeById(id);
        req.setAttribute("employee", existingEmployee);
        req.getRequestDispatcher("/employee-form.jsp").forward(req, resp);
    }

    private void insertEmployee(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException, ParseException, ServletException {
        req.setCharacterEncoding("UTF-8");
        String surname = new String(req.getParameter("surname").getBytes("ISO-8859-1"), "UTF-8");
        String name = new String(req.getParameter("name").getBytes("ISO-8859-1"), "UTF-8");
        String patronymic = new String(req.getParameter("patronymic").getBytes("ISO-8859-1"), "UTF-8");
        String role = new String(req.getParameter("role").getBytes("ISO-8859-1"), "UTF-8");
        double salary = 0;
        if(!Validator.isParsableAsDouble(req.getParameter("salary"))){
            req.setAttribute("errorMessage", "Salary must be decimal!");
            showNewForm(req, resp);
            return;
        }
        else{
            salary = Double.parseDouble(req.getParameter("salary"));
        }

        String dateOfBirth = new java.sql.Date(dateFormat.parse(req.getParameter("dateOfBirth")).getTime()).toString();
        String dateOfStart = new java.sql.Date(dateFormat.parse(req.getParameter("dateOfStart")).getTime()).toString();
        String phoneNumber = new String(req.getParameter("phoneNumber").getBytes("ISO-8859-1"), "UTF-8");
        String city = new String(req.getParameter("city").getBytes("ISO-8859-1"), "UTF-8");
        String street = new String(req.getParameter("street").getBytes("ISO-8859-1"), "UTF-8");
        String zipCode = new String(req.getParameter("zipCode").getBytes("ISO-8859-1"), "UTF-8");

        if(!Validator.isEmployeeOlderThan18(dateOfBirth)){
            req.setAttribute("errorMessage", "Employee must be older than 18!");
            showNewForm(req, resp);
            return;
        }

        if(!Validator.isNonNegative(salary)){
            req.setAttribute("errorMessage", "Salary must be non-negative!");
            showNewForm(req, resp);
            return;
        }
        if(!Validator.isPhoneNumberValid(phoneNumber)){
            req.setAttribute("errorMessage", "Phone number is invalid!");
            showNewForm(req, resp);
            return;
        }


        Employee employee = new Employee("", surname, name, patronymic, role, phoneNumber, city, street, zipCode, dateOfBirth, dateOfStart, salary);
        employeeDAO.addEmployee(employee);
        resp.sendRedirect("employees?action=list");
    }

    private void updateEmployee(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException, ParseException, ServletException {
        req.setCharacterEncoding("UTF-8");
        String id = req.getParameter("id");
        String surname = new String(req.getParameter("surname").getBytes("ISO-8859-1"), "UTF-8");
        String name = new String(req.getParameter("name").getBytes("ISO-8859-1"), "UTF-8");
        String patronymic = new String(req.getParameter("patronymic").getBytes("ISO-8859-1"), "UTF-8");
        String role = new String(req.getParameter("role").getBytes("ISO-8859-1"), "UTF-8");
        double salary = 0;
        if(!Validator.isParsableAsDouble(req.getParameter("salary"))){
            req.setAttribute("errorMessage", "Salary must be decimal!");
            showNewForm(req, resp);
            return;
        }
        else{
            salary = Double.parseDouble(req.getParameter("salary"));
        }
        String dateOfBirth = new java.sql.Date(dateFormat.parse(req.getParameter("dateOfBirth")).getTime()).toString();
        String dateOfStart = new java.sql.Date(dateFormat.parse(req.getParameter("dateOfStart")).getTime()).toString();
        String phoneNumber = new String(req.getParameter("phoneNumber").getBytes("ISO-8859-1"), "UTF-8");
        String city = new String(req.getParameter("city").getBytes("ISO-8859-1"), "UTF-8");
        String street = new String(req.getParameter("street").getBytes("ISO-8859-1"), "UTF-8");
        String zipCode = new String(req.getParameter("zipCode").getBytes("ISO-8859-1"), "UTF-8");

        if(!Validator.isEmployeeOlderThan18(dateOfBirth)){
            req.setAttribute("errorMessage", "Employee must be older than 18!");
            showEditForm(req, resp);
            return;
        }

        if(!Validator.isNonNegative(salary)){
            req.setAttribute("errorMessage", "Salary must be non-negative!");
            showEditForm(req, resp);
            return;
        }
        if(!Validator.isPhoneNumberValid(phoneNumber)){
            req.setAttribute("errorMessage", "Phone number is invalid!");
            showEditForm(req, resp);
            return;
        }

        Employee employee = new Employee(id, surname, name, patronymic, role, phoneNumber, city, street, zipCode, dateOfBirth, dateOfStart, salary);
        employeeDAO.updateEmployee(employee);
        resp.sendRedirect("employees?action=list");
    }

    private void deleteEmployee(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException {
        String id = req.getParameter("id");
        employeeDAO.deleteEmployee(id);
        resp.sendRedirect("employees?action=list");
    }
}
