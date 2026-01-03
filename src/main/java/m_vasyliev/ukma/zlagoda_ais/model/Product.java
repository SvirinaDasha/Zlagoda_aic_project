package m_vasyliev.ukma.zlagoda_ais.model;

public class Product {
    private int id;
    private int categoryNumber;
    private String productName;
    private String characteristics;
    private String categoryName;

    public Product() {}

    public Product(int categoryNumber, String productName, String characteristics) {
        this.categoryNumber = categoryNumber;
        this.productName = productName;
        this.characteristics = characteristics;
    }

    public Product(int id, int categoryNumber, String productName, String characteristics) {
        this.id = id;
        this.categoryNumber = categoryNumber;
        this.productName = productName;
        this.characteristics = characteristics;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryNumber() {
        return categoryNumber;
    }

    public void setCategoryNumber(int categoryNumber) {
        this.categoryNumber = categoryNumber;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(String characteristics) {
        this.characteristics = characteristics;
    }
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
