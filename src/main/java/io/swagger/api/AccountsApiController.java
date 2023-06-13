package io.swagger.api;

import io.swagger.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.service.AccountService;
import io.swagger.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Log
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-06-01T11:41:56.516Z[GMT]")
@RestController
public class AccountsApiController implements AccountsApi {

    private static final Logger log = LoggerFactory.getLogger(AccountsApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    public AccountsApiController(ObjectMapper objectMapper, HttpServletRequest request, UserService userService, AccountService accountService) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.userService = userService;
        this.accountService = accountService;
    }

    @org.springframework.beans.factory.annotation.Autowired
    public AccountsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<Double> GetTotalBalanceInAccounts(){
        return new ResponseEntity<Double>(accountService.GetTotalBalanceInAccounts(), HttpStatus.OK);
    }

    public ResponseEntity<Void> closeAccount(@Parameter(in = ParameterIn.PATH, description = "The IBAN of the account required", required=true, schema=@Schema()) @PathVariable("IBAN") String IBAN) {
        accountService.CloseAccount(IBAN);
        return new ResponseEntity<>(HttpStatus.OK);

    }
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_BANK')")
    public ResponseEntity<Void> editAccountAbsoluteLimit(@Parameter(in = ParameterIn.DEFAULT, description = "Account object is created", required=true, schema=@Schema()) @Valid @RequestBody EditAbsoluteLimit editAbsoluteLimit){
        accountService.EditAccountAbsoluteLimit(editAbsoluteLimit);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_BANK')")
    public ResponseEntity createAccount(@Parameter(in = ParameterIn.DEFAULT, description = "Account object is created", required=true, schema=@Schema()) @Valid @RequestBody InsertAccount account) {
        accountService.CreateAccount(account);
        return new ResponseEntity(HttpStatus.CREATED);
    }


    public ResponseEntity<Account> getAccountByIban(@Parameter(in = ParameterIn.PATH, description = "IBAN information", required=true, schema=@Schema()) @PathVariable("IBAN") String IBAN) {
        return new ResponseEntity<Account>(accountService.GetAccountByIban(IBAN), HttpStatus.OK);

    }

    public ResponseEntity<List<Account>> getAllAccountsByCustomer(){
        return new ResponseEntity<List<Account>>(accountService.GetAllAccountsByCustomer(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_BANK')")
    public ResponseEntity<List<Account>> getAccounts(@Min(0) @Max(50) @Parameter(in = ParameterIn.QUERY, description = "maximum number of records to return" ,schema=@Schema(allowableValues={  }, maximum="50"
            , defaultValue="50")) @Valid @RequestParam(value = "limit", required = false, defaultValue="50") Integer limit,@Parameter(in = ParameterIn.QUERY, description = "Find Account by username" ,schema=@Schema()) @Valid @RequestParam(value = "username", required = false) String username) {
        return new ResponseEntity<List<Account>>(accountService.GetAccounts(limit, username), HttpStatus.OK);
    }

    public ResponseEntity<ReturnBalance> getBalanceByIban(@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("IBAN") String IBAN) {
        return new ResponseEntity<ReturnBalance>(accountService.GetBalanceByIban(IBAN), HttpStatus.OK);

    }

    public ResponseEntity<Deposit> depositMoney(@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("IBAN") String IBAN,@NotNull @DecimalMin("0.01") @DecimalMax("10000") @Parameter(in = ParameterIn.QUERY, description = "The amount to deposit" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "amount", required = true) Double amount) throws Exception {
            return new ResponseEntity<Deposit>(accountService.DepositMoney(IBAN, amount), HttpStatus.OK);
    }

    public ResponseEntity<Withdrawal> withdrawal(@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("IBAN") String IBAN,@NotNull @DecimalMin("0.01") @DecimalMax("10000") @Parameter(in = ParameterIn.QUERY, description = "The amount to withdraw" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "amount", required = true) Double amount) throws Exception {
        return new ResponseEntity<Withdrawal>(accountService.WithdrawMoney(IBAN, amount), HttpStatus.OK);

    }
}
