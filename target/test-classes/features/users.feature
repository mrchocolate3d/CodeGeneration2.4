#noinspection SpellCheckingInspection
  Feature: User tests
    Scenario: Login returns Ok Status
      When i log in with username "test" amd password "test"
      Then i get http code 200

