package m_vasyliev.ukma.zlagoda_ais.controller;

import m_vasyliev.ukma.zlagoda_ais.dao.ProductDAO;
import m_vasyliev.ukma.zlagoda_ais.dao.CategoryDAO;
import m_vasyliev.ukma.zlagoda_ais.model.Category;
import m_vasyliev.ukma.zlagoda_ais.model.Product;
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

@WebServlet("/products")
public class ProductServlet extends HttpServlet {
    private ProductDAO productDAO = new ProductDAO();
    private CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        String role = user.getRole();

        if(action == null) {
            action = "";
        }
        try {
            switch (action) {
                case "new":
                    if (!"Manager".equals(role)) {
                        resp.sendRedirect("products");
                    } else {
                        showNewForm(req, resp);
                    }
                    break;
                case "insert":
                    if (!"Manager".equals(role)) {
                        resp.sendRedirect("products");
                    } else {
                        insertProduct(req, resp);
                    }
                    break;
                case "delete":
                    if (!"Manager".equals(role)) {
                        resp.sendRedirect("products");
                    } else {
                        deleteProduct(req, resp);
                    }
                    break;
                case "edit":
                    if (!"Manager".equals(role)) {
                        resp.sendRedirect("products");
                    } else {
                        showEditForm(req, resp);
                    }
                    break;
                case "update":
                    if (!"Manager".equals(role)) {
                        resp.sendRedirect("products");
                    } else {
                        updateProduct(req, resp);
                    }
                    break;
                case "checkConnection":
                    checkDatabaseConnection(req, resp);
                    break;
                default:
                    listProduct(req, resp);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    private void listProduct(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException, ServletException {
        String search = req.getParameter("search");
        String category = req.getParameter("category");
        List<Product> listProduct;
        if (search != null && !search.isEmpty()) {
            if (category != null && !category.equals("-1")) {
                int categoryNumber = Integer.parseInt(category);
                listProduct = productDAO.searchProductsByNameAndCategoryWithCategoryName(search, categoryNumber);
            } else {
                listProduct = productDAO.searchProductsByNameWithCategoryName(search);
            }
        } else if (category != null && !category.equals("-1")) {
            int categoryNumber = Integer.parseInt(category);
            listProduct = productDAO.getProductsByCategoryWithCategoryName(categoryNumber);
        } else {
            listProduct = productDAO.getAllProductsWithCategory();
        }

        List<Category> listCategory = categoryDAO.getAllCategories();
        req.setAttribute("listProduct", listProduct);
        req.setAttribute("listCategory", listCategory);
        req.setAttribute("selectedCategory", category);
        req.getRequestDispatcher("/products.jsp").forward(req, resp);
    }


    private void showNewForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException, SQLException {
        List<Category> listCategory = categoryDAO.getAllCategories();
        req.setAttribute("listCategory", listCategory);
        req.getRequestDispatcher("/product-form.jsp").forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Product existingProduct = productDAO.getProductById(id);
        List<Category> listCategory = categoryDAO.getAllCategories();
        req.setAttribute("product", existingProduct);
        req.setAttribute("listCategory", listCategory);
        req.getRequestDispatcher("/product-form.jsp").forward(req, resp);
    }

    private void insertProduct(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException {
        int categoryNumber = Integer.parseInt(req.getParameter("categoryNumber"));
        String productName = new String(req.getParameter("productName").getBytes("ISO-8859-1"), "UTF-8");
        String characteristics = new String(req.getParameter("characteristics").getBytes("ISO-8859-1"), "UTF-8");

        Product newProduct = new Product(categoryNumber, productName, characteristics);
        productDAO.addProduct(newProduct);
        resp.sendRedirect("products?action=list");
    }


    private void updateProduct(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        int categoryNumber = Integer.parseInt(req.getParameter("categoryNumber"));
        String productName = new String(req.getParameter("productName").getBytes("ISO-8859-1"), "UTF-8");
        String characteristics = new String(req.getParameter("characteristics").getBytes("ISO-8859-1"), "UTF-8");

        Product product = new Product(id, categoryNumber, productName, characteristics);
        productDAO.updateProduct(product);
        resp.sendRedirect("products");
    }

    private void deleteProduct(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        productDAO.deleteProduct(id);
        resp.sendRedirect("products");
    }

    private void checkDatabaseConnection(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        boolean isConnected = productDAO.checkConnection();
        resp.getWriter().write("Database connection is " + (isConnected ? "successful" : "failed"));
    }
}
