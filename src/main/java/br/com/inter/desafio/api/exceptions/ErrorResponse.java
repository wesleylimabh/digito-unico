package br.com.inter.desafio.api.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private Integer status;
    private OffsetDateTime date;
    private String message;
    private List<Field> fields;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Field {

        private String name;
        private String message;
    }
}
