package org.example.repository;

import org.example.service.UserService;
import org.example.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {
  private UserService userController;

  @BeforeEach
  void setUp() {
    MockUserRepository mockRepository = new MockUserRepository();
    userController = new UserService(mockRepository);
  }

  @Test
  void testRegisterUserSuccessfully() {
    userController.registerUser(
        "User",
        "password"
    );
    Optional<User> user = userController.getUser("User");
    assertNotNull(user);
    assertEquals("User", user.get().getName());
    assertEquals("password", user.get().getPassword());
  }

  @Test
  void testRegisterDuplicateUser() {
    userController.registerUser(
        "User",
        "password"
    );
    assertThrows(IllegalArgumentException.class, () -> {
      userController.registerUser(
          "User",
          "differentPassword"
      );
    });
  }

  @Test
  void testGetNonexistentUser() {
    assertThrows(IllegalArgumentException.class, () -> userController.getUser("NewUser"));
  }
}
