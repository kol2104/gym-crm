package com.epam.gymcrm.dao;

import com.epam.gymcrm.model.User;

import java.util.Optional;

public interface UserDao {
    Optional<User> getByUsername(String username);
    User update(User user);
    Optional<User> getByFirstNameAndLastName(String firstName, String lastName);
}
