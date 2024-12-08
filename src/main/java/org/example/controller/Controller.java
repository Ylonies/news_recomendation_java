package org.example.controller;

import static spark.Spark.*;

public abstract class Controller {

    protected abstract void startController();

    protected void stopController() {
        stop();
    }
}