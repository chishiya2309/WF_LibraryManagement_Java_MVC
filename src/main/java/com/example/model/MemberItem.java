package com.example.model;

/**
 * Model cho item thành viên để hiển thị trong combobox
 */
public class MemberItem {
    private String id;
    private String name;
    
    public MemberItem(String id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return name + " (" + id + ")";
    }
} 