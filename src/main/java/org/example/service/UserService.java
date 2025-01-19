package org.example.service;

import org.example.entity.Response;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.example.repository.UserRepositoryImpl;
import spark.Request;

import java.util.Optional;
import java.util.UUID;
import org.mindrot.jbcrypt.BCrypt;


public class UserService extends Service {
  private final UserRepository userRepository = new UserRepositoryImpl();


  private String hashPassword(String password) {
    return BCrypt.hashpw(password, BCrypt.gensalt()); // Хэшируем пароль
  }

  public boolean checkPassword(String enteredPassword, String storedHash) {
    return BCrypt.checkpw(enteredPassword, storedHash);
  }

public Response<User> getCurrentUser(Request request){
  if (!authService.isAuthenticated(request)){
    return new Response<>(401, "Unauthorized");
  }
  return new Response<>(authService.getUser(request));
}

  public Response<User> getUser(UUID id) {
    Optional<User> user = userRepository.getById(id);
    if (user.isEmpty()) {
      return new Response<>(404, "User with this id doesn't exist");
    }
    return new Response<>(user.get());
  }

  public Response<User> registerUser(Request request) {
    String name = request.queryParams("name");
    String password = request.queryParams("password");
    if (userRepository.exists(name)) {
      return new Response<>(401, "User Already Exist");
    }
    Optional<User> user = userRepository.save(name, hashPassword(password));
    if (user.isPresent()){
      return new Response<>(user.get());
    }
    else {
      return new Response<>(404, "Registration Error");
    }
  }

  public Response<User> loginUser (Request request) {
    String name = request.queryParams("name");
    String password = request.queryParams("password");

    Optional<User> user = userRepository.findByName(name);
    if (user.isPresent()) {
      if (!checkPassword(user.get().getPassword(), password)) {
        return new Response<>(401, "Password not correct");
      }
      authService.setUser(request, user.get());
      return new Response<>(user.get());
    }
    return new Response<>(401, "User  with this name doesn't exist");
  }
}
