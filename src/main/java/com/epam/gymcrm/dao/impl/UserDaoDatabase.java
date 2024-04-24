package com.epam.gymcrm.dao.impl;

import com.epam.gymcrm.dao.UserDao;
import com.epam.gymcrm.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Component
public class UserDaoDatabase implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    @Override
    public Optional<User> getByUsername(String username) {
        Optional<User> foundUser = entityManager
                .createQuery("from User t where t.username = :username", User.class)
                .setParameter("username", username)
                .getResultStream().findFirst();
        if (foundUser.isPresent()) {
            log.debug("Found user by username '{}'", username);
        } else {
            log.debug("User with username '{}'.", username);
        }
        return foundUser;
    }

    @Transactional
    @Override
    public User update(User user) {
        if (entityManager.find(User.class, user.getId()) == null) {
            log.debug("User with id {} not found. Unable to update.", user.getId());
            return null;
        }
        entityManager.merge(user);
        log.info("Trainee updated successfully: {}", user);
        return user;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> getByFirstNameAndLastName(String firstName, String lastName) {
        Optional<User> foundUser = entityManager
                .createQuery("from User u where u.firstName = :firstName and u.lastName = :lastName", User.class)
                .setParameter("firstName", firstName)
                .setParameter("lastName", lastName)
                .getResultStream().findFirst();
        if (foundUser.isPresent()) {
            log.debug("Found user by first name '{}' and last name '{}'", firstName, lastName);
        } else {
            log.debug("User with first name '{}' and last name '{}' not found.", firstName, lastName);
        }
        return foundUser;
    }
}
