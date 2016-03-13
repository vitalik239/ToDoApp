package com.example.vitalik.todolist;

public class Item {
    private String title;
    private String description;

    public Item() {
        title = "title";
        description = "description";
    }

    public Item(String t, String d) {
        title = t;
        description = d;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
