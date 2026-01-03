package m_vasyliev.ukma.zlagoda_ais.controller;

import m_vasyliev.ukma.zlagoda_ais.dao.CategoryDAO;
import m_vasyliev.ukma.zlagoda_ais.model.Category;
import m_vasyliev.ukma.zlagoda_ais.model.User;
import m_vasyliev.ukma.zlagoda_ais.utils.Validator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/categories")
public class CategoryServlet extends HttpServlet {
    private CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) {
            action = "";
        }
        try {
            switch (action) {
                case "new":
                case "insert":
                case "delete":
                case "edit":
                case "update":
                    if (!Validator.isManager(req)) {
                        resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied.");
                        return;
                    }
                    break;
            }
            switch (action) {
                case "new":
                    showNewForm(req, resp);
                    break;
                case "insert":
                    insertCategory(req, resp);
                    break;
                case "delete":
                    deleteCategory(req, resp);
                    break;
                case "edit":
                    showEditForm(req, resp);
                    break;
                case "update":
                    updateCategory(req, resp);
                    break;
                default:
                    listCategory(req, resp);
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

    private void listCategory(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException, ServletException {
        List<Category> listCategory = categoryDAO.getAllCategories();
        req.setAttribute("listCategory", listCategory);
        req.getRequestDispatcher("/category-list.jsp").forward(req, resp);
    }

    private void showNewForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("category", null);
        req.getRequestDispatcher("/category-form.jsp").forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Category existingCategory = categoryDAO.getCategoryById(id);
        req.setAttribute("category", existingCategory);
        req.getRequestDispatcher("/category-form.jsp").forward(req, resp);
    }

    private void insertCategory(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException {
        String categoryName = new String(req.getParameter("categoryName").getBytes("ISO-8859-1"), "UTF-8");
        int maxCategoryNumber = categoryDAO.getMaxCategoryNumber();
        int newCategoryNumber = maxCategoryNumber + 1;
        Category newCategory = new Category(newCategoryNumber, categoryName);
        categoryDAO.addCategory(newCategory);
        resp.sendRedirect("categories");
    }

    private void updateCategory(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException {
        int id = Integer.parseInt(req.getParameter("categoryId"));
        String categoryName = new String(req.getParameter("categoryName").getBytes("ISO-8859-1"), "UTF-8");
        Category category = new Category(id, categoryName);
        categoryDAO.updateCategory(category);
        resp.sendRedirect("categories");
    }

    private void deleteCategory(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        categoryDAO.deleteCategory(id);
        resp.sendRedirect("categories");
    }


}
