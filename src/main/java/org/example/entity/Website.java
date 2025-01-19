package org.example.entity;

import java.util.UUID;

public class Website {
    private UUID id;
    private String name;
    private String url;
    private UUID userId;

    public Website(UUID id, String name, String url, UUID userId){
        this.id = id;
        this.url = url;
        this.userId = userId;
    }
    public String getUrl(){
        return url;
    }
    public UUID getUserId() {return userId;}
    public UUID getId() {return id;}
    public String getName() {return name;}
}
