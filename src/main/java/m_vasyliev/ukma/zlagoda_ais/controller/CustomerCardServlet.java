package m_vasyliev.ukma.zlagoda_ais.controller;

import m_vasyliev.ukma.zlagoda_ais.utils.Validator;
import m_vasyliev.ukma.zlagoda_ais.dao.CustomerCardDAO;
import m_vasyliev.ukma.zlagoda_ais.model.CustomerCard;
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

@WebServlet("/customers")
public class CustomerCardServlet extends HttpServlet {
    private CustomerCardDAO customerCardDAO = new CustomerCardDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if(action == null) {
            action = "";
        }

        try {
            switch (action) {
                case "new":
                    showNewForm(req, resp);
                    break;
                case "insert":
                    insertCustomerCard(req, resp);
                    break;
                case "delete":
                    deleteCustomerCard(req, resp);
                    if (!Validator.isManager(req)) {
                        resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied.");
                        return;
                    }
                    break;
                case "edit":
                    showEditForm(req, resp);
                    break;
                case "update":
                    updateCustomerCard(req, resp);
                    break;
                case "checkConnection":
                    checkDatabaseConnection(req, resp);
                    break;
                case "search":
                    listCustomerCards(req, resp);
                    break;
                default:
                    listCustomerCards(req, resp);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void checkDatabaseConnection(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        boolean isConnected = customerCardDAO.checkConnection();
        resp.getWriter().write("Database connection is " + (isConnected ? "successful" : "failed"));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    private void listCustomerCards(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException, ServletException {
        String search = req.getParameter("search");
        String percentParam = req.getParameter("percent");
        List<CustomerCard> listCustomerCards;

        if ((search != null && !search.isEmpty()) && (percentParam != null && !percentParam.isEmpty())) {
            int percent = Integer.parseInt(percentParam);
            listCustomerCards = customerCardDAO.searchCustomerCardsBySurnameAndPercent(search, percent);
        } else if (search != null && !search.isEmpty()) {
            listCustomerCards = customerCardDAO.searchCustomerCardsBySurname(search);
        } else if (percentParam != null && !percentParam.isEmpty()) {
            int percent = Integer.parseInt(percentParam);
            listCustomerCards = customerCardDAO.getCustomerCardsByPercent(percent);
        } else {
            listCustomerCards = customerCardDAO.getAllCustomerCards();
        }

        req.setAttribute("listCustomerCards", listCustomerCards);
        req.getRequestDispatcher("/customer-cards.jsp").forward(req, resp);
    }

    private void showNewForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/customer-card-form.jsp").forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {
        String cardNumber = req.getParameter("cardNumber");
        CustomerCard existingCustomerCard = customerCardDAO.getCustomerCardByCardNumber(cardNumber);
        req.setAttribute("customerCard", existingCustomerCard);
        req.getRequestDispatcher("/customer-card-form.jsp").forward(req, resp);
    }

    private void insertCustomerCard(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException, ServletException {
        String custSurname = new String(req.getParameter("custSurname").getBytes("ISO-8859-1"), "UTF-8");
        String custName = new String(req.getParameter("custName").getBytes("ISO-8859-1"), "UTF-8");
        String custPatronymic = new String(req.getParameter("custPatronymic").getBytes("ISO-8859-1"), "UTF-8");
        String phoneNumber = new String(req.getParameter("phoneNumber").getBytes("ISO-8859-1"), "UTF-8");
        String city = new String(req.getParameter("city").getBytes("ISO-8859-1"), "UTF-8");
        String street = new String(req.getParameter("street").getBytes("ISO-8859-1"), "UTF-8");
        String zipCode = new String(req.getParameter("zipCode").getBytes("ISO-8859-1"), "UTF-8");
        int percent = Integer.parseInt(req.getParameter("percent"));

        if(!Validator.isPhoneNumberValid(phoneNumber)) {
            req.getSession().setAttribute("errorMessage", "Incorrect phone number length");
            showNewForm(req,resp);
            return;
        }
        if(!Validator.isNonNegative(percent)) {
            req.getSession().setAttribute("errorMessage", "Invalid percentage");
            showNewForm(req,resp);
            return;
        }

        CustomerCard customerCard = new CustomerCard("", custSurname, custName, custPatronymic, phoneNumber, city, street, zipCode, percent);
        customerCardDAO.addCustomerCard(customerCard);
        resp.sendRedirect("customers?action=list");
    }

    private void updateCustomerCard(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException, ServletException {
        String cardNumber = req.getParameter("cardNumber");
        String custSurname = new String(req.getParameter("custSurname").getBytes("ISO-8859-1"), "UTF-8");
        String custName = new String(req.getParameter("custName").getBytes("ISO-8859-1"), "UTF-8");
        String custPatronymic = new String(req.getParameter("custPatronymic").getBytes("ISO-8859-1"), "UTF-8");
        String phoneNumber = new String(req.getParameter("phoneNumber").getBytes("ISO-8859-1"), "UTF-8");
        String city = new String(req.getParameter("city").getBytes("ISO-8859-1"), "UTF-8");
        String street = new String(req.getParameter("street").getBytes("ISO-8859-1"), "UTF-8");
        String zipCode = new String(req.getParameter("zipCode").getBytes("ISO-8859-1"), "UTF-8");
        int percent = Integer.parseInt(req.getParameter("percent"));

        if(!Validator.isPhoneNumberValid(phoneNumber)) {
            req.getSession().setAttribute("errorMessage", "Incorrect phone number length");
            showEditForm(req,resp);
            return;
        }
        if(!Validator.isNonNegative(percent)) {
            req.getSession().setAttribute("errorMessage", "Invalid percentage");
            showEditForm(req,resp);
            return;
        }

        CustomerCard customerCard = new CustomerCard(cardNumber, custSurname, custName, custPatronymic, phoneNumber, city, street, zipCode, percent);
        customerCardDAO.updateCustomerCard(customerCard);
        resp.sendRedirect("customers?action=list");
    }

    private void deleteCustomerCard(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        if (!"Manager".equals(user.getRole())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied.");
            return;
        }

        String cardNumber = req.getParameter("cardNumber");
        customerCardDAO.deleteCustomerCard(cardNumber);
        resp.sendRedirect("customers?action=list");
    }
}
