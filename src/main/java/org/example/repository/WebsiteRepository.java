package org.example.repository;

import org.example.entity.Catalog;
import org.example.entity.Website;

import java.util.List;
import java.util.UUID;

public interface WebsiteRepository {
    public List<Website> getBasicWebsites();
    public List<Website> getUserWebsites(UUID userId);
    public boolean addedByName(UUID userId, String name); // добавлен пользователем
    public boolean existsByName(String name); //существует в basic
    public Website getByName(UUID userId, String name);
    public Website addToUser(UUID userId, String name);
    public void deleteByName(UUID userId, String name);
    public Website addUserWebsite(UUID userId, String name, String url);
}
