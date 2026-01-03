package m_vasyliev.ukma.zlagoda_ais.controller;

import m_vasyliev.ukma.zlagoda_ais.utils.Validator;
import m_vasyliev.ukma.zlagoda_ais.dao.ProductDAO;
import m_vasyliev.ukma.zlagoda_ais.dao.StoreProductDAO;
import m_vasyliev.ukma.zlagoda_ais.dto.StoreProductDetails;
import m_vasyliev.ukma.zlagoda_ais.model.Product;
import m_vasyliev.ukma.zlagoda_ais.model.StoreProduct;
import m_vasyliev.ukma.zlagoda_ais.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@WebServlet("/store-products")
public class StoreProductServlet extends HttpServlet {
    private StoreProductDAO storeProductDAO = new StoreProductDAO();
    private ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");
        if (user == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        String action = req.getParameter("action");
        String filter = req.getParameter("filter");
        String sort = req.getParameter("sort");
        String upcSearch = req.getParameter("upcSearch");
        if (action == null) {
            action = "";
        }
        if (filter == null) {
            filter = "all";
        }
        if (sort == null) {
            sort = "products_number";
        }
        try {
            if (upcSearch != null && !upcSearch.isEmpty()) {
                searchStoreProduct(req, resp, upcSearch);
            } else {
                switch (action) {
                    case "new":
                        if (user.getRole().equals("Manager")) {
                            showNewForm(req, resp);
                        } else {
                            resp.sendRedirect("store-products");
                        }
                        break;
                    case "insert":
                        if (user.getRole().equals("Manager")) {
                            insertStoreProduct(req, resp);
                        } else {
                            resp.sendRedirect("store-products");
                        }
                        break;
                    case "delete":
                        if (user.getRole().equals("Manager")) {
                            deleteStoreProduct(req, resp, filter, sort);
                        } else {
                            resp.sendRedirect("store-products");
                        }
                        break;
                    case "edit":
                        if (user.getRole().equals("Manager")) {
                            showEditForm(req, resp);
                        } else {
                            resp.sendRedirect("store-products");
                        }
                        break;
                    case "update":
                        if (user.getRole().equals("Manager")) {
                            updateStoreProduct(req, resp);
                        } else {
                            resp.sendRedirect("store-products");
                        }
                        break;
                    default:
                        listStoreProducts(req, resp, filter, sort);
                        break;
                }
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    private void listStoreProducts(HttpServletRequest req, HttpServletResponse resp, String filter, String sort)
            throws SQLException, IOException, ServletException {
        List<StoreProductDetails> listStoreProduct;
        switch (filter) {
            case "promotional":
                listStoreProduct = storeProductDAO.getPromotionalStoreProducts(true, sort);
                break;
            case "non-promotional":
                listStoreProduct = storeProductDAO.getPromotionalStoreProducts(false, sort);
                break;
            default:
                listStoreProduct = storeProductDAO.getAllStoreProductDetails(sort);
                break;
        }
        req.setAttribute("listStoreProduct", listStoreProduct);
        req.getRequestDispatcher("/store-products.jsp").forward(req, resp);
    }

    private void searchStoreProduct(HttpServletRequest req, HttpServletResponse resp, String upc)
            throws SQLException, IOException, ServletException {
        StoreProductDetails searchResult = storeProductDAO.getStoreProductDetailsByUpc(upc);
        List<StoreProductDetails> searchResults = new ArrayList<>();
        if (searchResult != null) {
            searchResults.add(searchResult);
        }
        req.setAttribute("searchResults", searchResults);
        listStoreProducts(req, resp, "all", "products_number");  // Default list
    }

    private void showNewForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException, SQLException {
        List<Product> listProduct = productDAO.getAllProducts();
        req.setAttribute("listProduct", listProduct);
        req.getRequestDispatcher("/store-product-form.jsp").forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {
        String upc = req.getParameter("upc");
        StoreProduct existingStoreProduct = storeProductDAO.getStoreProductByUpc(upc);
        List<Product> listProduct = productDAO.getAllProducts();
        req.setAttribute("storeProduct", existingStoreProduct);
        req.setAttribute("listProduct", listProduct);
        req.getRequestDispatcher("/store-product-form.jsp").forward(req, resp);
    }

    private void insertStoreProduct(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException, ServletException {
        String upc = req.getParameter("upc");
        String upcProm = req.getParameter("upcProm");
        int productId = Integer.parseInt(req.getParameter("productId"));
        double sellingPrice = Double.parseDouble(req.getParameter("sellingPrice"));
        int productsNumber = Integer.parseInt(req.getParameter("productsNumber"));
        boolean promotionalProduct = req.getParameter("promotionalProduct") != null;


        if (promotionalProduct && !storeProductDAO.existsNonPromotionalProduct(productId)) {
            req.setAttribute("error", "Cannot create promotional product without existing non-promotional product.");
            showNewForm(req, resp);
            return;
        }


        if (promotionalProduct) {
            double originalPrice = storeProductDAO.getNonPromotionalPrice(productId);
            sellingPrice = originalPrice * 0.8;
        }

        StoreProduct newStoreProduct = new StoreProduct();
        newStoreProduct.setUpc(upc);
        newStoreProduct.setUpcProm(upcProm);
        newStoreProduct.setProductId(productId);
        newStoreProduct.setSellingPrice(sellingPrice);
        newStoreProduct.setProductsNumber(productsNumber);
        newStoreProduct.setPromotionalProduct(promotionalProduct);

        try {
            storeProductDAO.addStoreProduct(newStoreProduct);
            resp.sendRedirect("store-products");
        } catch (SQLException e) {
            req.setAttribute("error", "Error inserting store product: " + e.getMessage());
            showNewForm(req, resp);
        }
    }

    private void updateStoreProduct(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException, ServletException {
        String upc = req.getParameter("upc");
        String upcProm = req.getParameter("upcProm");
        int productId = Integer.parseInt(req.getParameter("productId"));
        double sellingPrice = Double.parseDouble(req.getParameter("sellingPrice"));
        int productsNumber = Integer.parseInt(req.getParameter("productsNumber"));
        boolean promotionalProduct = req.getParameter("promotionalProduct") != null;

        StoreProduct originalStoreProduct = storeProductDAO.getStoreProductByUpc(upc);

        if (!originalStoreProduct.isPromotionalProduct() && promotionalProduct && storeProductDAO.countNonPromotionalProducts(productId) == 1) {
            req.setAttribute("error", "Cannot update the last non-promotional product to promotional.");
            showEditForm(req, resp);
            return;
        }


        if (promotionalProduct) {
            double originalPrice = storeProductDAO.getNonPromotionalPrice(productId);
            sellingPrice = originalPrice * 0.8;
        } else {

            storeProductDAO.updatePromotionalPrice(upc, sellingPrice);
        }

        StoreProduct storeProduct = new StoreProduct();
        storeProduct.setUpc(upc);
        storeProduct.setUpcProm(upcProm);
        storeProduct.setProductId(productId);
        storeProduct.setSellingPrice(sellingPrice);
        storeProduct.setProductsNumber(productsNumber);
        storeProduct.setPromotionalProduct(promotionalProduct);

        if(!Validator.isNonNegative(storeProduct.getProductsNumber())){
            req.setAttribute("error", "Products number can`t be negative!");
            showEditForm(req, resp);
            return;
        }
        if(!Validator.isNonNegative(storeProduct.getSellingPrice())){
            req.setAttribute("error", "Selling price can`t be negative!");
            showEditForm(req, resp);
            return;
        }

        try {
            storeProductDAO.updateStoreProduct(storeProduct);
            resp.sendRedirect("store-products");
        } catch (SQLException e) {
            req.setAttribute("error", "Error updating store product: " + e.getMessage());
            showEditForm(req, resp);
        }
    }

    private void deleteStoreProduct(HttpServletRequest req, HttpServletResponse resp, String filter, String sort)
            throws SQLException, IOException, ServletException {
        String upc = req.getParameter("upc");
        try {
            storeProductDAO.deleteStoreProduct(upc);
            resp.sendRedirect("store-products");
        } catch (SQLException e) {
            req.setAttribute("error", "Error deleting store product: " + e.getMessage());
            listStoreProducts(req, resp, filter, sort);
        }
    }
}
