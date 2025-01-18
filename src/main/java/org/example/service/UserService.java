package org.example.service;

import org.example.entity.User;
import org.example.repository.UserRepository;

import java.util.Optional;

public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }


  public Optional<User> getUser(String name) {
    Optional<User> user = userRepository.findByName(name);
    if (user.isEmpty()) {
      throw new IllegalArgumentException("User not found");
    }
    return user;
  }
}
