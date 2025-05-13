package com.example.model;

/**
 * Model cho item sách phổ biến
 */
public class PopularBookItem {
    private int orderNumber;
    private String bookId;
    private String bookTitle;
    private int borrowCount;
    
    public PopularBookItem(int orderNumber, String bookId, String bookTitle, int borrowCount) {
        this.orderNumber = orderNumber;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.borrowCount = borrowCount;
    }
    
    // Getters
    public int getOrderNumber() {
        return orderNumber;
    }
    
    public String getBookId() {
        return bookId;
    }
    
    public String getBookTitle() {
        return bookTitle;
    }
    
    public int getBorrowCount() {
        return borrowCount;
    }
    
    // Setters
    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }
    
    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
    
    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }
    
    public void setBorrowCount(int borrowCount) {
        this.borrowCount = borrowCount;
    }
} 