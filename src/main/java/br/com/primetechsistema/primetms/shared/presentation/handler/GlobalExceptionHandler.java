package br.com.primetechsistema.primetms.shared.presentation.handler;

import br.com.primetechsistema.primetms.shared.domain.exception.DomainValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DomainValidationException.class)
    public ResponseEntity<ErrorResponse> handleDomainValidation(DomainValidationException ex) {
        String msg = ex.getMessage();

        HttpStatus status = HttpStatus.BAD_REQUEST;
        if (msg != null && msg.toLowerCase().contains("already exists")) {
            status = HttpStatus.CONFLICT;
        }
        if (msg != null && msg.toLowerCase().contains("not found")) {
            status = HttpStatus.NOT_FOUND;
        }

        return ResponseEntity.status(status).body(ErrorResponse.of(msg, ex.getClass().getSimpleName()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String msg = "Validation failed: " + ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + " " + e.getDefaultMessage())
                .reduce((a, b) -> a + ", " + b)
                .orElse("Invalid input");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(msg, "ValidationError"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of("Internal server error", ex.getClass().getSimpleName()));
    }
}

