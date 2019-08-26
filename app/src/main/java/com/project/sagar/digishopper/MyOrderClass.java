package com.project.sagar.digishopper;

public class MyOrderClass {
    String txtPrdId,txtPrdIdValue,order_id,order_id_value,order_address,order_address_value,order_status,order_status_value;

    public MyOrderClass(String txtPrdId, String txtPrdIdValue, String order_id, String order_id_value, String order_address, String order_address_value, String order_status, String order_status_value){
        this.txtPrdId=txtPrdId;
        this.txtPrdIdValue=txtPrdIdValue;
        this.order_id=order_id;
        this.order_id_value=order_id_value;
        this.order_address=order_address;
        this.order_address_value=order_address_value;
        this.order_status=order_status;
        this.order_address_value=order_status_value;
    }
    public String getTxtPrdId(){return txtPrdId;}

    public String getTxtPrdIdValue(){return txtPrdIdValue;}

    public String getOrder_id(){return order_id;}

    public String getOrder_id_value(){return order_id_value;}

    public String getOrder_address(){return order_address;}

    public String getOrder_address_value(){return order_address_value;}

    public String getOrder_status(){return order_status;}

    public String getOrder_status_value(){return order_status_value;}

}
