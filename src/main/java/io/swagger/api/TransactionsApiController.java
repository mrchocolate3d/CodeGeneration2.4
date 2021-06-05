package io.swagger.api;

import io.swagger.model.dbTransaction;
import io.swagger.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
import java.util.ArrayList;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-06-01T11:41:56.516Z[GMT]")
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

    @RequestMapping(value = "",method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Transaction>> getTransactions(@NotNull @DecimalMin("1") @Parameter(in = ParameterIn.QUERY, description = "" , required=true, schema=@Schema()) @Valid @RequestParam(value = "IBAN", required = true)
                                                               String IBAN, @Parameter(in = ParameterIn.QUERY, description = "" , schema=@Schema()) @Valid @RequestParam(value = "fromDate", required = false)
            OffsetDateTime fromDate, @Parameter(in = ParameterIn.QUERY, description = "" , schema=@Schema()) @Valid @RequestParam(value = "toDate", required = false)
            OffsetDateTime toDate, @Min(0) @Max(50) @Parameter(in = ParameterIn.QUERY, description = "maximum number of transactions to return" , schema=@Schema(allowableValues={  }, maximum="50", defaultValue="50")) @Valid @RequestParam(value = "limit", required = false, defaultValue="50")
            Integer limit) {

        if(fromDate == null){
            fromDate = OffsetDateTime.parse(fromDate + "T00:00:00.001+02:00");
        }
        if(toDate == null){
            toDate = OffsetDateTime.parse(toDate + "T23:59:59.999+02:00");
        }
        if(limit == null){
            limit = transactionService.CountAllTransactions();
        }

        List<dbTransaction> transactions = transactionService.getTransactions(IBAN,fromDate,toDate,limit);
        List<Transaction> transactionsList = new ArrayList<>();
        for(dbTransaction i : transactions){
            Transaction tr = new Transaction(i.getIBAN());
            transactionsList.add(tr);
        }
        return ResponseEntity.status(200).body(transactionsList);
    }

    //COPY
//    public ResponseEntity<Transaction> getTransactions(@NotNull @DecimalMin("1") @Parameter(in = ParameterIn.QUERY, description = "" , required=true, schema=@Schema()) @Valid @RequestParam(value = "IBAN", required = true)
//                                                               String IBAN, @Parameter(in = ParameterIn.QUERY, description = "" , schema=@Schema()) @Valid @RequestParam(value = "fromDate", required = false)
//                                                               OffsetDateTime fromDate, @Parameter(in = ParameterIn.QUERY, description = "" , schema=@Schema()) @Valid @RequestParam(value = "toDate", required = false)
//                                                               OffsetDateTime toDate,@Min(0) @Max(50) @Parameter(in = ParameterIn.QUERY, description = "maximum number of transactions to return" , schema=@Schema(allowableValues={  }, maximum="50", defaultValue="50")) @Valid @RequestParam(value = "limit", required = false, defaultValue="50")
//                                                               Integer limit) {
//        String accept = request.getHeader("Accept");
//        if (accept != null && accept.contains("application/json")) {
//            try {
//                //return new ResponseEntity<>(transactionService.getTransactions(IBAN,fromDate,toDate,limit),HttpStatus.OK);
//                return new ResponseEntity<Transaction>(objectMapper.readValue("{\n  \"amount\" : 0.8008281904610115,\n  \"IBANTo\" : \"NL01INHO0000000000\",\n  \"IBANFrom\" : \"NL01INHO0000000001\",\n  \"time\" : \"2000-01-23T04:56:07.000+00:00\",\n  \"userPerform\" : \"username\",\n  \"token\" : \"a1b2c3b4d5e6\"\n}", Transaction.class), HttpStatus.NOT_IMPLEMENTED);
//                //does transactionservice go here???
//            } catch (IOException e) {
//                log.error("Couldn't serialize response for content type application/json", e);
//                return new ResponseEntity<Transaction>(HttpStatus.INTERNAL_SERVER_ERROR);
//            }
//        }
//
//        return new ResponseEntity<Transaction>(HttpStatus.NOT_IMPLEMENTED);
//    }

    @RequestMapping(value = "" ,method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<dbTransaction> makeNewTransaction(@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody dbTransaction transaction) {
        String accept = request.getHeader("Accept");
        transactionService.createTransaction(transaction);
        return new ResponseEntity<dbTransaction>(transaction,HttpStatus.CREATED);
    }
    //COPY
//    public ResponseEntity<Void> makeNewTransaction(@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody Transaction body) {
//        //TODO: finish this
//        String accept = request.getHeader("Accept");
//        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
//    }

}
