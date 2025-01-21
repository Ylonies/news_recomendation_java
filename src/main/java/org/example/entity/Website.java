package org.example.entity;

import java.util.UUID;

public class Website {
    private final UUID id;
    private final String name;
    private final String url;
    private final UUID userId;

    public Website(UUID id, String name, String url, UUID userId){
        this.id = id;
        this.url = url;
        this.userId = userId;
        this.name = name;
    }
    public String getUrl(){
        return url;
    }
    public String getName(){return name;}
    public UUID getUserId() {return userId;}
    public UUID getId() {return id;}
}