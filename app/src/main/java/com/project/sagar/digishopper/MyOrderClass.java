package com.project.sagar.digishopper;

public class MyOrderClass {
    public String prd_id,txn_id,order_status;
    int txt_amt;

    public MyOrderClass(){
    }

    public MyOrderClass(String prd_id, String txn_id, String order_status, int txt_amt) {
        this.prd_id = prd_id;
        this.txn_id = txn_id;
        this.order_status = order_status;
        this.txt_amt = txt_amt;
    }

    public String getPrd_id() {
        return prd_id;
    }

    public void setPrd_id(String prd_id) {
        this.prd_id = prd_id;
    }

    public String getTxn_id() {
        return txn_id;
    }

    public void setTxn_id(String txn_id) {
        this.txn_id = txn_id;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public int getTxt_amt() {
        return txt_amt;
    }

    public void setTxt_amt(int txt_amt) {
        this.txt_amt = txt_amt;
    }
}
