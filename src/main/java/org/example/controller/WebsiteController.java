package org.example.controller;

import static spark.Spark.*;

public class WebsiteController extends Controller {
    @Override
    public void startController() {
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
