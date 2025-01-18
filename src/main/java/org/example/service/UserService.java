package org.example.service;

import org.example.entity.Response;
import org.example.entity.User;
import org.example.repository.MockUserRepository;
import org.example.repository.UserRepository;
import spark.Request;

import java.util.Optional;

public class UserService {
  private final UserRepository userRepository = new MockUserRepository();


  public Response<User> getUser(String name) {
    Optional<User> user = userRepository.findByName(name);
    if (user.isEmpty()) {
      return new Response<>(404, "User with this name doesn't exist");
    }

    return new Response<>(200, user.get());
  }

  public Response<User> registerUser(String name, String password) {
    if (userRepository.exists(name)) {
      return new Response<>(401, "User Already Exist");
    }
    Optional<User> user = userRepository.save(name, password);
    if (user.isPresent()){
      return new Response<>(200, user.get());
    }
    else {
      return new Response<>(404, "Registration Error");
    }
    //TODO REWRITE
  }

  public Response<User> loginUser(String name, String password){
    // Логика для проверки учетных данных
    Optional<User> user = userRepository.findByName(name);

    if (user.isPresent()) {
      if (!user.get().getPassword().equals(password)){
        return new Response<>(401, "Password not correct");
      }
      return new Response<>(200, user.get());
    }
    return new Response<>(401, "User with this name doesn't exist");
    }
}
