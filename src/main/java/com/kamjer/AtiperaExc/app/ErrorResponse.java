package com.kamjer.AtiperaExc.app;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public class ErrorResponse{
    private HttpStatus status;
    private String message;

    public ErrorResponse() {
    }

    public ErrorResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
