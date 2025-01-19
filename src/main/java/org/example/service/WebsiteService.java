package org.example.service;

import org.example.entity.Response;
import org.example.entity.User;
import org.example.entity.Website;
import org.example.repository.WebsiteRepository;
import org.example.repository.WebsiteRepositoryImpl;
import spark.Request;

import java.util.ArrayList;
import java.util.List;

public class WebsiteService  extends Service{
    private final WebsiteRepository websiteRepository = new WebsiteRepositoryImpl();


    public Response<List<String>> getBasicWebsites(Request request) {
        if (!authService.isAuthenticated(request)){
            return new Response<>(401, "Unauthorized");
        }
        List<Website> websites = websiteRepository.getBasicWebsites();
        List<String> names = new ArrayList<>();
        try {
            for (Website website : websites) {
                names.add(website.getUrl()); // Изменено на getUrl()
            }
            return new Response<>(names);
        } catch (Exception e) {
            return new Response<>(500);
        }
    }

    public Response<List<String>> getUserWebsites(Request request) {

        if (!authService.isAuthenticated(request)) {
            return new Response<>(401, "Unauthorized");
        }
        User currentUser  = authService.getUser(request);
        List<Website> websites = websiteRepository.getUserWebsites(currentUser .getId());
        List<String> names = new ArrayList<>();
        try {
            for (Website website : websites) {
                names.add(website.getUrl()); // Изменено на getUrl()
            }
            return new Response<>(names);
        } catch (Exception e) {
            return new Response<>(500, "Internal Error");
        }
    }

    public Response<Website> getByName(Request request, String name) {

        if (!authService.isAuthenticated(request)) {
            return new Response<>(401, "Unauthorized");
        }

        User currentUser  = authService.getUser(request);
        if (!websiteRepository.addedByName(currentUser.getId(), name)) {
            return new Response<>(409, "There is no added website with this name"); // Conflict
        }
        try {
            Website website = websiteRepository.getByName(currentUser.getId(), name);
            return new Response<>(website);
        } catch (Exception e) {
            return new Response<>(500);
        }
    }

    public Response<Website> addBasicByName(Request request) {
        if (!authService.isAuthenticated(request)) {
            return new Response<>(401, "Unauthorized");
        }

        String name = request.params(":name");
        User currentUser  = authService.getUser(request);
        if (websiteRepository.addedByName(currentUser.getId(), name)) {
            return new Response<>(409, "Website is already added");
        }
        try {
            if (!websiteRepository.existsByName(name)) {
                return new Response<>(409, "Website is not in our list");
            }
        } catch (Exception e) {
            return new Response<>(500, "Internal server error while checking website existence");
        }
        try {
            Website website = websiteRepository.addBasicToPreferences(currentUser.getId(), name);
            return new Response<>(website);
        } catch (Exception e) {
            return new Response<>(500);
        }
    }

    public Response<Website> addCustom(Request request){
        if (!authService.isAuthenticated(request)) {
            return new Response<>(401, "Unauthorized");
        }
        User currentUser  = authService.getUser(request);

        String name = request.queryParams("name");
        String url = request.queryParams("url");

        if (name == null || url == null) {
            return new Response<>(400, "Name and url is required");
        }
        try {
            Website website = websiteRepository.addCustom(currentUser.getId(), name, url);
            return new Response<>(website);
        } catch (Exception e) {
            return new Response<>(500, "Internal server error adding to your websites");
        }
    }
    //TODO delete
}