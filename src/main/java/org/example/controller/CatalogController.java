package org.example.controller;

import org.example.entity.Catalog;
import org.example.entity.Response;
import org.example.service.CatalogService;

import java.util.List;

import static spark.Spark.*;

public class CatalogController extends Controller {
    CatalogService service = new CatalogService();
    @Override
    public void startController() {
        path("/catalog", () -> {
            get("/basic", (request, response) -> {
                Response<List<String>> serviceResponse = service.getBasicCatalogs(request);
                response.status(serviceResponse.getStatusCode());
                response.status(serviceResponse.getStatusCode());
                if (serviceResponse.isSuccess()) {
                    return serviceResponse.getData();
                }
                return serviceResponse.getMessage();
            });

            get("/user", (request, response) -> {
                Response<List<String>> serviceResponse = service.getUserCatalogs(request);
                response.status(serviceResponse.getStatusCode());
                response.status(serviceResponse.getStatusCode());
                if (serviceResponse.isSuccess()) {
                    return serviceResponse.getData();
                }
                return serviceResponse.getMessage();
            });

            get("/user", (request, response) -> {
                Response<Catalog> serviceResponse = service.getCatalog(request);
                response.status(serviceResponse.getStatusCode());
                if (serviceResponse.isSuccess()){
                    return serviceResponse.getData().getName() + " successfully found";
                }
                return serviceResponse.getMessage();
            });

            post("/user/basic", (request, response) -> {
                Response<Catalog> serviceResponse = service.addBasicCatalog(request);
                response.status(serviceResponse.getStatusCode());
                if (serviceResponse.isSuccess()){
                    return serviceResponse.getData().getName() + " successfully added";
                }
                return serviceResponse.getMessage();
            });

            post("/user/custom", (request, response) -> {
                Response<Catalog> serviceResponse = service.addCustomCatalog(request);
                response.status(serviceResponse.getStatusCode());
                if (serviceResponse.isSuccess()){
                    return serviceResponse.getData().getName() + " successfully added";
                }
                return serviceResponse.getMessage();
            });

            delete("/user", (request, response) -> {
                //TODO rewrite
                String catalogName = request.params(":name");
                response.status(501);
                return "Not Implemented";
            });

        });
    }
}
