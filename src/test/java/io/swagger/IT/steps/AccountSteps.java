package io.swagger.IT.steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

public class AccountSteps {
    RestTemplate template = new RestTemplate();
    ResponseEntity<String> responseEntity;
    String response;
    public HttpEntity<String> httpEntity;
    HttpHeaders headers = new HttpHeaders();
    String baseUrl = "https://localhost:8080/api/";

    @When("I create an account")
    public void iCreateAccount() throws URISyntaxException{
        URI uri = new URI(baseUrl+"Accounts");
        headers.setContentType(MediaType.APPLICATION_JSON);
        String account = "{\"userId\":1,\"accountType\":\"TYPE_CURRENT\"}";
        httpEntity = new HttpEntity<>(account, headers);
        responseEntity = template.exchange(uri, HttpMethod.POST, httpEntity, String.class);
    }
    @Then("I should see http status {int}")
    public void iGetHttpStatusAndToken(int status){
        Assert.assertEquals(responseEntity.getStatusCode(), status);
    }

    @When("I get accounts")
    public void iGetAccounts() throws URISyntaxException{
        URI uri = new URI(baseUrl+"Accounts");
        httpEntity = new HttpEntity<>(null, headers);
        responseEntity = template.exchange(uri, HttpMethod.GET, httpEntity, String.class);
    }

    @When("I get all accounts with limit {int} and username {string}")
    public void iGetAccountsWithLimitAndUsername(String username, int limit) throws URISyntaxException{
        URI uri = new URI(baseUrl+"Accounts");
        headers.add("limit", String.valueOf(limit));
        headers.add("username", String.valueOf(username));
        httpEntity = new HttpEntity<>(null, headers);
        responseEntity = template.exchange(uri, HttpMethod.GET, httpEntity, String.class);
    }

    @When("Retrieve account with iban {string}")
    public void iGetAccountsByIban(String IBAN) throws URISyntaxException{
        URI uri = new URI(baseUrl+IBAN);
        httpEntity = new HttpEntity<>(null, headers);
        responseEntity = template.exchange(uri, HttpMethod.GET, httpEntity, String.class);

    }

    @Then("I delete account with Iban {string}")
    public void iDeleteAccountWithIban(String IBAN) throws Exception{
        URI uri = new URI(baseUrl+"Accounts");
        headers.setContentType(MediaType.APPLICATION_JSON);
        httpEntity = new HttpEntity<>(null, headers);
        responseEntity = template.exchange(uri, HttpMethod.DELETE, httpEntity, String.class);
    }
}
