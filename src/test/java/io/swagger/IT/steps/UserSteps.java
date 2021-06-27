package io.swagger.IT.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.model.*;
import io.swagger.model.User;
import org.junit.Assert;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

public class UserSteps {
    RestTemplate template = new RestTemplate();
    ResponseEntity<String> responseEntity;
    String response;
    public HttpEntity<String> httpEntity;
    HttpHeaders headers = new HttpHeaders();
    String baseUrl = "https://localhost:8080/api/";

    @When("I create an User")
    public void iCreateUser() throws URISyntaxException {
        URI uri = new URI(baseUrl+"Accounts");
        headers.setContentType(MediaType.APPLICATION_JSON);
        String account = "{\"firstname\":seyifunmi,\"roles\":\"ROLE_EMPLOYEE\"lastname\":gandonu,\"password\":dogowner123,\"phone\":06348273392,\"email\":seyifunmi@outlook.com,\"username\":seyiG,\"transactionLimit\":2000,\"}";
        httpEntity = new HttpEntity<>(account, headers);
        responseEntity = template.exchange(uri, HttpMethod.POST, httpEntity, String.class);
    }
    @Then("I should see http status {int}")
    public void iGetHttpStatusAndToken(int status){
        Assert.assertEquals(responseEntity.getStatusCode(), status);
    }

    @When("I get all users")
    public void iGetUsers() throws URISyntaxException{
        URI uri = new URI(baseUrl+"Users");
        httpEntity = new HttpEntity<>(null, headers);
        responseEntity = template.exchange(uri, HttpMethod.GET, httpEntity, String.class);
    }

    @When("I get all users with limit {int} and username {string}")
    public void iGetUsersWithLimitAndUsername(int limit) throws URISyntaxException{
        URI uri = new URI(baseUrl+"Users");
        headers.add("limit", String.valueOf(limit));
        httpEntity = new HttpEntity<>(null, headers);
        responseEntity = template.exchange(uri, HttpMethod.GET, httpEntity, String.class);
    }



}
