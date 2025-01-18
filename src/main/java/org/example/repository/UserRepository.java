package org.example.repository;

import org.example.entity.Response;
import org.example.entity.User;

import java.util.Optional;

public interface UserRepository {
  public Optional<User> save(String name, String password) ;

  Optional<User> findByName(String name);

  boolean exists(String name);

}