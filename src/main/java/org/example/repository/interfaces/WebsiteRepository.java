package org.example.repository.interfaces;

import org.example.entity.Website;

import java.util.List;
import java.util.UUID;

public interface WebsiteRepository {
    List<Website> getBasicWebsites();
    List<Website> getUserWebsites(UUID userId);
    boolean addedByName(UUID userId, String name); // добавлен пользователем
    boolean existsByName(String name); //существует в basic
    Website getByName(UUID userId, String name);
    Website addToUser(UUID userId, String name);
    void deleteByName(UUID userId, String name);
    Website addUserWebsite(UUID userId, String name, String url);
}
