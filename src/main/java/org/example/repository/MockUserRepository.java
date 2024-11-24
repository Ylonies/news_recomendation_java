package org.example.repository;

import org.example.dto.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MockUserRepository implements UserRepository {
  private final Map<String, User> users = new HashMap<>();

  @Override
  public void save(User user) {
    if (users.containsKey(user.getName())) {
      throw new IllegalArgumentException("Пользователь уже существует");
    }
    users.put(user.getName(), user);
  }

  @Override
  public Optional<User> findByName(String name) {
    return Optional.ofNullable(users.get(name));
  }

  @Override
  public boolean exists(String name) {
    return users.containsKey(name);
  }
}
