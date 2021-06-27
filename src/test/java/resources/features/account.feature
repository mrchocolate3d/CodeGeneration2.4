Feature: Account Test
  Scenario: Create account return OK
    When I create an account
    Then I should see http status 201
  Scenario: Retrieve all account
    When I get accounts
    Then I should see http status 200
  Scenario: Retrieve account with limit and username
    When I get all accounts with limit 1 and username test
    Then I should see http status 200
  Scenario: Retrieve account with iban
    When I get account with Iban ""
    Then I should see http status 200