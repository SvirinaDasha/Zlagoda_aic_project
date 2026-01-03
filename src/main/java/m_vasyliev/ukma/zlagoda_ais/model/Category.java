package m_vasyliev.ukma.zlagoda_ais.model;

public class Category {
    private int categoryNumber;
    private String categoryName;

    public Category(int id, String name) {
        categoryNumber = id;
        categoryName = name;
    }

    public Category() {

    }

    public int getCategoryNumber() {
        return categoryNumber;
    }

    public void setCategoryNumber(int categoryNumber) {
        this.categoryNumber = categoryNumber;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}

