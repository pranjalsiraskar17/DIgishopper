package com.project.sagar.digishopper;

public class NotificationClass {
    public String order_status;
    public long timestamp;

    public NotificationClass(){

    }
    public NotificationClass(String order_status,long timestamp){
        this.order_status=order_status;
        this.timestamp=timestamp;
    }

    public String getOrder_status(){return order_status;}

    public void setOrder_status(String order_status){this.order_status=order_status;}

    public long getTimestamp(){return timestamp;}

    public void setTimestamp(long timestamp){this.timestamp=timestamp;}


}
