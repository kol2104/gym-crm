package com.epam.gymcrm.controller.handler;

import com.epam.gymcrm.exception.AuthenticationException;
import com.epam.gymcrm.exception.AuthorizationException;
import com.epam.gymcrm.exception.TooManyLoginAttemptsException;
import com.epam.gymcrm.exception.TraineeNotFoundException;
import com.epam.gymcrm.exception.TrainerNotFoundException;
import com.epam.gymcrm.exception.TrainingNotFoundException;
import com.epam.gymcrm.exception.TrainingTypeNotFoundException;
import com.epam.gymcrm.exception.UserNotFoundException;
import com.epam.gymcrm.exception.model.ExceptionResponse;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GymCrmExceptionHandler {
    @ExceptionHandler({TraineeNotFoundException.class, TrainerNotFoundException.class,
            TrainingNotFoundException.class, UserNotFoundException.class, TrainingTypeNotFoundException.class})
    public ResponseEntity<ExceptionResponse> exceptionHandlerNotFound(Exception exception) {
        ExceptionResponse response = buildExceptionResponse(exception, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<ExceptionResponse> exceptionHandlerUnauthorized(Exception exception) {
        ExceptionResponse response = buildExceptionResponse(exception, HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({AuthorizationException.class})
    public ResponseEntity<ExceptionResponse> exceptionHandlerForbidden(Exception exception) {
        ExceptionResponse response = buildExceptionResponse(exception, HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, JsonMappingException.class,
            TooManyLoginAttemptsException.class})
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
