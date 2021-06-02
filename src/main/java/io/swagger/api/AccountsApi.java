/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.25).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package io.swagger.api;

import io.swagger.model.Account;
import io.swagger.model.Deposit;
import io.swagger.model.ReturnAccount;
import io.swagger.model.ReturnBalance;
import io.swagger.model.Withdrawal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.CookieValue;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-06-01T11:41:56.516Z[GMT]")
@Validated
public interface AccountsApi {

    @Operation(summary = "Close an account", description = "Can only be done by employee", security = {
        @SecurityRequirement(name = "bearerAuth")    }, tags={ "Account" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "successful operation"),
        
        @ApiResponse(responseCode = "400", description = "Invalid UserID"),
        
        @ApiResponse(responseCode = "404", description = "UserID not found") })
    @RequestMapping(value = "/Accounts/{UserID}",
        method = RequestMethod.DELETE)
    ResponseEntity<Void> closeAccount(@Parameter(in = ParameterIn.PATH, description = "The UserID of the account required", required=true, schema=@Schema()) @PathVariable("UserID") String userID);


    @Operation(summary = "Create an account", description = "Can only be done by user", security = {
        @SecurityRequirement(name = "bearerAuth")    }, tags={ "Account" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ReturnAccount.class))),
        
        @ApiResponse(responseCode = "400", description = "operation failed"),
        
        @ApiResponse(responseCode = "404", description = "account not added") })
    @RequestMapping(value = "/Accounts",
        produces = { "application/json", "application/xml" }, 
        consumes = { "application/json" }, 
        method = RequestMethod.POST)
    ResponseEntity<ReturnAccount> createAccount(@Parameter(in = ParameterIn.DEFAULT, description = "account object is created", required=true, schema=@Schema()) @Valid @RequestBody Account body);


    @Operation(summary = "Depositing money to an account using IBAN", description = "Depositing money into a customer account", security = {
        @SecurityRequirement(name = "bearerAuth")    }, tags={ "Account" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Money has been deposited", content = @Content(schema = @Schema(implementation = Deposit.class))),
        
        @ApiResponse(responseCode = "400", description = "Not enough money in account"),
        
        @ApiResponse(responseCode = "405", description = "Unauthorized") })
    @RequestMapping(value = "/Accounts/{IBAN}/deposit",
        produces = { "application/json", "application/xml" }, 
        method = RequestMethod.POST)
    ResponseEntity<Deposit> depositMoney(@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("IBAN") String IBAN, @NotNull @DecimalMin("0.01") @DecimalMax("10000") @Parameter(in = ParameterIn.QUERY, description = "The amount to deposit" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "amount", required = true) Double amount);


    @Operation(summary = "Get specific account by iban", description = "Return an account by iban", security = {
        @SecurityRequirement(name = "bearerAuth")    }, tags={ "Account" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Account.class))),
        
        @ApiResponse(responseCode = "400", description = "Invalid iban") })
    @RequestMapping(value = "/Accounts/{IBAN}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<Account> getAccountByIban(@Parameter(in = ParameterIn.PATH, description = "iban needed for finding", required=true, schema=@Schema()) @PathVariable("IBAN") String IBAN);


    @Operation(summary = "Get all customer accounts", description = "Return a list of customer accounts", security = {
        @SecurityRequirement(name = "bearerAuth")    }, tags={ "Account" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Account.class)))),
        
        @ApiResponse(responseCode = "400", description = "operation failed"),
        
        @ApiResponse(responseCode = "404", description = "not found") })
    @RequestMapping(value = "/Accounts",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<Account>> getAccounts(@Min(0) @Max(50) @Parameter(in = ParameterIn.QUERY, description = "maximum number of records to return" ,schema=@Schema(allowableValues={  }, maximum="50"
, defaultValue="50")) @Valid @RequestParam(value = "limit", required = false, defaultValue="50") Integer limit, @Parameter(in = ParameterIn.QUERY, description = "Find Account by username" ,schema=@Schema()) @Valid @RequestParam(value = "username", required = false) String username);


    @Operation(summary = "Get balance of an account using IBAN", description = "Get balance of account using IBAN", security = {
        @SecurityRequirement(name = "bearerAuth")    }, tags={ "Account" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Balance is showed", content = @Content(schema = @Schema(implementation = ReturnBalance.class))) })
    @RequestMapping(value = "/Accounts/{IBAN}/balance",
        produces = { "application/json", "application/xml" }, 
        method = RequestMethod.GET)
    ResponseEntity<ReturnBalance> getBalanceByIban(@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("IBAN") String IBAN);


    @Operation(summary = "Withdraw from an account using IBAN", description = "Customers withdraw money from their accounts", security = {
        @SecurityRequirement(name = "bearerAuth")    }, tags={ "Account" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Money has been withdrawn", content = @Content(schema = @Schema(implementation = Withdrawal.class))),
        
        @ApiResponse(responseCode = "400", description = "Not enough money is in account"),
        
        @ApiResponse(responseCode = "405", description = "Unauthorized") })
    @RequestMapping(value = "/Accounts/{IBAN}/withdraw",
        produces = { "application/json" }, 
        method = RequestMethod.POST)
    ResponseEntity<Withdrawal> withdrawal(@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("IBAN") String IBAN, @NotNull @DecimalMin("0.01") @DecimalMax("10000") @Parameter(in = ParameterIn.QUERY, description = "The amount to withdraw" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "amount", required = true) Double amount);

}

