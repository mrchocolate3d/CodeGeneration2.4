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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

    public ResponseEntity<Void> closeAccount(@Parameter(in = ParameterIn.PATH, description = "The UserID of the account required", required=true, schema=@Schema()) @PathVariable("UserID") String userID) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }


//    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<ReturnAccount> createAccount(@Parameter(in = ParameterIn.DEFAULT, description = "account object is created", required=true, schema=@Schema()) @Valid @RequestBody InsertAccount account) {

        dbUser user = userService.getUserById(account.getUserId());

        if(user != null){

            dbAccount dbAccount = new dbAccount();
            dbAccount.setUser(user);
            if(account.getAccountType().equals("TYPE_CURRENT")) {
                AccountType accountType = AccountType.TYPE_CURRENT;
                dbAccount.setAccountType(accountType);
            }else if(account.getAccountType().equals("TYPE_SAVING")){
                AccountType accountType = AccountType.TYPE_SAVING;
                dbAccount.setAccountType(accountType);
            }
            dbAccount accountAdded = accountService.add(user, account.getAccountType());

            ReturnAccount returnAccount = new ReturnAccount();
            returnAccount.setAccountType(accountAdded.getAccountType());
            returnAccount.setIBAN(accountAdded.getIban());
            returnAccount.setUserId(accountAdded.getUser().getId());


            return new ResponseEntity<ReturnAccount>(HttpStatus.OK);
        }


        return new ResponseEntity<ReturnAccount>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Deposit> depositMoney(@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("IBAN") String IBAN,@NotNull @DecimalMin("0.01") @DecimalMax("10000") @Parameter(in = ParameterIn.QUERY, description = "The amount to deposit" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "amount", required = true) Double amount) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<Deposit>(objectMapper.readValue("{\n  \"IBAN\" : \"NL40INGB778321\",\n  \"Amount\" : 0.8008281904610115\n}", Deposit.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Deposit>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Deposit>(HttpStatus.NOT_IMPLEMENTED);
    }

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
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

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
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
        }else if(username != null){
            for(dbAccount dbAccount : dbAccounts){
                if(dbAccount.getUser().getUsername().equals(username)){


                    User user = setUserFromDTO(dbAccount);

                    Account account = setAccountFromDb(dbAccount, user);

                    accounts.add(account);

                }
                continue;
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
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<ReturnBalance>(objectMapper.readValue("{\n  \"IBAN\" : \"NL90RABO34\",\n  \"balance\" : 500,\n  \"accountType\" : \"CurrentAccount\"\n}", ReturnBalance.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<ReturnBalance>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<ReturnBalance>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Withdrawal> withdrawal(@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("IBAN") String IBAN,@NotNull @DecimalMin("0.01") @DecimalMax("10000") @Parameter(in = ParameterIn.QUERY, description = "The amount to withdraw" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "amount", required = true) Double amount) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<Withdrawal>(objectMapper.readValue("{\n  \"IBAN\" : \"NL90RABO34567763\",\n  \"Amount\" : 0.8008281904610115\n}", Withdrawal.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Withdrawal>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Withdrawal>(HttpStatus.NOT_IMPLEMENTED);
    }

}
