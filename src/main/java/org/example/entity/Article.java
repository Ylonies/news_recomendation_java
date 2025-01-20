package org.example.entity;

import java.util.UUID;

import java.util.UUID;

public class Article {
    private final UUID id;
    private final String name;
    private final String description;
    private final String date;
    private final String link;
    private UUID catalogID; // Поле catalogID теперь изменяемое
    private UUID websiteID;

    public Article(UUID id, String name, String description, String date, String link) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
        this.link = link;
        this.catalogID = null;
        this.websiteID = null;// По умолчанию catalogID равен null
    }


    public Article(UUID id, String name, String description, String date, String link, UUID catalogID) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
        this.link = link;
        this.catalogID = catalogID;
    }

    public UUID id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public String date() {
        return date;
    }

    public String link() {
        return link;
    }

    public UUID catalogID() {
        return catalogID;
    }

    public UUID websiteID(){
        return websiteID;
    }

    public void setCatalogID(UUID catalogID) {
        this.catalogID = catalogID;
    }

    public void setWebsiteID(UUID websiteID) {
        this.websiteID = websiteID;
    }
}