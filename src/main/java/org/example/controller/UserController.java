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
                Response<User> userResponse = userService.loginUser(request);
                response.status(userResponse.getStatusCode());
                if (userResponse.isSuccess()) {
                    return new UserResponse(userResponse.getData()).json();
                }
                return userResponse.getMessage();
            });

            post("/register", (request, response) -> {
                Response<User> userResponse = userService.registerUser(request);
                response.status(userResponse.getStatusCode());
                if (userResponse.isSuccess()) {
                    User user = userResponse.getData();
                    return new UserResponse(user).json();
                }
                return userResponse.getMessage();
            });

            get("/", (req, res) -> {
                Response<User> userResponse = userService.getCurrentUser(req);
                res.status(userResponse.getStatusCode());
                if (userResponse.isSuccess()){
                    return new UserResponse(userResponse.getData()).json();
                }
                return userResponse.getMessage();
            });


        });
    }
}