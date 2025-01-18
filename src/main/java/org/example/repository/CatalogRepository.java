package org.example.repository;

import org.example.entity.Catalog;

import java.util.List;
import java.util.UUID;

public interface CatalogRepository {
    public List<Catalog> getBasicCatalogs();
    public List<Catalog> getUserCatalogs(UUID userId);

    public boolean existsByName(UUID userId, String name);
    public boolean existsInBasicByName(String name);
    public Catalog getByName(UUID userId, String name);
    public Catalog addByName(UUID userId, String name);
    public void postByName(UUID userId, String name);
    public void deleteByName(UUID userId, String name);
}
