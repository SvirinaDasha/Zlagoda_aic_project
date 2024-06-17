package m_vasyliev.ukma.zlagoda_ais.controller;

import m_vasyliev.ukma.zlagoda_ais.dao.*;
import m_vasyliev.ukma.zlagoda_ais.dto.StoreProductDetails;
import m_vasyliev.ukma.zlagoda_ais.model.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@WebServlet("/add-check")
public class AddCheckServlet extends HttpServlet {
    private CheckDAO checkDAO;
    private StoreProductDAO storeProductDAO;
    private CustomerCardDAO customerCardDAO;
    private EmployeeDAO employeeDAO;
    private SaleDAO saleDAO;

    public void init() {
        checkDAO = new CheckDAO();
        storeProductDAO = new StoreProductDAO();
        customerCardDAO = new CustomerCardDAO();
        employeeDAO = new EmployeeDAO();
        saleDAO = new SaleDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<StoreProductDetails> products = storeProductDAO.getAllStoreProductDetails("products_number");
        List<CustomerCard> customerCards = customerCardDAO.getAllCustomerCards();

        // Get employee details
        User user = (User) request.getSession().getAttribute("user");
        Employee employee = employeeDAO.getEmployeeById(user.getIdEmployee());
        String employeeFullName = employee.getSurname() + " " + employee.getName() + " " + employee.getPatronymic();

        List<Sale> sales = (List<Sale>) request.getSession().getAttribute("sales");
        if (sales == null) {
            sales = new ArrayList<>();
            request.getSession().setAttribute("sales", sales);
        }

        String selectedCardNumber = (String) request.getSession().getAttribute("selectedCardNumber");

        LocalDate today = LocalDate.now();

        String successMessage = (String) request.getSession().getAttribute("successMessage");
        request.getSession().removeAttribute("successMessage");

        request.setAttribute("products", products);
        request.setAttribute("customerCards", customerCards);
        request.setAttribute("employeeFullName", employeeFullName);
        request.setAttribute("sales", sales);
        request.setAttribute("selectedCardNumber", selectedCardNumber);
        request.setAttribute("today", today);
        request.setAttribute("successMessage", successMessage);
        request.getRequestDispatcher("/add-check.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String cardNumber = request.getParameter("cardNumber");

        // Save the selected card number to the session
        if (cardNumber != null && !cardNumber.isEmpty()) {
            request.getSession().setAttribute("selectedCardNumber", cardNumber);
            applyDiscountToSales(request, cardNumber);
        } else {
            request.getSession().removeAttribute("selectedCardNumber");
            removeDiscountFromSales(request);
        }

        if ("addProduct".equals(action)) {
            addProductToCheck(request, response);
        } else if ("submitCheck".equals(action)) {
            submitCheck(request, response);
        } else if ("removeProduct".equals(action)) {
            removeProductFromCheck(request, response);
        } else if ("selectCard".equals(action)) {
            doGet(request, response);
        }
    }

    private void applyDiscountToSales(HttpServletRequest request, String cardNumber) {
        List<Sale> sales = (List<Sale>) request.getSession().getAttribute("sales");
        if (sales != null && cardNumber != null) {
            CustomerCard customerCard = customerCardDAO.getCustomerCardByNumber(cardNumber);
            if (customerCard != null) {
                double discount = customerCard.getPercent();
                for (Sale sale : sales) {
                    StoreProductDetails storeProduct = storeProductDAO.getStoreProductDetailsByUpc(sale.getUpc());
                    double originalPrice = storeProduct.getSellingPrice();
                    double discountedPrice = originalPrice - (originalPrice * discount / 100);
                    sale.setSellingPrice(discountedPrice);
                }
                request.getSession().setAttribute("sales", sales);
            }
        }
    }

    private void removeDiscountFromSales(HttpServletRequest request) {
        List<Sale> sales = (List<Sale>) request.getSession().getAttribute("sales");
        if (sales != null) {
            for (Sale sale : sales) {
                StoreProductDetails storeProduct = storeProductDAO.getStoreProductDetailsByUpc(sale.getUpc());
                double originalPrice = storeProduct.getSellingPrice();
                sale.setSellingPrice(originalPrice);
            }
            request.getSession().setAttribute("sales", sales);
        }
    }

    private void addProductToCheck(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String upc = request.getParameter("upc");
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        String cardNumber = (String) request.getSession().getAttribute("selectedCardNumber");

        StoreProductDetails storeProduct = storeProductDAO.getStoreProductDetailsByUpc(upc);
        double price = storeProduct.getSellingPrice();

        // Apply discount if customer card is selected
        if (cardNumber != null && !cardNumber.isEmpty()) {
            CustomerCard customerCard = customerCardDAO.getCustomerCardByNumber(cardNumber);
            if (customerCard != null) {
                double discount = customerCard.getPercent();
                price = price - (price * discount / 100);
            }
        }

        double total = price * quantity;

        Sale sale = new Sale();
        sale.setUpc(upc);
        sale.setProductNumber(quantity);
        sale.setSellingPrice(price);

        List<Sale> sales = (List<Sale>) request.getSession().getAttribute("sales");
        if (sales == null) {
            sales = new ArrayList<>();
        }
        sales.add(sale);
        request.getSession().setAttribute("sales", sales);

        doGet(request, response);
    }

    private void removeProductFromCheck(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String upc = request.getParameter("upc");

        List<Sale> sales = (List<Sale>) request.getSession().getAttribute("sales");
        if (sales != null) {
            sales.removeIf(sale -> sale.getUpc().equals(upc));
            request.getSession().setAttribute("sales", sales);
        }

        doGet(request, response);
    }

    private void submitCheck(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        String employeeId = user.getIdEmployee();
        String cardNumber = (String) request.getSession().getAttribute("selectedCardNumber");
        List<Sale> sales = (List<Sale>) request.getSession().getAttribute("sales");

        LocalDateTime now = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(now);

        String checkNumber = UUID.randomUUID().toString();

        Check salesCheck = new Check();
        salesCheck.setCheckNumber(checkNumber);
        salesCheck.setEmployeeId(employeeId);
        salesCheck.setCardNumber(cardNumber);
        salesCheck.setPrintDate(timestamp);

        double sumTotal = 0.0;
        for (Sale sale : sales) {
            double total = sale.getSellingPrice() * sale.getProductNumber();
            sumTotal += total;
            sale.setCheckNumber(checkNumber);
            sale.setSellingPrice(total);
            saleDAO.addSale(sale);
        }

        salesCheck.setSumTotal(sumTotal);
        salesCheck.setVat(sumTotal * 0.2); // Assuming 20% VAT

        checkDAO.saveCheck(salesCheck);
        request.getSession().removeAttribute("sales");
        request.getSession().removeAttribute("selectedCardNumber");
        request.getSession().setAttribute("successMessage", "Check successfully added");

        doGet(request, response);
    }
}
