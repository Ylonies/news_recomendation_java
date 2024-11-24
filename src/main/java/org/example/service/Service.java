package org.example.service;

import static spark.Spark.*;

public abstract class Service {

    protected abstract void startService();

    protected void stopService() {
        stop();
    }
}