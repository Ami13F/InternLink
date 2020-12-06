Feature: See all internships
    As a student
    I want to see a list of all the available internships

    Scenario: user is authenticated
        Given that I am authenticated as a student with email "student@test.com" and password "password"
        When I make a request to receive all the internships
        Then I receive a list containing all the internships