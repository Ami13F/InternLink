Feature: User Login
    As a user
    I want to be able to login into the system

    Scenario: already registered user
        Given a valid username "student@test.com" and password "password"
        When I make a request to login into the system as a registered user
        Then I receive a token representing my user session

    Scenario: invalid user credentials
        Given a username "test@test.com" and password "password"
        When I make a request to login into the system with invalid credentials
        Then I receive a message informing me that the credentials are invalid