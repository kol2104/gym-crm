package com.epam.gymcrm.model;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
public enum TrainingCriteria {
    FROM_DATE("trainingDate"),
    TO_DATE("trainingDate"),
    TRAINEE_USERNAME("trainee.username"),
    TRAINER_USERNAME("trainer.username"),
    TRAINING_TYPE("trainingType");

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private final String fieldName;

    public static Predicate getPredicateForCriterion(TrainingCriteria trainingCriteria, CriteriaBuilder criteriaBuilder,
                                               Root<Training> trainingRoot, String value) {
        return switch (trainingCriteria) {
            case FROM_DATE -> criteriaBuilder.greaterThanOrEqualTo(trainingRoot.get(trainingCriteria.fieldName),
                    LocalDateTime.parse(value, DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));
            case TO_DATE -> criteriaBuilder.lessThanOrEqualTo(trainingRoot.get(trainingCriteria.fieldName),
                    LocalDateTime.parse(value, DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));
            case TRAINEE_USERNAME, TRAINER_USERNAME, TRAINING_TYPE ->
                    criteriaBuilder.equal(getPath(trainingRoot, trainingCriteria.fieldName), value);
        };
    }
    private static Path<?> getPath(Root<Training> root, String fieldPath) {
        String[] fieldNames = fieldPath.split("\\.");
        Path<?> path = root;
        for (String fieldName : fieldNames) {
            path = path.get(fieldName);
        }
        return path;
    }

}
