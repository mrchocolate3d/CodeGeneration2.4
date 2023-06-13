package io.swagger.api;

import io.swagger.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.repository.UserRepository;
import io.swagger.service.TransactionService;
import io.swagger.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-06-01T11:41:56.516Z[GMT]")
@Controller
public class UsersApiController implements UsersApi {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionService transactionService;

    private static final Logger log = LoggerFactory.getLogger(UsersApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;
    @Autowired
    PasswordEncoder passwordEncoder;

    @org.springframework.beans.factory.annotation.Autowired
    public UsersApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }
    @PostMapping(value="/Users")
    public ResponseEntity<User> createUser(@Parameter(in = ParameterIn.DEFAULT, description = "Created User object", schema = @Schema()) @Valid @RequestBody InsertUser body) {
        User u = userService.CreateUser(body);
        return new ResponseEntity<User>(u, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<Void> deleteUserById(@Parameter(in = ParameterIn.PATH, description = "The Id of the customer to delete", required = true, schema = @Schema()) @PathVariable("id") Integer id) {
        userService.deleteUser(id);
        return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<Void> editUserbyId(@Parameter(in = ParameterIn.PATH, description = "The Id of the customer to edit", required = false, schema = @Schema())
                                             @PathVariable("id") long id,
                                             @Parameter(in = ParameterIn.DEFAULT, description = "", schema = @Schema())
                                             @Valid @RequestBody EditUser body) {
        String accept = request.getHeader("Accept");

        User u = userService.EditUser(id, body);
        if(u != null){
            return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<List<User>> getUser(@Min(0) @Max(50) @Parameter(in = ParameterIn.QUERY, description = "maximum number of records to return", schema = @Schema(allowableValues = {}, maximum = "50"
            , defaultValue = "50")) @Valid @RequestParam(value = "limit", required = false, defaultValue = "50") Integer limit, @Parameter(in = ParameterIn.QUERY, description = "get User by name", schema = @Schema()) @Valid @RequestParam(value = "name", required = false) String name) {
        return new ResponseEntity<List<User>>(userService.getUsersWithParameters(name, limit), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<User> getUserByID(@Parameter(in = ParameterIn.PATH, description = "The Id of the customer to get", required = true, schema = @Schema())
                                            @PathVariable("id") Integer id,
                                            @Min(1) @Max(50) @Parameter(in = ParameterIn.QUERY, description = "maximum number of records to return", schema = @Schema(allowableValues = {}, minimum = "1", maximum = "50", defaultValue = "50"))
                                            @Valid @RequestParam(value = "limit", required = false, defaultValue = "50") Integer limit) {
        return new ResponseEntity<User>(userService.convertDbUserToUser(userService.getUserById(Long.parseLong(id.toString()))), HttpStatus.OK);
    }

    public ResponseEntity<User> getOwnedUserData() {
        return new ResponseEntity<User>(userService.GetOwnedUserData(), HttpStatus.OK);
    }

    public ResponseEntity<List<ReturnLimitAndRemainingAmount>> getUserRemainingAmount(){
        List<ReturnLimitAndRemainingAmount> responses = userService.GetUserRemainingAmount();
        return new ResponseEntity<List<ReturnLimitAndRemainingAmount>>(responses, HttpStatus.OK);
    }

    public ResponseEntity<List<User>> GetUsersWithNoAccount(){
        return new ResponseEntity<List<User>>(userService.GetUserWithNoAccount(), HttpStatus.OK);

    }
}
