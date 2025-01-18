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
                Response<List<String>> serviceResponse = websiteService.getBasicWebsites();
                response.status(serviceResponse.getStatusCode());
                response.status(serviceResponse.getStatusCode());
                if (serviceResponse.isSuccess()) {
                    return serviceResponse.getData();
                }
                //TODO OTHER ERRORS;
                return serviceResponse.getMessage();
            });

            get("/user-preferences", (request, response) -> {
                Response<List<String>> serviceResponse = websiteService.getUserWebsites(request);
                response.status(serviceResponse.getStatusCode());
                response.status(serviceResponse.getStatusCode());
                if (serviceResponse.isSuccess()) {
                    return serviceResponse.getData();
                }
                //TODO OTHER ERRORS;
                return serviceResponse.getMessage();
            });

            get("/:name", (request, response) -> {
                String websiteName = request.params(":name");

                Response<Website> serviceResponse = websiteService.getByName(request, websiteName);
                response.status(serviceResponse.getStatusCode());

                if (serviceResponse.isSuccess()) {
                    return serviceResponse.getData().getUrl();
                }
                //TODO OTHER ERRORS;
                return serviceResponse.getMessage();
            });

            post("/:name", (request, response) -> {
                String websiteName = request.params(":name");

                Response<Website> serviceResponse = websiteService.addByName(request, websiteName);
                response.status(serviceResponse.getStatusCode());

                if (serviceResponse.isSuccess()) {
                    return serviceResponse.getData().getUrl();
                }
                //TODO OTHER ERRORS;
                return serviceResponse.getMessage();
            });
        });
    }
}
