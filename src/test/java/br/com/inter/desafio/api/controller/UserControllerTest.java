package br.com.inter.desafio.api.controller;

import br.com.inter.desafio.api.dto.UserDTO;
import br.com.inter.desafio.domain.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import br.com.inter.desafio.domain.exceptions.EntityNotFoundException;
import br.com.inter.desafio.domain.model.User;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class UserControllerTest {

    static final MediaType JSON = MediaType.APPLICATION_JSON;
    public static final String API_URL = "/users";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ModelMapper modelMapper;

    @MockBean
    private UserRepository repository;

    @Test
    void shouldReturnCreated_WhenInformedValidUser() throws Exception {
        // Arrange
        UserDTO dto = new UserDTO();
        dto.setId(1L);
        dto.setName("Teste");
        dto.setEmail("valid@email.com");
        String payload = objectMapper.writeValueAsString(dto);

        Mockito.when(repository.save(any(User.class))).thenReturn(modelMapper.map(dto, User.class));

        // Act
        ResultActions resultActions = this.mockMvc.perform(post(API_URL)
                .contentType(JSON)
                .content(payload));

        // Assert
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1l))
                .andExpect(jsonPath("$.name").value(dto.getName()))
                .andExpect(jsonPath("$.email").value(dto.getEmail()))
                .andExpect(jsonPath("$.singleDigits").value(dto.getSingleDigits()));
    }

    @Test
    void shouldReturnBadRequest_WhenInformedInvalidData() throws Exception {
        // Arrange
        UserDTO dto = new UserDTO();
        dto.setName("Teste");
        dto.setEmail("invalidemail.com");
        String payload = objectMapper.writeValueAsString(dto);
        String expectedMessage = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente";


        Mockito.when(repository.save(any(User.class))).thenReturn(modelMapper.map(dto, User.class));

        // Act
        ResultActions resultActions = this.mockMvc.perform(post(API_URL)
                .contentType(JSON)
                .content(payload));

        // Assert
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value(expectedMessage))
                .andExpect(jsonPath("$.fields").isArray());
    }

    @Test
    void shouldReturnBadRequest_WhenFindUnregisteredUser() throws Exception {
        // Arrange
        String expectedMessage = "Usuario não encontrado";

        Mockito.when(repository.findById(any(Long.class))).thenThrow(new EntityNotFoundException("Usuario não encontrado"));

        // Act
        ResultActions resultActions = this.mockMvc.perform(get(API_URL + "/1")
                .contentType(JSON));

        // Assert
        resultActions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value(expectedMessage));
    }

    @Test
    void shouldReturnUser_WhenFindRegisteredUser() throws Exception {
        // Arrange
        User user = new User();
        user.setId(1l);
        user.setName("Teste");
        user.setEmail("valid@email.com");

        Mockito.when(repository.findById(any(Long.class))).thenReturn(Optional.of(user));

        // Act
        ResultActions resultActions = this.mockMvc.perform(get(API_URL + "/1")
                .contentType(JSON));

        // Assert
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.singleDigits").isArray());
    }

    @Test
    void shouldReturnListOfUsers_WhenFindAllUsers() throws Exception {
        // Arrange
        User user = new User();
        user.setId(1l);
        user.setName("Teste");
        user.setEmail("valid@email.com");

        Mockito.when(repository.findAll()).thenReturn(List.of(user));

        // Act
        ResultActions resultActions = this.mockMvc.perform(get(API_URL)
                .contentType(JSON));

        // Assert
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(user.getId()))
                .andExpect(jsonPath("$[0].name").value(user.getName()))
                .andExpect(jsonPath("$[0].email").value(user.getEmail()))
                .andExpect(jsonPath("$[0].singleDigits").isArray());
    }


    @Test
    void shouldReturnNoContent_WhenDeleteUser() throws Exception {
        // Arrange
        User user = new User();

        Mockito.when(repository.findById(any(Long.class))).thenReturn(Optional.of(user));

        // Act
        ResultActions resultActions = this.mockMvc.perform(delete(API_URL + "/1")
                .contentType(JSON));

        // Assert
        resultActions
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void shouldReturnUpdatedUser_WhenUpdateUser() throws Exception {
        // Arrange
        User user = new User();
        user.setId(1l);
        user.setName("Teste");
        user.setEmail("valid@email.com");

        UserDTO dto = new UserDTO();
        dto.setName("Outro Nome");
        dto.setEmail("outro@email.com");

        String payload = objectMapper.writeValueAsString(dto);

        Mockito.when(repository.findById(any(Long.class))).thenReturn(Optional.of(user));
        Mockito.when(repository.save(any(User.class))).thenReturn(user);

        // Act
        ResultActions resultActions = this.mockMvc.perform(put(API_URL + "/1")
                .content(payload)
                .contentType(JSON));

        // Assert
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.name").value(dto.getName()))
                .andExpect(jsonPath("$.email").value(dto.getEmail()))
                .andExpect(jsonPath("$.singleDigits").isArray());
    }
}