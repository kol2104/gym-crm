package com.epam.gymcrm.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Role {
    TRAINEE("ROLE_TRAINEE"),
    TRAINER("ROLE_TRAINER");

    private final String roleName;
}
