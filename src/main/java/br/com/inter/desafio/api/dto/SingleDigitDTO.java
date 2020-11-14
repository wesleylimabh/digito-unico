package br.com.inter.desafio.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SingleDigitDTO {

    @NotBlank
    @Pattern(regexp = "^[0-9]+$", message = "Deve conter apenas números")
    private String number;

    @Positive
    private Integer multiplier;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer result;
}
