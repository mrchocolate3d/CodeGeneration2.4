package io.swagger.api;

import io.swagger.model.Account;
import io.swagger.model.User;
import io.swagger.model.dbAccount;
import io.swagger.model.dbUser;
import io.swagger.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

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

    @BeforeEach
    public void init(){
        dbAccount = new dbAccount();
        account = new Account();
    }

    @Test
    public void getAllAccountsShouldReturnAJsonArray() throws Exception{
        given(accountService.getAllAccounts()).willReturn(List.of());
    }
}