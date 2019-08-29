package com.project.sagar.digishopper;

public class MyProductInOrderClass {

   public String order_status,prd_id;
    long txn_timestamp;
    int product_qty;
    public MyProductInOrderClass(){
    }

    public MyProductInOrderClass(String order_status,String prd_id,int product_qty,long txn_timestamp){
        this.order_status=order_status;
        this.prd_id=prd_id;
        this.product_qty=product_qty;
        this.txn_timestamp=txn_timestamp;
    }

    public String getPrd_id(){return prd_id;}

    public String getOrder_status(){return  order_status;}

    public int getProduct_qty(){return product_qty;}

    public long getTxn_timestamp(){return txn_timestamp;}

}
