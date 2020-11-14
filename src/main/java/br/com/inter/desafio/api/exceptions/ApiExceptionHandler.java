package br.com.inter.desafio.api.exceptions;

import br.com.inter.desafio.domain.exceptions.BusinessException;
import br.com.inter.desafio.domain.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.ArrayList;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private MessageSource messageSource;

    @Autowired
    public ApiExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFound(BusinessException ex, WebRequest request) {
        var status = HttpStatus.NOT_FOUND;
        ErrorResponse errorResponse = getErrorResponse(status, ex.getMessage());
        return super.handleExceptionInternal(ex, errorResponse, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusiness(BusinessException ex, WebRequest request) {
        var status = HttpStatus.BAD_REQUEST;
        ErrorResponse errorResponse = getErrorResponse(status, ex.getMessage());
        return super.handleExceptionInternal(ex, errorResponse, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {

        var message = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente";
        var errorResponse = getErrorResponse(status, message);
        var fields = getFields(ex);
        errorResponse.setFields(fields);

        return super.handleExceptionInternal(ex, errorResponse, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        var message = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente";
        var errorResponse = getErrorResponse(status, message);
        return super.handleExceptionInternal(ex, errorResponse, headers, status, request);
    }

    private ArrayList<ErrorResponse.Field> getFields(MethodArgumentNotValidException ex) {
        var fields = new ArrayList<ErrorResponse.Field>();

        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            String fieldName = ((FieldError) error).getField();
            String message = messageSource.getMessage(error, LocaleContextHolder.getLocale());

            fields.add(new ErrorResponse.Field(fieldName, message));
        }
        return fields;
    }

    private ErrorResponse getErrorResponse(HttpStatus status, String message) {
        var errorResponse = new ErrorResponse();
        errorResponse.setStatus(status.value());
        errorResponse.setMessage(message);
        errorResponse.setDate(OffsetDateTime.now());
        return errorResponse;
    }

}
