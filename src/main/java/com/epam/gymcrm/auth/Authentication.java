package com.epam.gymcrm.auth;

import com.epam.gymcrm.model.Role;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
public class Authentication {
    private Role role;
}
