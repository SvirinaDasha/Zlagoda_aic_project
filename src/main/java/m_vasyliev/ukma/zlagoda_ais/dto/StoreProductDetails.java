package m_vasyliev.ukma.zlagoda_ais.dto;

public class StoreProductDetails {
    private String upc;
    private String upcProm;
    private int productId;
    private double sellingPrice;
    private int productsNumber;
    private boolean promotionalProduct;
    private String productName;
    private String characteristics;

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getUpcProm() {
        return upcProm;
    }

    public void setUpcProm(String upcProm) {
        this.upcProm = upcProm;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public int getProductsNumber() {
        return productsNumber;
    }

    public void setProductsNumber(int productsNumber) {
        this.productsNumber = productsNumber;
    }

    public boolean isPromotionalProduct() {
        return promotionalProduct;
    }

    public void setPromotionalProduct(boolean promotionalProduct) {
        this.promotionalProduct = promotionalProduct;
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
