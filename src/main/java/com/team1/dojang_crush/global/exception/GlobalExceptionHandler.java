package com.team1.dojang_crush.global.exception;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({CustomException.class})
    protected ResponseEntity handlerIllegalAccessException(CustomException e){
        ErrorDto errorDto = new ErrorDto(
                LocalDateTime.now().toString(),
                e.getErrorCode().getStatus(),
                e.getErrorCode().name(),
                e.getErrorCode().getMessage()
        );
        return new ResponseEntity(errorDto, HttpStatusCode.valueOf(e.getErrorCode().getStatus()));
    }
}
