package org.example.service;

import org.example.dto.User;
import org.example.repository.UserRepository;

import java.util.List;
import java.util.Optional;

public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public void registerUser(String name, String password, List<String> interests, List<String> newsSources, List<String> lastArticles) {
    if (userRepository.exists(name)) {
      throw new IllegalArgumentException("User already exists");
    }
    User user = new User(name, password, interests, newsSources, lastArticles);
    userRepository.save(user);
  }

  public Optional<User> getUser(String name) {
    Optional<User> user = userRepository.findByName(name);
    if (user.isEmpty()) {
      throw new IllegalArgumentException("User not found");
    }
    return user;
  }
}
