package com.epam.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Builder
@Document(collection = "trainer_workloads")
@Data
@CompoundIndex(def = "{'firstname': 1, 'lastname': 1}", name = "names_index")
public class TrainerWorkload {
    @Id
    private String username;
    private String firstname;
    private String lastname;
    private Boolean isActive;
    private List<Year> years;
}
