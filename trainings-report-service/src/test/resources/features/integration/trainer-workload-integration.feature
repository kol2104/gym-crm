@integration
Feature: Trainer Workload Integration Management

    @database
    Scenario: Save new workload
        Given a trainer workload named "JohnDoe"
        And a empty workload for "JohnDoe"
        When I save workload
        Then saved workload for "JohnDoe"

    @database
    Scenario: Find trainer workload successfully
        Given exist workload for "JohnDoe"
        When I find the workload for "JohnDoe"
        Then trainer workload found

    @jms
    Scenario: Get message using JMS
        Given message in topic for "JohnDoe"
        And a empty workload for "JohnDoe"
        When wait for message
        Then message is received for "JohnDoe"