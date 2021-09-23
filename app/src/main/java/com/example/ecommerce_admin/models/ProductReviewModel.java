package com.example.ecommerce_admin.models;

public class ProductReviewModel {
    private String buyerId;
    private String buyerName;
    private int rating;
    private String review;
    private String reviewDate;

    public ProductReviewModel(String buyerId, String buyerName, int rating, String review, String reviewDate) {
        this.buyerId = buyerId;
        this.buyerName = buyerName;
        this.rating = rating;
        this.review = review;
        this.reviewDate = reviewDate;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerNAme(String buyerNAme) {
        this.buyerName = buyerName;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }
}
