package org.example.repository.interfaces;

import org.example.entity.Catalog;

import java.util.List;
import java.util.UUID;

public interface CatalogRepository {
    List<Catalog> getBasicCatalogs();
    List<Catalog> getUserCatalogs(UUID userId);
    boolean existsByName(UUID userId, String name);
    Catalog getByName(UUID userId, String name);
    Catalog addUserCatalog(UUID userId, String name);
    Catalog addToUser(UUID userId, String name);
    void deleteByName(UUID userId, String name);
}