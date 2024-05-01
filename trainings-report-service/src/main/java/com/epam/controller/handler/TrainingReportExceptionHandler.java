package com.epam.controller.handler;

import com.epam.exception.model.ExceptionResponse;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class TrainingReportExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class, JsonMappingException.class})
    public ResponseEntity<ExceptionResponse> exceptionHandlerBadRequest(Exception exception) {
        ExceptionResponse response = buildExceptionResponse(exception, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private ExceptionResponse buildExceptionResponse(Exception exception, HttpStatus httpStatus) {
        return ExceptionResponse.builder()
                .message(exception.getMessage())
                .status(httpStatus.value())
                .build();
    }
}
