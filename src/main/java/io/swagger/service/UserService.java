package io.swagger.service;

import io.swagger.model.InsertUser;
import io.swagger.model.UserRole;
import io.swagger.repository.UserRepository;
import io.swagger.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12);
    }

    public List<InsertUser> getUsers() {
        return (List<InsertUser>) userRepository.findAll();
    }


    public InsertUser addUser(InsertUser user) {
        if (userRepository.findUserByUsername(user.getUsername()) == null) {
            InsertUser u = new InsertUser(
                    user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail(), user.getPhone(), passwordEncoder().encode(user.getPassword()), user.getTransactionLimit(),List.of(UserRole.ROLE_EMPLOYEE, UserRole.ROLE_CUSTOMER)
            );

            if (user.getRoles().size() == 0) {
                u.setRoles(List.of(UserRole.ROLE_EMPLOYEE, UserRole.ROLE_CUSTOMER));
            } else {
                u.setRoles(user.getRoles());
            }

            userRepository.save(u);
            return u;
        }
        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Username already in use");
    }


    public String login(String username, String password) {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return jwtTokenProvider.createToken(username, userRepository.findUserByUsername(username).getRoles());
        }catch(AuthenticationException ae){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Invallid credentials");
        }
    }
}

