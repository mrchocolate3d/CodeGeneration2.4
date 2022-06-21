package io.swagger.api;

import io.swagger.model.InsertUser;
import io.swagger.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.UserRole;
import io.swagger.model.dbUser;
import io.swagger.repository.UserRepository;
import io.swagger.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-06-01T11:41:56.516Z[GMT]")
@Controller
public class UsersApiController implements UsersApi {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

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

    //@PreAuthorize("permitAll")
    public ResponseEntity<User> createUser(@Parameter(in = ParameterIn.DEFAULT, description = "Created User object", schema = @Schema()) @Valid @RequestBody InsertUser body) {
        String accept = request.getHeader("Accept");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        dbUser userFromDB = userService.getUserByUsername(auth.getName());
        if(userFromDB != null){
            System.out.println(userFromDB.getRoles());
        }
        if (accept.contains("application/json")) {
            List<User> userList = userService.getUsers();
            if (userList.stream().anyMatch((user) -> user.getUsername().equals(body.getUsername()))) {
                return new ResponseEntity<User>(HttpStatus.NOT_ACCEPTABLE);
            } else {
                if(userFromDB != null){
                    dbUser user = new dbUser(body.getFirstName(), body.getLastName(), body.getUsername(), body.getEmail(),  body.getPhone(), passwordEncoder.encode(body.getPassword()), body.getRoles(), body.getTransactionLimit());
                    userService.addUser(user);
                    return new ResponseEntity<User>(userService.convertDbUserToUser(user),HttpStatus.CREATED);
                } else{
                    dbUser user = new dbUser(body.getFirstName(), body.getLastName(), body.getUsername(), body.getEmail(),  body.getPhone() , passwordEncoder.encode(body.getPassword()), List.of(UserRole.ROLE_CUSTOMER), 300);
                    userService.addUser(user);
                    return new ResponseEntity<User>(userService.convertDbUserToUser(user),HttpStatus.CREATED);
                }
            }
        }
        return new ResponseEntity<User>(HttpStatus.NOT_IMPLEMENTED);
    }

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<Void> deleteUserById(@Parameter(in = ParameterIn.PATH, description = "The Id of the customer to delete", required = true, schema = @Schema()) @PathVariable("id") Integer id) {
        String accept = request.getHeader("Accept");

        userService.deleteUser(id);
        return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
    }

    //@PreAuthorize("hasRole('ROLE_EMPLOYEE')" , "hasRole('ROLE_CUSTOMER')")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE','ROLE_CUSTOMER')")
    public ResponseEntity<Void> editUserbyId(@Parameter(in = ParameterIn.PATH, description = "The Username of the customer you want to edit", required = false, schema = @Schema()) @PathVariable("username") String Editusername, @Parameter(in = ParameterIn.DEFAULT, description = "", schema = @Schema()) @Valid @RequestBody InsertUser body) {
        String accept = request.getHeader("Accept");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        dbUser userFromDB = userService.getUserByUsername(auth.getName());

        if (userFromDB != null && Editusername.equals(userFromDB.getUsername())) {
            if(body.getRoles() != userFromDB.getRoles() || body.getUsername() != userFromDB.getUsername() ||  body.getTransactionLimit() != userFromDB.getTransactionLimit()){
                userService.editUser(userFromDB, body);
                return new ResponseEntity<Void>(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
            }
            userService.editUser(userFromDB, body);
            return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
        } else if(userFromDB != null && userFromDB.getRoles().contains(UserRole.ROLE_EMPLOYEE)){
            dbUser selectedUser = userService.getUserByUsername(Editusername);
            userService.AdminEditUser(selectedUser, body);
            return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);
    }

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<List<User>> getUser(@Min(0) @Max(50) @Parameter(in = ParameterIn.QUERY, description = "maximum number of records to return", schema = @Schema(allowableValues = {}, maximum = "50"
            , defaultValue = "50")) @Valid @RequestParam(value = "limit", required = false, defaultValue = "50") Integer limit, @Parameter(in = ParameterIn.QUERY, description = "get User by name", schema = @Schema()) @Valid @RequestParam(value = "name", required = false) String name) {
        return new ResponseEntity<List<User>>(userService.getUsersWithParameters(name,limit), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<User> getUserByID(@Parameter(in = ParameterIn.PATH, description = "The Id of the customer to delete", required = true, schema = @Schema()) @PathVariable("id") Integer id, @Min(1) @Max(50) @Parameter(in = ParameterIn.QUERY, description = "maximum number of records to return", schema = @Schema(allowableValues = {}, minimum = "1", maximum = "50"
            , defaultValue = "50")) @Valid @RequestParam(value = "limit", required = false, defaultValue = "50") Integer limit) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<User>(userService.convertDbUserToUser(userService.getUserById(Long.parseLong(id.toString()))),HttpStatus.OK);
        //userService.convertDbUserToUser(userService.getUserById(Long.parseLong(id.toString()))),
    }

}
