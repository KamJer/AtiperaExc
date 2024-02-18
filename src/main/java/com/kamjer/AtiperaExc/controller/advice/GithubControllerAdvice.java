package com.kamjer.AtiperaExc.controller.advice;

import com.kamjer.AtiperaExc.exception.ErrorResponse;
import com.kamjer.AtiperaExc.exception.ErrorResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GithubControllerAdvice extends ResponseEntityExceptionHandler {

    private static final HttpStatus NOT_FOUND = HttpStatus.NOT_FOUND;

    @ExceptionHandler(ErrorResponseException.class)
    public ResponseEntity<ErrorResponse> handleNotAcceptable(ErrorResponseException ex)  {
        return new ResponseEntity<>(ex.getErrorResponse(), ex.getErrorResponse().getStatus());
    }

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<ErrorResponse> handleRestClientException(RestClientException ex) {
            return new ResponseEntity<>(new ErrorResponse(NOT_FOUND, ex.getMessage()), NOT_FOUND);
    }
}
