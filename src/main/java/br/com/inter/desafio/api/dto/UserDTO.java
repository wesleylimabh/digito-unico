package br.com.inter.desafio.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserDTO {

    @JsonProperty(access = Access.READ_ONLY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @JsonProperty(access = Access.READ_ONLY)
    private List<SingleDigitDTO> singleDigits = new ArrayList<>();


}
