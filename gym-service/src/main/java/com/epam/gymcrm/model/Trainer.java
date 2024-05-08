package com.epam.gymcrm.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "trainers")
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class Trainer extends User {
    @ManyToOne(targetEntity = TrainingType.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "specialization", nullable = false)
    @ToString.Exclude
    private TrainingType specialization;

    @ManyToMany(mappedBy = "trainers")
    @ToString.Exclude
    private List<Trainee> trainees;

    public Trainer(Long id) {
        super();
        this.setId(id);
    }
}
