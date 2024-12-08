package org.example.controller;

import static spark.Spark.*;

public class CatalogController extends Controller {
    @Override
    public void startController() {
        path("/catalog", () -> {
            get("/", (request, response) -> {
                response.status(501);
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
