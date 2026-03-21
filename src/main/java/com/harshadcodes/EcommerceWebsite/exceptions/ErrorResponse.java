package com.harshadcodes.EcommerceWebsite.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;


@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private String message;
    private int statusCode;
    private LocalDateTime timeStamp;
    private Map<String,Object> errors;

    public ErrorResponse(String message, int statusCode, LocalDateTime timeStamp) {
        this.message = message;
        this.statusCode = statusCode;
        this.timeStamp = timeStamp;
    }

    public ErrorResponse(int statusCode, LocalDateTime timeStamp, Map<String, Object> errors) {
        this.statusCode = statusCode;
        this.timeStamp = timeStamp;
        this.errors = errors;
    }

}
