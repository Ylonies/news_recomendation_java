package org.example.controller;

import static spark.Spark.*;

public class ArticleController extends Controller {
    @Override
    public void startController()  {

        path("/article", () -> {
            get("/:id", (request, response) -> {
                String catalogName = request.params(":id");
                response.status(501);
                return "Not Implemented";
            });
            post("/", (request, response) -> {
                response.status(501);
                return "Not Implemented";
            });
            delete("/:id", (request, response) -> {
                String catalogName = request.params(":id");
                response.status(501);
                return "Not Implemented";
            });
            get("/catalog/:catalog_name", (request, response) -> {
                String catalogName = request.params(":catalog_name");
                response.status(501);
                return "Not Implemented";
            });
        });
    }
}
