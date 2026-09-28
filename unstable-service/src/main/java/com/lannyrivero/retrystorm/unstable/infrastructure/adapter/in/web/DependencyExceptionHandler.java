package com.lannyrivero.retrystorm.unstable.infrastructure.adapter.in.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.lannyrivero.retrystorm.unstable.application.exception.InvalidDependencySimulationRequest;
import com.lannyrivero.retrystorm.unstable.infrastructure.adapter.in.web.dto.ErrorResponse;

@RestControllerAdvice
public class DependencyExceptionHandler {

    @ExceptionHandler(InvalidDependencySimulationRequest.class)
    public ResponseEntity<ErrorResponse> handleInvalidRequest(InvalidDependencySimulationRequest exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(exception.getMessage()));
    }
}
