package io.swagger.IT.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.model.LoggedIn;
import io.swagger.model.LoginUser;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Assert;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

public class TransactionSteps {
    private final String baseTransactionUrl = "https://localhost:8080/api/Transactions";
    private RestTemplate restTemplate = new RestTemplate();
    private HttpClientErrorException httpClientErrorException;
    private ObjectMapper objectMapper = new ObjectMapper();
    private ResponseEntity<String>responseEntity;

    private String getJwtToken(String username, String password) throws URISyntaxException, JsonProcessingException{
        URI uri  = new URI(baseTransactionUrl + "/login");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        LoginUser login = new LoginUser();
        login.setUsername(username);
        login.setPassword(password);
        String requestBody = objectMapper.writeValueAsString(login);

        HttpEntity<String> entity = new HttpEntity<>(requestBody,headers);
        ResponseEntity<LoggedIn> response = restTemplate.postForEntity(uri,entity,LoggedIn.class);
        return "Bearer " + Objects.requireNonNull(response.getBody().getToken());
    }
    @When("User makes a request to the /Transactions API without authentication token")
    public void requestWithoutAuthToken()throws URISyntaxException{
        URI uri = new URI(baseTransactionUrl + "/Transactions");
        restTemplate.getForEntity(uri,Exception.class);
    }
    @Then("The server will return error")
    public void serverReturnsError(int length) throws JSONException{
        JSONArray jsonArray = new JSONArray(responseEntity.getBody());
        Assert.assertEquals(length,jsonArray.length());
    }

    @When("A user makes a POST request to /Transactions API endpoint")
    public void userMakesPostRequestToAPIEndpoint() throws JsonProcessingException, URISyntaxException{
        //start by creating the request
        URI uri = new URI(baseTransactionUrl + "/Transactions");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization",getJwtToken("test","test"));
        //getting the request body
        String requestBody = "{\"IBANFrom\":\"NL20INHO8088264065\",\"IBANTo\":\"NL30INHO8088264065\",\"amount\":500,\"userPerform\":\"Testusername\"}";
        //performing the request
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        responseEntity = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
    }
    @Then("Server will return an {int} unauthorized error status")
    public void serverReturnsUnauthorizedError(int status){
        Assert.assertNotNull(httpClientErrorException);
        Assert.assertEquals(status,httpClientErrorException.getStatusCode());
    }
}
