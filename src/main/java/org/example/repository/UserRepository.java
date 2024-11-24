package org.example.repository;

import org.example.dto.User;

import java.util.Optional;

public interface UserRepository {
  void save(User user);

  Optional<User> findByName(String name);

  boolean exists(String name);
}