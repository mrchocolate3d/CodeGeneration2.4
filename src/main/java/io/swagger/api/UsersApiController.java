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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-06-01T11:41:56.516Z[GMT]")
@Controller
public class UsersApiController implements UsersApi {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping(value = "add")
    public ResponseEntity<String> addUser(@RequestBody dbUser user){
            String token = userService.add(user.getFirstName(), user.getLastName(), user.getUsername(),
                user.getEmail(), user.getPhone(), user.getPassword(), user.getRoles(), user.getTransactionLimit());
        return ResponseEntity.ok(token);
    }

    @RequestMapping(value="", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<dbUser>> getUser(){
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);

    }
//    @RequestMapping(value="/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<dbUser> editUserInfo(@PathVariable Long id, @RequestBody dbUser changedInfoUser){
//        dbUser user = userServiceImplement.getUserById(id);
//
//
//    }




    private static final Logger log = LoggerFactory.getLogger(UsersApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public UsersApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public List<User> getUserFromDB(){
        List<dbUser> dbUsers = userService.getUsers();
        List<User> user = new ArrayList<>();
        for (dbUser x : dbUsers) {
            Double transInt = (Double)x.getTransactionLimit();
            User u = new User(x.getId(),x.getUsername(), x.getFirstName(), x.getLastName(), x.getEmail(), x.getPhone(),transInt);
            user.add(u);
        }
        return user;
    }

    public ResponseEntity<User> createUser(@Parameter(in = ParameterIn.DEFAULT, description = "Created User object", schema=@Schema()) @Valid @RequestBody InsertUser body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            List<User> userList = getUserFromDB();

            if (userList.stream().anyMatch((user) -> user.getUsername().equals(body.getUsername()))){
                return new ResponseEntity<User>(HttpStatus.NOT_ACCEPTABLE);
            } else {

                dbUser user = new dbUser(body.getFirstName(), body.getLastName(), body.getUsername(), body.getEmail(), body.getPassword(), body.getPhone(), List.of(UserRole.ROLE_EMPLOYEE) ,body.getTransactionLimit());
                userService.addUser(user);
            }
            return  new ResponseEntity<User>(HttpStatus.OK);
            //return new ResponseEntity<User>(objectMapper.readValue("{\n  \"firstName\" : \"James\",\n  \"lastName\" : \"Brown\",\n  \"phone\" : \"3138348173799\",\n  \"CurrentIBAN\" : \"NL*INH!@##$%&^&\",\n  \"SavingIBAN\" : \"NL*INH!@##$%&^&\",\n  \"id\" : 1,\n  \"transactionLimit\" : 10000,\n  \"email\" : \"jamesBrown120@outlook.com\",\n  \"username\" : \"jamesB\"\n}", User.class), HttpStatus.NOT_IMPLEMENTED);
        }

        return new ResponseEntity<User>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Void> deleteUserById(@Parameter(in = ParameterIn.PATH, description = "The Id of the customer to delete", required=true, schema=@Schema()) @PathVariable("id") Integer id) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Void> editUserbyId(@Parameter(in = ParameterIn.PATH, description = "The Id of the customer to delete", required=true, schema=@Schema()) @PathVariable("id") Integer id,@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody InsertUser body) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<List<User>> getUser(@Min(0) @Max(50) @Parameter(in = ParameterIn.QUERY, description = "maximum number of records to return" ,schema=@Schema(allowableValues={  }, maximum="50"
, defaultValue="50")) @Valid @RequestParam(value = "limit", required = false, defaultValue="50") Integer limit,@Parameter(in = ParameterIn.QUERY, description = "get User by name" ,schema=@Schema()) @Valid @RequestParam(value = "name", required = false) String name) {

            List<dbUser> dbUsers = userService.getUsers();
            List<User> user = new ArrayList<>();
            for (dbUser x : dbUsers) {
                Double transInt = (Double)x.getTransactionLimit();
                User u = new User(x.getId(),x.getUsername(), x.getFirstName(), x.getLastName(), x.getEmail(), x.getPhone(),transInt);
                user.add(u);
            }
            return new ResponseEntity<List<User>> (user,HttpStatus.OK);

            //return ResponseEntity.status(200).body(user);



//        for (int i = 0; i<3; i++){
//            dbUser user = new dbUser();
//            user.setFirstName("seyi");
//            user.setLastName("gandonu");
//            user.setEmail("seyifunmig75@outlook.com");
//
//            userList.add(user);
//
//        }




       // return ResponseEntity.status(200).body(userList);



        /*String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<User>>(objectMapper.readValue("[ {\n  \"firstName\" : \"James\",\n  \"lastName\" : \"Brown\",\n  \"phone\" : \"3138348173799\",\n  \"CurrentIBAN\" : \"NL*INH!@##$%&^&\",\n  \"SavingIBAN\" : \"NL*INH!@##$%&^&\",\n  \"id\" : 1,\n  \"transactionLimit\" : 10000,\n  \"email\" : \"jamesBrown120@outlook.com\",\n  \"username\" : \"jamesB\"\n}, {\n  \"firstName\" : \"James\",\n  \"lastName\" : \"Brown\",\n  \"phone\" : \"3138348173799\",\n  \"CurrentIBAN\" : \"NL*INH!@##$%&^&\",\n  \"SavingIBAN\" : \"NL*INH!@##$%&^&\",\n  \"id\" : 1,\n  \"transactionLimit\" : 10000,\n  \"email\" : \"jamesBrown120@outlook.com\",\n  \"username\" : \"jamesB\"\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<User>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }*/
        //return new ResponseEntity<List<User>>(HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<User> getUserByID(@Parameter(in = ParameterIn.PATH, description = "The Id of the customer to delete", required=true, schema=@Schema()) @PathVariable("id") Integer id,@Min(1) @Max(50) @Parameter(in = ParameterIn.QUERY, description = "maximum number of records to return" ,schema=@Schema(allowableValues={  }, minimum="1", maximum="50"
, defaultValue="50")) @Valid @RequestParam(value = "limit", required = false, defaultValue="50") Integer limit) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<User>(objectMapper.readValue("{\n  \"firstName\" : \"James\",\n  \"lastName\" : \"Brown\",\n  \"phone\" : \"3138348173799\",\n  \"CurrentIBAN\" : \"NL*INH!@##$%&^&\",\n  \"SavingIBAN\" : \"NL*INH!@##$%&^&\",\n  \"id\" : 1,\n  \"transactionLimit\" : 10000,\n  \"email\" : \"jamesBrown120@outlook.com\",\n  \"username\" : \"jamesB\"\n}", User.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<User>(HttpStatus.NOT_IMPLEMENTED);
    }

}
