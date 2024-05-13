FROM maven:3.9 AS build
COPY . /app
WORKDIR /app
RUN mvn clean package

FROM openjdk:17-jdk
ENV SPRING_PROFILES_ACTIVE=prod
WORKDIR /usr/src/trainings-report-service
COPY --from=build /app/target/trainings-report-service-0.0.1-SNAPSHOT.jar /usr/src/trainings-report-service

ARG TRAININGS_REPORT_SERVICE_PORT
HEALTHCHECK --interval=5s \
            --timeout=3s \
            CMD curl -f http://localhost:${TRAININGS_REPORT_SERVICE_PORT}/actuator/health || exit 1

CMD ["java", "-jar", "trainings-report-service-0.0.1-SNAPSHOT.jar"]