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
import java.sql.SQLException;
import java.util.*;

@WebServlet("/reports")
public class ReportServlet extends HttpServlet {
    private EmployeeDAO employeeDAO = new EmployeeDAO();
    private CustomerCardDAO customerCardDAO  = new CustomerCardDAO();
    private CategoryDAO categoryDAO = new CategoryDAO();
    private ProductDAO productDAO = new ProductDAO();
    private StoreProductDAO storeProductDAO = new StoreProductDAO();
    private CheckDAO checkDAO = new CheckDAO();
    private SaleDAO saleDAO = new SaleDAO();



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String reportType = request.getParameter("reportType");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cashierId = request.getParameter("cashierId");
        String productId = request.getParameter("productId");

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
                    List<Check> checks = checkDAO.getAllChecks();
                    for (Check check : checks) {
                        Map<String, Object> row = new HashMap<>();
                        row.put("Check Number", check.getCheckNumber());
                        row.put("Employee ID", check.getEmployeeId());
                        row.put("Card Number", check.getCardNumber());
                        row.put("Print Date", check.getPrintDate());
                        row.put("Total Sum", check.getSumTotal());
                        row.put("VAT", check.getVat());
                        data.add(row);
                    }
                    columns = Arrays.asList("Check Number", "Employee ID", "Card Number", "Print Date", "Total Sum", "VAT");
                    break;
                case "TSBCAP":
                    data = saleDAO.getTotalSalesByCashierAndPeriod(cashierId, startDate, endDate);
                    columns = Arrays.asList("Cashier ID", "Total Sales");
                    break;
                case "TSBP":
                    data = saleDAO.getTotalSalesByPeriod(startDate, endDate);
                    columns = Arrays.asList("Total Sales");
                    break;
                case "TUSBPAP":
                    data = saleDAO.getTotalUnitsSoldByProductAndPeriod(productId, startDate, endDate);
                    columns = Arrays.asList("Product ID", "Total Units Sold");
                    break;
                default:
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        request.setAttribute("data", data);
        request.setAttribute("columns", columns);
        request.setAttribute("products", products);
        request.setAttribute("cashiers", cashiers);
        request.getRequestDispatcher("/reports.jsp").forward(request, response);
    }
}
