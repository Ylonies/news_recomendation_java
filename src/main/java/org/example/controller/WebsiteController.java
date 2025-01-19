package org.example.controller;

import org.example.entity.Response;
import org.example.entity.Website;
import org.example.service.WebsiteService;

import java.util.List;

import static spark.Spark.*;

public class WebsiteController extends Controller {
    WebsiteService websiteService = new WebsiteService();

    @Override
    public void startController() {
        path("/website", () -> {
            get("/basic", (request, response) -> {
                response.status(501);
                Response<List<String>> serviceResponse = websiteService.getBasicWebsites(request);
                response.status(serviceResponse.getStatusCode());
                response.status(serviceResponse.getStatusCode());
                if (serviceResponse.isSuccess()) {
                    return serviceResponse.getData();
                }
                return serviceResponse.getMessage();
            });

            get("/user", (request, response) -> {
                Response<List<String>> serviceResponse = websiteService.getUserWebsites(request);
                response.status(serviceResponse.getStatusCode());
                response.status(serviceResponse.getStatusCode());
                if (serviceResponse.isSuccess()) {
                    return serviceResponse.getData();
                }
                return serviceResponse.getMessage();
            });

            get("/user/:name", (request, response) -> {
                String websiteName = request.params(":name");

                Response<Website> serviceResponse = websiteService.getByName(request, websiteName);
                response.status(serviceResponse.getStatusCode());

                if (serviceResponse.isSuccess()) {
                    return serviceResponse.getData().getUrl();
                }
                return serviceResponse.getMessage();
            });

            post("/user/basic/:name", (request, response) -> {
                Response<Website> serviceResponse = websiteService.addBasicByName(request);
                response.status(serviceResponse.getStatusCode());

                if (serviceResponse.isSuccess()) {
                    return serviceResponse.getData().getUrl();
                }
                return serviceResponse.getMessage();
            });

            post("/user/custom/:name", (request, response) -> {
                Response<Website> serviceResponse = websiteService.addCustom(request);
                response.status(serviceResponse.getStatusCode());

                if (serviceResponse.isSuccess()) {
                    return serviceResponse.getData().getUrl();
                }
                return serviceResponse.getMessage();
            });
        });
    }
}
