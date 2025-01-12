package org.example.dto;

import java.util.ArrayList;
import java.util.List;

public class Catalog {
    private String name_;
    private List<Website> websites_;

    public Catalog(String name){
        name_ = name;
        websites_ = new ArrayList<>();
    }
    public String getName() {
        return name_;
    }
    public List<Website> getWebsites(){
        return websites_;
    }
}
