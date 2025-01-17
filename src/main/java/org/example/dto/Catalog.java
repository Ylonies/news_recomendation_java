package org.example.dto;

import java.util.ArrayList;
import java.util.List;

public class Catalog {
    private int id;
    private String name;

    public Catalog(String name){
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
