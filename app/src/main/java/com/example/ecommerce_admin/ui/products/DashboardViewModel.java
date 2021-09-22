package com.example.ecommerce_admin.ui.products;

public class DashboardViewModel  {

    private String productId;
    private int position;

    public DashboardViewModel(String productId,int position) {
        this.productId = productId;
        this.position = position;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}