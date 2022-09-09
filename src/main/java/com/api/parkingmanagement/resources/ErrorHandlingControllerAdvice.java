package com.api.parkingmanagement.resources;

import com.api.parkingmanagement.domain.exception.ErrorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ErrorHandlingControllerAdvice {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    List<ErrorDto> onMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        List<ErrorDto> errors = new ArrayList<>();
        e.getBindingResult().getFieldErrors().forEach( error ->
                errors.add(new ErrorDto(error.getField(), messageSource.getMessage(error, LocaleContextHolder.getLocale())))
        );
        return errors;
    }
}
