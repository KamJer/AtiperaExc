package com.kamjer.AtiperaExc.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Value
public class ErrorResponse{
    HttpStatusCode status;
    String message;

    public ErrorResponse(HttpStatusCode status, String message) {
        this.status = status;
        this.message = message;
    }
}
