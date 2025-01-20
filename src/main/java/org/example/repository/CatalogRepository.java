package org.example.repository;

import org.example.entity.Catalog;

import java.util.List;
import java.util.UUID;

public interface CatalogRepository {
    public List<Catalog> getBasicCatalogs();
    public List<Catalog> getUserCatalogs(UUID userId);
    public boolean existsByName(UUID userId, String name);
    public Catalog getByName(UUID userId, String name);
    Catalog addUserCatalog(UUID userId, String name);
    public Catalog addToUser(UUID userId, String name);
    public void deleteByName(UUID userId, String name);
}