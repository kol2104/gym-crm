package com.epam.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Builder
@Document(collection = "training_workloads")
@Data
public class TrainerWorkload {
    @Id
    private String username;
    private String firstname;
    private String lastname;
    private Boolean isActive;
    private List<Year> years;
}
