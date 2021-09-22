package com.example.ecommerce_admin.models;

import androidx.recyclerview.widget.RecyclerView;

public class OrderItemModel  {
    private String productID;
    private String orderID;
    private String orderStatus;
    private int quantity;
    private String orderPrice;


    public OrderItemModel(String productID, String orderID, String orderStatus, int quantity, String orderPrice) {
        this.productID = productID;
        this.orderID = orderID;
        this.orderStatus = orderStatus;
        this.quantity = quantity;
        this.orderPrice = orderPrice;
    }
    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }


    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }
}
