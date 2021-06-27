Feature: User Test
  Scenario: Employee Gets all Users
    When I create an User
    Then I should see http status 201
  Scenario: Retrieve all Users
    When I get all users
    Then I should see http status 200
  Scenario: Retrieve User with limit and username
    When I get all users with limit 1 and username test
    Then I should see http status 200