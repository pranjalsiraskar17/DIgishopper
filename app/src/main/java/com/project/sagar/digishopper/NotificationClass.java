package com.project.sagar.digishopper;

public class NotificationClass {
    public String order_status,nft_buyer_name,isViewed;
    public long nft_timestamp;


    public NotificationClass(){

    }
    public NotificationClass(String nft_buyer_name,String order_status,long nft_timestamp,String isViewed){
        this.nft_buyer_name=nft_buyer_name;
        this.order_status=order_status;
        this.nft_timestamp=nft_timestamp;
        this.isViewed=isViewed;
    }

    public String getNft_buyer_name(){return nft_buyer_name;}

    public String getOrder_status(){return order_status;}

    public void setOrder_status(String order_status){this.order_status=order_status;}

    public long getTimestamp(){return nft_timestamp;}

    public void setTimestamp(long nft_timestamp){this.nft_timestamp=nft_timestamp;}

    public String getIsViewed(){return isViewed;}

    public void setIsViewed(String isViewed){this.isViewed=isViewed;}

}
