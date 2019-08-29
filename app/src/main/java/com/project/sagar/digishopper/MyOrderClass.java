package com.project.sagar.digishopper;

public class MyOrderClass {
    public String prd_id,txn_id,order_status;

    public MyOrderClass(){
    }

    public MyOrderClass(String prd_id, String txn_id, String order_status){
        this.prd_id=prd_id;
        this.txn_id=txn_id;
        this.order_status=order_status;
    }

    public String getPrd_id(){return prd_id;}

    public String getTxn_id(){return txn_id;}

    public String getOrder_status(){return order_status;}

}
