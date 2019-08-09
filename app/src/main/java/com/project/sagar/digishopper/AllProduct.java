package com.project.sagar.digishopper;

public class AllProduct {
    String productToAll;
    String product_base_price;
    String product_category;
    String product_desc;
    String product_id;
    String product_image;
    String product_name;

    public AllProduct(String productToAll, String product_base_price, String product_category, String product_desc, String product_id, String product_image, String product_name) {
        this.productToAll = productToAll;
        this.product_base_price = product_base_price;
        this.product_category = product_category;
        this.product_desc = product_desc;
        this.product_id = product_id;
        this.product_image = product_image;
        this.product_name = product_name;
    }

    public AllProduct(){}

    public String getProductToAll() {
        return productToAll;
    }

    public void setProductToAll(String productToAll) {
        this.productToAll = productToAll;
    }

    public String getProduct_base_price() {
        return product_base_price;
    }

    public void setProduct_base_price(String product_base_price) {
        this.product_base_price = product_base_price;
    }

    public String getProduct_category() {
        return product_category;
    }

    public void setProduct_category(String product_category) {
        this.product_category = product_category;
    }

    public String getProduct_desc() {
        return product_desc;
    }

    public void setProduct_desc(String product_desc) {
        this.product_desc = product_desc;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }
}
