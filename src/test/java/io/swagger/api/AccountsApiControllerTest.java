package io.swagger.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.*;
import io.swagger.service.AccountService;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import java.util.*;

@SpringBootTest
@AutoConfigureMockMvc
class AccountsApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    private Account account;

    private dbAccount dbAccount;

    private dbUser dbUser;

    private Deposit deposit;

    private Withdrawal withdrawal;

    private String IBAN;

    private Double delta = 0.001;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void init() throws Exception {
/*        dbAccount = new dbAccount(AccountType.TYPE_CURRENT, 0, new dbUser(
                "test", "test", "test", "test", "test", passwordEncoder.encode("test"), List.of(UserRole.ROLE_EMPLOYEE), 2500, 2500
        ), 0);
        dbAccount.setIban("NL02INHO14665901");*/
    }

    @Test
    public void getAccountsShouldReturnAJsonArray() throws Exception{
        given(accountService.getAllAccounts()).willReturn(List.of(dbAccount));

        String token = getTokenWhenLoggingIn();

        this.mockMvc.perform (
                get("/Accounts").header("Authorization", "Bearer " + token))
                .andExpect(
                        status().isOk())
                .andExpect(jsonPath("$", hasSize(1))
                );


    }

    @Test
    public void createAnAccountShouldReturnStatusCodeCreated201() throws Exception{
        String token = getTokenWhenLoggingIn();
        Object createAccount = new Object(){
            public final int userId = 1;
            public final String accountType = "TYPE_CURRENT";
        };
        ObjectMapper createAccountMapper = new ObjectMapper();
        String json = createAccountMapper.writeValueAsString(createAccount);
        this.mockMvc.perform(
                post("/Accounts").header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());

    }

    public String getTokenWhenLoggingIn() throws Exception {
        Object login = new Object(){
            public final String username = "test";
            public final String password = "test";
        };
        ObjectMapper loginMapper = new ObjectMapper();
        String json = loginMapper.writeValueAsString(login);


        MvcResult result = this.mockMvc.perform(
                post("/Login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        JSONObject contentInJson = new JSONObject(content);
        return contentInJson.getString("Token");
    }

    @Test
    public void closedAccountShouldBeInactive() throws Exception{
        accountService.closeAccount(IBAN);
        assertEquals(accountService.getAccountByIban("NL02INHO14697882"), dbAccount);
    }

    @Test
    public void getBalanceByIban() throws Exception{
        accountService.createAccount(dbUser);
        assertEquals(accountService.getBalance(IBAN), dbAccount);
    }

    @Test
    public void depositTest() throws Exception{
        accountService.deposit("NL01INHO1547654890", 500.00);
        assertEquals(dbAccount.getBalance(), 5500.00, delta, "Deposit Test");
    }

    @Test
    public void withdrawTest() throws Exception{
        accountService.withdraw("NL01INHO1547654890", 500.00);
        assertEquals(dbAccount.getBalance(),4500.00, delta, "Withdrawal Test");
    }

}