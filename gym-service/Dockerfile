FROM maven:3.9 AS build
COPY . /app
WORKDIR /app
RUN mvn clean package

FROM openjdk:17-jdk
ENV SPRING_PROFILES_ACTIVE=prod
WORKDIR /usr/src/gym-service
COPY --from=build /app/target/gym-service-0.0.1-SNAPSHOT.jar /usr/src/gym-service

ARG GYM_SERVICE_PORT
HEALTHCHECK --interval=5s \
            --timeout=3s \
            CMD curl -f http://localhost:${GYM_SERVICE_PORT}/actuator/health || exit 1

CMD ["java", "-jar", "gym-service-0.0.1-SNAPSHOT.jar"]