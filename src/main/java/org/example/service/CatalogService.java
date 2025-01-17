package org.example.service;

import org.example.dto.Catalog;
import org.example.dto.Response;
import org.example.dto.User;
import org.example.dto.Website;
import org.example.repository.CatalogRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CatalogService {
    private CatalogRepository repository = new CatalogRepository();


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

    public Response<Catalog> addCatalog(String name){
        if (repository.existsByName(name)) {
            return new Response<>(409); // Conflict
        }
        try {
            repository.addByName(name);
            return new Response<>(201); // Created
        } catch (Exception e) {
            return new Response<>(500);
        }
    }

    public int deleteCatalog(){
        //TODO
        return 400;
    }
}
