package org.example.repository;

import org.example.entity.Catalog;
import org.example.entity.Website;

import java.util.List;
import java.util.UUID;

public interface WebsiteRepository {
    public List<Website> getBasicWebsites();
    public List<Website> getUserWebsites(UUID userId);
    public boolean existsByName(UUID userId, String name);
    public Website getByName(UUID userId, String name);
    public Website addByName(UUID userId, String name);
    public void deleteByName(UUID userId, String name);
}
