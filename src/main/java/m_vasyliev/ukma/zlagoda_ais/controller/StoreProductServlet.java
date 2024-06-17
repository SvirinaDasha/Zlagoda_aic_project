package m_vasyliev.ukma.zlagoda_ais.controller;

import m_vasyliev.ukma.zlagoda_ais.dao.ProductDAO;
import m_vasyliev.ukma.zlagoda_ais.dao.StoreProductDAO;
import m_vasyliev.ukma.zlagoda_ais.dto.StoreProductDetails;
import m_vasyliev.ukma.zlagoda_ais.model.Product;
import m_vasyliev.ukma.zlagoda_ais.model.StoreProduct;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@WebServlet("/store-products")
public class StoreProductServlet extends HttpServlet {
    private StoreProductDAO storeProductDAO = new StoreProductDAO();
    private ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
            sort = "products_number";  // Default sort by quantity
        }
        try {
            if (upcSearch != null && !upcSearch.isEmpty()) {
                searchStoreProduct(req, resp, upcSearch);
            } else {
                switch (action) {
                    case "new":
                        showNewForm(req, resp);
                        break;
                    case "insert":
                        insertStoreProduct(req, resp);
                        break;
                    case "delete":
                        deleteStoreProduct(req, resp, filter, sort);
                        break;
                    case "edit":
                        showEditForm(req, resp);
                        break;
                    case "update":
                        updateStoreProduct(req, resp);
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
