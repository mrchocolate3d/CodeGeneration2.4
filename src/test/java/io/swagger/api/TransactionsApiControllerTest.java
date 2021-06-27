package io.swagger.api;

import io.swagger.model.*;
import io.swagger.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneOffset;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionsApiControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private TransactionService transactionService;
    dbTransaction dbtransaction;
    @Autowired
    TransactionsApiController transactionsApiController;
    List<dbTransaction>expectedTransactionsByIBAN;

    @BeforeEach
    public void init() throws Exception {
        dbtransaction = new dbTransaction(
          "Testuser","NL01INH0000000001","NL01INH0000000002",400.00, OffsetDateTime.of(2021,5,1,10,20,40,0, ZoneOffset.UTC)
        );

    }
    @Test
    public void getTransactionsShouldReturnJsonArray() throws Exception{
        //given(transactionService.getTransactionByIBAN())


//        given(accountService.getAllAccounts()).willReturn(List.of(dbAccount));
//
//        String token = getTokenWhenLoggingIn();
//
//        this.mockMvc.perform (
//                get("/Accounts").header("Authorization", "Bearer " + token))
//                .andExpect(
//                        status().isOk())
//                .andExpect(jsonPath("$", hasSize(1))
//                );
//
//
//    }
    }




}
