package org.example.repository;

import org.example.controller.UserController;
import org.example.dto.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {
  private UserController userController;

  @BeforeEach
  void setUp() {
    MockUserRepository mockRepository = new MockUserRepository();
    userController = new UserController(mockRepository);
  }

  @Test
  void testRegisterUserSuccessfully() {
    userController.registerUser(
        "User",
        "password",
        List.of("DB", "Java"),
        List.of("Site1", "Site2"),
        List.of("Article1")
    );
    Optional<User> user = userController.getUser("User");
    assertNotNull(user);
    assertEquals("User", user.get().getName());
    assertEquals("password", user.get().getPassword());
    assertEquals(List.of("DB", "Java"), user.get().getInterests());
    assertEquals(List.of("Site1", "Site2"), user.get().getNewsSources());
    assertEquals(List.of("Article1"), user.get().getLastArticles());
  }

  @Test
  void testRegisterDuplicateUser() {
    userController.registerUser(
        "User",
        "password",
        List.of(),
        List.of(),
        List.of()
    );
    assertThrows(IllegalArgumentException.class, () -> {
      userController.registerUser(
          "User",
          "differentPassword",
          List.of(),
          List.of(),
          List.of()
      );
    });
  }

  @Test
  void testGetNonexistentUser() {
    assertThrows(IllegalArgumentException.class, () -> userController.getUser("NewUser"));
  }
}
