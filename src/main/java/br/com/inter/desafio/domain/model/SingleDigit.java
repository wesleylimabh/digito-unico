package br.com.inter.desafio.domain.model;

import br.com.inter.desafio.api.dto.SingleDigitDTO;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Entity
@Data
public class SingleDigit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Pattern(regexp = "^[0-9]+$")
    private String number;

    @Min(1)
    private Integer multiplier;

    private Integer result;

    public SingleDigitDTO toDto() {
        SingleDigitDTO singleDigitDTO = new SingleDigitDTO();
        singleDigitDTO.setNumber(this.getNumber());
        singleDigitDTO.setMultiplier(this.getMultiplier());
        singleDigitDTO.setResult(this.getResult());
        return singleDigitDTO;
    }
}
