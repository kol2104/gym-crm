package com.epam.gymcrm.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Constants {
    AUTH_TOKEN("Authorization");

    private final String name;
}
