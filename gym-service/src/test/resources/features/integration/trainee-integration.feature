@integration
Feature: Trainee Database Management

    @database
    Scenario: Save new trainee
        Given a trainee named "JohnDoe"
        And a not existing trainee named "JohnDoe" in DB
        When I save trainee
        Then saved trainee named "JohnDoe"