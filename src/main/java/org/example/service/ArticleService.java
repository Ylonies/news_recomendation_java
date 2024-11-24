package org.example.service;

import static spark.Spark.*;

public class ArticleService extends Service {
    @Override
    public void startService()  {

        path("/article", () -> {
            get("/:id", (request, response) -> {
                String catalogName = request.params(":id");
                response.status(501);
                return "Not Implemented";
            });
            post("/:id", (request, response) -> {
                String catalogName = request.params(":id");
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
