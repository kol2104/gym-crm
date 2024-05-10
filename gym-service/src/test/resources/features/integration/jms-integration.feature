@integration
Feature: JMS Integration Management

    @jms
    Scenario: Send message using JMS
        Given message for topic with trainer "JohnDoe"
        When message sending to topic
        Then message in topic