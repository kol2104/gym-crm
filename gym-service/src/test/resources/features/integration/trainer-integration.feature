@integration
Feature: Trainer Database Management

    @database
    Scenario: Find trainer successfully
        Given exist trainer named "JohnDoe"
        When I find the trainer named "JohnDoe"
        Then trainer found