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
                Response<List<String>> serviceResponse = service.getBasicCatalogs();
                response.status(serviceResponse.getStatusCode());
                response.status(serviceResponse.getStatusCode());
                if (serviceResponse.isSuccess()) {
                    return serviceResponse.getData();
                }
                //TODO OTHER ERRORS;
                return serviceResponse.getMessage();
            });

            get("/user-preferences", (request, response) -> {
                Response<List<String>> serviceResponse = service.getUserCatalogs(request);
                response.status(serviceResponse.getStatusCode());
                response.status(serviceResponse.getStatusCode());
                if (serviceResponse.isSuccess()) {
                    return serviceResponse.getData();
                }
                //TODO OTHER ERRORS;
                return serviceResponse.getMessage();
            });

            get("/:name", (request, response) -> {
                //TODO rewrite
                String catalogName = request.params(":name");
                Response<String> serviceResponse = service.getCatalog(request, catalogName);
                response.status(serviceResponse.getStatusCode());
                if (serviceResponse.isSuccess()){
                    return serviceResponse.getData() + " successfully found";
                }
                //TODO OTHER ERRORS;

                return "Not Implemented";
            });

            post("/:name", (request, response) -> {
                String catalogName = request.params(":name");
                Response<Catalog> serviceResponse = service.addCatalog(request, catalogName);
                response.status(serviceResponse.getStatusCode());
                if (serviceResponse.isSuccess()){
                    return serviceResponse.getData().getName() + " successfully added";
                }
                //TODO OTHER ERRORS;

                return "Not Implemented";
            });
            delete("/:name", (request, response) -> {
                //TODO rewrite

                String catalogName = request.params(":name");
                response.status(501);
                return "Not Implemented";
            });

        });
    }
}
