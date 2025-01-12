package org.example.controller;

import java.util.ArrayList;
import java.util.List;

import static spark.Spark.*;

public class ServerManager {
    private static final int port = 4567;
    private final List<Controller> controllers;

    public ServerManager() {
        this.controllers = new ArrayList<>();
        controllers.add(new UserController());
        controllers.add(new WebsiteController());
        controllers.add(new CatalogController());
        controllers.add(new ArticleController());
    }
    public void startAll() {
        port(port);
        for (Controller service : controllers) {
            service.startController();
        }
    }
    public void stopAll() {
        for (Controller service : controllers) {
            service.stopController();
        }
        stop();
    }

}