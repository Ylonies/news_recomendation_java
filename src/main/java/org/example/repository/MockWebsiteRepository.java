package org.example.repository;

import org.example.entity.Website;

import java.util.List;
import java.util.UUID;

import java.util.*;

public class MockWebsiteRepository implements WebsiteRepository {
    // Хранение веб-сайтов пользователей в формате: userId -> List<Website>
    private final Map<UUID, List<Website>> userWebsites = new HashMap<>();
    List<Website> basicWebsites = List.of(
            new Website(UUID.randomUUID(), "DevOps", "https://DevOps.com", null),
            new Website(UUID.randomUUID(), "BackEnd", "https://Backend.org", null)
    );
    @Override
    public List<Website> getBasicWebsites() {
        return basicWebsites;
    }


    @Override
    public List<Website> getUserWebsites(UUID userId) {
        return userWebsites.getOrDefault(userId, new ArrayList<>());
    }

    @Override
    public boolean addedByName(UUID userId, String name) {
        List<Website> websites = userWebsites.get(userId);
        if (websites != null) {
            return websites.stream().anyMatch(website -> website.getUrl().equals(name));
        }
        return false;
    }

    @Override
    public boolean existsByName(String name) {
        return false;
    }

    @Override
    public Website getByName(UUID userId, String name) {
        List<Website> websites = userWebsites.get(userId);
        if (websites != null) {
            return websites.stream()
                    .filter(website -> website.getUrl().equals(name))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    @Override
    public Website addToUser(UUID userId, String name) {
        Website newWebsite = new Website(UUID.randomUUID(), name, name+".org", userId);
        userWebsites.computeIfAbsent(userId, k -> new ArrayList<>()).add(newWebsite);
        return newWebsite;
    }

    @Override
    public void deleteByName(UUID userId, String name) {
        List<Website> websites = userWebsites.get(userId);
        if (websites != null) {
            websites.removeIf(website -> website.getUrl().equals(name));
        }
    }
}
