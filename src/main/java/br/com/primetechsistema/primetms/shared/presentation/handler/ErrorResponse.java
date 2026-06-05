package br.com.primetechsistema.primetms.shared.presentation.handler;

import java.time.LocalDateTime;

public record ErrorResponse(LocalDateTime timestamp, String message, String detail) {

    public static ErrorResponse of(String message, String detail) {
        return new ErrorResponse(LocalDateTime.now(), message, detail);
    }
}

