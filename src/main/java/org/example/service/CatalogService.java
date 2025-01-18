package org.example.service;

import org.example.entity.Catalog;
import org.example.entity.Response;
import org.example.entity.User;
import org.example.repository.CatalogRepository;

import java.util.ArrayList;
import java.util.List;

public class CatalogService {
    private CatalogRepository repository = new CatalogRepository();
    private User currentUser;


    public Response<List<String>> getAll(){
        List<Catalog> catalogs = repository.getAll();
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

    public Response<Catalog> getCatalog(String name){
        if (!repository.existsByName(name)) {
            return new Response<>(409); // Conflict
        }
        try {
            Catalog catalog = repository.getByName(name);
            return new Response<>(200, catalog); // Created
        } catch (Exception e) {
            return new Response<>(500);
        }
    }
    public Response<Catalog> addCatalog(String name){
        if (repository.existsByName(name)) {
            return new Response<>(409); // Conflict
        }
        try {
            Catalog catalog = repository.addByName(name);
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
