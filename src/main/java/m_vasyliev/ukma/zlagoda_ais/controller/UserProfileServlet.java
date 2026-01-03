package m_vasyliev.ukma.zlagoda_ais.controller;

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

@WebServlet("/user-profile")
public class UserProfileServlet extends HttpServlet {
    private EmployeeDAO employeeDAO = new EmployeeDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");
        String employeeId = user.getIdEmployee();

        Employee employee = employeeDAO.getEmployeeById(employeeId);
        req.setAttribute("employee", employee);
        req.getRequestDispatcher("/user-profile.jsp").forward(req, resp);
    }
}
