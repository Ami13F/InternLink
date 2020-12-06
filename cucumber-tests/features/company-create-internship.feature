Feature: Create internship
    As a company
    I want to be able to create internships

    Background: User is authenticated with a company account
        Given that I am authenticated with email: "company@test.com" and password: "password"

    Scenario:
        Given the internship details
        When I make a request to create a new internship
        Then I receive an object containing the internship details