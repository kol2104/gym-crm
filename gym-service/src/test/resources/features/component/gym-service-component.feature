@component
Feature: Gym Service Component Management

    Scenario: Successfully create training
        Given a training to save with trainee "Trainee", trainer "Trainer" and training type "TrainingType"
        And a mocked trainer "Trainer" from DB
        And a mocked trainee "Trainee" from DB
        And a mocked training type "TrainingType" from DB
        And a authentication token for "Trainer"
        When I send request to save training
        Then response status is "200"
        And trainer "Trainer" from DB was called
        And trainee "Trainee" from DB was called
        And training type "TrainingType" from DB was called
        And message for training report service was created with trainer "Trainer"

    Scenario: Retrieve training types successfully
        Given a authentication token for "JohnDoe"
        And mocked training types from DB
        When I retrieve training types
        Then response status is "200"
        And returned training types equal types in DB

    Scenario: Invalid request body
        Given a training to save with trainee "", trainer "" and training type "TrainingType"
        And a mocked trainer "" from DB
        And a mocked trainee "" from DB
        And a mocked training type "TrainingType" from DB
        And a authentication token for ""
        When I send request to save training
        Then response status is "400"

    Scenario: Trainee not found
        Given a training to save with trainee "Trainee", trainer "Trainer" and training type "TrainingType"
        And a mocked trainer "Trainer" from DB
        And a mocked empty trainee "Trainee" from DB
        And a mocked training type "TrainingType" from DB
        And a authentication token for "Trainer"
        When I send request to save training
        Then response status is "404"

    Scenario: User not authenticated
        Given a training to save with trainee "Trainee", trainer "Trainer" and training type "TrainingType"
        And a mocked trainer "Trainer" from DB
        And a mocked trainee "Trainee" from DB
        And a mocked training type "TrainingType" from DB
        When I send request to save training
        Then response status is "401"