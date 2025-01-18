package org.example.controller;

import org.example.dto.response.UserResponse;
import org.example.entity.Response;
import org.example.entity.User;
import org.example.service.UserService;

import static spark.Spark.*;

public class UserController extends Controller {
    UserService userService = new UserService();

    public void startController() {
        path("/user", () -> {
            post("/login", (request, response) -> {
                String name = request.queryParams("name");
                String password = request.queryParams("password");

                Response<User> userResponse = userService.loginUser(name, password);
                response.status(userResponse.getStatusCode());
                if (userResponse.isSuccess()) {
                    User user = userResponse.getData();
                    request.session().attribute("currentUser ", user);
                    return new UserResponse(user).json();
                }
                return userResponse.getMessage();
                //TODO other errors
            });

            post("/register", (request, response) -> {
                String name = request.queryParams("name");
                String password = request.queryParams("password");
                Response<User> userResponse = userService.registerUser(name, password);
                //TODO rewrite
                if (userResponse.isSuccess()) {
                    User user = userResponse.getData();
                    request.session().attribute("currentUser", user);
                    return new UserResponse(user).json();
                }
                return "Not Implemented";
                //TODO other errors
            });

            get("/currentUser", (req, res) -> {
                User currentUser  = req.session().attribute("currentUser");
                if (currentUser  != null) {
                    return new UserResponse(currentUser).json();
                } else {
                    res.status(401);
                    return "No user is currently logged in.";
                }
            });


        });
    }
}