package io.swagger.api;

import io.swagger.model.*;
import io.swagger.repository.AccountRepository;
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
import org.threeten.bp.OffsetDateTime;
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
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
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
                                                                     String IBAN, @Parameter(in = ParameterIn.QUERY, description = "" , schema=@Schema()) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Valid @RequestParam(value = "fromDate", required = false)
    LocalDate fromDate, @Parameter(in = ParameterIn.QUERY, description = "" , schema=@Schema()) @Valid @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam(value = "toDate", required = false)
    LocalDate toDate, @Min(0) @Max(50) @Parameter(in = ParameterIn.QUERY, description = "maximum number of transactions to return" , schema=@Schema(allowableValues={  }, maximum="50")) @Valid @RequestParam(value = "limit", required = false)
                                                                     Integer limit) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        dbUser user = userService.getdbUserByUserName(username);

        if(user == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"No authentication token was given");
        }
        List<Transaction> transactionList = new ArrayList<>();
        List<dbTransaction> dbTransactionsFrom = transactionService.getTransactionByIBANfrom(IBAN);

        for (dbTransaction DbTransaction : dbTransactionsFrom){
            if(fromDate !=  null && toDate == null){
                if(DbTransaction.getTimestamp().isAfter(fromDate)){
                    addToTransactionList(DbTransaction, transactionList);
                }
            }
            else if(fromDate == null && toDate != null){
                if(DbTransaction.getTimestamp().isBefore(toDate)){
                    addToTransactionList(DbTransaction, transactionList);
                }
            }
            else if(fromDate != null && toDate != null) {
                if (DbTransaction.getTimestamp().isBefore(toDate) && DbTransaction.getTimestamp().isAfter(fromDate)) {
                    addToTransactionList(DbTransaction, transactionList);
                }
            }
            else{
                addToTransactionList(DbTransaction, transactionList);
            }
        }

        List<Transaction> limitList = new ArrayList<Transaction>();
        if(limit != null && limit < transactionList.size() && limit > 0){
            limitList = transactionList.subList(0, limit);
        }
        else{
            limitList = transactionList;
        }

        return new ResponseEntity<List<Transaction>>(limitList,HttpStatus.OK);

    }

    public ResponseEntity<Transaction> makeNewTransaction(@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody Transaction transaction) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        dbUser user = userService.getdbUserByUserName(username);

        if(user == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"No authentication token was given");
        }

        if (transaction.getAmount() == null ||
                transaction.getIbANFrom() == null || transaction.getIbANTo() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Input field is missing");
        }

        dbAccount account = accountService.getAccountByIban(transaction.getIbANFrom());

        System.out.println(account.toString());

        System.out.println(account.getUser() == user);

        if (user.getRoles().contains(UserRole.ROLE_EMPLOYEE) || account.getUser() == user) {
            System.out.println(account.getUser() == user);
            System.out.println(user.getRoles().contains(UserRole.ROLE_EMPLOYEE));
            if(account.getAbsoluteLimit() > account.getBalance() - transaction.getAmount())
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You don't have enough credit to make the transaction");
            }

            if(account.getUser().getDayLimit() < transactionService.getTotalTransactionAmountByAccountAndDate(LocalDate.now(), transaction.getIbANFrom()) + transaction.getAmount()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Day limit reached");
            }

            if(account.getUser().getTransactionLimit() < transaction.getAmount()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transaction limit reached");
            }

            dbTransaction tr = new dbTransaction(
                    user.getUsername(), transaction.getIbANTo(), transaction.getIbANFrom(), transaction.getAmount(), LocalDate.now()
            );

            transactionService.createTransaction(tr);
            Transaction transaction1 = transactionService.setTransactionsFromDb(tr);
            return new ResponseEntity<Transaction>(transaction1, HttpStatus.CREATED);
        }

        throw new ResponseStatusException(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED, "You are not allowed to make this transaction");
    }

    public void addToTransactionList(dbTransaction DbTransaction, List<Transaction> transactionList){
        Transaction transaction = transactionService.setTransactionsFromDb(DbTransaction);
        transactionList.add(transaction);
    }
}
