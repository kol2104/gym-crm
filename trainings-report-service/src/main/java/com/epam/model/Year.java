package com.epam.model;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Builder
@Data
public class Year {
    private Integer yearNumber;
    private Map<String, Long> summaryDurationByMonths;
}
