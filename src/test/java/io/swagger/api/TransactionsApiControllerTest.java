package io.swagger.api;

import io.swagger.model.*;
import io.swagger.models.Response;
import io.swagger.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
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


    @Autowired
    TransactionsApiController transactionsApiController;
    List<dbTransaction>transactionList;

    @BeforeEach
    public void init() throws Exception {

        dbTransaction testuser = new dbTransaction();
        testuser.setUserPerform("Testuser");
        testuser.setTimestamp(OffsetDateTime.of(2021,5,1,10,20,40,0, ZoneOffset.UTC));
        testuser.setIBANfrom("NL01INH0000000002");
        testuser.setIBANto("NL01INH0000000001");
        testuser.setAmount(400.00);

        transactionList.add(testuser);

    }
    @Test
    public void getTransactionsShouldReturnJsonArray() throws Exception{
        given(transactionService.getTransactionByIBAN(transactionList.get(2).toString())).willReturn(transactionList);

        OffsetDateTime dateFrom = OffsetDateTime.of(2020,8,1,1,1,1,0,ZoneOffset.UTC);
        OffsetDateTime dateTo = OffsetDateTime.of(2020,9,1,1,1,1,0,ZoneOffset.UTC);

        List<dbTransaction> tr = (List<dbTransaction>) transactionsApiController.getTransactions(transactionList.get(2).toString(),dateFrom,dateTo,0);

    }




}
