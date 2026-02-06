package com.progresssoft.warehouse.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void shouldHandleRuntimeException() {
        RuntimeException ex = new RuntimeException("Something went wrong");

        ProblemDetail response = handler.handleRuntimeException(ex); // سمية الميثود اللي عندك

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
        assertEquals("Something went wrong", response.getDetail());
    }

    @Test
    void shouldHandleGlobalException() {
        Exception ex = new Exception("Unexpected error");

        ProblemDetail response = handler.handleGlobalError(ex); // سمية الميثود اللي عندك

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
        assertEquals("An unexpected internal error occurred.", response.getDetail());
    }
}