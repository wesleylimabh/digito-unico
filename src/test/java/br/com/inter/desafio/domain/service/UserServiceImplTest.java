package br.com.inter.desafio.domain.service;

import br.com.inter.desafio.domain.exceptions.EntityNotFoundException;
import br.com.inter.desafio.domain.model.User;
import br.com.inter.desafio.domain.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.ConstraintViolationException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveValidUser() {
        // Arrange
        User user = getValidUser();

        // Act
        User saved = userService.save(user);

        // Assert
        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertTrue(saved.getEmail().equals(user.getEmail()));
        assertTrue(saved.getName().equals(user.getName()));
        assertTrue(saved.getSingleDigits().isEmpty());
    }

    @Test
    void shouldReturnRegisteredUser() {
        // Arrange
        User user = getValidUser();
        user.setId(1L);
        Long id = userService.save(user).getId();

        // Act
        User savedUser = userService.findById(id);

        // Assert
        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
        assertTrue(savedUser.getEmail().equals(user.getEmail()));
        assertTrue(savedUser.getName().equals(user.getName()));
        assertTrue(savedUser.getSingleDigits().isEmpty());
    }

    @Test
    void shouldThrowEntityNotFoundException_WhenSearchUnregisteredUsers() {
        // Arrange
        Long unregisteredUserId = 2L;

        // Act
        Throwable exception  = assertThrows(EntityNotFoundException.class, () -> {
            User saved = userService.findById(unregisteredUserId);
        });

        // Assert
        assertEquals("Usuario não encontrado", exception.getMessage());
    }

    @Test
    void shouldThrowConstraintViolationException_WhenNameIsNull() {
        // Arrange
        User user = new User();
        user.setName(null);

        // Assert
        assertThrows(ConstraintViolationException.class, () -> {
            userService.save(user);
        });
    }

    @Test
    void shouldThrowConstraintViolationException_WhenInvalidEmail() {
        // Arrange
        User user = new User();
        user.setEmail("wesleylima.gmail.com");

        // Assert
        assertThrows(ConstraintViolationException.class, () -> {
            userService.save(user);
        });
    }

    @Test
    void shouldReturnListOfUsers() {
        // Arrange
        User user1 = getValidUser();
        User user2 = getValidUser();

        userService.save(user1);
        userService.save(user2);

        // Act
        List<User> userList = userService.findAll();

        // Assert
        assertTrue(userList.size() == 2);
    }

    @Test
    void shouldDeleteUser() {
        // Arrange
        User user = getValidUser();
        Long idSavedUser = userService.save(user).getId();

        // Act
        userService.delete(idSavedUser);

        // Assert
        Throwable exception  = assertThrows(EntityNotFoundException.class, () -> {
            userService.findById(idSavedUser);
        });

    }

    @Test
    void shouldThrowEntityNotFoundException_WhenTryUpdateNonExistentUser() {
        // Arrange
        User unregisteredUser = getValidUser();
        Long unregisteredId = 100L;

        // Act
        // Assert
        Throwable exception  = assertThrows(EntityNotFoundException.class, () -> {
            userService.update(unregisteredId, unregisteredUser);
        });

    }

    @Test
    void shouldUpdateRegisteredUser() {
        // Arrange
        User user = getValidUser();
        Long idSavedUser = userService.save(user).getId();

        String newName = "Outro Nome";
        String newEmail = "outro@email.com";

        user.setName(newName);
        user.setEmail(newEmail);

        // Act
        userService.update(idSavedUser, user);
        User updatedUser = userService.findById(idSavedUser);

        // Assert
        assertTrue(updatedUser.getName().equals(newName));
        assertTrue(updatedUser.getEmail().equals(newEmail));

    }


    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }

    private User getValidUser() {
        User user = new User();
        user.setName("Wesley");
        user.setEmail("wesleylima@gmx.com");
        return user;
    }
}