package com.epam.gymcrm.exception.model;

import lombok.Builder;

@Builder
public record ExceptionResponse(
    String message,
    int status
) {
}
