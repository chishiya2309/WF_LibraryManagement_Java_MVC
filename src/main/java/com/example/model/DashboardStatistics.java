package com.example.model;

public class DashboardStatistics {
    private int totalAvailableBooks;
    private int totalMembers;
    private int totalStaff;
    private int booksBorrowedToday;
    private int booksReturnedToday;
    private int overDueBooks;
    
    // Constructor mặc định
    public DashboardStatistics() {
        this.totalAvailableBooks = 0;
        this.totalMembers = 0;
        this.totalStaff = 0;
        this.booksBorrowedToday = 0;
        this.booksReturnedToday = 0;
        this.overDueBooks = 0;
    }
    
    // Constructor với tham số
    public DashboardStatistics(int totalAvailableBooks, int totalMembers, int totalStaff, 
                            int booksBorrowedToday, int booksReturnedToday, int overDueBooks) {
        this.totalAvailableBooks = totalAvailableBooks;
        this.totalMembers = totalMembers;
        this.totalStaff = totalStaff;
        this.booksBorrowedToday = booksBorrowedToday;
        this.booksReturnedToday = booksReturnedToday;
        this.overDueBooks = overDueBooks;
    }
    
    // Getters và Setters
    public int getTotalAvailableBooks() {
        return totalAvailableBooks;
    }
    
    public void setTotalAvailableBooks(int totalAvailableBooks) {
        this.totalAvailableBooks = totalAvailableBooks;
    }
    
    public int getTotalMembers() {
        return totalMembers;
    }
    
    public void setTotalMembers(int totalMembers) {
        this.totalMembers = totalMembers;
    }
    
    public int getTotalStaff() {
        return totalStaff;
    }
    
    public void setTotalStaff(int totalStaff) {
        this.totalStaff = totalStaff;
    }
    
    public int getBooksBorrowedToday() {
        return booksBorrowedToday;
    }
    
    public void setBooksBorrowedToday(int booksBorrowedToday) {
        this.booksBorrowedToday = booksBorrowedToday;
    }
    
    public int getBooksReturnedToday() {
        return booksReturnedToday;
    }
    
    public void setBooksReturnedToday(int booksReturnedToday) {
        this.booksReturnedToday = booksReturnedToday;
    }
    
    public int getOverDueBooks() {
        return overDueBooks;
    }
    
    public void setOverDueBooks(int overDueBooks) {
        this.overDueBooks = overDueBooks;
    }
} 