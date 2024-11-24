package org.example.service;

import static spark.Spark.*;

public class UserService extends Service {

    public void startService() {
        path("/user", () -> {
            post("/login", (request, response) -> {
                response.status(501);  // Not Implemented
                return "Not Implemented";
            });
            post("/register", (request, response) -> {
                response.status(501);  // Not Implemented
                return "Not Implemented";
            });
            get("/:catalog_name", (request, response) -> {
                String catalogName = request.params(":catalog_name");
                response.status(501);
                return "Not Implemented";
            });
        });
    }
}