package m_vasyliev.ukma.zlagoda_ais.controller;

import m_vasyliev.ukma.zlagoda_ais.dao.*;
import m_vasyliev.ukma.zlagoda_ais.dto.CheckDTO;
import m_vasyliev.ukma.zlagoda_ais.dto.SaleDTO;
import m_vasyliev.ukma.zlagoda_ais.dto.StoreProductDetails;
import m_vasyliev.ukma.zlagoda_ais.model.*;
import m_vasyliev.ukma.zlagoda_ais.utils.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@WebServlet("/reports")
public class ReportServlet extends HttpServlet {
    private EmployeeDAO employeeDAO = new EmployeeDAO();
    private CustomerCardDAO customerCardDAO = new CustomerCardDAO();
    private CategoryDAO categoryDAO = new CategoryDAO();
    private ProductDAO productDAO = new ProductDAO();
    private StoreProductDAO storeProductDAO = new StoreProductDAO();
    private CheckDAO checkDAO = new CheckDAO();
    private SaleDAO saleDAO = new SaleDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!Validator.isManager(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied.");
            return;
        }
        String reportType = request.getParameter("reportType");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cashierId = request.getParameter("cashierId");
        String productId = request.getParameter("productId");
        String productName = null;

        String errorMessage = null;

        List<Map<String, Object>> data = new ArrayList<>();
        List<String> columns = new ArrayList<>();
        List<Employee> cashiers = new ArrayList<>();
        List<Product> products = new ArrayList<>();

        try {
            cashiers = employeeDAO.getAllEmployees();
            products = productDAO.getAllProducts();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (!"Manager".equals(user.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied.");
            return;
        }
        try {
            if(reportType == null){
                reportType="";
            }
            switch (reportType) {
                case "employees":
                    List<Employee> employees = employeeDAO.getAllEmployees();
                    for (Employee employee : employees) {
                        Map<String, Object> row = new HashMap<>();
                        row.put("ID", employee.getIdEmployee());
                        row.put("Surname", employee.getSurname());
                        row.put("Name", employee.getName());
                        row.put("Role", employee.getRole());
                        row.put("Date of Birth", employee.getDateOfBirth());
                        row.put("Date of Start", employee.getDateOfStart());
                        data.add(row);
                    }
                    columns = Arrays.asList("ID", "Surname", "Name", "Role", "Date of Birth", "Date of Start");
                    break;
                case "customers":
                    List<CustomerCard> customers = customerCardDAO.getAllCustomerCards();
                    for (CustomerCard customer : customers) {
                        Map<String, Object> row = new HashMap<>();
                        row.put("Card Number", customer.getCardNumber());
                        row.put("Surname", customer.getCustSurname());
                        row.put("Name", customer.getCustName());
                        row.put("Patronymic", customer.getCustPatronymic());
                        row.put("Phone Number", customer.getPhoneNumber());
                        row.put("City", customer.getCity());
                        row.put("Street", customer.getStreet());
                        row.put("Zip Code", customer.getZipCode());
                        row.put("Discount Percentage", customer.getPercent());
                        data.add(row);
                    }
                    columns = Arrays.asList("Card Number", "Surname", "Name", "Patronymic", "Phone Number", "City", "Street", "Zip Code", "Discount Percentage");
                    break;
                case "categories":
                    List<Category> categories = categoryDAO.getAllCategories();
                    for (Category category : categories) {
                        Map<String, Object> row = new HashMap<>();
                        row.put("Category Number", category.getCategoryNumber());
                        row.put("Category Name", category.getCategoryName());
                        data.add(row);
                    }
                    columns = Arrays.asList("Category Number", "Category Name");
                    break;
                case "products":
                    for (Product product : products) {
                        Map<String, Object> row = new HashMap<>();
                        row.put("ID", product.getId());
                        row.put("Category Number", product.getCategoryNumber());
                        row.put("Name", product.getProductName());
                        row.put("Characteristics", product.getCharacteristics());
                        data.add(row);
                    }
                    columns = Arrays.asList("ID", "Category Number", "Name", "Characteristics");
                    break;
                case "store-products":
                    List<StoreProductDetails> storeProducts = storeProductDAO.getAllStoreProductDetails("product_name");
                    for (StoreProductDetails storeProduct : storeProducts) {
                        Map<String, Object> row = new HashMap<>();
                        row.put("UPC", storeProduct.getUpc());
                        row.put("Product Name", storeProduct.getProductName());
                        row.put("Selling Price", storeProduct.getSellingPrice());
                        row.put("Available Quantity", storeProduct.getProductsNumber());
                        row.put("Promotional Product", storeProduct.isPromotionalProduct());
                        data.add(row);
                    }
                    columns = Arrays.asList("UPC", "Product Name", "Selling Price", "Available Quantity", "Promotional Product");
                    break;
                case "checks":
                    data = checkDAO.getChecksReport(cashierId, startDate, endDate);
                    columns = Arrays.asList("Check Number", "Employee", "Card Number", "Print Date", "Total Sum", "VAT");
                    break;
                case "TSBCAP":
                    if (cashierId == null || cashierId.isEmpty()) {
                        errorMessage = "Cashier must be selected for Sales by Cashier and Period report.";
                    } else {


                    List<SaleDTO> salesByCashier = saleDAO.getTotalSalesByCashierAndPeriod(cashierId, startDate, endDate);
                    for (SaleDTO sale : salesByCashier) {
                        Map<String, Object> row = new HashMap<>();
                        row.put("Cashier ID", sale.getCashierId());
                        row.put("Cashier Name", sale.getCashierName());
                        row.put("Total Sales", sale.getTotalSales());
                        data.add(row);
                    }
                    columns = Arrays.asList("Cashier ID", "Cashier Name", "Total Sales");
                    }
                    break;
                case "TSBP":
                    List<SaleDTO> salesByPeriod = saleDAO.getTotalSalesByPeriod(startDate, endDate);
                    for (SaleDTO sale : salesByPeriod) {
                        Map<String, Object> row = new HashMap<>();
                        row.put("Total Sales", sale.getTotalSales());
                        data.add(row);
                    }
                    columns = Collections.singletonList("Total Sales");
                    break;
                case "TUSBPAP":
                    List<SaleDTO> unitsSoldByProduct = saleDAO.getTotalUnitsSoldByProductAndPeriod(productId, startDate, endDate);
                    for (SaleDTO sale : unitsSoldByProduct) {
                        Map<String, Object> row = new HashMap<>();
                        row.put("Product ID", sale.getProductId());
                        row.put("Product Name", sale.getProductName());
                        row.put("Total Units Sold", sale.getTotalUnitsSold());
                        data.add(row);
                    }
                    columns = Arrays.asList("Product ID", "Product Name", "Total Units Sold");
                    break;
                case "checkByPeriod":
                    List<CheckDTO> checksByPeriod = checkDAO.getChecksByPeriod(startDate, endDate);
                    for (CheckDTO check : checksByPeriod) {
                        Map<String, Object> row = new HashMap<>();
                        row.put("Check Number", check.getCheckNumber());
                        row.put("Employee Name", check.getEmployeeFullName());
                        row.put("Card Number", check.getCardNumber());
                        row.put("Print Date", check.getPrintDate());
                        row.put("Total Sum", check.getSumTotal());
                        row.put("VAT", check.getVat());
                        data.add(row);
                    }
                    columns = Arrays.asList("Check Number", "Employee Name", "Card Number", "Print Date", "Total Sum", "VAT");
                    break;
                case "product-sales":

                    if(productId!=null && !productId.isEmpty()){
                        productName = productDAO.getProductById(Integer.parseInt(productId)).getProductName();
                    }
                    else{
                        errorMessage = "Product must be provided.";
                    }
                    if (productName == null || productName.isEmpty()) {
                        errorMessage = "Product name must be provided.";
                    } else {
                        request.setAttribute("productName", productName);
                        data = saleDAO.getProductSales(productName, startDate, endDate);
                        columns = Arrays.asList("Print Date", "Total Sales");
                    }
                    break;

                case "categories-with-sales":
                    data = categoryDAO.getCategoriesWithSales();
                    columns = Arrays.asList("Category Number", "Category Name");
                    break;
                default:
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
        if (user != null) {
            Employee employee = new EmployeeDAO().getEmployeeById(user.getIdEmployee());
            String fullName = employee.getSurname() + " " + employee.getName() + " " + employee.getPatronymic();
            request.getSession().setAttribute("userFullName", fullName);
        }

        request.setAttribute("data", data);
        request.setAttribute("columns", columns);
        request.setAttribute("products", products);
        request.setAttribute("errorMessage", errorMessage);
        request.setAttribute("startDate", startDate);
        request.setAttribute("endDate", endDate);
        request.setAttribute("cashiers", cashiers);
        request.getRequestDispatcher("/reports.jsp").forward(request, response);
    }
}
