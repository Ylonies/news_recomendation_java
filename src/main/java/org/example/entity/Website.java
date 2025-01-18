package org.example.entity;

import java.util.UUID;

public class Website {
    private UUID id;
    private String url;
    private UUID userId;

    public Website(UUID id, String url, UUID userId){
        this.id = id;
        this.url = url;
        this.userId = userId;
    }
    public String getUrl(){
        return url;
    }
    public UUID getUserId() {return userId;}
}
