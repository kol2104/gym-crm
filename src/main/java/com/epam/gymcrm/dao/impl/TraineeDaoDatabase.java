package com.epam.gymcrm.dao.impl;

import com.epam.gymcrm.dao.TraineeDao;
import com.epam.gymcrm.model.Trainee;
import com.epam.gymcrm.model.Trainer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class TraineeDaoDatabase implements TraineeDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public Trainee create(Trainee trainee) {
        entityManager.persist(trainee);
        log.info("Trainee created successfully: {}", trainee);
        return trainee;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Trainer> getUnassignedOnTraineeTrainerListByUsername(String username) {
        List<Trainer> trainers = entityManager.createQuery("select tr " +
                        "from Trainee te join te.trainers tt with te.username = :username " +
                        "right join Trainer tr on tt.id = tr.id " +
                        "where te is null and tr.isActive = true", Trainer.class)
                .setParameter("username", username)
                .getResultList();
        log.debug("Found {} trainers.", trainers.size());
        return trainers;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Trainee> getByUsername(String username) {
        Optional<Trainee> foundTrainee = entityManager
                .createQuery("from Trainee t left join fetch t.trainers where t.username = :username", Trainee.class)
                .setParameter("username", username)
                .getResultStream().findFirst();
        if (foundTrainee.isPresent()) {
            log.debug("Found trainee by username '{}'", username);
        } else {
            log.debug("Trainee with username '{}'.", username);
        }
        return foundTrainee;
    }

    @Transactional
    @Override
    public Trainee update(Trainee trainee) {
        if (entityManager.find(Trainee.class, trainee.getId()) == null) {
            log.debug("Trainee with id {} not found. Unable to update.", trainee.getId());
            return null;
        }
        entityManager.merge(trainee);
        log.info("Trainee updated successfully: {}", trainee);
        return trainee;
    }

    @Transactional
    @Override
    public void delete(Long id) {
        int deletedRows = entityManager.createQuery("delete from Trainee t where t.id = :id")
                .setParameter("id", id)
                .executeUpdate();
        if (deletedRows != 0) {
            log.info("Trainee with id '{}' deleted successfully", id);
        } else {
            log.debug("Trainee with id {} not found. Unable to delete.", id);
        }
    }

    @Transactional
    @Override
    public void deleteByUsername(String username) {
        int deletedRows = entityManager.createQuery("delete from Trainee t where t.username = :username")
                .setParameter("username", username)
                .executeUpdate();
        if (deletedRows != 0) {
            log.info("Trainee with username '{}' deleted successfully", username);
        } else {
            log.debug("Trainee with username {} not found. Unable to delete.", username);
        }
    }
}
