package com.kamjer.AtiperaExc.controller.advice;

import com.kamjer.AtiperaExc.exception.ErrorResponse;
import com.kamjer.AtiperaExc.exception.ErrorResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientResponseException;

@ControllerAdvice
public class GithubControllerAdvice {

    @ExceptionHandler(ErrorResponseException.class)
    public ResponseEntity<ErrorResponse> handleNotAcceptable(ErrorResponseException ex)  {
        return new ResponseEntity<>(ex.getErrorResponse(), ex.getErrorResponse().getStatus());
    }

    @ExceptionHandler(RestClientResponseException.class)
    public ResponseEntity<ErrorResponse> handleRestClientException(RestClientResponseException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getStatusCode(), ex.getMessage()), ex.getStatusCode());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleRestClientException(Exception ex) {
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
