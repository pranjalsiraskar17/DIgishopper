package com.project.sagar.digishopper;

public class MyProductInOrderClass {

   public String order_status,prd_id;
    int product_qty;
    public String product_price;
    public MyProductInOrderClass(){
    }

    public MyProductInOrderClass(String prd_id,int product_qty,String product_price){
        this.prd_id=prd_id;
        this.product_qty=product_qty;
        this.product_price=product_price;
    }

    public String getPrd_id(){return prd_id;}


    public int getProduct_qty(){return product_qty;}

    public String getProduct_price(){return product_price;}

}
