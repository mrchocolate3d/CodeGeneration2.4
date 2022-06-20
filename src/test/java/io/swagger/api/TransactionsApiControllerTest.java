package io.swagger.api;

import com.fasterxml.jackson.datatype.jsr310.ser.OffsetTimeSerializer;
import io.swagger.model.*;
import io.swagger.models.Response;
import io.swagger.service.TransactionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.threeten.bp.OffsetDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
    private TransactionsApiController transactionsApiController;
    private dbTransaction transaction;
    List<dbTransaction> transactionList;
    @Autowired
    private TransactionService transactionService;



    @BeforeEach
    public void setup() throws Exception {

        transaction = new dbTransaction(
                "testuser","NL01INH0000000000","NL02INH0000000000",500.00,transactionService.getDateToString()
        );

    }
    @Test
    public void getTransactionsByIBANShouldReturnJsonArray() throws Exception{
//        OffsetDateTime today = OffsetDateTime.now();
//        OffsetDateTime dateFrom = OffsetDateTime.parse(today + "T00:00:00.001+02:00");
//        OffsetDateTime dateTo = OffsetDateTime.parse(today + "T23:59:59.999+02:00");
//        List<dbTransaction> transactionList = (List<dbTransaction>) transactionsApiController.getTransactions(transaction.getIBANfrom(),dateFrom,dateTo,50);
//        assertEquals(transactionList.get(0).getTimestamp(),today.getDayOfWeek());

    }




}
