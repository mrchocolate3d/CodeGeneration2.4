package io.swagger.api;

import io.swagger.model.User;
import io.swagger.model.dbTransaction;
import io.swagger.model.dbUser;
import io.swagger.service.TransactionService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.threeten.bp.OffsetDateTime;
import io.swagger.model.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-06-01T11:41:56.516Z[GMT]")
@Log
@RestController
@RequestMapping(value = "transactions")
public class TransactionsApiController implements TransactionsApi {

    private static final Logger log = LoggerFactory.getLogger(TransactionsApiController.class);
    private final ObjectMapper objectMapper;
    private final HttpServletRequest request;
    @Autowired
    private TransactionService transactionService;

    @org.springframework.beans.factory.annotation.Autowired
    public TransactionsApiController(ObjectMapper objectMapper, HttpServletRequest request,TransactionService transactionService) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.transactionService = transactionService;
    }

    //@PreAuthorize("hasRole('CUSTOMER') or hasRole('EMPLOYEE')")
//    @RequestMapping(value = "",method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Transaction>> getTransactions(@NotNull @DecimalMin("1") @Parameter(in = ParameterIn.QUERY, description = "" , required=true, schema=@Schema()) @Valid @RequestParam(value = "IBAN", required = true)
                                                               String IBAN, @Parameter(in = ParameterIn.QUERY, description = "" , schema=@Schema()) @Valid @RequestParam(value = "fromDate", required = false)
            OffsetDateTime fromDate, @Parameter(in = ParameterIn.QUERY, description = "" , schema=@Schema()) @Valid @RequestParam(value = "toDate", required = false)
            OffsetDateTime toDate, @Min(0) @Max(50) @Parameter(in = ParameterIn.QUERY, description = "maximum number of transactions to return" , schema=@Schema(allowableValues={  }, maximum="50", defaultValue="50")) @Valid @RequestParam(value = "limit", required = false, defaultValue="50")
            Integer limit) {

        String accept = request.getHeader("Accept");
        if(accept!=null && accept.contains("application/json")){
            List<Transaction> transactionList = new ArrayList<>();
            List<dbTransaction> dbTransactions = transactionService.getTransactions(IBAN,fromDate,toDate,limit);
            //Transaction transaction = setTransactionsFromDb(dbTransactions);
            for (dbTransaction tr : dbTransactions){
                Transaction transaction = setTransactionsFromDb(tr);
                transactionList.add(transaction);
            }
            return new ResponseEntity<List<Transaction>>(transactionList,HttpStatus.OK);

        }
        else{
            return new ResponseEntity<List<Transaction>>(HttpStatus.BAD_REQUEST);
        }
    }

//    @PreAuthorize("hasRole('CUSTOMER') or hasRole('EMPLOYEE')")
//    @RequestMapping(value = "" ,method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<dbTransaction> makeNewTransaction(@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody dbTransaction transaction) {
        String accept = request.getHeader("Accept");
        if(accept!=null && accept.contains("application/json")){
           transactionService.createTransaction(transaction);
            return new ResponseEntity<dbTransaction>(transaction,HttpStatus.CREATED); //complete
        }
        else{
            return new ResponseEntity<dbTransaction>(HttpStatus.BAD_REQUEST);
        }
    }

    public Transaction setTransactionsFromDb(dbTransaction dbTransaction){
        Transaction transaction = new Transaction();
        transaction.setTime(dbTransaction.getTimestamp());
        transaction.setIbANFrom(dbTransaction.getIBANfrom());
        return transaction;
    }
}
