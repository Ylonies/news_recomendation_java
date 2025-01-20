package org.example.service;

import org.example.entity.Catalog;
import org.example.entity.Response;
import org.example.entity.User;
import org.example.repository.CatalogRepository;
import org.example.repository.CatalogRepositoryImpl;
import spark.Request;

import java.util.ArrayList;
import java.util.List;

public class CatalogService {
    private final CatalogRepository catalogRepository = new CatalogRepositoryImpl();
    private final AuthenticationService authService = new AuthenticationService();

    public Response<List<String>> getBasicCatalogs(Request request){
        if (!authService.isAuthenticated(request)) {
            return new Response<>(401, "Unauthorized");
        }

        List<Catalog> catalogs = catalogRepository.getBasicCatalogs();
        List<String> names = new ArrayList<>();
        try {
            for (Catalog person : catalogs) {
                names.add(person.getName());
            }
            return new Response<>(names);
        }
        catch (Exception e) {
            return new Response<>(500);
        }
    }

    public Response<List<String>> getUserCatalogs(Request request){
        if (!authService.isAuthenticated(request)) {
            return new Response<>(401, "Unauthorized");
        }

        User currentUser = authService.getUser(request);
        List<Catalog> catalogs = catalogRepository.getUserCatalogs(currentUser.getId());
        List<String> names = new ArrayList<>();
        try {
            for (Catalog person : catalogs) {
                names.add(person.getName());
            }
            return new Response<>(names);
        }
        catch (Exception e) {
            return new Response<>(500, "Internal Error");
        }
    }

    public Response<Catalog> getCatalog(Request request){
        if (!authService.isAuthenticated(request)) {
            return new Response<>(401, "Unauthorized");
        }
        String name = request.params(":name");

        User currentUser = authService.getUser(request);
        if (!catalogRepository.existsByName(currentUser.getId(), name)) {
            return new Response<>(409, "there is no catalog with this name"); // Conflict
        }
        try {
            Catalog catalog = catalogRepository.getByName(currentUser.getId(), name);
            return new Response<>(catalog); // Created
        } catch (Exception e) {
            return new Response<>(500);
        }
    }

    public Response<Catalog> addCatalog(Request request){
        if (!authService.isAuthenticated(request)) {
            return new Response<>(401, "Unauthorized");
        }
        String name = request.params(":name");


        User currentUser = authService.getUser(request);
        if (catalogRepository.existsByName(currentUser.getId(), name)) {
            return new Response<>(409, "Catalog is already added"); // Conflict
        }
        try {
            Catalog catalog = catalogRepository.addToUser(currentUser.getId(), name);
            return new Response<>(catalog); // Created
        } catch (Exception e) {
            return new Response<>(500);
        }
    }

    public int deleteCatalog(){
        //TODO
        return 400;
    }
}
