package m_vasyliev.ukma.zlagoda_ais.model;

public class Product {
    private int id;
    private int categoryNumber;
    private String productName;
    private String characteristics;

    // Конструктор без параметрів
    public Product() {}

    // Конструктор з параметрами без id (для створення нового продукту)
    public Product(int categoryNumber, String productName, String characteristics) {
        this.categoryNumber = categoryNumber;
        this.productName = productName;
        this.characteristics = characteristics;
    }

    // Конструктор з параметрами з id (для існуючого продукту)
    public Product(int id, int categoryNumber, String productName, String characteristics) {
        this.id = id;
        this.categoryNumber = categoryNumber;
        this.productName = productName;
        this.characteristics = characteristics;
    }

    // Геттери та сеттери
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
}
