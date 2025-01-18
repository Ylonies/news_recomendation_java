package org.example.service;

import org.example.entity.Catalog;
import org.example.entity.Response;
import org.example.entity.User;
import org.example.repository.CatalogRepository;
import org.example.repository.MockCatalogRepository;
import spark.Request;

import java.util.ArrayList;
import java.util.List;

public class CatalogService {
    private CatalogRepository catalogRepository = new MockCatalogRepository();

    private User getCurrentUser (Request request) {
        return request.session().attribute("currentUser ");
    }

    public Response<List<String>> getBasicCatalogs(){
        List<Catalog> catalogs = catalogRepository.getBasicCatalogs();
        List<String> names = new ArrayList<>();
        try {
            for (Catalog person : catalogs) {
                names.add(person.getName());
            }
            return new Response<>(200, names);
        }
        catch (Exception e) {
            return new Response<>(500);
        }
    }

    public Response<List<String>> getUserCatalogs(Request request){
        User currentUser = getCurrentUser(request);
        List<Catalog> catalogs = catalogRepository.getUserCatalogs(currentUser.getId());
        List<String> names = new ArrayList<>();
        try {
            for (Catalog person : catalogs) {
                names.add(person.getName());
            }
            return new Response<>(200, names);
        }
        catch (Exception e) {
            return new Response<>(500, "Internal Error");
        }
    }

    public Response<String> getCatalog(Request request, String name){
        User currentUser = getCurrentUser(request);
        if (!catalogRepository.existsByName(currentUser.getId(), name)) {
            return new Response<>(409, "there is no catalog with this name"); // Conflict
        }
        try {
            Catalog catalog = catalogRepository.getByName(currentUser.getId(), name);
            return new Response<>(200, catalog.getName()); // Created
        } catch (Exception e) {
            return new Response<>(500);
        }
    }

    public Response<Catalog> addCatalog(Request request, String name){
        User currentUser = getCurrentUser(request);
        if (catalogRepository.existsByName(currentUser.getId(), name)) {
            return new Response<>(409, "Catalog is already added"); // Conflict
        }
        try {
            Catalog catalog = catalogRepository.addByName(currentUser.getId(), name);
            return new Response<>(200, catalog); // Created
        } catch (Exception e) {
            return new Response<>(500);
        }
    }
    public int deleteCatalog(){
        //TODO
        return 400;
    }
}
