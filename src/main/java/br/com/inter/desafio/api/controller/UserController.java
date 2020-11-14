package br.com.inter.desafio.api.controller;

import br.com.inter.desafio.api.dto.UserDTO;
import br.com.inter.desafio.domain.model.User;
import br.com.inter.desafio.domain.service.interfaces.UserServiceInterface;
import io.swagger.annotations.Api;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Api(tags = "User")
@RequestMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserServiceInterface userService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserServiceInterface userService, ModelMapper mapper) {
        this.userService = userService;
        this.modelMapper = mapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO createUser(@Valid @RequestBody UserDTO userDTO) {
        User user = toEntity(userDTO);
        return toDTO(userService.save(user));
    }

    @GetMapping(consumes = {})
    public List<UserDTO> findAllUsers() {
        return toCollectionModel(userService.findAll());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> findById(@PathVariable Long userId) {
        User user = userService.findById(userId);
        return ResponseEntity.ok(toDTO(user));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long userId) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDTO, @PathVariable Long userId) {
        User updatedUser = userService.update(userId, toEntity(userDTO));
        return ResponseEntity.ok(toDTO(updatedUser));
    }


    private UserDTO toDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    private List<UserDTO> toCollectionModel(List<User> userList) {
        return userList.stream()
                .map(usuario -> toDTO(usuario))
                .collect(Collectors.toList());
    }

    private User toEntity(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }
}
