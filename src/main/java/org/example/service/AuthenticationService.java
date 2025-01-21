package org.example.service;

import org.example.entity.Response;
import org.example.entity.User;
import spark.Request;

public class AuthenticationService {

    public boolean isAuthenticated(Request request){
        try {
            User currentUser  = request.session().attribute("currentUser");
            return (currentUser != null);
        } catch (Exception e) {
            return false;
        }
    }

    public User getUser(Request request){
        try {
            return request.session().attribute("currentUser");
        } catch (Exception e) {
            return null;
        }
    }

    public void setUser(Request request, User user){
        request.session().attribute("currentUser", user);
    }
}
