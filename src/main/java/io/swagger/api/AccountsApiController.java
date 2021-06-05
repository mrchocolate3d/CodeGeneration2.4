package io.swagger.api;

import io.swagger.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-06-01T11:41:56.516Z[GMT]")
@RestController
public class AccountsApiController implements AccountsApi {

    private static final Logger log = LoggerFactory.getLogger(AccountsApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    private AccountService accountService;

    @org.springframework.beans.factory.annotation.Autowired
    public AccountsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<Void> closeAccount(@Parameter(in = ParameterIn.PATH, description = "The UserID of the account required", required=true, schema=@Schema()) @PathVariable("UserID") String userID) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<ReturnAccount> createAccount(@Parameter(in = ParameterIn.DEFAULT, description = "account object is created", required=true, schema=@Schema()) @Valid @RequestBody Account body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<ReturnAccount>(objectMapper.readValue("{\n  \"IBAN\" : \"NL07RABO0258798420\",\n  \"accountType\" : \"CurrentAccount\",\n  \"userId\" : 1\n}", ReturnAccount.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<ReturnAccount>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<ReturnAccount>(HttpStatus.NOT_IMPLEMENTED);
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

    public ResponseEntity<Account> getAccountByIban(@Parameter(in = ParameterIn.PATH, description = "iban needed for finding", required=true, schema=@Schema()) @PathVariable("IBAN") String IBAN) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<Account>(objectMapper.readValue("{\n  \"UserId\" : 1,\n  \"accountType\" : \"CurrentAccount\"\n}", Account.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Account>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Account>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<List<Account>> getAccounts(@Min(0) @Max(50) @Parameter(in = ParameterIn.QUERY, description = "maximum number of records to return" ,schema=@Schema(allowableValues={  }, maximum="50"
, defaultValue="50")) @Valid @RequestParam(value = "limit", required = false, defaultValue="50") Integer limit,@Parameter(in = ParameterIn.QUERY, description = "Find Account by username" ,schema=@Schema()) @Valid @RequestParam(value = "username", required = false) String username) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<Account>>(objectMapper.readValue("[ {\n  \"UserId\" : 1,\n  \"accountType\" : \"CurrentAccount\"\n}, {\n  \"UserId\" : 1,\n  \"accountType\" : \"CurrentAccount\"\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Account>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<Account>>(HttpStatus.NOT_IMPLEMENTED);
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

    //get all accounts
//    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<List<dbAccount>> getAllAccounts(){
//        return new ResponseEntity<>(accountService.getAllAccounts(), HttpStatus.OK);
//    }
}
