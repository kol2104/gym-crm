package com.epam.gymcrm.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "trainers")
@Getter
@Setter
public class Trainer extends User {
    @ManyToOne(targetEntity = TrainingType.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "specialization", nullable = false)
    private TrainingType specialization;
}
