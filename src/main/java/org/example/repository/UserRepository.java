package org.example.repository;

import org.example.entity.Response;
import org.example.entity.User;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
  public Optional<User> save(String name, String password) ;
  public Optional<User> getById(UUID userId);
  Optional<User> findByName(String name);
  boolean exists(String name);
}