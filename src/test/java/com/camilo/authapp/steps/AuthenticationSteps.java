package com.camilo.authapp.steps;

import com.camilo.authapp.model.User;
import com.camilo.authapp.service.AuthResult;
import com.camilo.authapp.service.AuthService;
import com.camilo.authapp.service.PasswordHasher;
import com.camilo.authapp.support.InMemoryUserRepository;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuthenticationSteps {
    private InMemoryUserRepository repository;
    private AuthService authService;
    private AuthResult lastResult;

    @Before
    public void setUp() {
        repository = new InMemoryUserRepository();
        authService = new AuthService(repository, new PasswordHasher());
    }

    @Given("the system is empty")
    public void theSystemIsEmpty() {
        repository = new InMemoryUserRepository();
        authService = new AuthService(repository, new PasswordHasher());
    }

    @Given("a registered user {string} with password {string}")
    public void aRegisteredUserWithPassword(String username, String password) {
        User user = new User(username, "Camilo Example", new PasswordHasher().hash(password));
        repository.save(user);
    }

    @When("I register {string} with name {string} password {string} and confirmation {string}")
    public void iRegisterWithNamePasswordAndConfirmation(String username, String fullName, String password, String confirmation) {
        lastResult = authService.register(username, fullName, password, confirmation);
    }

    @When("I log in with username {string} and password {string}")
    public void iLogInWithUsernameAndPassword(String username, String password) {
        lastResult = authService.login(username, password);
    }

    @Then("registration should be successful")
    public void registrationShouldBeSuccessful() {
        assertTrue(lastResult.success());
        assertEquals("Registro exitoso", lastResult.message());
    }

    @Then("the user {string} should exist")
    public void theUserShouldExist(String username) {
        assertTrue(repository.existsByUsername(username));
    }

    @Then("login should be successful")
    public void loginShouldBeSuccessful() {
        assertTrue(lastResult.success());
        assertEquals("Login exitoso", lastResult.message());
    }

    @Then("authentication should fail with message {string}")
    public void authenticationShouldFailWithMessage(String message) {
        assertTrue(!lastResult.success());
        assertEquals(message, lastResult.message());
    }
}