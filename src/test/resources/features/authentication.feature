Feature: Authentication

  Scenario: Register a new user successfully
    Given the system is empty
    When I register "camilo" with name "Camilo Perez" password "Secret123!" and confirmation "Secret123!"
    Then registration should be successful
    And the user "camilo" should exist

  Scenario: Login with valid credentials
    Given a registered user "camilo" with password "Secret123!"
    When I log in with username "camilo" and password "Secret123!"
    Then login should be successful

  Scenario: Login with invalid password
    Given a registered user "camilo" with password "Secret123!"
    When I log in with username "camilo" and password "wrong"
    Then authentication should fail with message "Credenciales inválidas"

  Scenario: Register fails when passwords do not match
    Given the system is empty
    When I register "maria" with name "Maria Gomez" password "Secret123!" and confirmation "Wrong123!"
    Then authentication should fail with message "Las contraseñas no coinciden"