package org.example.entity;

public class Catalog {
    private int catalogId;
    private String name;
    private int userId;

    public Catalog(String name){
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
