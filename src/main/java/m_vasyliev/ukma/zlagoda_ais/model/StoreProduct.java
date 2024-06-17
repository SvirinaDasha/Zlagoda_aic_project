package m_vasyliev.ukma.zlagoda_ais.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class StoreProduct {
    private String upc;
    private String upcProm;
    private int productId;
    private double sellingPrice;
    private int productsNumber;
    private boolean promotionalProduct;


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
        this.sellingPrice = round(sellingPrice, 2);
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

    public double getPromotionalPrice() {
        return round(this.sellingPrice * 0.8, 2);
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
