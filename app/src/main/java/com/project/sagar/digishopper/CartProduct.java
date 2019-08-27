package com.project.sagar.digishopper;

public class CartProduct {
    String product_id;
    long timestamp;
    int product_qty;

    public CartProduct(String product_id, long timestamp, int product_qty) {
        this.product_id = product_id;
        this.timestamp = timestamp;
        this.product_qty = product_qty;
    }

    public CartProduct() {
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getProduct_qty() {
        return product_qty;
    }

    public void setProduct_qty(int product_qty) {
        this.product_qty = product_qty;
    }
}
