package com.epam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class TrainingsReportApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrainingsReportApplication.class, args);
    }

}
