package com.epam.gymcrm.auth;

import com.epam.gymcrm.model.Role;
import lombok.Builder;

@Builder
public record Authentication (
        String username,
        Role role
) {
}
