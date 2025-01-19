package org.example.repository;

import org.example.entity.User;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
  Optional<User> save(String name, String password) ;
  Optional<User> getById(UUID userId) throws SQLException;
  Optional<User> findByName(String name);
  boolean exists(String name);
}