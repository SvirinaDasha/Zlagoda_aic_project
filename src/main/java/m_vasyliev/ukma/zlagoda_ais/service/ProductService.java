package m_vasyliev.ukma.zlagoda_ais.service;

import m_vasyliev.ukma.zlagoda_ais.dao.CategoryDAO;
import m_vasyliev.ukma.zlagoda_ais.dao.ProductDAO;
import m_vasyliev.ukma.zlagoda_ais.model.Category;
import m_vasyliev.ukma.zlagoda_ais.model.Product;

import java.sql.SQLException;
import java.util.List;

public class ProductService {
    private ProductDAO productDAO = new ProductDAO();
    private CategoryDAO categoryDAO = new CategoryDAO();

    public List<Product> getAllProducts() throws SQLException {
        return productDAO.getAllProducts();
    }

    public Product getProductById(int id) throws SQLException {
        return productDAO.getProductById(id);
    }

    public void addProduct(Product product) throws SQLException {
        productDAO.addProduct(product);
    }

    public void updateProduct(Product product) throws SQLException {
        productDAO.updateProduct(product);
    }

    public void deleteProduct(int id) throws SQLException {
        productDAO.deleteProduct(id);
    }

    public List<Category> getAllCategories() throws SQLException {
        return categoryDAO.getAllCategories();
    }
}

