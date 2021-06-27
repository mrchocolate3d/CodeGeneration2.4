Feature: User tests
  Scenario: Login User status returns OK
    When i log in with username "test" and password "test"
    Then i get http code 200


  Scenario: Employee Gets all Users
    When i log in with username "test" and password "test"
    And  I get all user
    Then i get http code 200