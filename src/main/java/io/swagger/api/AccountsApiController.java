package io.swagger.api;

import io.swagger.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.service.AccountService;
import io.swagger.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<Void> closeAccount(@Parameter(in = ParameterIn.PATH, description = "The IBAN of the account required", required=true, schema=@Schema()) @PathVariable("IBAN") String IBAN) {
        accountService.closeAccount(IBAN);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_BANK')")
    public ResponseEntity<ReturnAccount> createAccount(@Parameter(in = ParameterIn.DEFAULT, description = "account object is created", required=true, schema=@Schema()) @Valid @RequestBody InsertAccount account) {

        dbUser user = userService.getUserById(account.getUserId());

        if(user != null){
            dbAccount dbAccount = new dbAccount();
            ReturnAccount returnAccount = new ReturnAccount();
            returnAccount.setUserId(user.getId());
            dbAccount.setUser(user);
            AccountType accountType = AccountType.TYPE_CURRENT;
            if(account.getAccountType().equals("TYPE_CURRENT")) {
                accountType = AccountType.TYPE_CURRENT;
            }else if(account.getAccountType().equals("TYPE_SAVING")){
                accountType = AccountType.TYPE_SAVING;
            }
            returnAccount.setAccountType(accountType);
            dbAccount.setAccountType(returnAccount.getAccountType());
            returnAccount.setIBAN(dbAccount.getIban());
            dbAccount accountAdded = accountService.add(user, account.getAccountType());

            return new ResponseEntity<ReturnAccount>(HttpStatus.CREATED);
        }
        return new ResponseEntity<ReturnAccount>(HttpStatus.NOT_FOUND);
    }


    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_BANK')")
    public ResponseEntity<Account> getAccountByIban(@Parameter(in = ParameterIn.PATH, description = "iban needed for finding", required=true, schema=@Schema()) @PathVariable("IBAN") String IBAN) {
        dbAccount dbAccount = accountService.getAccountByIban(IBAN);
        if(dbAccount != null){
            User user = new User();
            dbUser dbUser = dbAccount.getUser();
            user.setEmail(dbUser.getEmail());
            user.setFirstName(dbUser.getFirstName());
            user.setId(dbUser.getId());
            user.setLastName(dbUser.getLastName());
            user.setPhone(dbUser.getPhone());
            user.setTransactionLimit(dbUser.getTransactionLimit());
            user.setUsername(dbUser.getUsername());



            Account account = new Account();
            account.setAccountType(dbAccount.getAccountType());
            account.setUser(user);
            account.setIban(dbAccount.getIban());

            return new ResponseEntity<Account>(account, HttpStatus.OK);

        }
        return new ResponseEntity<Account>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_BANK')")
    public ResponseEntity<List<Account>> getAccounts(@Min(0) @Max(50) @Parameter(in = ParameterIn.QUERY, description = "maximum number of records to return" ,schema=@Schema(allowableValues={  }, maximum="50"
            , defaultValue="50")) @Valid @RequestParam(value = "limit", required = false, defaultValue="50") Integer limit,@Parameter(in = ParameterIn.QUERY, description = "Find Account by username" ,schema=@Schema()) @Valid @RequestParam(value = "username", required = false) String username) {
        List<dbAccount> dbAccounts = accountService.getAllAccounts();
        List<Account> accounts = new ArrayList<>();
        if(limit == null && username == null) {
            for (dbAccount dbAccount : dbAccounts) {
                User user = setUserFromDTO(dbAccount);
                Account account = setAccountFromDb(dbAccount, user);
                accounts.add(account);
            }
        }else if(limit != null && username == null){
            int count = 0;
            for (dbAccount dbAccount : dbAccounts) {
                if (count >= limit){
                    break;
                }
                User user = setUserFromDTO(dbAccount);
                Account account = setAccountFromDb(dbAccount, user);
                accounts.add(account);
                count++;
            }
        }else if(username != null && limit == null){
            for(dbAccount dbAccount : dbAccounts){
                if(dbAccount.getUser().getUsername().equals(username)){
                    User user = setUserFromDTO(dbAccount);
                    Account account = setAccountFromDb(dbAccount, user);
                    accounts.add(account);
                }
                continue;
            }
        }else if(username != null && limit != null){
            int count = 0;

            for (dbAccount dbAccount : dbAccounts) {
                if (count >= limit){
                    break;
                }
                if(dbAccount.getUser().getUsername().equals(username)){
                User user = setUserFromDTO(dbAccount);
                Account account = setAccountFromDb(dbAccount, user);
                accounts.add(account);
                }
                count++;
            }
        }

        return new ResponseEntity<List<Account>>(accounts, HttpStatus.OK);
    }

    public User setUserFromDTO(dbAccount dbAccount){
        User user = new User();
        dbUser dbUser = dbAccount.getUser();
        user.setEmail(dbUser.getEmail());
        user.setFirstName(dbUser.getFirstName());
        user.setId(dbUser.getId());
        user.setLastName(dbUser.getLastName());
        user.setPhone(dbUser.getPhone());
        user.setTransactionLimit(dbUser.getTransactionLimit());
        user.setUsername(dbUser.getUsername());

        return user;
    }

    public Account setAccountFromDb(dbAccount dbAccount, User user){
        Account account = new Account();
        account.setAccountType(dbAccount.getAccountType());
        account.setUser(user);
        account.setIban(dbAccount.getIban());

        return account;
    }


    public ResponseEntity<ReturnBalance> getBalanceByIban(@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("IBAN") String IBAN) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        dbUser userFromDB = userService.getUserByUsername(auth.getName());

        dbAccount account = accountService.getBalance(IBAN);
        if(account != null && userFromDB.getUsername().equals(account.getUser().getUsername())){
            ReturnBalance balance = new ReturnBalance();
            balance.setIBAN(account.getIban());
            balance.setAccountType(account.getAccountType());
            balance.setBalance(account.getBalance());
            return new ResponseEntity<ReturnBalance>(balance, HttpStatus.OK);
        }
        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Account not found or Not your account");
    }

    public ResponseEntity<Deposit> depositMoney(@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("IBAN") String IBAN,@NotNull @DecimalMin("0.01") @DecimalMax("10000") @Parameter(in = ParameterIn.QUERY, description = "The amount to deposit" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "amount", required = true) Double amount) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        dbUser user = userService.getUserByUsername(auth.getName());
        dbAccount account = accountService.getAccountByIban(IBAN);
        if (account.getUser().getUsername().equals(user.getUsername())){
            Deposit updatedAccount = accountService.deposit(IBAN, amount);
            return new ResponseEntity<Deposit>(updatedAccount, HttpStatus.OK);
        }
        else
            return new ResponseEntity<Deposit>(HttpStatus.NOT_ACCEPTABLE);
    }

    public ResponseEntity<Withdrawal> withdrawal(@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("IBAN") String IBAN,@NotNull @DecimalMin("0.01") @DecimalMax("10000") @Parameter(in = ParameterIn.QUERY, description = "The amount to withdraw" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "amount", required = true) Double amount) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        dbUser user = userService.getUserByUsername(auth.getName());
        dbAccount account = accountService.getAccountByIban(IBAN);
        if (account.getUser().getUsername().equals(user.getUsername())) {
            Withdrawal updatedAccount = accountService.withdraw(IBAN, amount);
            return new ResponseEntity<Withdrawal>(updatedAccount, HttpStatus.OK);
        }
        else
            return new ResponseEntity<Withdrawal>(HttpStatus.NOT_ACCEPTABLE);
    }
}
