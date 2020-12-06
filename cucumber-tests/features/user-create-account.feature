Feature: User account creation
    As a user
    I want to be able to create an account

    Scenario: create student account
        Given the student username "student@test.com" and password "password"
        When I make a request to create a student account
        Then I receive back a token representing the new student's credentials

    Scenario: create company account
        Given the company username "company@test.com" and password "password"
        When I make a request to create a company account
        Then I receive back a token representing the new company's credentials