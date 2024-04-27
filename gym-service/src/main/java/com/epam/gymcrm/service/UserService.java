package com.epam.gymcrm.service;

import com.epam.gymcrm.dto.TokenDto;
import com.epam.gymcrm.dto.user.UserCredentialsDto;
import com.epam.gymcrm.dto.user.UserNewPasswordRequestDto;

public interface UserService {
    TokenDto authenticateUser(UserCredentialsDto userCredentialsDto);
    void updatePassword(UserNewPasswordRequestDto userNewPasswordRequestDto, String username);
}
