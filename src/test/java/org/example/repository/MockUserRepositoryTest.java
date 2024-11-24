package org.example.repository;

import org.example.dto.User;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MockUserRepositoryTest {

  @Test
  void testSaveAndRetrieve() {
    MockUserRepository repository = new MockUserRepository();
    User user = new User("User", "password", List.of(), List.of(), List.of());
    repository.save(user);

    assertTrue(repository.findByName("User").isPresent());
    assertEquals(user, repository.findByName("User").get());
  }
}
