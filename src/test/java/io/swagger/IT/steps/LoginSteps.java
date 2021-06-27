package io.swagger.IT.steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

public class LoginSteps {
    RestTemplate template = new RestTemplate();
    ResponseEntity<String> responseEntity;
    String response;
    public HttpEntity<String> httpEntity;
    HttpHeaders headers = new HttpHeaders();
    String baseUrl = "https://localhost:8080/api/";


    @When("I log in with username {string} and password {string}")
    public  void iLogInWithUsernameAndPassword(String username, String password) throws URISyntaxException {
        URI uri = new URI(baseUrl+"Login");
        MultiValueMap<String, String> body = new LinkedMultiValueMap();
        headers.add("Content-type", "application/json");
        String login = "{\"username\":\"test\",\"password\":\"test\"}";
        httpEntity = new HttpEntity(login, headers);
        responseEntity = template.exchange(uri, HttpMethod.POST, httpEntity, String.class);

    }

    @Then("I get a http status {int}")
    public void iGetHttpStatusAndToken(int status){
        Assert.assertEquals(responseEntity.getStatusCode(), status);
    }
}
