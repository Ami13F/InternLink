Feature: Accept candidate application
    As a company
    I want to be able to accept a candidate's application to one of my internships

    Background: User is authenticated with a company account
        Given that I am authenticated with email: "company@test.com" and password: "password"

    Scenario:
        Given a candidate application to one of my internships
        When I make a request to update the state of the application to "ACCEPTED"
        Then I receive back the updated application