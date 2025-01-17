package org.example.controller;

import org.example.dto.Response;
import org.example.service.CatalogService;

import java.util.List;

import static spark.Spark.*;

public class CatalogController extends Controller {
    CatalogService service = new CatalogService();
    @Override
    public void startController() {
        path("/catalog", () -> {
            get("/", (request, response) -> {
                Response<List<String>> catalogs = service.getAll();
                response.status(catalogs.getErrorCode());
                if (response.status() == 200) {
                    return catalogs;
                }
                //TODO OTHER ERRORS;
                return "Not Implemented";
            });

            get("/:name", (request, response) -> {
                String catalogName = request.params(":name");
                response.status(501);
                return "Not Implemented";
            });

            post("/:name", (request, response) -> {
                String catalogName = request.params(":name");
                response.status(501);
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
