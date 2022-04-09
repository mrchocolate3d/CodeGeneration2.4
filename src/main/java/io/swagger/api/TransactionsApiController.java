package io.swagger.api;

import io.swagger.model.dbTransaction;
import io.swagger.model.dbUser;
import io.swagger.service.TransactionService;
import io.swagger.service.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-06-01T11:41:56.516Z[GMT]")
@Log
@RestController

public class TransactionsApiController implements TransactionsApi {

    private static final Logger log = LoggerFactory.getLogger(TransactionsApiController.class);
    private final ObjectMapper objectMapper;
    private final HttpServletRequest request;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private UserService userService;

    @org.springframework.beans.factory.annotation.Autowired
    public TransactionsApiController(ObjectMapper objectMapper, HttpServletRequest request,TransactionService transactionService) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.transactionService = transactionService;
    }

    public ResponseEntity<List<Transaction>> getTransactions(@NotNull @DecimalMin("1") @Parameter(in = ParameterIn.QUERY, description = "" , required=true, schema=@Schema()) @Valid @RequestParam(value = "IBAN", required = true)
                                                                     String IBAN, @Parameter(in = ParameterIn.QUERY, description = "" , schema=@Schema()) @Valid @RequestParam(value = "fromDate", required = false)
                                                                     OffsetDateTime fromDate, @Parameter(in = ParameterIn.QUERY, description = "" , schema=@Schema()) @Valid @RequestParam(value = "toDate", required = false)
                                                                     OffsetDateTime toDate, @Min(0) @Max(50) @Parameter(in = ParameterIn.QUERY, description = "maximum number of transactions to return" , schema=@Schema(allowableValues={  }, maximum="50", defaultValue="50")) @Valid @RequestParam(value = "limit", required = false, defaultValue="50")
                                                                     Integer limit) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        dbUser user = userService.getUserByUsername(username);

        if(user == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"No authentication token was given");
        }
        List<Transaction> transactionList = new ArrayList<>();
        if(fromDate == null && toDate == null && limit !=0 ){
            //getting transactions for both iban from and iban to
            List<dbTransaction> dbTransactionsFrom = transactionService.getTransactionByIBANfrom(IBAN);
            for (dbTransaction dbFrom : dbTransactionsFrom) {
                Transaction transaction = transactionService.setTransactionsFromDb(dbFrom);
                transactionList.add(transaction);
            }

            List<dbTransaction>dbTransactionTo = transactionService.getTransactionByIBANto(IBAN);
            for(dbTransaction dbTo : dbTransactionTo){
                Transaction transaction = transactionService.setTransactionsFromDb(dbTo);
                transactionList.add(transaction);
            }

        }
        OffsetDateTime dateFrom;
        OffsetDateTime dateTo;

        if(limit == null){
            limit = transactionService.CountAllTransactions();
        }
        if(fromDate == null){
            dateFrom = OffsetDateTime.MIN;
        }
        else{
            dateFrom = OffsetDateTime.parse(fromDate + "00:00:00.001+02:00");
        }
        if(toDate == null){
            dateTo = OffsetDateTime.MAX;
        }
        else{
            dateTo = OffsetDateTime.parse(toDate + "T23:59:59.999+02:00");
        }
        return new ResponseEntity<List<Transaction>>(transactionList,HttpStatus.OK);

    }

    public ResponseEntity<Transaction> makeNewTransaction(@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody Transaction transaction) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        dbUser user = userService.getUserByUsername(username);

        if(user == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"No authentication token was given");
        }
//        if(transaction.getUserPerform() == null || transaction.getAmount() == null ||
//                transaction.getIbANFrom() == null || transaction.getIbANTo()==null){
//            throw new ResponseStatusException(HttpStatus.CREATED,"Input field is missing");
//        }

        dbTransaction tr = new dbTransaction(
                transaction.getUserPerform(),transaction.getIbANTo(),transaction.getIbANFrom(),transaction.getAmount(),OffsetDateTime.now()
        );
        transactionService.createTransaction(tr);
        Transaction transaction1 = transactionService.setTransactionsFromDb(tr);
        return new ResponseEntity<Transaction>(transaction1,HttpStatus.CREATED);

    }

}
