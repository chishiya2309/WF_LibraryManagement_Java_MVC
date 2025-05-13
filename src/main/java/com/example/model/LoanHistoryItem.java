package com.example.model;

import java.util.Date;

/**
 * Model cho item lịch sử mượn sách
 */
public class LoanHistoryItem {
    private int loanId;
    private String memberId;
    private String memberName;
    private String bookId;
    private String bookTitle;
    private Date loanDate;
    private Date dueDate;
    private Date returnDate;
    private String status;
    private int quantity;
    
    public LoanHistoryItem(int loanId, String memberId, String memberName, String bookId, String bookTitle,
                          Date loanDate, Date dueDate, Date returnDate, String status, int quantity) {
        this.loanId = loanId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
        this.quantity = quantity;
    }
    
    // Getters
    public int getLoanId() {
        return loanId;
    }
    
    public String getMemberId() {
        return memberId;
    }
    
    public String getMemberName() {
        return memberName;
    }
    
    public String getBookId() {
        return bookId;
    }
    
    public String getBookTitle() {
        return bookTitle;
    }
    
    public Date getLoanDate() {
        return loanDate;
    }
    
    public Date getDueDate() {
        return dueDate;
    }
    
    public Date getReturnDate() {
        return returnDate;
    }
    
    public String getStatus() {
        return status;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    // Setters
    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }
    
    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
    
    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
    
    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
    
    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }
    
    public void setLoanDate(Date loanDate) {
        this.loanDate = loanDate;
    }
    
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
    
    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
} 