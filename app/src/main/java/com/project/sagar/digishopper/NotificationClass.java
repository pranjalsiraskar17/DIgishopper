package com.project.sagar.digishopper;

public class NotificationClass {
    public String order_status;
    public long nft_timestamp;

    public NotificationClass(){

    }
    public NotificationClass(String order_status,long nft_timestamp){
        this.order_status=order_status;
        this.nft_timestamp=nft_timestamp;
    }

    public String getOrder_status(){return order_status;}

    public void setOrder_status(String order_status){this.order_status=order_status;}

    public long getTimestamp(){return nft_timestamp;}

    public void setTimestamp(long nft_timestamp){this.nft_timestamp=nft_timestamp;}


}
