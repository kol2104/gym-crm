@component
Feature: Trainer Workload Component Management

    Scenario: Successfully update trainer workload
        Given a trainer named "JohnDoe"
        And a mocked trainer workload for "JohnDoe" from DB
        And a authentication token for "JohnDoe"
        When I send request for update workload
        Then response status is "200"
        And workload for "JohnDoe" was called
        And the workload for "JohnDoe" should be updated successfully

    Scenario: Retrieve trainer workload successfully
        Given a mocked trainer workload for "JohnDoe" from DB
        And a authentication token for "JohnDoe"
        When I retrieve the workload for "JohnDoe"
        Then response status is "200"
        And workload for "JohnDoe" was called
        And returned workload details

    Scenario: Create new trainer workload
        Given a trainer named "JohnDoe"
        And a mocked empty trainer workload from DB
        And a authentication token for "JohnDoe"
        When I send request for update workload
        Then response status is "200"
        And workload for "JohnDoe" was called
        And the workload for "JohnDoe" should be created successfully

    Scenario: Invalid request body
        Given a trainer named ""
        And a authentication token for ""
        When I send request for update workload
        Then response status is "400"