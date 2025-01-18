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
            get("/", (request, response) -> {
                Response<List<String>> serviceResponse = service.getAll();
                response.status(serviceResponse.getStatusCode());
                response.status(serviceResponse.getStatusCode());
                if (serviceResponse.isSuccess()) {
                    return serviceResponse.getData();
                }
                //TODO OTHER ERRORS;
                return "Not Implemented";
            });

            get("/:name", (request, response) -> {
                String catalogName = request.params(":name");
                Response<Catalog> serviceResponse = service.getCatalog(catalogName);
                response.status(serviceResponse.getStatusCode());
                if (serviceResponse.isSuccess()){
                    return serviceResponse.getData();
                }
                //TODO OTHER ERRORS;

                return "Not Implemented";
            });

            post("/:name", (request, response) -> {
                String catalogName = request.params(":name");
                Response<Catalog> serviceResponse = service.addCatalog(catalogName);
                response.status(serviceResponse.getStatusCode());
                if (serviceResponse.isSuccess()){
                    return serviceResponse.getData();
                }
                //TODO OTHER ERRORS;

                return "Not Implemented";
            });
            delete("/:name", (request, response) -> {
                String catalogName = request.params(":name");
                response.status(501);
                return "Not Implemented";
            });

        });
    }
}
