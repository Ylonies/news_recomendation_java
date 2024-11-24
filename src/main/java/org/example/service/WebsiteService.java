package org.example.service;

import static spark.Spark.*;

public class WebsiteService extends Service {
    @Override
    public void startService() {
        path("/website", () -> {
            get("/", (request, response) -> {
                response.status(501);
                return "Not Implemented";
            });
            get("/:url", (request, response) -> {
                String catalogName = request.params(":url");
                response.status(501);
                return "Not Implemented";
            });
            post("/:url", (request, response) -> {
                String catalogName = request.params(":url");
                response.status(501);
                return "Not Implemented";
            });

            delete("/:url", (request, response) -> {
                String catalogName = request.params(":url");
                response.status(501);
                return "Not Implemented";
            });
        });
    }
}
