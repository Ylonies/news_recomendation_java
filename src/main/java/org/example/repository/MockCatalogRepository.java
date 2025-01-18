package org.example.repository;

import org.example.entity.Catalog;

import java.util.*;

public class MockCatalogRepository implements CatalogRepository{
    // Хранение каталогов пользователей в формате: userId -> List<Catalog>
    private final Map<UUID, List<Catalog>> userCatalogs = new HashMap<>();
    private final List<Catalog> basicCatalogs = new ArrayList<>(Arrays.asList(
            new Catalog("DevOps"),
                new Catalog("Backend")
        ));

    // Метод для получения базовых каталогов
    @Override
    public List<Catalog> getBasicCatalogs() {
        return basicCatalogs;
    }

    // Метод для получения каталогов пользователя
    @Override
    public List<Catalog> getUserCatalogs(UUID userId) {
        return userCatalogs.getOrDefault(userId, new ArrayList<>());
    }

    // Метод для проверки существования каталога по имени
    @Override
    public boolean existsByName(UUID userId, String name) {
        List<Catalog> catalogs = userCatalogs.get(userId);
        if (catalogs != null) {
            return catalogs.stream().anyMatch(catalog -> catalog.getName().equals(name));
        }
        return false;
    }

    // Метод для получения каталога по имени
    @Override
    public Catalog getByName(UUID userId, String name) {
        List<Catalog> catalogs = userCatalogs.get(userId);
        if (catalogs != null) {
            return catalogs.stream()
                    .filter(catalog -> catalog.getName().equals(name))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }


    // Метод для добавления каталога по имени
    @Override
    public Catalog addByName(UUID userId, String name) {
        Catalog newCatalog = new Catalog(name);
        userCatalogs.computeIfAbsent(userId, k -> new ArrayList<>()).add(newCatalog);
        return newCatalog;
    }

    // Метод для удаления каталога по имени
    @Override
    public void deleteByName(UUID userId, String name) {
        List<Catalog> catalogs = userCatalogs.get(userId);
        if (catalogs != null) {
            catalogs.removeIf(catalog -> catalog.getName().equals(name));
        }
    }
}