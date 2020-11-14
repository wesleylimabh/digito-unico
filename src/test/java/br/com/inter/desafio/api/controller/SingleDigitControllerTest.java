package br.com.inter.desafio.api.controller;

import br.com.inter.desafio.domain.exceptions.EntityNotFoundException;
import br.com.inter.desafio.domain.model.SingleDigit;
import br.com.inter.desafio.domain.model.User;
import br.com.inter.desafio.domain.repository.SingleDigitRepository;
import br.com.inter.desafio.domain.service.interfaces.UserServiceInterface;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class SingleDigitControllerTest {

    static final MediaType JSON = MediaType.APPLICATION_JSON;
    public static final String API_URL = "/single-digits";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private UserServiceInterface usuarioService;

    @MockBean
    private SingleDigitRepository digitRepository;

    @Test
    public void shouldReturnBadRequest_WhenCallingWithoutParameter() throws Exception {
        // Arrange
        SingleDigit singleDigit = new SingleDigit();
        String expectedMessage = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente";

        // Act
        ResultActions resultActions = this.mockMvc.perform(post(API_URL)
                .contentType(JSON));

        // Assert
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(expectedMessage))
                .andExpect(jsonPath("$.status").value(400));

    }

    @Test
    public void shouldReturnEntityNotFoundException_WhenInformedUnregisteredUser() throws Exception {
        // Arrange
        SingleDigit singleDigit = new SingleDigit();
        singleDigit.setNumber("11");
        String payload = objectMapper.writeValueAsString(singleDigit);
        String expectedMessage = "Usuario não encontrado";

        Mockito.when(usuarioService.findById(any(Long.class))).thenThrow(new EntityNotFoundException(expectedMessage));

        // Act
        ResultActions resultActions = this.mockMvc.perform(post(API_URL)
                .contentType(JSON)
                .content(payload)
                .param("user_id", "100"));

        // Assert
        resultActions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value(expectedMessage));

    }

    @Test
    public void shouldReturnCreated_WhenCallingWithCorrectParameter() throws Exception {
        // Arrange
        SingleDigit singleDigit = new SingleDigit();
        singleDigit.setNumber("123");
        singleDigit.setResult(6);
        String payload = objectMapper.writeValueAsString(singleDigit);

        Mockito.when(digitRepository.save(any(SingleDigit.class))).thenReturn(singleDigit);

        // Act
        ResultActions resultActions = this.mockMvc.perform(post(API_URL)
                .contentType(JSON)
                .content(payload));

        // Assert
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.number").value(singleDigit.getNumber()))
                .andExpect(jsonPath("$.result").value(singleDigit.getResult()));

    }


    @Test
    void shouldReturnNotFound_WhenInformedUnregisteredUser() throws Exception{
        // Arrange
        String expectedMessage = "Usuario não encontrado";

        Mockito.when(usuarioService.findById(any(Long.class))).thenThrow(new EntityNotFoundException(expectedMessage));

        // Act
        ResultActions resultActions = this.mockMvc.perform(get(API_URL.concat("/user/1")).contentType(JSON));

        // Assert
        resultActions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value(expectedMessage));

    }

    @Test
    void shouldReturnListOfCalculetedSingleDigit_WhenInformedValidUser() throws Exception{
        // Arrange
        SingleDigit singleDigit = new SingleDigit();
        singleDigit.setNumber("123");
        singleDigit.setResult(6);
        User user = new User();
        user.setSingleDigits(List.of(singleDigit));

        Mockito.when(usuarioService.findById(any(Long.class))).thenReturn(user);

        // Act
        ResultActions resultActions = this.mockMvc.perform(get(API_URL.concat("/user/1")).contentType(JSON));

        // Assert
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].number").value(singleDigit.getNumber()))
                .andExpect(jsonPath("$[0].result").value(singleDigit.getResult()));

    }

    @Test
    void shouldCalculateASingleDigitAndincludeInTheUserList() throws Exception{
        // Arrange
        SingleDigit singleDigit = new SingleDigit();
        singleDigit.setNumber("123");
        singleDigit.setResult(6);

        String payload = objectMapper.writeValueAsString(singleDigit);

        Mockito.when(digitRepository.save(any(SingleDigit.class))).thenReturn(singleDigit);
        Mockito.when(usuarioService.findById(any(Long.class))).thenReturn(new User());

        // Act
        ResultActions resultActions = this.mockMvc.perform(post(API_URL)
                .contentType(JSON)
                .param("user_id", "1")
                .content(payload));

        // Assert
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.number").value(singleDigit.getNumber()))
                .andExpect(jsonPath("$.result").value(singleDigit.getResult()));

    }
}