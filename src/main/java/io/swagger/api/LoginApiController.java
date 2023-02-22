package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.LoggedIn;
import io.swagger.model.LoginUser;
import io.swagger.model.dbUser;
import io.swagger.repository.UserRepository;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Log
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-06-01T11:41:56.516Z[GMT]")
@Controller
public class LoginApiController implements LoginApi {

    private static final Logger log = LoggerFactory.getLogger(LoginApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;


    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


    @org.springframework.beans.factory.annotation.Autowired
    public LoginApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @PostMapping(value="/Login")
    public ResponseEntity<LoggedIn> loginUser(@Parameter(in = ParameterIn.QUERY, description = "Logged into the system", schema=@Schema()) @Valid @RequestBody LoginUser userLogin) {
        dbUser user = userService.getdbUserByUserName(userLogin.getUsername());
        if(user != null){
            String token = userService.login(userLogin.getUsername(), userLogin.getPassword());
            LoggedIn userLoggedIn = new LoggedIn();
            userLoggedIn.setId(user.getId());
            userLoggedIn.setToken(token);
            userLoggedIn.setUsername(user.getUsername());
            return new ResponseEntity<LoggedIn> (userLoggedIn,HttpStatus.OK);
        }


        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid credentials");
    }

}
