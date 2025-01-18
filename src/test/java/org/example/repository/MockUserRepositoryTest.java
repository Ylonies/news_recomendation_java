package org.example.repository;

import org.example.entity.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MockUserRepositoryTest {

  @Test
  void testSaveAndRetrieve() {
    MockUserRepository repository = new MockUserRepository();
    User user = new User("User", "password");
    repository.save(user);

    assertTrue(repository.findByName("User").isPresent());
    assertEquals(user, repository.findByName("User").get());
  }
}
