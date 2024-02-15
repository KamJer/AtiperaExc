package com.kamjer.AtiperaExc.app.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import org.springframework.http.HttpStatus;

@Value
public class ErrorResponse{
    private HttpStatus status;
    private String message;

    public ErrorResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
