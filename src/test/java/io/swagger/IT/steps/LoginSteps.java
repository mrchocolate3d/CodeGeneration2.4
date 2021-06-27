package io.swagger.IT.steps;

import io.cucumber.java.en.When;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class LoginSteps {
    private final String baseLoginUrl = "http://localhost:8080/api/login";
    private RestTemplate restTemplate = new RestTemplate();


    @When("i log in with username {string} and password {string}")
    public  void iLogInWithUsernameAndPassword(String username, String password){

    }
}
