package com.kamjer.AtiperaExc.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@Setter
public class ErrorResponseException extends Exception{
    private ErrorResponse errorResponse;

    public ErrorResponseException(HttpStatusCode status, String message) {
        super(message);
        this.errorResponse = new ErrorResponse(status, message);
    }
}
