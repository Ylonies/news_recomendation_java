package org.example.controller;

import java.util.ArrayList;
import java.util.List;

import static spark.Spark.*;

public class ServerManager {
    private static final int port = 4567;
    private final List<Controller> services;

    public ServerManager() {
        this.services = new ArrayList<>();
        services.add(new UserController());
        services.add(new WebsiteController());
        services.add(new CatalogController());
        services.add(new ArticleController());
    }
    public void startAll() {
        port(port);
        for (Controller service : services) {
            service.startController();
        }
    }
    public void stopAll() {
        for (Controller service : services) {
            service.stopController();
        }
        stop();
    }

}