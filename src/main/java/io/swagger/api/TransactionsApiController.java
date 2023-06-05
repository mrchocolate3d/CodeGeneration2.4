package io.swagger.api;

import io.swagger.model.*;
import io.swagger.service.AccountService;
import io.swagger.service.TransactionService;
import io.swagger.service.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
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
import java.time.LocalDate;
import java.util.List;

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
    @Autowired
    private AccountService accountService;

    @org.springframework.beans.factory.annotation.Autowired
    public TransactionsApiController(ObjectMapper objectMapper, HttpServletRequest request,TransactionService transactionService) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.transactionService = transactionService;
    }

    public ResponseEntity<List<Transaction>> getTransactions(@NotNull @DecimalMin("1") @Parameter(in = ParameterIn.QUERY, description = "" , required=true, schema=@Schema()) @Valid @RequestParam(value = "IBAN", required = true)
                                                                     String IBAN, @Parameter(in = ParameterIn.QUERY, description = "" , schema=@Schema()) @DateTimeFormat(pattern= "yyyy-MM-dd") @Valid @RequestParam(value = "fromDate", required = false)
    LocalDate fromDate, @Parameter(in = ParameterIn.QUERY, description = "" , schema=@Schema()) @Valid @DateTimeFormat(pattern= "yyyy-MM-dd") @RequestParam(value = "toDate", required = false)
    LocalDate toDate, @Min(0) @Max(50) @Parameter(in = ParameterIn.QUERY, description = "maximum number of transactions to return" , schema=@Schema(allowableValues={  }, maximum="50")) @Valid @RequestParam(value = "limit", required = false)
                                                                     Integer limit) {
        List<Transaction> response = transactionService.getTransactions(IBAN, fromDate, toDate, limit);
        return new ResponseEntity<List<Transaction>>(response, HttpStatus.OK);
    }

    public ResponseEntity<Transaction> makeNewTransaction(@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody Transaction transaction) {
        return new ResponseEntity<Transaction>(transactionService.createTransaction(transaction), HttpStatus.CREATED);
    }
}
