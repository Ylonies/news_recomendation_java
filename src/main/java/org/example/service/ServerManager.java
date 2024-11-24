package org.example.service;

import java.util.ArrayList;
import java.util.List;

import static spark.Spark.*;

public class ServerManager {
    private static final int port = 4567;
    private final List<Service> services;

    public ServerManager() {
        this.services = new ArrayList<>();
        services.add(new UserService());
        services.add(new WebsiteService());
        services.add(new CatalogService());
        services.add(new ArticleService());
    }
    public void startAll() {
        port(port);
        for (Service service : services) {
            service.startService();
        }
    }
    public void stopAll() {
        for (Service service : services) {
            service.stopService();
        }
        stop();
    }

}