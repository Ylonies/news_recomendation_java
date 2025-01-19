package org.example.repository;

import org.example.entity.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
//
//public class MockUserRepository implements UserRepository {
//  private final Map<String, User> users = new HashMap<>();
//
//  @Override
//  public Optional<User> save(String name, String password) {
//    if (users.containsKey(name)) {
//      throw new IllegalArgumentException("Пользователь уже существует");
//    }
//    users.put(name, new User(UUID.randomUUID(), name, password));
//    return findByName(name);
//  }
//
//
//  @Override
//  public Optional<User> findByName(String name) {
//    return Optional.ofNullable(users.get(name));
//  }
//
//  @Override
//  public boolean exists(String name) {
//    return users.containsKey(name);
//  }
//}
