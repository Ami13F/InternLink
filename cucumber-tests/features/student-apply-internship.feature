Feature: Create internship application
    As a student
    I want to be able to apply to an internship

    Background: User is authenticated with a student account
        Given that I am authenticated with email: "student@test.com" and password: "password"

    Scenario: Internship application is open
        Given an internship to which applications are open
        When I make a request to create an application for an open internship
        Then I receive the application to that internship

    Scenario: Internship application is closed
        Given an internship to which applications are closed
        When I make a request to create an application for a closed internship
        Then I receive a message informing me that I am unable to apply