package org.example.repository;

import org.example.entity.Catalog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CatalogRepository {
    public List<Catalog> getAll() {
        //TODO
        List<Catalog> catalogs = new ArrayList<>(Arrays.asList(
                new Catalog("DevOps"),
                new Catalog("Backend")
        ));
        return catalogs;
    }

    public boolean existsByName(String name){
        //TODO

        return true;
    }

    public Catalog getByName(String name){
        //TODO
        return null;
    }

    public Catalog addByName(String name){
        //TODO
        return null;
    }

    public void postByName(String name){
        //TODO
    }

    public void deleteByName(String name){
        //TODO
    }
}
