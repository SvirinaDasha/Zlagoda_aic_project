package m_vasyliev.ukma.zlagoda_ais.controller;

import m_vasyliev.ukma.zlagoda_ais.dao.CheckDAO;
import m_vasyliev.ukma.zlagoda_ais.dao.EmployeeDAO;
import m_vasyliev.ukma.zlagoda_ais.dao.SaleDAO;
import m_vasyliev.ukma.zlagoda_ais.dto.CheckDTO;
import m_vasyliev.ukma.zlagoda_ais.dto.SaleDTO;
import m_vasyliev.ukma.zlagoda_ais.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/checks")
public class CheckServlet extends HttpServlet {
    private CheckDAO checkDAO;
    private SaleDAO saleDAO;
    private EmployeeDAO employeeDAO;

    @Override
    public void init() {
        checkDAO = new CheckDAO();
        saleDAO = new SaleDAO();
        employeeDAO = new EmployeeDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        try {
            switch (action) {
                case "view":
                    viewCheck(request, response);
                    break;
                case "delete":
                    deleteCheck(request, response);
                    break;
                case "search":
                    searchCheck(request, response);
                    break;
                default:
                    listChecks(request, response);
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void listChecks(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String role = user.getRole();
        String cashierId = request.getParameter("cashierId");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        List<CheckDTO> checks;

        if ("Cashier".equals(role)) {
            cashierId = user.getIdEmployee();
        }

        if (cashierId == null && startDate == null && endDate == null) {
            checks = checkDAO.getAllCheckDTOs();
        } else {
            checks = checkDAO.getFilteredChecks(cashierId, startDate, endDate);
        }

        request.setAttribute("checks", checks);
        request.setAttribute("cashiers", employeeDAO.getAllCashiers());
        request.getRequestDispatcher("/checks.jsp").forward(request, response);
    }

    private void viewCheck(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        String checkNumber = request.getParameter("checkNumber");
        CheckDTO check = checkDAO.getCheckDTOByNumber(checkNumber);
        List<SaleDTO> sales = saleDAO.getSalesByCheckNumber(checkNumber);
        request.setAttribute("check", check);
        request.setAttribute("sales", sales);
        request.getRequestDispatcher("/check-details.jsp").forward(request, response);
    }

    private void deleteCheck(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (!"Manager".equals(user.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied.");
            return;
        }

        String checkNumber = request.getParameter("checkNumber");
        checkDAO.deleteCheck(checkNumber);
        response.sendRedirect("checks");
    }

    private void searchCheck(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        String checkNumber = request.getParameter("checkNumber");
        CheckDTO check = checkDAO.getCheckDTOByNumber(checkNumber);
        if (check != null) {
            request.setAttribute("check", check);
            List<SaleDTO> sales = saleDAO.getSalesByCheckNumber(checkNumber);
            request.setAttribute("sales", sales);
            request.getRequestDispatcher("/check-details.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", "Check not found.");
            listChecks(request, response);
        }
    }
}
