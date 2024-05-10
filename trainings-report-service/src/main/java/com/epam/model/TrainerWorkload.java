package com.epam.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Builder
@Document(collection = "trainer_workloads")
@Data
@CompoundIndex(def = "{'firstname': 1, 'lastname': 1}", name = "names_index")
@AllArgsConstructor
@NoArgsConstructor
public class TrainerWorkload {
    @Id
    private String username;
    private String firstname;
    private String lastname;
    private Boolean isActive;
    private List<Year> years;
}
