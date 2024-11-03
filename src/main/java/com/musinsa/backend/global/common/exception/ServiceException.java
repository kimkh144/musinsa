package com.musinsa.backend.global.common.exception;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ServiceException extends RuntimeException {

    @JsonIgnore
    private HttpStatus httpStatus;
    private Integer code;
    private String message;

    public ServiceException(Integer code, String massage) {
        this.code = code;
        this.message = massage;
    }
}
